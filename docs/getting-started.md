# Getting Started

This guide covers how to set up and run the Collection Tracker application.

## Prerequisites

- Java 17+
- MySQL 8.0+
- Gradle 8.5+ (or use the included wrapper)

## Database Setup

### 1. Create the Database

```bash
sudo mysql -e "CREATE DATABASE IF NOT EXISTS collection_tracker;"
```

### 2. Create a Database User

```bash
sudo mysql -e "CREATE USER IF NOT EXISTS 'collectionuser'@'localhost' IDENTIFIED BY 'collectionpass';"
sudo mysql -e "GRANT ALL PRIVILEGES ON collection_tracker.* TO 'collectionuser'@'localhost';"
sudo mysql -e "FLUSH PRIVILEGES;"
```

### 3. Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/collection_tracker
spring.datasource.username=collectionuser
spring.datasource.password=collectionpass
```

## Running the Application

### Using Gradle Wrapper

```bash
./gradlew bootRun
```

### Building and Running the JAR

```bash
./gradlew bootJar
java -jar build/libs/collection-tracker-1.0.0-SNAPSHOT.jar
```

### Running in Background

```bash
nohup ./gradlew bootRun > app.log 2>&1 &
```

To stop the background process:

```bash
pkill -f "gradlew bootRun"
```

## Accessing the Application

Once running, the application is available at:

- **Web UI**: http://localhost:8081
- **REST API**: http://localhost:8081/api

### Web UI Routes

| Route | Description |
|-------|-------------|
| `/` | Home dashboard |
| `/books` | Books list |
| `/books/new` | Add new book |
| `/books/{id}` | Book details |
| `/books/{id}/edit` | Edit book |
| `/comics` | Comics list |
| `/comics/new` | Add new comic |
| `/comics/{id}` | Comic details |
| `/comics/{id}/edit` | Edit comic |
| `/games` | Games list |
| `/games/new` | Add new game |
| `/games/{id}` | Game details |
| `/games/{id}/edit` | Edit game |

## Loading Sample Data

### Using the Ingest Script

```bash
# Create virtual environment (first time only)
python3 -m venv venv
source venv/bin/activate
pip install requests

# Run ingest
python3 scripts/ingest_collection.py --base-url http://localhost:8081 --csv-dir data
```

### Using CLI Scripts

```bash
./scripts/books.sh list
./scripts/comics.sh list
./scripts/games.sh list
```

## Building for Production

```bash
./gradlew clean build
```

The JAR file will be in `build/libs/`.

## Running Tests

```bash
./gradlew test
```

Test reports are generated in `build/reports/tests/test/index.html`.
