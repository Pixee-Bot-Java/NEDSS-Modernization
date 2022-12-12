# NEDSS-Modernization

### Getting started

This repo contains two main components.

1. [CDC Sandbox](cdc-sandbox/README.md) - primarily consists of a docker-compose containing the bulk of the supporting infrastructure for the modernization app
1. [Patient Search](patient-search/README.md) - contains the Modernization app (Java/Spring Boot, React)

## TLDR: Running everything inside docker

### Mac ARM system (M1/M2)

1. Install Java 17
1. Install Node / NPM
1. Clone [NBS](https://github.com/cdcent/NEDSSDev)
1. Set `NEDSS_HOME` environment variable, this should point to the directory `NBS` was cloned into
   ```sh
   export NEDSS_HOME="/Users/michaelpeels/Projects/NBS/NEDSSDev"
   ```
1. CD into the `cdc-sandbox` directory
   ```sh
   cd cdc-sandbox
   ```
1. Run the NBS [build script](cdc-sandbox/build.sh) to build the image
   ```sh
   ./build.sh
   ```
1. Run the NBS [run script](cdc-sandbox/run.sh) to start the `nbs-mssql` database and `nbs`. NBS runs inside [WildFly 10.0.0](https://www.wildfly.org/news/2016/01/30/WildFly10-Released/), so the container is named `wildfly`
   ```sh
   ./run.sh
   ```
1. Start `Elasticsearch`, `Kibana`, and the [Traefik](https://traefik.io/) reverse proxy
   ```sh
   docker-compose up elasticsearch kibana reverse-proxy -d
   ```
1. Run the NiFi [build script](cdc-sandbox/nifi/buildImage.sh) to build an M1 compatibile image (if not on ARM architecture, skip this step)
   ```sh
   ./nifi/buildImage.sh
   ```
1. CD into the `patient-search/ui` directory
   ```sh
   cd ../patient-search/ui
   ```
1. Run `npm install`
   ```sh
   npm i
   ```
1. CD to the `patient-serch` directory
   ```sh
   cd ..
   ```
1. Start the `modernization` container
   ```sh
   docker-compose up -d
   ```
1. CD into the `cdc-sandbox` directory and Start NiFi
   ```sh
   cd ../cdc-sandbox
   docker-compose up nifi -d
   ```
1. Visit http://localhost:8080/nbs/login

   ```
   username: msa
   password:
   ```
