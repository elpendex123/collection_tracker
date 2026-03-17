# curl API Reference

This guide provides curl commands for all REST API endpoints.

## Prerequisites

- curl installed (`sudo apt install curl`)
- jq installed for JSON formatting (`sudo apt install jq`)

## Base URL

```
http://localhost:8081/api
```

---

## Books API

### List All Books

```bash
curl -s http://localhost:8081/api/books | jq .
```

### Get Book by ID

```bash
curl -s http://localhost:8081/api/books/1 | jq .
```

### Create a Book

```bash
curl -s -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Dune",
    "author": "Frank Herbert",
    "isbn": "9780441013593",
    "publisher": "Chilton Books",
    "publishedYear": 1965,
    "genre": "Sci-Fi",
    "readStatus": "UNREAD",
    "format": "PHYSICAL",
    "notes": "Classic science fiction epic"
  }' | jq .
```

### Create Book (Minimal)

```bash
curl -s -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"title": "Quick Book"}' | jq .
```

### Update a Book

```bash
curl -s -X PUT http://localhost:8081/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Dune",
    "author": "Frank Herbert",
    "readStatus": "READ"
  }' | jq .
```

### Update Single Field

```bash
curl -s -X PUT http://localhost:8081/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "Dune", "readStatus": "READ"}' | jq .
```

### Delete a Book

```bash
curl -s -X DELETE http://localhost:8081/api/books/1 -w "%{http_code}\n"
```

### Search Books by Title

```bash
curl -s "http://localhost:8081/api/books/search?title=Dune" | jq .
```

### Search (URL Encoded)

```bash
curl -s "http://localhost:8081/api/books/search?title=Game%20of%20Thrones" | jq .
```

### Filter Books

```bash
# By genre
curl -s "http://localhost:8081/api/books/filter?genre=Sci-Fi" | jq .

# By status
curl -s "http://localhost:8081/api/books/filter?status=READ" | jq .

# By format
curl -s "http://localhost:8081/api/books/filter?format=PHYSICAL" | jq .

# Combined filters
curl -s "http://localhost:8081/api/books/filter?genre=Fantasy&status=UNREAD&format=PHYSICAL" | jq .
```

### Count Books

```bash
curl -s http://localhost:8081/api/books | jq 'length'
```

### Get Book Titles Only

```bash
curl -s http://localhost:8081/api/books | jq '.[].title'
```

---

## Comics API

### List All Comics

```bash
curl -s http://localhost:8081/api/comics | jq .
```

### Get Comic by ID

```bash
curl -s http://localhost:8081/api/comics/1 | jq .
```

### Create a Comic

```bash
curl -s -X POST http://localhost:8081/api/comics \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Watchmen",
    "series": "Watchmen",
    "issueNumber": "1",
    "isbn": "9780930289232",
    "writer": "Alan Moore",
    "artist": "Dave Gibbons",
    "readStatus": "READ",
    "format": "PHYSICAL",
    "notes": "Groundbreaking graphic novel"
  }' | jq .
```

### Update a Comic

```bash
curl -s -X PUT http://localhost:8081/api/comics/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Watchmen",
    "readStatus": "READ"
  }' | jq .
```

### Delete a Comic

```bash
curl -s -X DELETE http://localhost:8081/api/comics/1 -w "%{http_code}\n"
```

### Search Comics by Title

```bash
curl -s "http://localhost:8081/api/comics/search?title=Batman" | jq .
```

### Filter Comics

```bash
# By status
curl -s "http://localhost:8081/api/comics/filter?status=UNREAD" | jq .

# By format
curl -s "http://localhost:8081/api/comics/filter?format=DIGITAL" | jq .

# Combined
curl -s "http://localhost:8081/api/comics/filter?status=READ&format=PHYSICAL" | jq .
```

---

## Video Games API

### List All Games

```bash
curl -s http://localhost:8081/api/games | jq .
```

### Get Game by ID

```bash
curl -s http://localhost:8081/api/games/1 | jq .
```

### Create a Game

```bash
curl -s -X POST http://localhost:8081/api/games \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Elden Ring",
    "isbn": "9780744016987",
    "console": "PS5",
    "publisher": "Bandai Namco",
    "developer": "FromSoftware",
    "genre": "Action RPG",
    "completionStatus": "PLAYING",
    "format": "PHYSICAL",
    "notes": "Open world masterpiece"
  }' | jq .
```

### Update a Game

```bash
curl -s -X PUT http://localhost:8081/api/games/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Elden Ring",
    "completionStatus": "BEATEN"
  }' | jq .
```

### Delete a Game

```bash
curl -s -X DELETE http://localhost:8081/api/games/1 -w "%{http_code}\n"
```

### Search Games by Title

```bash
curl -s "http://localhost:8081/api/games/search?title=Zelda" | jq .
```

### Filter Games

```bash
# By genre
curl -s "http://localhost:8081/api/games/filter?genre=RPG" | jq .

# By status
curl -s "http://localhost:8081/api/games/filter?status=BACKLOG" | jq .

# By console
curl -s "http://localhost:8081/api/games/filter?console=PS5" | jq .

# By publisher
curl -s "http://localhost:8081/api/games/filter?publisher=Nintendo" | jq .

# Combined
curl -s "http://localhost:8081/api/games/filter?console=Nintendo%20Switch&status=BEATEN&genre=Action-Adventure" | jq .
```

---

## Useful jq Filters

### Pretty Print

```bash
curl -s http://localhost:8081/api/books | jq .
```

### Count Items

```bash
curl -s http://localhost:8081/api/books | jq 'length'
```

### Get Specific Fields

```bash
curl -s http://localhost:8081/api/books | jq '.[] | {title, author, status: .readStatus}'
```

### Filter in jq

```bash
curl -s http://localhost:8081/api/books | jq '[.[] | select(.readStatus == "UNREAD")]'
```

### Get First N Items

```bash
curl -s http://localhost:8081/api/books | jq '.[0:5]'
```

### Sort by Field

```bash
curl -s http://localhost:8081/api/books | jq 'sort_by(.title)'
```

### Group by Field

```bash
curl -s http://localhost:8081/api/books | jq 'group_by(.genre) | map({genre: .[0].genre, count: length})'
```

---

## HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success (GET, PUT) |
| 201 | Created (POST) |
| 204 | No Content (DELETE) |
| 400 | Bad Request (validation error) |
| 404 | Not Found |
| 500 | Server Error |

---

## Enum Values

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

---

## Batch Operations Example

### Export All Data

```bash
mkdir -p backup
curl -s http://localhost:8081/api/books > backup/books.json
curl -s http://localhost:8081/api/comics > backup/comics.json
curl -s http://localhost:8081/api/games > backup/games.json
```

### Health Check

```bash
curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/api/books
```
