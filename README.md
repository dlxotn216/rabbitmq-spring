## Docker 설정 
docker run -d -p 5672:5672 -p 15672:15672 --hostname taesu-rabbit --name local-rabbit -e RABBITMQ_DEFAULT_USER=taesu -e RABBITMQ_DEFAULT_PASS=password rabbitmq:3-management

## Spring RabbitMW Config
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: taesu
    password: password
```

## Management
* localhost:15672
* taesu/password

