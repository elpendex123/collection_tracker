# Python API Interaction

This guide shows how to interact with the Collection Tracker REST API using Python.

## Setup

### Install Dependencies

```bash
python3 -m venv venv
source venv/bin/activate
pip install requests
```

## Basic Usage

### Import and Configuration

```python
import requests

BASE_URL = "http://localhost:8081/api"
HEADERS = {"Content-Type": "application/json"}
```

---

## Books API

### List All Books

```python
import requests

response = requests.get("http://localhost:8081/api/books")
books = response.json()

for book in books:
    print(f"{book['id']}: {book['title']} by {book['author']}")
```

### Get a Single Book

```python
book_id = 1
response = requests.get(f"http://localhost:8081/api/books/{book_id}")

if response.status_code == 200:
    book = response.json()
    print(f"Title: {book['title']}")
    print(f"Author: {book['author']}")
    print(f"Status: {book['readStatus']}")
else:
    print(f"Book not found: {response.status_code}")
```

### Create a Book

```python
new_book = {
    "title": "The Pragmatic Programmer",
    "author": "David Thomas, Andrew Hunt",
    "isbn": "9780135957059",
    "publisher": "Addison-Wesley",
    "publishedYear": 2019,
    "genre": "Programming",
    "readStatus": "UNREAD",
    "format": "PHYSICAL",
    "notes": "20th Anniversary Edition"
}

response = requests.post(
    "http://localhost:8081/api/books",
    json=new_book
)

if response.status_code == 201:
    created = response.json()
    print(f"Created book with ID: {created['id']}")
else:
    print(f"Error: {response.json()}")
```

### Update a Book

```python
book_id = 1
updates = {
    "title": "The Pragmatic Programmer",
    "author": "David Thomas, Andrew Hunt",
    "readStatus": "READ"
}

response = requests.put(
    f"http://localhost:8081/api/books/{book_id}",
    json=updates
)

if response.status_code == 200:
    print("Book updated successfully")
else:
    print(f"Error: {response.status_code}")
```

### Delete a Book

```python
book_id = 1
response = requests.delete(f"http://localhost:8081/api/books/{book_id}")

if response.status_code == 204:
    print("Book deleted successfully")
else:
    print(f"Error: {response.status_code}")
```

### Search Books by Title

```python
search_term = "Dune"
response = requests.get(
    "http://localhost:8081/api/books/search",
    params={"title": search_term}
)

books = response.json()
print(f"Found {len(books)} books matching '{search_term}'")
for book in books:
    print(f"  - {book['title']}")
```

### Filter Books

```python
filters = {
    "genre": "Sci-Fi",
    "status": "READ",
    "format": "PHYSICAL"
}

response = requests.get(
    "http://localhost:8081/api/books/filter",
    params=filters
)

books = response.json()
print(f"Found {len(books)} books matching filters")
```

---

## Comics API

### List All Comics

```python
response = requests.get("http://localhost:8081/api/comics")
comics = response.json()

for comic in comics:
    print(f"{comic['title']} - {comic['series']} #{comic['issueNumber']}")
```

### Create a Comic

```python
new_comic = {
    "title": "Saga Vol. 1",
    "series": "Saga",
    "issueNumber": "1",
    "writer": "Brian K. Vaughan",
    "artist": "Fiona Staples",
    "readStatus": "READ",
    "format": "PHYSICAL",
    "notes": "Amazing space opera"
}

response = requests.post(
    "http://localhost:8081/api/comics",
    json=new_comic
)

if response.status_code == 201:
    print(f"Created comic with ID: {response.json()['id']}")
```

### Filter Comics

```python
response = requests.get(
    "http://localhost:8081/api/comics/filter",
    params={"status": "UNREAD", "format": "DIGITAL"}
)

comics = response.json()
print(f"Unread digital comics: {len(comics)}")
```

---

## Video Games API

### List All Games

```python
response = requests.get("http://localhost:8081/api/games")
games = response.json()

for game in games:
    print(f"{game['title']} ({game['console']}) - {game['completionStatus']}")
```

### Create a Game

```python
new_game = {
    "title": "Baldur's Gate 3",
    "console": "PC",
    "publisher": "Larian Studios",
    "developer": "Larian Studios",
    "genre": "RPG",
    "completionStatus": "PLAYING",
    "format": "DIGITAL",
    "notes": "Game of the Year 2023"
}

response = requests.post(
    "http://localhost:8081/api/games",
    json=new_game
)

if response.status_code == 201:
    print(f"Created game with ID: {response.json()['id']}")
```

### Filter Games

```python
response = requests.get(
    "http://localhost:8081/api/games/filter",
    params={
        "console": "PS5",
        "status": "BACKLOG",
        "genre": "RPG"
    }
)

games = response.json()
print(f"PS5 RPGs in backlog: {len(games)}")
```

---

## Complete Example: Collection Stats

```python
import requests

BASE_URL = "http://localhost:8081/api"

def get_collection_stats():
    # Get all collections
    books = requests.get(f"{BASE_URL}/books").json()
    comics = requests.get(f"{BASE_URL}/comics").json()
    games = requests.get(f"{BASE_URL}/games").json()

    print("=== Collection Statistics ===\n")

    # Books stats
    print(f"Books: {len(books)}")
    read_books = len([b for b in books if b.get('readStatus') == 'READ'])
    print(f"  - Read: {read_books}")
    print(f"  - Unread: {len(books) - read_books}")

    # Comics stats
    print(f"\nComics: {len(comics)}")
    read_comics = len([c for c in comics if c.get('readStatus') == 'READ'])
    print(f"  - Read: {read_comics}")
    print(f"  - Unread: {len(comics) - read_comics}")

    # Games stats
    print(f"\nGames: {len(games)}")
    beaten = len([g for g in games if g.get('completionStatus') == 'BEATEN'])
    playing = len([g for g in games if g.get('completionStatus') == 'PLAYING'])
    backlog = len([g for g in games if g.get('completionStatus') == 'BACKLOG'])
    print(f"  - Beaten: {beaten}")
    print(f"  - Playing: {playing}")
    print(f"  - Backlog: {backlog}")

    print(f"\nTotal items: {len(books) + len(comics) + len(games)}")

if __name__ == "__main__":
    get_collection_stats()
```

---

## Error Handling

```python
import requests

def safe_request(method, url, **kwargs):
    try:
        response = requests.request(method, url, **kwargs)
        response.raise_for_status()
        return response.json() if response.content else None
    except requests.exceptions.ConnectionError:
        print("Error: Cannot connect to the server. Is it running?")
    except requests.exceptions.HTTPError as e:
        print(f"HTTP Error: {e.response.status_code}")
        if e.response.content:
            print(f"Details: {e.response.json()}")
    except Exception as e:
        print(f"Error: {e}")
    return None

# Usage
books = safe_request("GET", "http://localhost:8081/api/books")
if books:
    print(f"Found {len(books)} books")
```

---

## Enum Values Reference

### ReadStatus (Books & Comics)
- `UNREAD`
- `IN_PROGRESS`
- `READ`

### CompletionStatus (Video Games)
- `BACKLOG`
- `PLAYING`
- `BEATEN`
- `DROPPED`

### Format (All)
- `PHYSICAL`
- `DIGITAL`
