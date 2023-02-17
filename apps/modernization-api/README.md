# NEDSS-Modernization API

## Running

### Prerequisites

1. Java 17
1. Node / NPM
1. nbs-mssql, elasticsearch, and nifi docker containers are running. See [CDC Sandbox](../../cdc-sandbox/README.md)

### VSCode

1. In the ui directory run `npm install`
1. In the modernization-api directory run `./gradlew build`
    - Alternatively, from the root directory run `./gradlew :modernization-api:buildDependents`
1. Press `Cmd+Shift+P` and run `Java: Clean Language Server Workspace`
1. VSCode should now recognize the QueryDSL generated Q classes and be able to launch the debugger

## Tests

To run all tests:

```bash
./gradlew test
```

To execute specific test tags:

```bash
./gradlew test -Dcucumber.filter.tags="@patient_create" --tests "RunCucumberTest"
```

## Running

The Modernization API can be started from the root directory by runninng:

```bash
./gradlew :modernization-api:bootRun
```

It assumes that ElasticSeach and MSSQL Server are running on `localhost`.  Preconfigured containers are avaiable in the [CDC Sandbox](../../cdc-sandbox/README.md), `cdc-sandbox/elasticsearch` and `cdc-sandbox/db`


## GraphQL

The project utilizes GraphQL through the [spring-boot-starter-graphql](https://docs.spring.io/spring-graphql/docs/current/reference/html/) dependency. With the api running an interface is available at [/graphiql](http://localhost:8080/graphiql?path=/graphql#) for testing

## QueryDSL

[QueryDSL](https://github.com/querydsl/querydsl) allows the construction of type-safe SQL queries. The `QPatient` class is generated from the existing `Patient` JPA entity. When a new `@Entity` is added, run `./gradlew build` and the new Q classes will be created under `libs/database-entities/build/generated/sources/annotationProcessor/java/main`

### Example query:

```java
@PersistenceContext
private final EntityManager entityManager;

public List<Patient> findPatientsNamedJohn() {
    JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
    var patient = QPatient.patient;
    return queryFactory.selectFrom(patient).where(patient.firstNm.like("John")).fetch();
}
```

## Swagger

A swagger page is available at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


## Configuration

Spring Config allows configuration values to be overwritten at runtime.  Values can be set through Java System Variables, Environment Variable, and [other useful means](https://docs.spring.io/spring-boot/docs/2.7.5/reference/html/features.html#features.external-config).  The default profile contains the following properties configuration most likely to change.

| Name                     | Default   | Description                                       |
|--------------------------|-----------|---------------------------------------------------|
| nbs.elasticsearch.server | localhost | The host name of the server running ElasticSearch |
| nbs.elasticsearch.port   | 9200      | The port in which ElasticSearch is listening      |
| nbs.wildfly.server       | localhost | The host name of the server running NBS Classic   |
| nbs.wildfly.port         | 7001      | The port in which NBS Classic is listening        |
| nbs.datasource.server    | localhost | The host name of the server running MS SQL Server |