# NoteIt

Boilerplate Kotlin server for a simple note app backend

## Prerequisite

You can quickly set up the server by using [Docker](https://www.docker.com/). Once you have docker installed you can run
the below script in your terminal:

```shell
docker-compose up -d
```

You can then import the [Postman collection](assets/NoteIt.postman_collection) to test out the API.

## Libraries

- [Ktor Server](https://github.com/ktorio/ktor)
- [Ktor Auth JWT](https://ktor.io/docs/jwt.html)
- [Ktor Metrics Micrometer](https://ktor.io/docs/micrometer-metrics.html)
- [Prometheus](https://ktor.io/docs/micrometer-metrics.html)
- [Config4k](https://github.com/config4k/config4k)
- [Exposed](https://github.com/JetBrains/Exposed)
- [Hikari](https://mvnrepository.com/artifact/com.zaxxer/HikariCP)
- [KotlinX-Datetime](https://github.com/Kotlin/kotlinx-datetime)
- [KtLint](https://github.com/pinterest/ktlint)
