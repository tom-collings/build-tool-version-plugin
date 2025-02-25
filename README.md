to build:

```mvn package```

to build the Docker container:

```docker build -t build-tool-version -f ./Dockerfile.local .```

to run the Docker container:

```docker run -v <src dir on host machine>:/app-src -v <output dir on host machine>:/output build-tool-version /app-src /output```