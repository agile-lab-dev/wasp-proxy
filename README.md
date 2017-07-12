
# WASP Proxy

---

Table of contents
-------------

- General
  - [Overview](#overview)
  - [How to use](#how-to-use)
 
---


Gitter
-------------

Chat with the team [Gitter](https://gitter.im/agile-lab-wasp/Lobby?utm_source=share-link&utm_medium=link&utm_campaign=share-link)


Overview
-------------

WASP Proxy is a producer of [WASP](https://github.com/agile-lab-dev/wasp) to push easily messages in Kafka.

Thought a simple web REST API powered by Confluent [Kafka REST Proxy](https://github.com/confluentinc/kafka-rest).
The configuration of the WASP proxy is the same of [Kafka REST Proxy](http://docs.confluent.io/current/kafka-rest/docs/intro.html). 

Can be easily managed from WASP dashboard. 


How to use 
-------------

- Start WASP with zookeeper and Kafka
- Run the WASP Proxy ./target/universal/stage/bin/wasp-proxy \<producer-id\>
- Start the producer from WASP console 
- Call the API to push date into WASP 


## Produce a message with JSON data
    $ curl -X POST -H "Content-Type: application/vnd.kafka.json.v2+json" \
          --data '{"records":[{"value":{"name": "testUser"}}]}' \
          "http://localhost:8082/topics/raw"
          
      {"offsets":[{"partition":0,"offset":3,"error_code":null,"error":null}],"key_schema_id":null,"value_schema_id":null}
