# Java API Service Starter

This is a minimal Java API service starter based on [Google Cloud Run Quickstart](https://cloud.google.com/run/docs/quickstarts/build-and-deploy/deploy-java-service).

## Getting Started

Server should run automatically when starting a workspace. To run manually, run:
```sh
mvn spring-boot:run
```

CI and Docker image publishing
------------------------------
The repository CI now builds and runs tests on every push and pull request. On pushes to `main` the workflow also builds the `lims-web` Docker image. The workflow will push the image to GitHub Container Registry (GHCR) when the secret `GHCR_TOKEN` is configured in repository secrets.

To enable publishing to GHCR:

1. Create a personal access token (PAT) with the `read:packages` and `write:packages` scopes.
2. Add a repository secret named `GHCR_TOKEN` with that token value.

Local build & publish (example):

```sh
# build jar
mvn -DskipTests -pl lims-web -am package

# build docker image locally
docker build -f lims-web/Dockerfile -t ghcr.io/<OWNER>/<REPO>/lims-web:local-latest lims-web

# push (after authenticating with ghcr)
docker push ghcr.io/<OWNER>/<REPO>/lims-web:local-latest
```
