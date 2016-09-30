# Example Request Mapper

Request mapper is an extension to the Onegini Security Proxy, which allows to give instructions on how to modify request before it gets to a resource server.
For more information please see [Security Proxy Request Mapper Documentation](https://docs.onegini.com/msp/3.0/security-proxy/topics/request-mapper.html)

## Configuration

| Property                      | Description                                               |
|-------------------------------|-----------------------------------------------------------|
| basic.authentication.username | The basic authentication username used to access the api. |
| basic.authentication.password | The basic authentication password used to access the api. |
| onegini.undertow.port         | The port on which Request Mapper will be exposed.         |
| onegini.undertow.host         | The host where Request Mapper will be started up on       |                                                                                                                                                  | 

## Build the sourcecode

`mvn clean install`

## Build the application with docker

`mvn docker:build -Psnapshot`

## Run the application

You can either run the application via the Spring Boot Maven plugin or by using the jar file created while building the application.

`mvn spring-boot:run`

or 

`java -jar <location of the jar file>`