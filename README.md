# Smart Meter Reads Tech Test

## Starting the app

The application is a maven project so to run it you will either need to be using an IDE such as Intellij which comes pre-packaged with Maven, or you will need to have Maven installed globally on your local machine. In either case, once Maven is installed, you can run the following command to start the application. 

```
mvn spring-boot:run
```

## Login

The project currently uses an in memory `UserDetailsManager` with a single hard coded user. This choice was made to save on time while still being roughly representative of how a production application would handle user management. To take the application into a production ready state, this bean would need to be replaced with one that either connects to the database or a third party service such as Google or Okta in order to fetch user credentials.

Endpoints in the project are secured using self-signed JWT tokens. The tokens last one hour and can only be issued to users who have authenticated with the system. In the case of this project there is only one user who can authenticate. Credentials can be found below. 

```
username: gcook
password: password
```

More details about the Spring Security flow for the project can be found in [this wonderful tutorial by Dan Vega](https://www.danvega.dev/blog/spring-security-jwt). 

In order to log into the application you need to retrieve a JWT token by sending a HTTP POST request to the following endpoint. You will need to use basic auth to provide the username and password listed above when sending the request. If you have something like Postman installed then this is very simple and can be done through the **Authorization** tab inside the request builder.

```
http://localhost:8081/api/smart/token
```
If you are using Curl then the following command should get you a token 

```
curl http://gcook:password@localhost:8081/api/smart/token -X POST
```
From here, the token retrieved should be passed as a bearer token in every request. In Postman this is again very simple and can be configured through the UI. If using Curl then [this tutorial](https://reqbin.com/req/c-hlt4gkzd/curl-bearer-token-authorization-header-example) should help. 


## Testing the app

Tests can typically be run through the IDE or through maven. To run all unit and integration tests at once, run the following command.

**Run all tests:**

```
mvn clean verify
```

## Database schema & migrations

The project uses a H2 in memory database with the schema being created and synchronised using Flyway.
Spring will automatically run new migrations on start up, but this can also be done manually through maven.
Credentials needed by Flyway are stored in `pom.xml`, within Flyway's plugin definition.
SQL scripts are stored in `src/main/resource/db/migration` and to run the migration setup
using Maven run the following command:

```
mvn flyway:migrate
```

This will run all scripts in the directory listed above. Migration history is stored in a table called
`flyway_schema_history`. This means that whenever a new script is added to the directory, and you run the Maven migration
command again, it will only run the scripts that have been added since the last time Flyway updated this table.

## API Docs
Auto generated API documentation is available to view at the following URL 
```
http://localhost:8081/api/smart/swagger-ui/index.html
```

This documentation is also available in JSON format at the following location 
```
http://localhost:8081/api/smart/v3/api-docs
```
## Design Decisions
### Unit Tests
In the interest of saving time, not every class is fully unit tested. There is a decent sized suite of unit tests 
covering a breadth of functionality and testing techniques to demonstrate different competencies. I have chosen to use
`AssertJ` assertions instead of the basic jUnit ones as I think these are more readable and more explicitly demonstrate 
what is being tested. 

### Integration Tests
The same applies for Integration tests. Not all functionality is fully integration tested, however a wide variety of 
the functionality in the application is tested. So things like empty responses, validation failures and success states 
are all covered in the Cucumber Suite. 

This is actually the first time I have used Cucumber in a codebase. I have written integration tests in the past using 
Serenity, Newman (Postman for pipelines), Cypress and plain MockMVC so this was an interesting lesson. 

### Database Schema Design
I have chosen to only use one database table with a `type` discriminator column since the `gasReads` and `elecReads` 
collections in the test contain all the same attributes and data types. If these were different I might have chosen to 
use two tables, or built the objects and schema using polymorphism while still using a single table strategy in JPA. 

### DTOs & Object Mapping
I have chosen to use MapStruct to map between concrete objects and DTOs to save time but also because I feel like this 
is a solved problem in the same way that things like HTTP requests are. MapStruct is the best library I have used to map 
between objects and DTOs and it offers the most flexibility in terms of customisation and mixing automatic conversion 
with manual conversion. 

### Application properties 
I have chosen to use Yaml as in my experience `application.properties` files can become messy and difficult to maintain
in a way that YAML files just don't ever seem to.
