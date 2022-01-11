# SNLP-miniproject WS 2021/2022

Fact checker based on wikipedia corpus, StandfortNLP, TagMe and WordNet.

Steps for running the application

1. start docker container
2. run spring
3. 'access via UI' OR 'access the demos (training & test)'

## pre requirements for the development environment

- java 11
- docker
  - docker-compose
  - maven 3.1 or higher

## start docker container

Starts the docker containers necessary for the fact check.

```shell
docker-compose up
```

## run unit tests

Maven command, for running the unit tests.

```shell
mvn clean test
```

## run spring

Starts the spring boot application.

```shell
mvn spring-boot:run
```

## access via UI

Access the UI via browser (with enabled js).

- upload => GET http://localhost:8080/upload
- factCheck => GET http://localhost:8080/factCheck

## access the demos (training & test)

Run the the fact checking with the training & test data files of the execise.

- factCheck training file (.ttl-output) => GET http://localhost:8080/demo/runFileTraining
- factCheck test file (.ttl-output) => GET http://localhost:8080/demo/runFileTest
- factCheck training file (.html-output) => GET http://localhost:8080/demo/runFactCheck
- ner training file (.html-output) => GET http://localhost:8080/demo/runNer

## api access

Internal API access.

- ping => GET http://localhost:8080/api/ping
- upload => POST http://localhost:8080/api/upload
- factCheck => GET http://localhost:8080/api/factCheck?sentence={text}

## access wordnet

Internal WordNet API access.

- GET http://localhost:5679/synonyms/1/{word}
- GET http://localhost:5679/hypernyms/1/{word}
- GET http://localhost:5679/substance_meronyms/1/{word}
- GET http://localhost:5679/hyponyms/1/{word}
- GET http://localhost:5679/antonyms/1/{word}
- GET http://localhost:5679/substance_holonyms/1/{word}
- GET http://localhost:5679/causes/1/{word}

## licenses

- GNU v3 https://github.com/stanfordnlp/CoreNLP/blob/main/LICENSE.txt
- Apache License 2.0 https://github.com/gammaliu/tagme/blob/master/LICENSE
- WordNet 3.0 https://wordnet.princeton.edu/license-and-commercial-use