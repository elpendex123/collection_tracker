# Known Issues and Solutions

This document covers issues encountered during development and their solutions.

## Port Conflict with Jenkins

### Issue
Port 8080 is commonly used by Jenkins, causing a conflict when running the application.

### Solution
Changed the application port to 8081 in `application.properties`:

```properties
server.port=8081
```

All CLI scripts were also updated to use port 8081 as the default.

---

## Gradle Version Compatibility

### Issue
Gradle 9.x has compatibility issues with the Spring Boot plugin, causing build failures:

```
Failed to notify dependency resolution listener.
'java.util.Set org.gradle.api.artifacts.LenientConfiguration.getArtifacts(org.gradle.api.specs.Spec)'
```

### Solution
Downgraded the Gradle wrapper to version 8.5:

```bash
./gradlew wrapper --gradle-version=8.5
```

---

## Python Externally Managed Environment

### Issue
On modern Ubuntu/Debian systems, pip refuses to install packages system-wide:

```
error: externally-managed-environment
```

### Solution
Use a virtual environment:

```bash
python3 -m venv venv
source venv/bin/activate
pip install requests
```

---

## MySQL Root Access Denied

### Issue
On Ubuntu, MySQL root user requires socket authentication:

```
ERROR 1698 (28000): Access denied for user 'root'@'localhost'
```

### Solution
Use `sudo` to access MySQL as root:

```bash
sudo mysql -e "CREATE DATABASE collection_tracker;"
```

Or create a dedicated user with password authentication (recommended).

---

## CSV Ingest Script Error

### Issue
The ingest script failed with:

```
AttributeError: 'list' object has no attribute 'strip'
```

### Solution
Fixed the `clean_value` function in `scripts/ingest_collection.py` to handle non-string values:

```python
def clean_value(v):
    if isinstance(v, str):
        return v if v.strip() != "" else None
    return v
payload = {k: clean_value(v) for k, v in payload.items()}
```

---

## Hibernate Dialect Warning

### Issue
Hibernate logs a deprecation warning:

```
HHH90000025: MySQLDialect does not need to be specified explicitly
```

### Solution
This is just a warning and can be ignored. Alternatively, remove the dialect line from `application.properties`:

```properties
# Remove this line (optional):
# spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

---

## SSH Authentication for GitHub

### Issue
HTTPS push to GitHub fails without credentials:

```
fatal: could not read Username for 'https://github.com': No such device or address
```

### Solution
Set up SSH authentication:

```bash
# Generate SSH key
ssh-keygen -t rsa -b 4096 -C "your.email@example.com"

# Start SSH agent
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_rsa

# Copy public key to GitHub
cat ~/.ssh/id_rsa.pub

# Switch remote to SSH
git remote set-url origin git@github.com:username/collection_tracker.git
```

---

## Thymeleaf Template Rendering Error

### Issue
All HTML pages fail to render with the following error:

```
org.thymeleaf.exceptions.TemplateInputException: Error resolving fragment: "${content}":
template or fragment could not be resolved (template: "layout" - line 29, col 14)
```

### Cause
The `layout.html` template used deprecated Thymeleaf fragment syntax:

```html
<!-- WRONG (deprecated) -->
<div th:replace="${content}">
```

Modern Thymeleaf requires the complete fragment expression syntax.

### Solution
Updated `src/main/resources/templates/layout.html` to use the correct syntax:

```html
<!-- CORRECT -->
<div th:insert="~{::content}">
```

This allows child templates (index.html, books/list.html, comics/list.html, games/list.html, etc.) to properly inherit the layout and insert their content fragments.

---

## Empty Collection on First Run

### Issue
The application starts with empty tables.

### Solution
This is expected behavior. Hibernate creates the tables automatically with `ddl-auto=update`. Load sample data using the ingest script or add items manually through the UI.
