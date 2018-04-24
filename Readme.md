## Synopsis

Each service itself as client and server both consumes data from other service or provides services to other services. Each microservice has its own set of data service and independent operation on it. Any microservice want to consume API it should be validated through proper security mechanism. To doing so we have implemented one authentication gateway where API consumer has to get authenticated first and obtain token and communicate with any microservice which validate its token successfully.
Security token mechanism is used to authorize API consumer on each service call between microservice. Authentication service authenticate the user based upon current authentication mechanism. In our case we have provided basic database authentication and delegated authentication (current implementation: LDAP is the only delegated mechanism). Admin can choose any authentication mechanism. For authentication, only one authentication mechanism can be true at a time. 


## Code Example

Show what the library does as concisely as possible, developers should be able to figure out **how** your project solves their problem by looking at the code example. Make sure the API you are showing off is obvious, and that your code is short and concise.

## Motivation


This project demonstrates the microservices architecture as a suite of independently deployable, small, modular services in which each service runs a unique process and communicates through a well-defined rest protocol. A microservice is small independent process communicate using Feign client which is implemented as part of this project. 


## Installation

Project Build Steps:
•	Microservices can also build and deployed using docker. Following steps to build using docker.
1) Go to directory /microservice-template
2) This directory contains parent pom.xml, which has all modules included
I) customer-microservice
II) microservices-authentication-service
III) subscription-management-microservice
IV) subscription-microservice
3)  every project has its own Docker file.  On maven docker build image get build and publish on docker which available on its local.
mvn clean package -P docker dockerfile:build
this command will download/pull all images provided in Docker file and build the image for given microservice.

4) docker compose file (docker-compose.yml) is used start the all microservices. Which actually creates the containers for all docker images which are provided in docker-compose.yml.
Following is the command to bootstrap microservices, 
docker-compose up
this will start all microservices along with zookeeper which is required to register the service on startup of each microservice 


•	Following steps to build and start using jar.

1)	Go to directory /microservice-template
               2) This directory contains parent pom.xml, which has all modules included
I) customer-microservice
II) microservices-authentication-service
III) subscription-management-microservice
IV) subscription-microservice
            3)   run a following command
		mvn clean package
            4) You should start zookeeper before start of any microservice.
	 Download zookeeper from http://www-eu.apache.org/dist/zookeeper/zookeeper-3.4.10/
	Install and start zookeeper, by default zookeeper runs on 2181 port.
            5)  Once zookeeper is up, start each microservice and test.


## API Reference


1) In this architecture, first microservice is authentication gateway microservice which act as proxy gateway (Zuul) for all other microservices where all API request channel through.       
 Following is the configuration made in application. Properties of authentication-gateway microservice.
#Zuul
#Subscription-microservice
zuul.routes.subscription-microservice.url=http://localhost:8083/api/subscriptions
zuul.routes.subscription-microservice.path=/api/subscriptions/**
zuul.sensitive-headers=Cookie,Set-Cookie //to send headers to downstream
zuul.routes.subscription-microservice.path.serviceId=subscription-microservice

#Customer-microservice
zuul.routes.customers-microservice.url=http://localhost:8081/api/customers
zuul.routes.customers-microservice.path=/api/customers/**
zuul.sensitive-headers=Cookie,Set-Cookie //to send headers to downstream
zuul.routes.customers-microservice.path.serviceId=customers-microservice

#Subscription-management-service
zuul.routes.subscription-management-microservice.url=http://localhost:8084/api/subscriptions
zuul.routes.subscription-management-microservice.path=/api/customerSubscriptions/**
zuul.sensitive-headers=Cookie,Set-Cookie //to send headers to downstream
zuul.routes.subscription-management-microservice.path.serviceId=subscription-management-microservice

1)	This microservice also act as authentication configuration service. 
Following list of APIs available as part of this microservice.

              a) GET,POST,DELETE http://localhost:8080/api/users
Request: {
  "authorities": [
    {
      "name": "ROLE_ADMIN"
    }
  ],
  "email": "d@gmail.com",
  "enabled": true,
  "firstname": "ADMIN",
  "lastname": "admin",
  "username": "admin",
  "password":"secret",
  "lastPasswordResetDate":"2018-03-16T22:55:40.110"
}
  
- Users can be created and deleted though this API
- These are the user which are allowed to consume the APIs across all microservices by obtaining the tokens.
- This API only when you want get user to get authenticated from application configured database.


              b) POST http://localhost:8080/auth 
Request : {
"username":"admin2",
"password":"2222"
}
		-  Above API request obtains token on successful authentication.
              c) POST http://localhost:8080/api/authConfigs
Headers: Authorization - Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjIiLCJyb2xlcy9ST0xFX0FETUlOIjoiUk9MRV9BRE1JTiIsImV4cCI6MTUyNDQ2MjMwNSwiaWF0IjoxNTIzODU3NTA1fQ.GcDXS6ShqAmqTv8bxd_UJEFxd4B6d4E3M1P-aflxqu3MBHnPiTJQsR9XMcPxQAgsYrIfiVd5iKSJVJrJ_4JksA

Request:   {
	"authType": "ldap",
	"enabled":true,
	"authPropertyMap":{
			"url":"ldap://localhost:10389",
			"managerDn":"uid=admin,ou=system",
			"managerPassword":"test",
			"userDnPatterns":"uid={0},ou=users,ou=system"
              }

       }
 -One of the authentications can be enabled at a time which will get active on application bootstrap. Then user will get authenticated from that backend and token will be provided on successful authentication. With this token user can consume service from all above microservices.
- If no authentication provided or all authentications are made false then by default user will be authenticated from application configured database.




2)Second microservice, Subscription Management has subscription APIs exposed from authentication gateway. 


a)	POST http://localhost:8080/api/customerSubscriptions/0001712/ FUNKYHELLOTUNE 
Headers: Authorization - Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjIiLCJyb2xlcy9ST0xFX0FETUlOIjoiUk9MRV9BRE1JTiIsImV4cCI6MTUyNDQ2MjMwNSwiaWF0IjoxNTIzODU3NTA1fQ.GcDXS6ShqAmqTv8bxd_UJEFxd4B6d4E3M1P-aflxqu3MBHnPiTJQsR9XMcPxQAgsYrIfiVd5iKSJVJrJ_4JksA

Response:   {
    "customerId": "0001712",
    "name": Dhananjay Gaikwad,
    "firstname": "Dhananjay",
    "email": "a@dcom",
    "street": abc road,
    "city": "pune",
    "subscription": [
        {
            "plan_free_quantity": "1",
            "remaining_billing_cycles": "1",
            "plan_quantity": "1",
            "plan_id": "2323",
            "subscriptionId": "FUNKYHELLOTUNE",
            "has_scheduled_changes": "test",
            "resource_version": "test",
            "trial_end": "test",
            "current_term_start": "test",
            "total_dues": "test",
            "plan_unit_price": "test",
            "current_term_end": "test",
            "billing_period_unit": "test",
            "created_at": "test",
            "started_at": "test",
            "activated_at": "test",
            "currency_code": "001",
            "status": "active",
            "cancelled_at": "test",
            "deleted": "test",
            "due_invoices_count": "12",
            "billing_period": "test",
            "due_since": "2018-04-19T19:04:58.447+0000",
            "updated_at": "test",
            "trial_start": "2018-04-19T19:04:58.447+0000"
        }
    ]
}
  customer can subscribe to the any telecom by providing customer id and subscription id.       Once customer and subscription are available user get successfully subscribed to given   subscription which saved at subscription management datastore. 

b) DELETE http://localhost:8080/api/customerSubscriptions/FUNKYHELLOTUNE 
   subscription can be deleted
c) GET http://localhost:8080/api/customerSubscriptions/0001712 
   List subscription by customer id




## Tests

Describe and show how to run the tests with code examples.

## Contributors

1)Dhananjay