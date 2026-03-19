# Collection Tracker - Architecture Documentation

This document contains UML diagrams and architectural illustrations of the Collection Tracker application.

---

## 1. Entity Relationship Diagram (ERD)

Shows the database schema and relationships between tables.

```
┌─────────────────────────────────────┐
│            book                     │
├─────────────────────────────────────┤
│ id (PK)                             │
│ title (NOT NULL)                    │
│ author                              │
│ isbn (UNIQUE)                       │
│ publisher                           │
│ published_year                      │
│ genre                               │
│ read_status (ENUM)                  │
│ format (ENUM)                       │
│ notes                               │
│ created_at (AUTO)                   │
│ updated_at (AUTO)                   │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│            comic                    │
├─────────────────────────────────────┤
│ id (PK)                             │
│ title (NOT NULL)                    │
│ isbn                                │
│ series                              │
│ issue_number                        │
│ writer                              │
│ artist                              │
│ read_status (ENUM)                  │
│ format (ENUM)                       │
│ notes                               │
│ created_at (AUTO)                   │
│ updated_at (AUTO)                   │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│          video_game                 │
├─────────────────────────────────────┤
│ id (PK)                             │
│ title (NOT NULL)                    │
│ isbn                                │
│ console                             │
│ publisher                           │
│ developer                           │
│ genre                               │
│ completion_status (ENUM)            │
│ format (ENUM)                       │
│ notes                               │
│ created_at (AUTO)                   │
│ updated_at (AUTO)                   │
└─────────────────────────────────────┘

Legend:
- PK = Primary Key
- ENUM = Enumerated type
- AUTO = Auto-managed by JPA
- Each table inherits created_at/updated_at from BaseEntity
```

---

## 2. Layered Architecture Diagram

Shows the Spring Boot application's layered structure.

```
┌───────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                        │
├───────────────────────────────────────────────────────────────┤
│  Thymeleaf Templates      │      REST Controllers             │
│  ├─ index.html           │  ├─ BookController                │
│  ├─ books/list.html      │  ├─ ComicController              │
│  ├─ comics/list.html     │  ├─ VideoGameController          │
│  ├─ games/list.html      │  └─ @RestController (@RequestBody)
│  ├─ books/form.html      │                                    │
│  ├─ comics/form.html     │  View Controllers                 │
│  └─ games/form.html      │  ├─ BookViewController            │
│                           │  ├─ ComicViewController          │
│  HTTP: 8081              │  ├─ VideoGameViewController       │
└───────────────────────────────────────────────────────────────┘
                                    ↓
┌───────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                             │
├───────────────────────────────────────────────────────────────┤
│  BookService             ComicService          VideoGameService│
│  ├─ findAll()            ├─ findAll()          ├─ findAll()   │
│  ├─ findById()           ├─ findById()         ├─ findById()  │
│  ├─ save()               ├─ save()             ├─ save()      │
│  ├─ deleteById()         ├─ deleteById()       ├─ deleteById()│
│  ├─ searchByTitle()      ├─ searchByTitle()    ├─ searchByTitle()
│  └─ filter()             └─ filter()           └─ filter()    │
│                                                                 │
│  Business Logic & Validation                                   │
└───────────────────────────────────────────────────────────────┘
                                    ↓
┌───────────────────────────────────────────────────────────────┐
│                     REPOSITORY LAYER                           │
├───────────────────────────────────────────────────────────────┤
│  BookRepository          ComicRepository       VideoGameRepository
│  extends                 extends                extends        │
│  JpaRepository<Book, Long>  JpaRepository<Comic, Long>       │
│  JpaSpecificationExecutor   JpaSpecificationExecutor         │
│                                                                 │
│  Derived Query Methods:                                        │
│  ├─ findByTitle...()                                          │
│  ├─ findByStatus...()                                         │
│  ├─ findByFormat...()                                         │
│  ├─ findByGenre...() [Books & Games]                         │
│  ├─ findByConsole...() [Games only]                          │
│  └─ existsByIsbnAndTitle...()                                │
│                                                                 │
│  Specifications for Complex Queries                            │
│  ├─ BookSpecifications                                         │
│  ├─ ComicSpecifications                                        │
│  └─ VideoGameSpecifications                                    │
└───────────────────────────────────────────────────────────────┘
                                    ↓
┌───────────────────────────────────────────────────────────────┐
│                       DATA LAYER                               │
├───────────────────────────────────────────────────────────────┤
│                    MySQL Database (8.0+)                       │
│              collection_tracker (Database)                     │
│  ┌──────────────────────────────────────────────────────┐    │
│  │ • book table (99 rows)                               │    │
│  │ • comic table (100 rows)                             │    │
│  │ • video_game table (100 rows)                        │    │
│  │ • Indexes on id, isbn, title                         │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                                 │
│  ORM: Spring Data JPA / Hibernate 6.4.4                       │
└───────────────────────────────────────────────────────────────┘
```

---

## 3. Spring Boot Application Architecture (Detailed)

```
┌─────────────────────────────────────────────────────────────────┐
│  CollectionTrackerApplication (Main Entry Point)                │
│  @SpringBootApplication                                         │
└────────────────────────┬────────────────────────────────────────┘
                         │
         ┌───────────────┼───────────────┐
         │               │               │
    ┌────▼──────┐  ┌────▼──────┐  ┌────▼──────┐
    │ book pkg  │  │ comic pkg │  │ videogame │
    └────┬──────┘  └────┬──────┘  └────┬──────┘
         │               │               │
    ┌────┴──────────────┴───────────────┴────┐
    │                                         │
    │  Common Package:                        │
    │  ├─ BaseEntity (Abstract)              │
    │  ├─ ReadStatus (Enum)                  │
    │  ├─ CompletionStatus (Enum)            │
    │  ├─ Format (Enum)                      │
    │  ├─ ResourceNotFoundException           │
    │  ├─ GlobalExceptionHandler             │
    │  └─ HomeController                     │
    └────┬──────────────────────────────────┘
         │
    ┌────▼──────────────────────────────────┐
    │  Core Components:                      │
    │                                        │
    │  Entity Layer:                         │
    │  ├─ Book.java                          │
    │  ├─ Comic.java                         │
    │  └─ VideoGame.java                     │
    │                                        │
    │  Repository Layer:                     │
    │  ├─ BookRepository                     │
    │  ├─ ComicRepository                    │
    │  ├─ VideoGameRepository                │
    │  ├─ BookSpecifications                 │
    │  ├─ ComicSpecifications                │
    │  └─ VideoGameSpecifications            │
    │                                        │
    │  Service Layer:                        │
    │  ├─ BookService                        │
    │  ├─ ComicService                       │
    │  └─ VideoGameService                   │
    │                                        │
    │  Controller Layer:                     │
    │  ├─ BookController (REST)              │
    │  ├─ ComicController (REST)             │
    │  ├─ VideoGameController (REST)         │
    │  ├─ BookViewController                 │
    │  ├─ ComicViewController                │
    │  └─ VideoGameViewController            │
    │                                        │
    │  View Layer:                           │
    │  ├─ layout.html (Master template)      │
    │  ├─ index.html (Home)                  │
    │  ├─ books/* (Book views)               │
    │  ├─ comics/* (Comic views)             │
    │  └─ games/* (Game views)               │
    └────┬──────────────────────────────────┘
         │
         ▼
    MySQL Database @ localhost:3306
```

---

## 4. REST API Architecture

```
┌────────────────────────────────────────────────────────────┐
│              REST API Endpoints Structure                   │
└────────────────────┬───────────────────────────────────────┘
                     │
    ┌────────────────┼────────────────┐
    │                │                │
    ▼                ▼                ▼
/api/books       /api/comics      /api/games
    │                │                │
    ├─GET /          ├─GET /          ├─GET /
    │ (list all)     │ (list all)     │ (list all)
    │                │                │
    ├─GET /{id}      ├─GET /{id}      ├─GET /{id}
    │ (get one)      │ (get one)      │ (get one)
    │                │                │
    ├─POST /         ├─POST /         ├─POST /
    │ (create)       │ (create)       │ (create)
    │                │                │
    ├─PUT /{id}      ├─PUT /{id}      ├─PUT /{id}
    │ (update)       │ (update)       │ (update)
    │                │                │
    ├─DELETE /{id}   ├─DELETE /{id}   ├─DELETE /{id}
    │ (delete)       │ (delete)       │ (delete)
    │                │                │
    ├─GET/search     ├─GET/search     ├─GET/search
    │ ?title=query   │ ?title=query   │ ?title=query
    │ (search)       │ (search)       │ (search)
    │                │                │
    └─GET/filter     └─GET/filter     └─GET/filter
      ?genre=x        ?status=x        ?console=x
      &status=y       &format=y        &genre=y
      &format=z                        &status=z
      (advanced)      (advanced)       (advanced)

┌────────────────────────────────────────────────────────────┐
│  Response Format (JSON)                                     │
├────────────────────────────────────────────────────────────┤
│                                                              │
│  Single Item (GET /{id}):                                  │
│  {                                                          │
│    "id": 1,                                                │
│    "title": "Dune",                                        │
│    "createdAt": "2026-03-17T02:49:51",                    │
│    "updatedAt": "2026-03-17T02:49:51",                    │
│    ... (other fields)                                      │
│  }                                                          │
│                                                              │
│  Multiple Items (GET / or search/filter):                  │
│  [                                                          │
│    { "id": 1, "title": "Dune", ... },                    │
│    { "id": 2, "title": "Foundation", ... },              │
│    ...                                                      │
│  ]                                                          │
│                                                              │
│  Status Codes:                                             │
│  • 200 OK - Success (GET, PUT)                            │
│  • 201 Created - Success (POST)                           │
│  • 204 No Content - Success (DELETE)                      │
│  • 400 Bad Request - Validation error                     │
│  • 404 Not Found - Resource doesn't exist                 │
│  • 500 Internal Server Error                              │
│                                                              │
└────────────────────────────────────────────────────────────┘
```

---

## 5. Component Interaction Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                     USER REQUESTS                             │
├──────────────────────┬──────────────────┬────────────────────┤
│                      │                  │                    │
▼                      ▼                  ▼                     ▼
Web Browser     REST API Client      CLI Scripts          Direct
(Thymeleaf)     (curl/Postman)       (bash)              HTTP Call
                                                          (testing)
    │                  │                 │                     │
    └──────────────────┼─────────────────┴─────────────────────┘
                       │
                       ▼
        ┌──────────────────────────────┐
        │   Spring DispatcherServlet   │
        │   @RequestMapping routing    │
        └──────────┬───────────────────┘
                   │
        ┌──────────┴──────────┐
        │                     │
        ▼                     ▼
┌──────────────────┐  ┌──────────────────┐
│  View Controller │  │  REST Controller │
│  (Thymeleaf)     │  │  (JSON)          │
├──────────────────┤  ├──────────────────┤
│ BookViewController│  │ BookController   │
│ ComicViewControl │  │ ComicController  │
│ GameViewController│  │ GameController   │
└────────┬─────────┘  └────────┬─────────┘
         │                     │
         └──────────┬──────────┘
                    │
                    ▼
        ┌──────────────────────────┐
        │      SERVICE LAYER       │
        │                          │
        │  BookService             │
        │  ComicService            │
        │  VideoGameService        │
        │                          │
        │ • Validation             │
        │ • Business Logic         │
        │ • Search/Filter Logic    │
        └────────┬─────────────────┘
                 │
                 ▼
        ┌──────────────────────────┐
        │    REPOSITORY LAYER      │
        │                          │
        │  BookRepository          │
        │  ComicRepository         │
        │  VideoGameRepository     │
        │                          │
        │  + Specifications        │
        │  for complex queries     │
        └────────┬─────────────────┘
                 │
         ┌───────┴───────┐
         │               │
         ▼               ▼
    Spring Data    Hibernate
    JPA Interface   ORM Layer
         │               │
         └───────┬───────┘
                 │
                 ▼
    ┌────────────────────────────┐
    │   JDBC Driver              │
    │   (MySQL Connector)        │
    └────────┬───────────────────┘
             │
             ▼
    ┌────────────────────────────┐
    │   MySQL Database           │
    │   (collection_tracker)     │
    │                            │
    │   • book table             │
    │   • comic table            │
    │   • video_game table       │
    └────────────────────────────┘
```

---

## 6. Request Flow Diagram

```
User Action → Browser/API Client
       │
       ▼
HTTP Request (GET/POST/PUT/DELETE)
       │
       ▼
Spring DispatcherServlet
       │
       ├─ @RequestMapping matches endpoint
       │
       ▼
Controller Layer
       │
       ├─ Extract parameters
       ├─ Validate input
       ├─ Call appropriate service method
       │
       ▼
Service Layer
       │
       ├─ Implement business logic
       ├─ Validate business rules
       ├─ Call repository methods
       │
       ▼
Repository Layer (JPA)
       │
       ├─ Create or use existing query
       ├─ Use Specifications for filtering
       ├─ Call Hibernate ORM
       │
       ▼
Hibernate ORM
       │
       ├─ Convert Java objects ↔ SQL
       ├─ Execute SQL queries
       │
       ▼
JDBC / MySQL Connector
       │
       ├─ Execute SQL on MySQL
       │
       ▼
MySQL Database
       │
       ├─ Query execution
       ├─ Result set generation
       │
       ▼
(Response flows back up through layers)
       │
       ▼
Controller
       │
       ├─ REST API: Format as JSON
       ├─ View Controller: Render Thymeleaf
       │
       ▼
HTTP Response
       │
       ▼
Browser/API Client (Display result)
```

---

## 7. CLI Scripts Architecture

```
┌─────────────────────────────────────────────────────┐
│  User Terminal                                       │
│  ./scripts/books.sh <command> [args]                │
└────────────────┬────────────────────────────────────┘
                 │
        ┌────────┴───────────┐
        │                    │
        ▼                    ▼
    ┌────────┐          ┌────────┐
    │ Bash   │          │  jq    │
    │Script  │          │JSON    │
    │Parser  │          │Parser  │
    └────┬───┘          └───┬────┘
         │                  │
         ▼                  │
    curl HTTP Request       │
    (uses jq formatted output)
         │                  │
         └──────┬───────────┘
                │
                ▼
    Spring Boot Application
    (REST API: http://localhost:8081/api)
                │
                ▼
        REST Controller
        (JSON Response)
                │
                ▼
    ┌───────────────────┐
    │ curl captures     │
    │ JSON response     │
    └───────┬───────────┘
            │
            ▼
    ┌───────────────────┐
    │ jq formats JSON   │
    │ for readability   │
    └───────┬───────────┘
            │
            ▼
    Display in Terminal
```

---

## 8. Deployment Architecture

### Phase 1: Local Development

```
┌─────────────────────────────────────────────────┐
│         Developer's Machine (Linux)             │
├─────────────────────────────────────────────────┤
│                                                 │
│  ┌─────────────────────────────────────────┐  │
│  │  Spring Boot Application                │  │
│  │  • Running on port 8081                 │  │
│  │  • Java 17 runtime                      │  │
│  │  • Gradle build system                  │  │
│  └──────────────┬──────────────────────────┘  │
│                 │                             │
│                 ▼                             │
│  ┌─────────────────────────────────────────┐  │
│  │  MySQL Database                         │  │
│  │  • localhost:3306                       │  │
│  │  • collection_tracker database          │  │
│  │  • collectionuser credentials           │  │
│  └─────────────────────────────────────────┘  │
│                                                 │
│  ┌─────────────────────────────────────────┐  │
│  │  Jenkins (CI/CD)                        │  │
│  │  • Running on port 8080                 │  │
│  │  • collection-tracker job               │  │
│  │  • Pipeline: Checkout→Build→Test→      │  │
│  │    Package→Archive                      │  │
│  └─────────────────────────────────────────┘  │
│                                                 │
│  ┌─────────────────────────────────────────┐  │
│  │  Git Repository                         │  │
│  │  • Local: /home/enrique/PROJECTS/...   │  │
│  │  • Remote: GitHub elpendex123/...      │  │
│  └─────────────────────────────────────────┘  │
│                                                 │
│  Access:                                       │
│  • Web UI: http://localhost:8081              │
│  • API: http://localhost:8081/api             │
│  • Jenkins: http://localhost:8080             │
└─────────────────────────────────────────────────┘
```

### Phase 2: AWS EKS Deployment (Planned)

```
┌──────────────────────────────────────────────────┐
│            AWS Cloud Infrastructure              │
├──────────────────────────────────────────────────┤
│                                                  │
│  ┌─────────────────────────────────────────┐   │
│  │  AWS ECR (Elastic Container Registry)   │   │
│  │  • Docker image: collection-tracker     │   │
│  │  • Versioned tags                       │   │
│  └──────────────┬────────────────────────┬─┘   │
│                 │                        │     │
│                 ▼                        ▼     │
│  ┌───────────────────────────────────────────┐ │
│  │  AWS EKS (Kubernetes Cluster)             │ │
│  │                                           │ │
│  │  ┌─────────────────────────────────────┐ │ │
│  │  │  Pods                               │ │ │
│  │  │  ├─ collection-tracker-pod-1        │ │ │
│  │  │  ├─ collection-tracker-pod-2        │ │ │
│  │  │  └─ collection-tracker-pod-3        │ │ │
│  │  │  (Running Docker containers)        │ │ │
│  │  └─────────────────────────────────────┘ │ │
│  │                                           │ │
│  │  ┌─────────────────────────────────────┐ │ │
│  │  │  Kubernetes Service (Load Balancer) │ │ │
│  │  │  • Distributes traffic to pods      │ │ │
│  │  │  • Public endpoint                  │ │ │
│  │  └─────────────────────────────────────┘ │ │
│  └───────────────────────────────────────────┘ │
│                                                  │
│  ┌─────────────────────────────────────────┐   │
│  │  AWS RDS (Managed MySQL)                │   │
│  │  • collection_tracker database          │   │
│  │  • Multi-AZ deployment                  │   │
│  │  • Automated backups                    │   │
│  └─────────────────────────────────────────┘   │
│                                                  │
│  ┌─────────────────────────────────────────┐   │
│  │  AWS CloudWatch                         │   │
│  │  • Logs aggregation                     │   │
│  │  • Metrics & monitoring                 │   │
│  └─────────────────────────────────────────┘   │
│                                                  │
│  Access:                                        │
│  • Web UI: https://app.example.com             │
│  • API: https://api.example.com/api            │
└──────────────────────────────────────────────────┘
```

---

## 9. Data Flow Through Application

```
CREATE (POST /api/books)
┌─────────────────────────────────────────┐
│ Client sends JSON:                      │
│ {                                       │
│   "title": "Dune",                      │
│   "author": "Frank Herbert",            │
│   ...                                   │
│ }                                       │
└────────────┬────────────────────────────┘
             │
             ▼
      BookController
      ├─ @PostMapping("/")
      ├─ Receives @RequestBody
      ├─ Calls bookService.save(book)
      │
      ▼
      BookService
      ├─ Validates book object
      ├─ Calls bookRepository.save(book)
      │
      ▼
      BookRepository
      ├─ Calls JPA save()
      ├─ Hibernate generates INSERT SQL
      │
      ▼
      MySQL
      ├─ Executes: INSERT INTO book (...)
      ├─ Generates auto-increment ID
      ├─ Records created_at timestamp
      │
      ▼
      (Response flows back)
      │
      ▼
      BookService returns Book object
      │
      ▼
      BookController
      ├─ Returns ResponseEntity
      ├─ Status: 201 CREATED
      ├─ Body: Book as JSON
      │
      ▼
      Client receives JSON response
      {
        "id": 100,
        "title": "Dune",
        "author": "Frank Herbert",
        "createdAt": "2026-03-19T...",
        ...
      }

SEARCH (GET /api/books/search?title=Dune)
┌─────────────────────────────────────────┐
│ Client sends query parameter             │
│ GET /api/books/search?title=Dune        │
└────────────┬────────────────────────────┘
             │
             ▼
      BookController
      ├─ @GetMapping("/search")
      ├─ Receives @RequestParam String title
      ├─ Calls bookService.searchByTitle(title)
      │
      ▼
      BookService
      ├─ Calls bookRepository.findByTitleContainingIgnoreCase(title)
      │
      ▼
      BookRepository
      ├─ Spring Data JPA creates query:
      ├─ SELECT * FROM book
      │  WHERE LOWER(title) LIKE LOWER('%Dune%')
      │
      ▼
      MySQL
      ├─ Executes query
      ├─ Returns matching rows
      │
      ▼
      (Response flows back)
      │
      ▼
      Hibernate converts ResultSet to List<Book>
      │
      ▼
      BookService returns List<Book>
      │
      ▼
      BookController
      ├─ Serializes List<Book> to JSON array
      │
      ▼
      Client receives JSON array
      [
        { "id": 1, "title": "Dune", "author": "Frank Herbert", ... },
        { "id": 12, "title": "Dune Messiah", "author": "Frank Herbert", ... },
        ...
      ]

FILTER (GET /api/books/filter?genre=Sci-Fi&status=UNREAD)
┌──────────────────────────────────────────────┐
│ Client sends multiple query parameters       │
│ GET /api/books/filter?genre=Sci-Fi&         │
│     status=UNREAD                            │
└────────────┬─────────────────────────────────┘
             │
             ▼
      BookController
      ├─ @GetMapping("/filter")
      ├─ Receives @RequestParam(required=false) String genre
      ├─ Receives @RequestParam(required=false) ReadStatus status
      ├─ Receives @RequestParam(required=false) Format format
      ├─ Calls bookService.filter(genre, status, format)
      │
      ▼
      BookService
      ├─ Creates Specification<Book> using:
      │  Specification.where(hasGenre(genre))
      │    .and(hasStatus(status))
      │    .and(hasFormat(format))
      ├─ Calls bookRepository.findAll(spec)
      │
      ▼
      BookRepository
      ├─ Spring Data JPA + Specifications
      ├─ Builds complex WHERE clause:
      ├─ WHERE genre = 'Sci-Fi'
      │  AND read_status = 'UNREAD'
      │  AND format IS NULL (null check for format)
      │
      ▼
      MySQL
      ├─ Executes filtered query
      ├─ Returns matching rows
      │
      ▼
      (Response flows back through layers)
      │
      ▼
      Client receives filtered JSON array
      [
        { "id": 3, "title": "Neuromancer", "genre": "Sci-Fi", "readStatus": "UNREAD", ... },
        ...
      ]
```

---

## 10. Error Handling Flow

```
┌──────────────────────────────────────┐
│  Client Request                      │
│  GET /api/books/999 (non-existent)   │
└────────────┬───────────────────────┬─┘
             │                       │
             ▼                       ▼
      BookController             BookController
      ├─ Calls service.findById(999)
      │
      ▼
      BookService
      ├─ Calls repository.findById(999)
      ├─ Returns Optional.empty()
      │
      ▼
      BookController
      ├─ .orElseThrow(() →
      │   new ResourceNotFoundException(...))
      │
      ▼
      Exception thrown:
      ResourceNotFoundException

      ┌────────────────────────────────┐
      │ GlobalExceptionHandler         │
      │ @ControllerAdvice              │
      │ @ExceptionHandler              │
      │ (ResourceNotFoundException)     │
      └────────────┬───────────────────┘
                   │
                   ▼
            Generates ResponseEntity
            ├─ Status: 404 NOT FOUND
            ├─ Body: Error message JSON
            │  {
            │    "error": "Book not found with id: 999",
            │    "timestamp": "2026-03-19T...",
            │    "status": 404
            │  }
                   │
                   ▼
            Client receives error response
```

---

## 11. Authentication & Security (Current)

```
┌─────────────────────────────────────────────┐
│  Current Implementation (Phase 1)            │
├─────────────────────────────────────────────┤
│                                             │
│  Authentication: NONE                       │
│  ├─ Single user shared collection          │
│  ├─ No login required                       │
│  └─ No role-based access control           │
│                                             │
│  Security Measures Implemented:             │
│  ├─ Input validation (@NotBlank, @Size)    │
│  ├─ SQL Injection protection (JPA)         │
│  ├─ XSS protection (Thymeleaf escaping)    │
│  ├─ HTTPS ready (config in Phase 2)        │
│  └─ Error handling (no stack traces)       │
│                                             │
└─────────────────────────────────────────────┘

Future (Phase 2/3):
├─ JWT token-based authentication
├─ User roles: ADMIN, USER
├─ Collection ownership by user
├─ HTTPS/SSL enforcement
└─ API key for external access
```

---

## 12. System Components Summary

```
┌──────────────────────────────────────────────────────┐
│          COLLECTION TRACKER - SYSTEM OVERVIEW        │
├──────────────────────────────────────────────────────┤
│                                                      │
│  Frontend:                                          │
│  • HTML5 + Bootstrap 5 + CSS                       │
│  • Thymeleaf server-side rendering                 │
│  • Responsive design for all devices               │
│                                                      │
│  Backend:                                           │
│  • Java 17 + Spring Boot 3.2.5                     │
│  • Spring Data JPA + Hibernate                     │
│  • REST API with JSON serialization                │
│  • Service-based business logic                    │
│                                                      │
│  Database:                                          │
│  • MySQL 8.0+ (local: localhost:3306)             │
│  • 3 tables: book, comic, video_game              │
│  • 299+ sample records                             │
│                                                      │
│  Build & Deploy:                                    │
│  • Gradle 8.5 build automation                    │
│  • Jenkins CI/CD pipeline                         │
│  • GitHub version control                         │
│                                                      │
│  CLI & Scripting:                                   │
│  • Bash scripts wrapping curl                     │
│  • jq JSON formatting                             │
│  • Automated data ingest (Python)                 │
│                                                      │
└──────────────────────────────────────────────────────┘
```

---

## Architecture Principles

1. **Separation of Concerns**: Controller → Service → Repository layers
2. **DRY (Don't Repeat Yourself)**: Shared BaseEntity, common enums
3. **SOLID Principles**: Single responsibility, dependency injection
4. **RESTful Design**: Standard HTTP methods, meaningful URLs
5. **Database-Driven**: JPA for ORM, SQL expertise optional
6. **Scalability**: Ready for horizontal scaling in Phase 2
7. **Testability**: Service layer isolation for unit testing
8. **Security**: Input validation, exception handling, prepared statements

---

## Next Phase: AWS EKS Architecture

Phase 2 will extend this architecture with:

```
┌─────────────────────────────────────────────────┐
│  CI/CD Pipeline Enhancement                    │
├─────────────────────────────────────────────────┤
│                                                 │
│  Jenkins Pipeline (Enhanced):                  │
│  1. Checkout code from GitHub                 │
│  2. Build with Gradle                         │
│  3. Run tests                                 │
│  4. Build Docker image                        │
│  5. Push to AWS ECR                           │
│  6. Deploy to AWS EKS                         │
│  7. Run smoke tests                           │
│  8. Notify on success/failure                 │
│                                                 │
│  Monitoring & Logging:                        │
│  • CloudWatch for logs                        │
│  • Prometheus for metrics                     │
│  • Grafana for dashboards                     │
│  • ELK stack for advanced logging             │
│                                                 │
└─────────────────────────────────────────────────┘
```

---

**Last Updated**: March 19, 2026
