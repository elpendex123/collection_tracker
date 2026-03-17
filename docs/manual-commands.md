# Manual Commands Reference

This guide provides all commands to interact with the application manually, without using the provided scripts.

## Building the Application

### Clean Build

```bash
./gradlew clean build
```

### Build Without Tests

```bash
./gradlew clean build -x test
```

### Run Tests Only

```bash
./gradlew test
```

### Create Executable JAR

```bash
./gradlew bootJar
```

### Run the Application

```bash
./gradlew bootRun
```

Or using the JAR:

```bash
java -jar build/libs/collection-tracker-1.0.0-SNAPSHOT.jar
```

---

## Database Commands

### Connect to MySQL

```bash
mysql -u collectionuser -pcollectionpass collection_tracker
```

### Create Database

```bash
sudo mysql -e "CREATE DATABASE IF NOT EXISTS collection_tracker;"
```

### Create User

```bash
sudo mysql -e "CREATE USER 'collectionuser'@'localhost' IDENTIFIED BY 'collectionpass';"
sudo mysql -e "GRANT ALL PRIVILEGES ON collection_tracker.* TO 'collectionuser'@'localhost';"
sudo mysql -e "FLUSH PRIVILEGES;"
```

### View Tables

```bash
mysql -u collectionuser -pcollectionpass collection_tracker -e "SHOW TABLES;"
```

### Count Records

```bash
mysql -u collectionuser -pcollectionpass collection_tracker -e "SELECT 'books' AS type, COUNT(*) AS count FROM book UNION SELECT 'comics', COUNT(*) FROM comic UNION SELECT 'games', COUNT(*) FROM video_game;"
```

### Drop All Tables (Reset)

```bash
mysql -u collectionuser -pcollectionpass collection_tracker -e "DROP TABLE IF EXISTS book, comic, video_game;"
```

---

## REST API with curl

### Books

#### List All Books

```bash
curl -s http://localhost:8081/api/books | jq .
```

#### Get Book by ID

```bash
curl -s http://localhost:8081/api/books/1 | jq .
```

#### Create a Book

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
    "format": "PHYSICAL"
  }' | jq .
```

#### Update a Book

```bash
curl -s -X PUT http://localhost:8081/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Dune",
    "author": "Frank Herbert",
    "readStatus": "READ"
  }' | jq .
```

#### Delete a Book

```bash
curl -s -X DELETE http://localhost:8081/api/books/1
```

#### Search Books by Title

```bash
curl -s "http://localhost:8081/api/books/search?title=Dune" | jq .
```

#### Filter Books

```bash
curl -s "http://localhost:8081/api/books/filter?genre=Sci-Fi&status=READ&format=PHYSICAL" | jq .
```

### Comics

#### List All Comics

```bash
curl -s http://localhost:8081/api/comics | jq .
```

#### Create a Comic

```bash
curl -s -X POST http://localhost:8081/api/comics \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Watchmen",
    "series": "Watchmen",
    "issueNumber": "1",
    "writer": "Alan Moore",
    "artist": "Dave Gibbons",
    "readStatus": "READ",
    "format": "PHYSICAL"
  }' | jq .
```

#### Filter Comics

```bash
curl -s "http://localhost:8081/api/comics/filter?status=READ&format=PHYSICAL" | jq .
```

### Video Games

#### List All Games

```bash
curl -s http://localhost:8081/api/games | jq .
```

#### Create a Game

```bash
curl -s -X POST http://localhost:8081/api/games \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Elden Ring",
    "console": "PS5",
    "publisher": "Bandai Namco",
    "developer": "FromSoftware",
    "genre": "Action RPG",
    "completionStatus": "PLAYING",
    "format": "PHYSICAL"
  }' | jq .
```

#### Filter Games

```bash
curl -s "http://localhost:8081/api/games/filter?genre=RPG&status=BEATEN&console=PS5" | jq .
```

---

## Git Commands

### Initialize Repository

```bash
git init
```

### Add Remote

```bash
git remote add origin git@github.com:username/collection_tracker.git
```

### Stage Files

```bash
git add .
```

### Commit

```bash
git commit -m "Your commit message"
```

### Push

```bash
git push -u origin main
```

### Check Status

```bash
git status
```

### View Log

```bash
git log --oneline -10
```

---

## Process Management

### Find Running Application

```bash
ps aux | grep gradlew
```

Or:

```bash
lsof -i :8081
```

### Kill Application

```bash
pkill -f "gradlew bootRun"
```

Or by PID:

```bash
kill -9 <PID>
```

### Run in Background

```bash
nohup ./gradlew bootRun > app.log 2>&1 &
```

### View Logs

```bash
tail -f app.log
```
