# Example Request Mapper

Request mapper is an extension to the Onegini Security Proxy, which allows to give instructions on how to modify request before it gets to a resource server.
For more information please see [Security Proxy Request Mapper Documentation](https://docs.onegini.com/msp/latest/security-proxy/topics/embedded-resource-gateway-functionality/implement-request-mapper.html)

## Frameworks

In the Example Request Mapper project the following main frameworks are used:
- Undertow (the reason for using it over Spring MVC is because of its lightweight and better performance)
- Spring Boot

## Configuration

| Property                      | Default value | Description                                               |
|-------------------------------|---------------|-----------------------------------------------------------|
| basic.authentication.username | username      | The basic authentication username used to access the api. |
| basic.authentication.password | password      | The basic authentication password used to access the api. |
| onegini.undertow.port         | 5540          | The port on which Request Mapper will be exposed.         |
| onegini.undertow.host         | 0.0.0.0       | The host where Request Mapper will be exposed.            |                                                                                                                                                 | 

## Build the sourcecode

`mvn clean install`

## Build the application with docker

`mvn docker:build`

## Run the application

You can either run the application via the Spring Boot Maven plugin or by using the jar file created while building the application.

`mvn spring-boot:run`

or 

`java -jar <location of the jar file>`

or (via Docker)

`docker run example-security-proxy-resource-gateway:latest -P5540:5540`

## Example request

```http
POST /map-request HTTP/1.1
Host: localhost:5540
Content-Type: application/json
 
{
  "request_uri": "/some-resource-gateway-request-uri",
  "token_validation_result": {
    "scope": "exampleScope",
    "sub": "exampleUserId",
    "amr": [
       "DEFAULT"
     ]
    ...
  },
  "headers": {
    "header_1":"value_1",
    "header_2":"value_2",
    "authorization": "Bearer 060E7B02C875D5D74318FE0BBDA22BEFBA882B9B90F918BE77F9FCFF1A0E24B0"
  }
}
```

## Example response

```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8

{
  "request_uri": "/some-resource-gateway-request-uri/exampleUserId",
  "headers": {
    "header_1": "value_1",
    "header_2": "value_2",
    "authorized_scopes": "exampleScope"
  }
}
```
