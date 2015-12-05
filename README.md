## BingoDB

REST API service for [Bingo NoSQL](http://lifescience.opensource.epam.com/bingo/bingo-nosql.html) 

### Development

For development, simply run `Application` class.

### Deployment

For deployment, BingoDB should be installed as service. 

See `bin/config/application.properties` in deployment package for usual properties. 

Application server requirements:

- 1GB of free memory
- Java Runtime Environment version 7 (Better use the latest version of Java)

#### Unix service
See [Spring Boot docs](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment-service)

#### Windows service 
Use `bin/bingodb.exe` (install|uninstall|start|stop) in deployment package. For more information see [winsw docs](https://github.com/kohsuke/winsw)
