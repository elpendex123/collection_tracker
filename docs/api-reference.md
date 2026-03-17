# API Reference

Complete REST API documentation for the Collection Tracker application.

## Base URL

```
http://localhost:8081/api
```

## Content Type

All POST and PUT requests require:

```
Content-Type: application/json
```

---

## Books API

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/books` | List all books |
| GET | `/api/books/{id}` | Get book by ID |
| POST | `/api/books` | Create a book |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |
| GET | `/api/books/search?title={query}` | Search by title |
| GET | `/api/books/filter` | Filter books |

### Book Object

```json
{
  "id": 1,
  "title": "Dune",
  "author": "Frank Herbert",
  "isbn": "9780441013593",
  "publisher": "Chilton Books",
  "publishedYear": 1965,
  "genre": "Sci-Fi",
  "readStatus": "READ",
  "format": "PHYSICAL",
  "notes": "Classic science fiction epic",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Create Book Request

```json
{
  "title": "Dune",
  "author": "Frank Herbert",
  "isbn": "9780441013593",
  "publisher": "Chilton Books",
  "publishedYear": 1965,
  "genre": "Sci-Fi",
  "readStatus": "UNREAD",
  "format": "PHYSICAL",
  "notes": "Classic science fiction epic"
}
```

**Required fields:** `title`

### Filter Parameters

| Parameter | Type | Values |
|-----------|------|--------|
| genre | string | Any genre name |
| status | enum | UNREAD, IN_PROGRESS, READ |
| format | enum | PHYSICAL, DIGITAL |

---

## Comics API

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/comics` | List all comics |
| GET | `/api/comics/{id}` | Get comic by ID |
| POST | `/api/comics` | Create a comic |
| PUT | `/api/comics/{id}` | Update a comic |
| DELETE | `/api/comics/{id}` | Delete a comic |
| GET | `/api/comics/search?title={query}` | Search by title |
| GET | `/api/comics/filter` | Filter comics |

### Comic Object

```json
{
  "id": 1,
  "title": "Watchmen",
  "isbn": "9780930289232",
  "series": "Watchmen",
  "issueNumber": "1",
  "writer": "Alan Moore",
  "artist": "Dave Gibbons",
  "readStatus": "READ",
  "format": "PHYSICAL",
  "notes": "Groundbreaking graphic novel",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Create Comic Request

```json
{
  "title": "Watchmen",
  "series": "Watchmen",
  "issueNumber": "1",
  "isbn": "9780930289232",
  "writer": "Alan Moore",
  "artist": "Dave Gibbons",
  "readStatus": "UNREAD",
  "format": "PHYSICAL",
  "notes": "Groundbreaking graphic novel"
}
```

**Required fields:** `title`

### Filter Parameters

| Parameter | Type | Values |
|-----------|------|--------|
| status | enum | UNREAD, IN_PROGRESS, READ |
| format | enum | PHYSICAL, DIGITAL |

---

## Video Games API

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/games` | List all games |
| GET | `/api/games/{id}` | Get game by ID |
| POST | `/api/games` | Create a game |
| PUT | `/api/games/{id}` | Update a game |
| DELETE | `/api/games/{id}` | Delete a game |
| GET | `/api/games/search?title={query}` | Search by title |
| GET | `/api/games/filter` | Filter games |

### Video Game Object

```json
{
  "id": 1,
  "title": "Elden Ring",
  "isbn": "9780744016987",
  "console": "PS5",
  "publisher": "Bandai Namco",
  "developer": "FromSoftware",
  "genre": "Action RPG",
  "completionStatus": "BEATEN",
  "format": "PHYSICAL",
  "notes": "Open world masterpiece",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Create Game Request

```json
{
  "title": "Elden Ring",
  "console": "PS5",
  "publisher": "Bandai Namco",
  "developer": "FromSoftware",
  "genre": "Action RPG",
  "completionStatus": "BACKLOG",
  "format": "PHYSICAL",
  "notes": "Open world masterpiece"
}
```

**Required fields:** `title`

### Filter Parameters

| Parameter | Type | Values |
|-----------|------|--------|
| genre | string | Any genre name |
| status | enum | BACKLOG, PLAYING, BEATEN, DROPPED |
| console | string | PS5, Nintendo Switch, PC, Xbox Series X, etc. |
| publisher | string | Any publisher name |
| format | enum | PHYSICAL, DIGITAL |

---

## Enums

### ReadStatus

Used for books and comics.

| Value | Description |
|-------|-------------|
| UNREAD | Not yet read |
| IN_PROGRESS | Currently reading |
| READ | Finished reading |

### CompletionStatus

Used for video games.

| Value | Description |
|-------|-------------|
| BACKLOG | Not yet started |
| PLAYING | Currently playing |
| BEATEN | Completed |
| DROPPED | Abandoned |

### Format

Used for all collections.

| Value | Description |
|-------|-------------|
| PHYSICAL | Physical copy |
| DIGITAL | Digital copy |

---

## HTTP Status Codes

| Code | Description |
|------|-------------|
| 200 OK | Successful GET or PUT |
| 201 Created | Successful POST |
| 204 No Content | Successful DELETE |
| 400 Bad Request | Validation error |
| 404 Not Found | Resource not found |
| 500 Internal Server Error | Server error |

---

## Error Responses

### Validation Error (400)

```json
{
  "title": "Title is required"
}
```

### Not Found (404)

```json
{
  "error": "Book not found with id: 999"
}
```

---

## Examples

### Get all unread physical books

```
GET /api/books/filter?status=UNREAD&format=PHYSICAL
```

### Search for Batman comics

```
GET /api/comics/search?title=Batman
```

### Get PS5 games in backlog

```
GET /api/games/filter?console=PS5&status=BACKLOG
```

### Create a new book and mark as read

```bash
# Create
curl -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"title": "New Book", "author": "Author Name"}'

# Update status
curl -X PUT http://localhost:8081/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "New Book", "author": "Author Name", "readStatus": "READ"}'
```
