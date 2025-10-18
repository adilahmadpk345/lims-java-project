# BUILD.md

This file documents how to build and run the LIMS multi-module project locally.

Prerequisites
- Java 17 (JDK) installed
- Maven 3.6+ (3.9.10 used here)
- Docker (optional)

Quick build steps
1. Install / switch to Java 17 and verify Maven uses it:
```bash
sudo apt update
sudo apt install -y openjdk-17-jdk
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
mvn -v
```

2. Build the web module and its dependencies (skip tests for speed):
```bash
mvn -pl lims-web -am -DskipTests package
```

3. Run the web app (from project root):
```bash
# Run with Maven (builds first if needed)
mvn -pl lims-web -am spring-boot:run

# Or run the packaged jar (recommended for production-like runs)
java -jar lims-web/target/lims-web-0.0.1-SNAPSHOT.jar
```

Troubleshooting notes
- "release version 17 not supported": Maven is using Java 11. Ensure JAVA_HOME points to JDK 17 and that `mvn -v` shows Java 17.

- Spring Boot plugin or other plugins compiled for Java 17 will fail if Maven is running on Java 11; start Maven with JAVA_HOME pointing to Java 17 as shown above.

- On app startup you may see: "A database name must be provided." This repository includes a Cloud SQL auto-configuration class (spring-cloud-gcp-starter-sql-mysql) that expects cloud SQL properties. To run locally without Cloud SQL configured:
  - Use the H2 runtime dependency (already on the classpath for `lims-web`) by setting Spring profile or datasource properties, for example:
    ```bash
    # Option A: set an in-memory DB via environment variable
    export SPRING_DATASOURCE_URL=jdbc:h2:mem:limsdb;DB_CLOSE_DELAY=-1
    export SPRING_DATASOURCE_USERNAME=sa
    export SPRING_DATASOURCE_PASSWORD=
    java -jar lims-web/target/lims-web-0.0.1-SNAPSHOT.jar
    ```
  - Or disable Cloud SQL auto-configuration by setting a property (in `application.properties` or env):
    ```bash
    export SPRING_CLOUD_GCP_SQL_ENABLED=false
    ```


Files changed to aid local builds
- Several small compatibility changes were made to `lims-core` to avoid runtime external dependencies and to add method stubs used by the web module. These are intended to be lightweight placeholders and should be replaced for a production deployment.

Next steps
- (Optional) I can create a `run-local.sh` to set environment variables and start the app in a reproducible way.
Run locally with the included script
----------------------------------

A helper script `run-local.sh` has been added to the project root to make starting the web app locally quick and reproducible. It:

- ensures a Java 17 runtime is available (or uses $JAVA_HOME if set)
- builds the `lims-web` module if the jar doesn't exist
- supports an H2 in-memory option (recommended for local development)
- writes logs to `logs/lims-web.log` and runs the app in the background

Usage examples:

```bash
# Run using H2 in-memory DB (recommended for local development)
./run-local.sh --h2

# Run with Cloud SQL disabled but custom datasource (example)
SPRING_CLOUD_GCP_SQL_ENABLED=false SPRING_DATASOURCE_URL='jdbc:h2:mem:limsdb;DB_CLOSE_DELAY=-1' ./run-local.sh

# Show script help
./run-local.sh --help
```

Next steps
- (Optional) Reintroduce proper Vertex AI usage and Cloud SQL wiring; I can help with that if you want to connect to real cloud services.

If you'd like, I can: revert the local-only placeholder code, wire in production-ready Vertex AI and Cloud SQL settings, or extend the run script to support Docker.
- (Optional) Reintroduce proper Vertex AI usage and Cloud SQL wiring; I can help with that if you want to connect to real cloud services.

If you'd like, I can: start a reproducible local run script, revert placeholder code, or help wire real cloud credentials. Which should I do next?

---

Auto-save helper
-----------------

There is a helper script at `tools/git-auto-push.sh` that creates a timestamped branch, commits any
local changes, and pushes to the `origin` remote. Optionally it can create a PR if the GitHub CLI
(`gh`) is installed and authenticated.

Usage:

```bash
# Commit and push changes to a timestamped branch
tools/git-auto-push.sh

# Commit, push and create a PR (requires GitHub CLI auth)
tools/git-auto-push.sh --create-pr
```

Notes:
- The script requires a configured `origin` remote and push permissions.
- For automatic PR creation, install and authenticate `gh` (GitHub CLI): https://cli.github.com/
- For scheduled or CI-backed backups, consider using a GitHub Action or other CI runner to push changes from a centralized environment.