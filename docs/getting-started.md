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

## CI/CD with Jenkins

### Prerequisites

- Jenkins running locally (http://localhost:8080)
- JDK 17 configured in Jenkins Tools

### Initial Setup

1. Go to Jenkins Dashboard: http://localhost:8080/
2. Click **New Item**
3. Enter job name: `collection-tracker`
4. Select **Pipeline** and click OK

### Job Configuration

In the Pipeline section:

- **Definition**: Pipeline script from SCM
- **SCM**: Git
- **Repository URL**: `/home/enrique/PROJECTS/collection_tracker`
- **Branches**: `*/main`
- **Script Path**: `Jenkinsfile` (default)

Click **Save**

### Running Builds

```bash
# Manually trigger a build
# Go to http://localhost:8080/job/collection-tracker/
# Click "Build Now"
```

### Build Pipeline Stages

The Jenkinsfile executes:

1. **Checkout** - Clones the repository
2. **Build** - Runs `./gradlew clean build -x test`
3. **Test** - Runs `./gradlew test`
4. **Package** - Runs `./gradlew bootJar` (creates JAR artifact)

### Viewing Results

- Click build number in Build History
- View console output for logs
- Scroll down to see test reports and artifacts

### Troubleshooting Jenkins Builds

- **Java version mismatch**: Verify JDK 17 in Manage Jenkins → Tools Configuration
- **Port already in use**: Kill existing process or change port in application.properties
- **MySQL connection errors**: Ensure MySQL is running and credentials are correct
- **Permission denied on gradlew**: Run `chmod +x gradlew` in repository
