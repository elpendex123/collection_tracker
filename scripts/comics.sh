#!/bin/bash

BASE_URL="${COLLECTION_TRACKER_URL:-http://localhost:8081}"
API_URL="$BASE_URL/api/comics"

usage() {
    echo "Usage: $0 <command> [options]"
    echo ""
    echo "Commands:"
    echo "  list                                    List all comics"
    echo "  get <id>                                Get comic by ID"
    echo "  add <title> <series> <issue> <writer> <artist> <status> <format>"
    echo "                                          Add a new comic"
    echo "  update <id> <json>                      Update a comic (partial JSON)"
    echo "  delete <id>                             Delete a comic"
    echo "  search <title>                          Search comics by title"
    echo "  filter [--status <s>] [--format <f>]    Filter comics"
    echo ""
    echo "Status values: UNREAD, IN_PROGRESS, READ"
    echo "Format values: PHYSICAL, DIGITAL"
    exit 1
}

list_comics() {
    curl -s "$API_URL" | jq .
}

get_comic() {
    curl -s "$API_URL/$1" | jq .
}

add_comic() {
    local title="$1"
    local series="$2"
    local issue="$3"
    local writer="$4"
    local artist="$5"
    local status="$6"
    local format="$7"

    curl -s -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "{
            \"title\": \"$title\",
            \"series\": \"$series\",
            \"issueNumber\": \"$issue\",
            \"writer\": \"$writer\",
            \"artist\": \"$artist\",
            \"readStatus\": \"$status\",
            \"format\": \"$format\"
        }" | jq .
}

update_comic() {
    curl -s -X PUT "$API_URL/$1" \
        -H "Content-Type: application/json" \
        -d "$2" | jq .
}

delete_comic() {
    curl -s -X DELETE "$API_URL/$1" -w "%{http_code}" -o /dev/null
    echo "Deleted"
}

search_comics() {
    curl -s "$API_URL/search?title=$1" | jq .
}

filter_comics() {
    local params=""
    while [[ $# -gt 0 ]]; do
        case "$1" in
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
        list_comics
        ;;
    get)
        [ -z "$2" ] && usage
        get_comic "$2"
        ;;
    add)
        [ -z "$8" ] && usage
        add_comic "$2" "$3" "$4" "$5" "$6" "$7" "$8"
        ;;
    update)
        [ -z "$3" ] && usage
        update_comic "$2" "$3"
        ;;
    delete)
        [ -z "$2" ] && usage
        delete_comic "$2"
        ;;
    search)
        [ -z "$2" ] && usage
        search_comics "$2"
        ;;
    filter)
        shift
        filter_comics "$@"
        ;;
    *)
        usage
        ;;
esac
