# vrijBRP Balie
Front-desk application used to manage civil affairs processes by municipalities.

## License
Copyright &copy; 2021 Procura BV. \
Licensed under the [EUPL](https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md)

## Getting started
_Committing this code to GitHub is a first step towards an open source BRP._ \
_This application could have maven dependencies that might not be publicly available at this moment._\
_It also depends on several components that will become open source later._

### Build

#### Build requirements
- Java 8
- Tomcat 9
- Maven 3
- Postgres 11
- [Java Code formatting](https://github.com/vrijBRP/vrijBRP/blob/master/CONTRIBUTING.md)
- Eclipse code formatter (**Intellij**)
- Lombok plugin (**optional**)  

#### Build commands
`mvn clean package -P vaadin`

### Run the application

#### Run requirements
- a PostgreSQL database with the correct database schema
- an Oracle database with the correct database schema

#### Configuration
Running this application requires a minimal configured postgres database.

##### applicatie.properties

In `<tomcat home>\procura\applicaties\personen\config\applicatie.properties`
```properties
s_app=vrijBRP
c_gem=<municipality code> 
s_gem=<municipality name>
#specify the db type(postgres|oracle)
app_db_name=postgres
app_db_server=<postgres server>
app_db_port=5432
app_db_schema=<postgres schema>
app_db_sid=<postgres SID>
app_db_username=<postgres username>
app_db_password=<postgres password>
connections.min=1
connections.max=1
loglevel=INFO
```
##### log4j.properties
Save log4j.properties somewhere on your system with: 

```properties
log4j.rootCategory=INFO, CONSOLE
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern=%d{dd MMM yyyy HH\:mm\:ss} [%-5p] - %m%n
```

##### Internal configuration
Additional documentation will be added soon.

### Docker image
A docker image will become available publically in the near future.
