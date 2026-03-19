# Collection Tracker Documentation

Welcome to the Collection Tracker documentation. This application helps you track your personal collection of books, comics, and video games.

## Project Status

**Phase 1: ✅ COMPLETE**

All core functionality is implemented and tested:
- ✅ MySQL database with 3 tables (books, comics, video_games)
- ✅ Spring Boot REST API with full CRUD operations
- ✅ Thymeleaf server-side HTML rendering
- ✅ Search and filter endpoints
- ✅ Bash CLI scripts for terminal access
- ✅ Jenkins CI/CD pipeline for automated builds
- ✅ 299 sample items loaded (99 books, 100 comics, 100 games)

**Phase 2: AWS EKS Deployment (Future)**
- Dockerization
- AWS ECR image registry
- Kubernetes manifests
- RDS MySQL database
- EKS cluster deployment

## Documentation Index

| Document | Description |
|----------|-------------|
| [Phase 1 Completion](phase-1-completion.md) | Detailed completion report for Phase 1 |
| [Architecture](architecture.md) | System architecture & design patterns |
| [Getting Started](getting-started.md) | Installation, setup, and running the application |
| [API Reference](api-reference.md) | Complete REST API documentation |
| [curl Guide](curl-api.md) | Interacting with the API using curl |
| [Python Guide](python-api.md) | Interacting with the API using Python |
| [MySQL Guide](mysql-guide.md) | Direct database operations |
| [Manual Commands](manual-commands.md) | All commands without using scripts |
| [Known Issues](known-issues.md) | Common issues and solutions |

## Quick Start

```bash
# 1. Set up database
sudo mysql -e "CREATE DATABASE collection_tracker;"
sudo mysql -e "CREATE USER 'collectionuser'@'localhost' IDENTIFIED BY 'collectionpass';"
sudo mysql -e "GRANT ALL PRIVILEGES ON collection_tracker.* TO 'collectionuser'@'localhost';"

# 2. Run the application
./gradlew bootRun

# 3. Open in browser
# http://localhost:8081
```

## Technology Stack

- **Backend**: Spring Boot 3.2.5, Java 17
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA / Hibernate
- **Frontend**: Thymeleaf (server-side), Bootstrap 5 CSS
- **Build**: Gradle 8.5
- **CI/CD**: Jenkins

## Project Structure

```
collection_tracker/
├── src/
│   └── main/
│       ├── java/com/enrique/collectiontracker/
│       │   ├── book/           # Book entity, repo, service, controllers
│       │   ├── comic/          # Comic entity, repo, service, controllers
│       │   ├── videogame/      # VideoGame entity, repo, service, controllers
│       │   └── common/         # Shared enums, base entity, exception handler
│       └── resources/
│           ├── templates/      # Thymeleaf HTML templates
│           └── static/css/     # CSS styles
├── scripts/                    # CLI bash scripts
├── data/                       # Sample CSV data
├── docs/                       # Documentation (you are here)
├── build.gradle
└── Jenkinsfile
```

## Support

For issues and questions, refer to the [Known Issues](known-issues.md) document first.
