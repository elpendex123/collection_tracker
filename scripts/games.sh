#!/bin/bash

BASE_URL="${COLLECTION_TRACKER_URL:-http://localhost:8081}"
API_URL="$BASE_URL/api/games"

usage() {
    echo "Usage: $0 <command> [options]"
    echo ""
    echo "Commands:"
    echo "  list                                    List all games"
    echo "  get <id>                                Get game by ID"
    echo "  add <title> <console> <publisher> <developer> <genre> <status> <format>"
    echo "                                          Add a new game"
    echo "  update <id> <json>                      Update a game (partial JSON)"
    echo "  delete <id>                             Delete a game"
    echo "  search <title>                          Search games by title"
    echo "  filter [--genre <g>] [--status <s>] [--console <c>] [--publisher <p>] [--format <f>]"
    echo "                                          Filter games"
    echo ""
    echo "Status values: BACKLOG, PLAYING, BEATEN, DROPPED"
    echo "Format values: PHYSICAL, DIGITAL"
    exit 1
}

list_games() {
    curl -s "$API_URL" | jq .
}

get_game() {
    curl -s "$API_URL/$1" | jq .
}

add_game() {
    local title="$1"
    local console="$2"
    local publisher="$3"
    local developer="$4"
    local genre="$5"
    local status="$6"
    local format="$7"

    curl -s -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "{
            \"title\": \"$title\",
            \"console\": \"$console\",
            \"publisher\": \"$publisher\",
            \"developer\": \"$developer\",
            \"genre\": \"$genre\",
            \"completionStatus\": \"$status\",
            \"format\": \"$format\"
        }" | jq .
}

update_game() {
    curl -s -X PUT "$API_URL/$1" \
        -H "Content-Type: application/json" \
        -d "$2" | jq .
}

delete_game() {
    curl -s -X DELETE "$API_URL/$1" -w "%{http_code}" -o /dev/null
    echo "Deleted"
}

search_games() {
    curl -s "$API_URL/search?title=$1" | jq .
}

filter_games() {
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
            --console)
                params="${params}&console=$2"
                shift 2
                ;;
            --publisher)
                params="${params}&publisher=$2"
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
        list_games
        ;;
    get)
        [ -z "$2" ] && usage
        get_game "$2"
        ;;
    add)
        [ -z "$8" ] && usage
        add_game "$2" "$3" "$4" "$5" "$6" "$7" "$8"
        ;;
    update)
        [ -z "$3" ] && usage
        update_game "$2" "$3"
        ;;
    delete)
        [ -z "$2" ] && usage
        delete_game "$2"
        ;;
    search)
        [ -z "$2" ] && usage
        search_games "$2"
        ;;
    filter)
        shift
        filter_games "$@"
        ;;
    *)
        usage
        ;;
esac
