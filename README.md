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


## Medium
* https://medium.com/@taesulee93/spring-for-rabbitmq-in-action-declarables%EC%9D%84-%ED%99%9C%EC%9A%A9%ED%95%9C-exchange-queue-binding-dlq-%EA%B0%84%ED%8E%B8-%EC%84%A4%EC%A0%95-a248744d6240
