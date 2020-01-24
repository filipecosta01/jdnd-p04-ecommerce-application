# eCommerce Application

In this project, I had an opportunity to demonstrate the security and DevOps skills that I've learned in this lesson by completing an eCommerce application
. From a template to complete application, the goal was to take this template and add proper authentication and authorization controls
 so users can only access their data, and that data can only be accessed in a secure way. 

## Project Template
First, the set up was done with the template written in Java using Spring Boot, Hibernate ORM, and the H2 database. H2 is an in memory database, so if you
 need to
 retry
 something, every application startup is a fresh copy.

To use the template, import it in the IDE of your choice as a Spring Boot application. Where required, this readme assumes the eclipse IDE.

Once the project is set up, you will see 5 packages:

* demo - this package contains the main method which runs the application

* model.persistence - this package contains the data models that Hibernate persists to H2. There are 4 models: Cart, for holding a User's items; Item , for defining new items; User, to hold user account information; and UserOrder, to hold information about submitted orders. Looking back at the application “demo” class, you'll see the `@EntityScan` annotation, telling Spring that this package contains our data models

* model.persistence.repositories - these contain a `JpaRepository` interface for each of our models. This allows Hibernate to connect them with our database so we can access data in the code, as well as define certain convenience methods. Look through them and see the methods that have been declared. Looking at the application “demo” class, you’ll see the `@EnableJpaRepositories` annotation, telling Spring that this package contains our data repositories.

* model.requests - this package contains the request models. The request models will be transformed by Jackson from JSON to these models as requests are made. Note the `Json` annotations, telling Jackson to include and ignore certain fields of the requests. You can also see these annotations on the models themselves.

* controllers - these contain the api endpoints for our app, 1 per model. Note they all have the `@RestController` annotation to allow Spring to understand that they are a part of a REST API

In resources, you'll see the application configuration that sets up our database and Hibernate, It also contains a data.sql file with a couple of items to populate the database with. Spring will run this file every time the application starts

In eclipse, you can right click the project and click  “run as” and select Spring Boot application. The application should tell you it’s starting in the console view. Once started, using a REST client, such as Postman, explore the APIs.

Some examples are as below:
To create a new user for example, you would send a POST request to:
http://localhost:8080/api/user/create with an example body like 

```
{
    "username": "test"
}
```


and this would return
```
{
    "id" 1,
    "username": "test"
}
```

## Testing
Unit tests demonstrate at least 80% code coverage.

## Jenkins Job

### Build Configuration
![Build configuration](https://drive.google.com/uc?export=view&id=1KJAX635ExUl1xirfXzEe-kAeGov1H7Zd)

### Build Job 1 - Start
![Build Step 1](https://drive.google.com/uc?export=view&id=1eBpjR0CjmhOYTjwBPfMb4SdrL1ieoRu7)

### Build job 2 - Finish
![Build Step 2](https://drive.google.com/uc?export=view&id=1drmSdPefdCud1NGkw0ImEx14yr3FS0Yi)

## Splunk

### Query - Default
![Splunk default query](https://drive.google.com/uc?export=view&id=1eBFrCWCn0z0sIZkE2N8e6k4DFGnIa4Sb)

### Query - Requests per Minute
![Splunk query requests per minute](https://drive.google.com/uc?export=view&id=1c2drk4Hb9iZWcSqEq7uubaftdJC8pX7T)

### Chart - Requests per Minute
![Splunk chart requests per minute](https://drive.google.com/uc?export=view&id=1-_BCz2ihFFoRBNPC56xGsaBiuTSPerXf)

### Alert - Main page
![Splunk alert main page](https://drive.google.com/uc?export=view&id=1GlMC4ivwgKW0zPdehD6yotbap8opCFgb)

### Alert - Configuration page
![Splunk alert configuration page](https://drive.google.com/uc?export=view&id=1VNYBTmQJB-7TEJEhh6a4Goacydcv0W5h)
