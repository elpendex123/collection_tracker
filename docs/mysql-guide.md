# MySQL Database Guide

This guide covers direct MySQL database operations for the Collection Tracker application.

## Connection Details

| Property | Value |
|----------|-------|
| Host | localhost |
| Port | 3306 |
| Database | collection_tracker |
| Username | collectionuser |
| Password | collectionpass |

## Connecting to MySQL

### Command Line

```bash
mysql -u collectionuser -pcollectionpass collection_tracker
```

### With Prompts

```bash
mysql -u collectionuser -p collection_tracker
```

---

## Database Schema

### Book Table

```sql
CREATE TABLE book (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    isbn VARCHAR(20),
    publisher VARCHAR(255),
    published_year INT,
    genre VARCHAR(100),
    read_status ENUM('UNREAD', 'IN_PROGRESS', 'READ'),
    format ENUM('PHYSICAL', 'DIGITAL'),
    notes TEXT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB;
```

### Comic Table

```sql
CREATE TABLE comic (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(20),
    series VARCHAR(255),
    issue_number VARCHAR(20),
    writer VARCHAR(255),
    artist VARCHAR(255),
    read_status ENUM('UNREAD', 'IN_PROGRESS', 'READ'),
    format ENUM('PHYSICAL', 'DIGITAL'),
    notes TEXT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB;
```

### Video Game Table

```sql
CREATE TABLE video_game (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(20),
    console VARCHAR(100),
    publisher VARCHAR(255),
    developer VARCHAR(255),
    genre VARCHAR(100),
    completion_status ENUM('BACKLOG', 'PLAYING', 'BEATEN', 'DROPPED'),
    format ENUM('PHYSICAL', 'DIGITAL'),
    notes TEXT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB;
```

---

## Basic Queries

### View All Tables

```sql
SHOW TABLES;
```

### Describe Table Structure

```sql
DESCRIBE book;
DESCRIBE comic;
DESCRIBE video_game;
```

### Count Records

```sql
SELECT 'Books' AS collection, COUNT(*) AS count FROM book
UNION ALL
SELECT 'Comics', COUNT(*) FROM comic
UNION ALL
SELECT 'Games', COUNT(*) FROM video_game;
```

---

## Select Queries

### All Books

```sql
SELECT id, title, author, genre, read_status, format
FROM book
ORDER BY title;
```

### All Comics

```sql
SELECT id, title, series, issue_number, writer, read_status
FROM comic
ORDER BY series, issue_number;
```

### All Games

```sql
SELECT id, title, console, genre, completion_status, format
FROM video_game
ORDER BY title;
```

### Books by Status

```sql
SELECT title, author, read_status
FROM book
WHERE read_status = 'UNREAD'
ORDER BY title;
```

### Games by Console

```sql
SELECT title, genre, completion_status
FROM video_game
WHERE console = 'PS5'
ORDER BY title;
```

### Search by Title

```sql
SELECT * FROM book WHERE title LIKE '%Dune%';
SELECT * FROM comic WHERE title LIKE '%Batman%';
SELECT * FROM video_game WHERE title LIKE '%Zelda%';
```

---

## Insert Queries

### Insert a Book

```sql
INSERT INTO book (title, author, isbn, publisher, published_year, genre, read_status, format, notes, created_at, updated_at)
VALUES ('1984', 'George Orwell', '9780451524935', 'Signet Classic', 1949, 'Dystopian', 'READ', 'PHYSICAL', 'Classic dystopian novel', NOW(), NOW());
```

### Insert a Comic

```sql
INSERT INTO comic (title, series, issue_number, writer, artist, read_status, format, notes, created_at, updated_at)
VALUES ('The Sandman', 'Sandman', '1', 'Neil Gaiman', 'Sam Kieth', 'READ', 'PHYSICAL', 'Sleep of the Just', NOW(), NOW());
```

### Insert a Game

```sql
INSERT INTO video_game (title, console, publisher, developer, genre, completion_status, format, notes, created_at, updated_at)
VALUES ('God of War Ragnarok', 'PS5', 'Sony', 'Santa Monica Studio', 'Action-Adventure', 'BEATEN', 'PHYSICAL', 'Game of the Year contender', NOW(), NOW());
```

---

## Update Queries

### Update Read Status

```sql
UPDATE book SET read_status = 'READ', updated_at = NOW() WHERE id = 1;
```

### Update Game Completion

```sql
UPDATE video_game SET completion_status = 'BEATEN', updated_at = NOW() WHERE id = 1;
```

### Bulk Update

```sql
UPDATE book SET read_status = 'READ', updated_at = NOW() WHERE author = 'Frank Herbert';
```

---

## Delete Queries

### Delete by ID

```sql
DELETE FROM book WHERE id = 1;
DELETE FROM comic WHERE id = 1;
DELETE FROM video_game WHERE id = 1;
```

### Delete by Condition

```sql
DELETE FROM video_game WHERE completion_status = 'DROPPED';
```

### Clear All Data (Careful!)

```sql
DELETE FROM book;
DELETE FROM comic;
DELETE FROM video_game;
```

---

## Aggregation Queries

### Count by Status (Books)

```sql
SELECT read_status, COUNT(*) AS count
FROM book
GROUP BY read_status
ORDER BY count DESC;
```

### Count by Genre (Books)

```sql
SELECT genre, COUNT(*) AS count
FROM book
GROUP BY genre
ORDER BY count DESC;
```

### Count by Console (Games)

```sql
SELECT console, COUNT(*) AS count
FROM video_game
GROUP BY console
ORDER BY count DESC;
```

### Count by Completion Status (Games)

```sql
SELECT completion_status, COUNT(*) AS count
FROM video_game
GROUP BY completion_status
ORDER BY count DESC;
```

### Books per Year

```sql
SELECT published_year, COUNT(*) AS count
FROM book
WHERE published_year IS NOT NULL
GROUP BY published_year
ORDER BY published_year DESC;
```

---

## Advanced Queries

### Recently Added Items

```sql
SELECT 'Book' AS type, title, created_at FROM book
UNION ALL
SELECT 'Comic', title, created_at FROM comic
UNION ALL
SELECT 'Game', title, created_at FROM video_game
ORDER BY created_at DESC
LIMIT 10;
```

### Digital vs Physical

```sql
SELECT
    'Books' AS collection,
    SUM(CASE WHEN format = 'PHYSICAL' THEN 1 ELSE 0 END) AS physical,
    SUM(CASE WHEN format = 'DIGITAL' THEN 1 ELSE 0 END) AS digital
FROM book
UNION ALL
SELECT 'Comics',
    SUM(CASE WHEN format = 'PHYSICAL' THEN 1 ELSE 0 END),
    SUM(CASE WHEN format = 'DIGITAL' THEN 1 ELSE 0 END)
FROM comic
UNION ALL
SELECT 'Games',
    SUM(CASE WHEN format = 'PHYSICAL' THEN 1 ELSE 0 END),
    SUM(CASE WHEN format = 'DIGITAL' THEN 1 ELSE 0 END)
FROM video_game;
```

### Unread/Backlog Items

```sql
SELECT 'Book' AS type, title FROM book WHERE read_status = 'UNREAD'
UNION ALL
SELECT 'Comic', title FROM comic WHERE read_status = 'UNREAD'
UNION ALL
SELECT 'Game', title FROM video_game WHERE completion_status = 'BACKLOG'
ORDER BY type, title;
```

---

## Backup and Restore

### Export Database

```bash
mysqldump -u collectionuser -pcollectionpass collection_tracker > backup.sql
```

### Export Specific Tables

```bash
mysqldump -u collectionuser -pcollectionpass collection_tracker book comic video_game > backup.sql
```

### Export as CSV

```bash
mysql -u collectionuser -pcollectionpass collection_tracker -e "SELECT * FROM book" | tr '\t' ',' > books_export.csv
```

### Restore Database

```bash
mysql -u collectionuser -pcollectionpass collection_tracker < backup.sql
```

---

## Maintenance

### Reset Auto-Increment

```sql
ALTER TABLE book AUTO_INCREMENT = 1;
ALTER TABLE comic AUTO_INCREMENT = 1;
ALTER TABLE video_game AUTO_INCREMENT = 1;
```

### Drop Tables (Reset Schema)

```sql
DROP TABLE IF EXISTS book, comic, video_game;
```

The application will recreate tables on next startup (with `ddl-auto=update`).

### Check Table Size

```sql
SELECT
    table_name AS 'Table',
    ROUND(data_length / 1024, 2) AS 'Data (KB)',
    ROUND(index_length / 1024, 2) AS 'Index (KB)',
    table_rows AS 'Rows'
FROM information_schema.tables
WHERE table_schema = 'collection_tracker'
ORDER BY data_length DESC;
```
