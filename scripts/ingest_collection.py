#!/usr/bin/env python3
"""
Collection Tracker — CSV Ingest Script
=======================================
Loads books.csv, comics.csv, and games.csv into the Collection Tracker REST API.
Skips records that already exist (duplicate detection: ISBN + title combo).

Usage:
    python3 ingest_collection.py [--base-url http://localhost:8080] [--type books|comics|games|all]

Requirements:
    pip3 install requests
"""

import argparse
import csv
import sys
import time
from pathlib import Path

try:
    import requests
except ImportError:
    print("ERROR: 'requests' library not found. Run: pip3 install requests")
    sys.exit(1)

# ---------------------------------------------------------------------------
# Config
# ---------------------------------------------------------------------------
DEFAULT_BASE_URL = "http://localhost:8080"
RETRY_ATTEMPTS = 3
RETRY_DELAY_SEC = 2

# ---------------------------------------------------------------------------
# Field mappings: CSV header → JSON key (for any renaming needed)
# ---------------------------------------------------------------------------
BOOK_FIELDS = [
    "title", "author", "isbn", "publisher", "publishedYear",
    "genre", "readStatus", "format", "notes"
]
COMIC_FIELDS = [
    "title", "series", "issueNumber", "isbn", "writer",
    "artist", "readStatus", "format", "notes"
]
GAME_FIELDS = [
    "title", "isbn", "console", "publisher", "developer",
    "genre", "completionStatus", "format", "notes"
]

# CSV column headers (must match the actual CSV files)
BOOK_CSV_HEADERS   = ["title","author","isbn","publisher","published_year","genre","read_status","format","notes"]
COMIC_CSV_HEADERS  = ["title","series","issue_number","isbn","writer","artist","read_status","format","notes"]
GAME_CSV_HEADERS   = ["title","isbn","console","publisher","developer","genre","completion_status","format","notes"]

# Map CSV snake_case → JSON camelCase per collection type
BOOK_REMAP  = {"published_year": "publishedYear", "read_status": "readStatus"}
COMIC_REMAP = {"issue_number": "issueNumber",     "read_status": "readStatus"}
GAME_REMAP  = {"completion_status": "completionStatus"}


# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------
def remap_row(row: dict, remap: dict) -> dict:
    """Rename CSV snake_case keys to JSON camelCase keys."""
    return {remap.get(k, k): v for k, v in row.items()}


def fetch_existing(base_url: str, endpoint: str) -> set:
    """
    Fetch all existing records and return a set of (isbn, title) tuples
    for duplicate detection.
    """
    url = f"{base_url}/api/{endpoint}"
    try:
        resp = requests.get(url, timeout=10)
        resp.raise_for_status()
        records = resp.json()
        existing = set()
        for r in records:
            isbn  = (r.get("isbn") or "").strip().lower()
            title = (r.get("title") or "").strip().lower()
            existing.add((isbn, title))
        return existing
    except requests.exceptions.ConnectionError:
        print(f"\n  ERROR: Cannot connect to {url}")
        print("  Make sure the Spring Boot app is running on the base URL.")
        sys.exit(1)
    except Exception as e:
        print(f"\n  ERROR fetching existing records from {url}: {e}")
        sys.exit(1)


def post_record(base_url: str, endpoint: str, payload: dict) -> bool:
    """POST a single record to the API. Returns True on success."""
    url = f"{base_url}/api/{endpoint}"
    for attempt in range(1, RETRY_ATTEMPTS + 1):
        try:
            resp = requests.post(url, json=payload, timeout=10)
            if resp.status_code in (200, 201):
                return True
            else:
                print(f"\n  WARN: HTTP {resp.status_code} for '{payload.get('title')}' "
                      f"— {resp.text[:120]}")
                return False
        except requests.exceptions.ConnectionError:
            if attempt < RETRY_ATTEMPTS:
                print(f"\n  Connection lost, retrying ({attempt}/{RETRY_ATTEMPTS})...")
                time.sleep(RETRY_DELAY_SEC)
            else:
                print(f"\n  ERROR: Could not POST after {RETRY_ATTEMPTS} attempts.")
                return False
        except Exception as e:
            print(f"\n  ERROR posting '{payload.get('title')}': {e}")
            return False
    return False


def ingest(base_url: str, csv_path: Path, endpoint: str,
           csv_headers: list, remap: dict, label: str):
    """
    Core ingest function for one collection type.
    - Fetches existing records for duplicate checking (ISBN + title).
    - Reads the CSV file.
    - Posts only new records.
    """
    print(f"\n{'='*60}")
    print(f"  Ingesting {label}")
    print(f"  CSV:      {csv_path}")
    print(f"  Endpoint: {base_url}/api/{endpoint}")
    print(f"{'='*60}")

    if not csv_path.exists():
        print(f"  SKIP: File not found — {csv_path}")
        return

    # Fetch existing records for duplicate detection
    print("  Checking existing records in database...", end="", flush=True)
    existing = fetch_existing(base_url, endpoint)
    print(f" {len(existing)} found.")

    added = skipped = errors = 0

    with csv_path.open(newline="", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        rows = list(reader)

    print(f"  CSV rows to process: {len(rows)}")
    print()

    for i, row in enumerate(rows, start=1):
        # Build the duplicate key
        isbn  = (row.get("isbn") or "").strip().lower()
        title = (row.get("title") or "").strip().lower()
        key   = (isbn, title)

        if key in existing:
            skipped += 1
            print(f"  [{i:03d}] SKIP (exists): {row.get('title', '?')[:55]}")
            continue

        # Remap keys from CSV snake_case to JSON camelCase
        payload = remap_row(row, remap)

        # Clean up empty strings → None (omit from JSON)
        def clean_value(v):
            if isinstance(v, str):
                return v if v.strip() != "" else None
            return v
        payload = {k: clean_value(v) for k, v in payload.items()}

        # Convert numeric fields
        if "publishedYear" in payload and payload["publishedYear"]:
            try:
                payload["publishedYear"] = int(payload["publishedYear"])
            except ValueError:
                payload["publishedYear"] = None

        success = post_record(base_url, endpoint, payload)
        if success:
            added += 1
            existing.add(key)          # prevent duplicate within this run
            print(f"  [{i:03d}] ADD:  {row.get('title', '?')[:55]}")
        else:
            errors += 1
            print(f"  [{i:03d}] ERR:  {row.get('title', '?')[:55]}")

    print()
    print(f"  ✔ Added:   {added}")
    print(f"  ⊘ Skipped: {skipped}  (already in DB)")
    if errors:
        print(f"  ✗ Errors:  {errors}")


# ---------------------------------------------------------------------------
# CLI
# ---------------------------------------------------------------------------
def parse_args():
    parser = argparse.ArgumentParser(
        description="Ingest sample CSV data into Collection Tracker REST API."
    )
    parser.add_argument(
        "--base-url",
        default=DEFAULT_BASE_URL,
        help=f"Base URL of the running Spring Boot app (default: {DEFAULT_BASE_URL})"
    )
    parser.add_argument(
        "--type",
        choices=["books", "comics", "games", "all"],
        default="all",
        help="Which collection type to ingest (default: all)"
    )
    parser.add_argument(
        "--csv-dir",
        default=".",
        help="Directory containing the CSV files (default: current directory)"
    )
    return parser.parse_args()


def main():
    args = parse_args()
    base_url = args.base_url.rstrip("/")
    csv_dir  = Path(args.csv_dir)

    print(f"\nCollection Tracker — CSV Ingest")
    print(f"Base URL : {base_url}")
    print(f"CSV dir  : {csv_dir.resolve()}")
    print(f"Type     : {args.type}")

    # Health check
    try:
        resp = requests.get(f"{base_url}/actuator/health", timeout=5)
        if resp.status_code == 200:
            print(f"Health   : OK ✔")
        else:
            print(f"Health   : HTTP {resp.status_code} — continuing anyway")
    except Exception:
        print("Health   : /actuator/health not reachable — continuing anyway")

    collections = {
        "books": {
            "csv":      csv_dir / "books.csv",
            "endpoint": "books",
            "headers":  BOOK_CSV_HEADERS,
            "remap":    BOOK_REMAP,
            "label":    "Books",
        },
        "comics": {
            "csv":      csv_dir / "comics.csv",
            "endpoint": "comics",
            "headers":  COMIC_CSV_HEADERS,
            "remap":    COMIC_REMAP,
            "label":    "Comics",
        },
        "games": {
            "csv":      csv_dir / "games.csv",
            "endpoint": "games",
            "headers":  GAME_CSV_HEADERS,
            "remap":    GAME_REMAP,
            "label":    "Video Games",
        },
    }

    to_run = ["books", "comics", "games"] if args.type == "all" else [args.type]

    for key in to_run:
        cfg = collections[key]
        ingest(
            base_url  = base_url,
            csv_path  = cfg["csv"],
            endpoint  = cfg["endpoint"],
            csv_headers = cfg["headers"],
            remap     = cfg["remap"],
            label     = cfg["label"],
        )

    print(f"\n{'='*60}")
    print("  Ingest complete.")
    print(f"{'='*60}\n")


if __name__ == "__main__":
    main()
