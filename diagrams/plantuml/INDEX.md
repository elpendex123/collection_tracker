# PlantUML Diagrams - Collection Tracker

All diagrams have been generated from `.puml` source files using PlantUML.

## Generated Diagrams

### 1. Class Diagram
**File**: `class-diagram.puml` → `Collection_Tracker_Class_Diagram.png`

Shows the Java class structure including:
- Entity classes (Book, Comic, VideoGame)
- Repository interfaces
- Service classes
- Controllers (REST and Thymeleaf)
- Enumerations and shared types
- Relationships and dependencies

**Lines**: 280+ lines of detailed class definition

---

### 2. Sequence Diagrams
**File**: `sequence-diagram.puml` → `Collection_Tracker_Sequence_Diagrams.png`

Illustrates request flow for key operations:
- **CREATE** (POST /api/books) - Full flow from controller to database
- **READ** (GET /api/books/{id}) - Single item retrieval
- **SEARCH** (GET /api/books/search?title=) - Title search flow
- **FILTER** (GET /api/books/filter?genre=Sci-Fi) - Advanced filtering
- **UPDATE** (PUT /api/books/{id}) - Update operation
- **DELETE** (DELETE /api/books/{id}) - Delete operation
- **ERROR HANDLING** - Exception flow through GlobalExceptionHandler

Each sequence shows:
- Participant components (Client, Controller, Service, Repository, Database)
- Method calls and returns
- SQL generation
- Error paths

**Lines**: 350+ lines with detailed interactions

---

### 3. Package Diagram
**File**: `package-diagram.puml` → `Collection_Tracker_Package_Diagram.png`

Shows the layered architecture:
- **Presentation Layer**: Thymeleaf views, View Controllers, REST Controllers
- **Service Layer**: BookService, ComicService, VideoGameService
- **Repository Layer**: Repositories, Specifications, JPA
- **Entity Layer**: Entities, Enumerations, Base classes
- **Common**: Exception handlers, shared utilities
- **External**: Spring Framework, Bootstrap, MySQL

Clearly shows dependencies between layers

**Lines**: 120+ lines

---

### 4. Component Diagram
**File**: `component-diagram.puml` → `Collection_Tracker_Component_Diagram.png`

Shows runtime components and their interactions:
- Client layer (Web Browser, API Client, CLI)
- Request handling (DispatcherServlet, Interceptors)
- Web layer (All controllers)
- Service layer
- Data access (Repositories, Specifications)
- ORM layer (Hibernate, Spring Data JPA)
- View rendering (Thymeleaf)
- Exception handling
- External systems (MySQL, Jenkins, Gradle)
- Frontend assets (Bootstrap, CSS)

**Lines**: 100+ lines

---

### 5. Deployment Diagrams
**File**: `deployment-diagram.puml` → `Collection_Tracker_Deployment.png`

Two deployment scenarios:

**Phase 1: Local Development**
- Developer machine with IDE
- Local Spring Boot application (port 8081)
- Local MySQL (localhost:3306)
- Local Jenkins (port 8080)
- Gradle build system
- GitHub synchronization

**Phase 2: AWS EKS Deployment**
- AWS ECR (Docker registry)
- Amazon EKS (Kubernetes cluster)
  - Master node
  - 3 Worker nodes with pods
  - Kubernetes Service (Load Balancer)
- Amazon RDS (Managed MySQL)
- AWS CloudWatch (Monitoring)
- Application Load Balancer (ALB)
- SSL/TLS certificates (AWS Certificate Manager)
- CI/CD pipeline integration

Shows migration path from local to cloud

**Lines**: 150+ lines

---

### 6. Entity Relationship Diagram (ERD)
**File**: `entity-relationship-diagram.puml` → `Collection_Tracker_ERD.png`

Database schema visualization:
- **book table**: 13 columns, 99 records
- **comic table**: 12 columns, 100 records
- **video_game table**: 13 columns, 100 records

For each entity shows:
- All columns with types
- Primary key constraints
- Enum value options
- Indexes for performance
- Constraints and relationships
- Current record counts

Includes documentation for:
- Data type mappings
- Enum storage strategy
- Indexing strategy
- Duplicate detection logic

**Lines**: 200+ lines

---

## File Statistics

| Diagram | Source | PNG Size | Complexity |
|---------|--------|----------|------------|
| Class Diagram | class-diagram.puml (9.0K) | 5.4K | Very High |
| Sequence Diagrams | sequence-diagram.puml (6.3K) | 5.8K | Very High |
| Package Diagram | package-diagram.puml (3.3K) | 5.5K | Medium |
| Component Diagram | component-diagram.puml (4.2K) | 5.6K | Medium |
| Deployment Diagram | deployment-diagram.puml (4.1K) | 5.4K | High |
| ERD | entity-relationship-diagram.puml (4.7K) | 5.5K | High |

**Total Source Code**: 32.3K across 6 files
**Total PNG Output**: 33.2K across 6 diagrams
**Total Lines of Diagram Code**: 1000+

---

## How to Use These Diagrams

### View in Documentation
Include in markdown with relative paths:
```markdown
![Class Diagram](diagrams/plantuml/Collection_Tracker_Class_Diagram.png)
```

### Regenerate if Source Changes
```bash
cd diagrams/plantuml/
plantuml -Tpng *.puml          # Generate PNG
plantuml -Tsvg *.puml          # Generate SVG
plantuml -Tpdf *.puml          # Generate PDF
```

### Customize Appearance
Edit `.puml` files to:
- Change colors (skinparam)
- Add/remove details
- Reorder components
- Update relationships

### Convert to Other Formats
```bash
plantuml diagram.puml -Tsvg    # Scalable Vector Graphics
plantuml diagram.puml -Tpdf    # PDF (vector)
plantuml diagram.puml -Ttxt    # ASCII art
```

---

## PlantUML Features Used

✓ Object-oriented relationships (extends, implements)
✓ Bidirectional associations
✓ Composition and aggregation
✓ Sequence interactions (participant, message, activation)
✓ Component connections
✓ Deployment nodes
✓ Entity attributes and constraints
✓ Notes and documentation
✓ Color schemes (skinparam)
✓ Themes
✓ Multi-part diagrams (same source file)

---

## Diagram Coverage

- ✅ Class structure (Java OOP)
- ✅ Request/response sequences (Interactions)
- ✅ Package organization (Modules)
- ✅ Runtime components (System view)
- ✅ Deployment architecture (Infrastructure)
- ✅ Data schema (Database design)

---

## Next Steps

1. **Add more detailed notes** to diagrams
2. **Create activity diagrams** for complex workflows
3. **Generate state diagrams** for item status transitions
4. **Add use case diagrams** for user interactions
5. **Create timing diagrams** for performance analysis

---

**Generated**: March 19, 2026
**Tool**: PlantUML
**Format**: PNG (also available as SVG on request)
**Total Diagrams**: 6 comprehensive UML diagrams
