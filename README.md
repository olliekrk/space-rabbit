# Space Rabbit
> Short project using RabbitMQ client.

* Space agencies can publish tasks of 3 types: 
  * Cargo
  * Transit
  * Satellite
* Each space carrier can accept 2 task types. After task completion, the agency recieves confirmation message. 
* Each space admin receives a copy of every message that passes through the message exchange.

### Dependencies
* RabbitMQ AMQP Client
* Java Faker
* json4s
* sbt
