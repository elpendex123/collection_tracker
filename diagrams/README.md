# UML Diagrams & Code Analysis Tools

This directory contains UML diagrams and architectural visualizations generated from the Collection Tracker codebase.

## Available Tools Summary

| Tool | Type | Best For | Status | Location |
|------|------|----------|--------|----------|
| **PlantUML** | Manual | Workflows, Architecture, Class diagrams | ✅ Installed | `plantuml/` |
| **Mermaid** | Manual | GitHub integration, Flowcharts | ✅ Available | `mermaid/` |
| **diagrams** | Code-Gen | AWS architecture, Infrastructure | ⚠️ Python (Optional) | `diagrams/` |
| **javadoc** | Built-in | Java documentation, API reference | ✅ Built-in | `javadoc/` |
| **Graphviz/dot** | Graph Engine | Dependency graphs, Flow diagrams | ✅ Installed | Used by PlantUML |
| **py2puml** | Auto-Gen | Python code to UML | ⚠️ Python (Optional) | `py2puml/` |

---

## Java-Specific Tools for Collection Tracker

### 1. **javadoc** (Built-in)
- Generates HTML API documentation from Java source
- Extracts class relationships
- Produces JavaDoc HTML output
- Command: `javadoc -d docs/javadoc src/main/java/...`

### 2. **PlantUML with Java Support**
- PlantUML is already installed
- Supports Java code reverse engineering via `skinparam`
- Can generate class diagrams from source
- Formats: PNG, SVG, PDF
- Command: `plantuml diagram.puml`

### 3. **Graphviz (dot)**
- Graph visualization engine
- Used by PlantUML for rendering
- Can generate dependency graphs
- Formats: PNG, SVG, PDF
- Command: `dot -Tpng graph.dot -o graph.png`

### 4. **Java Analysis Tools (Available for Installation)**

Optional tools if you want deeper code analysis:

```bash
# JDiff - Compare Java API versions
sudo apt-get install jdiff

# Checkstyle - Code style analyzer
sudo apt-get install checkstyle

# Spotbugs - Bug detector
sudo apt-get install spotbugs

# Architecture analysis with Gradle plugin
# Add to build.gradle: id 'org.javamodularity.moduleplugin' version '1.6.1'
```

---

## Collection Tracker Diagram Directory Structure

```
diagrams/
├── README.md (this file)
│
├── plantuml/
│   ├── class-diagram.puml          # Class relationships
│   ├── package-diagram.puml        # Package structure
│   ├── sequence-diagram.puml       # Request sequence
│   ├── component-diagram.puml      # System components
│   ├── deployment-diagram.puml     # Deployment architecture
│   └── *.png                       # Rendered outputs
│
├── mermaid/
│   ├── class-diagram.mmd           # Class diagram (Mermaid)
│   ├── entity-relationship.mmd     # Database ERD
│   ├── flowchart.mmd               # Request flow
│   ├── sequence-diagram.mmd        # Sequence diagram
│   └── state-diagram.mmd           # State transitions
│
├── diagrams/
│   ├── aws-architecture.py         # AWS Phase 2 architecture
│   ├── local-deployment.py         # Phase 1 local setup
│   └── *.png                       # Rendered outputs
│
└── javadoc/
    └── (Generated HTML API docs)
```

---

## How to Generate Diagrams

### Using PlantUML (Already Installed)

```bash
# Generate PNG from PlantUML file
plantuml diagrams/plantuml/class-diagram.puml -o ../

# Generate SVG (better quality)
plantuml diagrams/plantuml/class-diagram.puml -tsvg -o ../

# Generate all diagrams in a directory
plantuml diagrams/plantuml/*.puml
```

### Using Mermaid (Web-based, No Installation Needed)

```bash
# View in GitHub (automatic rendering)
# Or use Mermaid CLI if installed:
mmdc -i diagram.mmd -o diagram.png
```

### Using javadoc for Java API Documentation

```bash
# Generate HTML documentation from Java source
javadoc -d docs/javadoc \
  -sourcepath src/main/java \
  -subpackages com.enrique.collectiontracker

# Open in browser
firefox docs/javadoc/index.html
```

### Using Graphviz for Dependency Graphs

```bash
# Render dot file to image
dot -Tpng dependency.dot -o dependency.png
dot -Tsvg dependency.dot -o dependency.svg
```

---

## Diagram Types Available

### 1. **Class Diagrams**
- Shows entity classes and their relationships
- Attributes and methods
- Inheritance hierarchies
- Associations between classes

**Java Classes in Collection Tracker:**
- `Book`, `Comic`, `VideoGame` (Entities)
- `BookRepository`, `ComicRepository`, `VideoGameRepository`
- `BookService`, `ComicService`, `VideoGameService`
- `BookController`, `ComicController`, `VideoGameController`
- `BaseEntity`, Enums (`ReadStatus`, `Format`, `CompletionStatus`)

### 2. **Package Diagrams**
- Shows package structure
- Dependencies between packages
- Module organization

**Java Packages:**
```
com.enrique.collectiontracker/
├── book/
├── comic/
├── videogame/
├── common/
└── (root - HomeController)
```

### 3. **Sequence Diagrams**
- Shows interaction between components
- Request/response flow
- Method calls and returns

**Key Flows:**
- GET /api/books → Controller → Service → Repository → Database
- POST /api/books → Controller → Service → Validation → Save
- GET /api/books/search → Controller → Service → Specification → Query

### 4. **Component Diagrams**
- Shows major system components
- External systems (MySQL, Jenkins)
- Interfaces and dependencies

### 5. **Deployment Diagrams**
- Shows runtime architecture
- Phase 1: Local development setup
- Phase 2: AWS EKS deployment

### 6. **Entity Relationship Diagrams (ERD)**
- Database schema
- Table relationships
- Columns and constraints

### 7. **State Diagrams**
- State transitions in the system
- Item status changes (UNREAD → IN_PROGRESS → READ)

---

## Recommended Workflow

### Step 1: Create Diagram Source Files
Create `.puml` files in `plantuml/` directory describing your architecture

### Step 2: Generate Images
```bash
cd diagrams/plantuml/
plantuml *.puml -o ../
```

### Step 3: Reference in Documentation
```markdown
![Class Diagram](diagrams/plantuml/class-diagram.png)
```

### Step 4: Version Control
- Commit `.puml` source files (text-based, version control friendly)
- Optionally commit `.png` exports (for quick preview)
- Regenerate images if source changes

---

## Java Code Structure for Analysis

### Entity Classes
```
src/main/java/com/enrique/collectiontracker/
├── book/
│   ├── Book.java                 # Entity
│   ├── BookRepository.java       # Data access
│   ├── BookService.java          # Business logic
│   ├── BookController.java       # REST API
│   ├── BookViewController.java    # Thymeleaf views
│   └── BookSpecifications.java   # Query specifications
├── comic/
│   ├── Comic.java
│   ├── ComicRepository.java
│   ├── ComicService.java
│   ├── ComicController.java
│   ├── ComicViewController.java
│   └── ComicSpecifications.java
├── videogame/
│   ├── VideoGame.java
│   ├── VideoGameRepository.java
│   ├── VideoGameService.java
│   ├── VideoGameController.java
│   ├── VideoGameViewController.java
│   └── VideoGameSpecifications.java
└── common/
    ├── BaseEntity.java           # Abstract base
    ├── ReadStatus.java           # Enum
    ├── CompletionStatus.java     # Enum
    ├── Format.java               # Enum
    ├── ResourceNotFoundException.java
    ├── GlobalExceptionHandler.java
    └── HomeController.java
```

### Dependency Relationships
```
Controller → Service → Repository → Entity → Database
            ↓
         Specification
```

---

## Generating Class Diagrams from Java Source

### Using PlantUML with Java Source

PlantUML can be combined with external tools to reverse-engineer Java code:

```bash
# Option 1: Manual - Create PlantUML from source inspection
# Edit diagrams/plantuml/class-diagram.puml

# Option 2: Use ObjectAid (Eclipse) or similar IDE tools
# IntelliJ IDEA has built-in UML generation:
# Right-click class → Diagrams → Show Diagram

# Option 3: Use Javadoc + custom parsing
javadoc -doclet MyUMLDoclet src/main/java/...
```

### Custom Java Source Analysis Script

You can create a script to analyze the Java structure:

```bash
# Count classes, methods, fields
find src/main/java -name "*.java" | wc -l    # 18 classes

# List all classes
find src/main/java -name "*.java" -exec basename {} \;

# Show class hierarchy
grep -r "extends\|implements" src/main/java/*.java
```

---

## Next Steps

1. **PlantUML Diagrams** (Recommended - Already Installed)
   - Create `.puml` files for class, sequence, component diagrams
   - Generate PNG/SVG outputs
   - Reference in docs/ARCHITECTURE.md

2. **Mermaid Diagrams** (GitHub-Friendly)
   - Create `.mmd` files
   - Embedded in README files for automatic rendering
   - Great for flowcharts and ERDs

3. **Generate Javadoc**
   - Creates HTML API reference
   - Useful for developers using the API
   - `./gradlew javadoc`

4. **Dependency Analysis** (Optional)
   - Use Graphviz for import/dependency graphs
   - Shows package dependencies
   - Helps identify circular dependencies

---

## Tools Comparison for Your Project

| Need | Tool | Why |
|------|------|-----|
| Quick class diagrams | **PlantUML** | Already installed, powerful |
| GitHub integration | **Mermaid** | Auto-renders in markdown |
| API documentation | **javadoc** | Built-in, industry standard |
| AWS architecture | **diagrams** | Python, visual & code-based |
| Dependency graphs | **Graphviz** | Already installed |

---

## Example PlantUML Commands

```bash
# Generate PNG (high quality)
plantuml -Tpng diagrams/plantuml/class-diagram.puml

# Generate SVG (scalable)
plantuml -Tsvg diagrams/plantuml/class-diagram.puml

# Generate PDF
plantuml -Tpdf diagrams/plantuml/class-diagram.puml

# Specify output directory
plantuml diagrams/plantuml/*.puml -o ./output/

# Watch mode (auto-regenerate on save)
plantuml -gui diagrams/plantuml/
```

---

## Checklist for Complete Diagram Coverage

- [ ] Class diagram (entities and repositories)
- [ ] Package diagram (module organization)
- [ ] Sequence diagram (request flow)
- [ ] Component diagram (system parts)
- [ ] Deployment diagram (Phase 1 & Phase 2)
- [ ] Entity relationship diagram (database)
- [ ] State diagram (item status transitions)
- [ ] Javadoc API reference
- [ ] Dependency graph (optional)

---

**Directory Structure Ready** ✓

All subdirectories created and ready for diagram files:
- `plantuml/` - For PlantUML diagrams
- `mermaid/` - For Mermaid diagrams
- `diagrams/` - For Python diagrams (AWS)
- `javadoc/` - For generated documentation

---

Last Updated: March 19, 2026
