#!/bin/bash

BASE_URL="${COLLECTION_TRACKER_URL:-http://localhost:8081}"
API_URL="$BASE_URL/api/books"

usage() {
    echo "Usage: $0 <command> [options]"
    echo ""
    echo "Commands:"
    echo "  list                                    List all books"
    echo "  get <id>                                Get book by ID"
    echo "  add <title> <author> <isbn> <publisher> <year> <genre> <status> <format>"
    echo "                                          Add a new book"
    echo "  update <id> <json>                      Update a book (partial JSON)"
    echo "  delete <id>                             Delete a book"
    echo "  search <title>                          Search books by title"
    echo "  filter [--genre <g>] [--status <s>] [--format <f>]"
    echo "                                          Filter books"
    echo ""
    echo "Status values: UNREAD, IN_PROGRESS, READ"
    echo "Format values: PHYSICAL, DIGITAL"
    exit 1
}

list_books() {
    curl -s "$API_URL" | jq .
}

get_book() {
    curl -s "$API_URL/$1" | jq .
}

add_book() {
    local title="$1"
    local author="$2"
    local isbn="$3"
    local publisher="$4"
    local year="$5"
    local genre="$6"
    local status="$7"
    local format="$8"

    curl -s -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "{
            \"title\": \"$title\",
            \"author\": \"$author\",
            \"isbn\": \"$isbn\",
            \"publisher\": \"$publisher\",
            \"publishedYear\": $year,
            \"genre\": \"$genre\",
            \"readStatus\": \"$status\",
            \"format\": \"$format\"
        }" | jq .
}

update_book() {
    curl -s -X PUT "$API_URL/$1" \
        -H "Content-Type: application/json" \
        -d "$2" | jq .
}

delete_book() {
    curl -s -X DELETE "$API_URL/$1" -w "%{http_code}" -o /dev/null
    echo "Deleted"
}

search_books() {
    curl -s "$API_URL/search?title=$1" | jq .
}

filter_books() {
    local params=""
    while [[ $# -gt 0 ]]; do
        case "$1" in
            --genre)
                params="${params}&genre=$2"
                shift 2
                ;;
            --status)
                params="${params}&status=$2"
                shift 2
                ;;
            --format)
                params="${params}&format=$2"
                shift 2
                ;;
            *)
                shift
                ;;
        esac
    done
    params="${params:1}"
    curl -s "$API_URL/filter?$params" | jq .
}

case "$1" in
    list)
        list_books
        ;;
    get)
        [ -z "$2" ] && usage
        get_book "$2"
        ;;
    add)
        [ -z "$9" ] && usage
        add_book "$2" "$3" "$4" "$5" "$6" "$7" "$8" "$9"
        ;;
    update)
        [ -z "$3" ] && usage
        update_book "$2" "$3"
        ;;
    delete)
        [ -z "$2" ] && usage
        delete_book "$2"
        ;;
    search)
        [ -z "$2" ] && usage
        search_books "$2"
        ;;
    filter)
        shift
        filter_books "$@"
        ;;
    *)
        usage
        ;;
esac
