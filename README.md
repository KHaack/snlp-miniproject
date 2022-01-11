# snlp-miniproject

## dev installation

- java 11
- docker
    - docker-compose
    - maven 3.1 or higher

## running docker on windows/wsl2 config

```powershell
wsl -d docker-desktop
sysctl -w vm.max_map_count=262144
```

## vserver config

https://stackoverflow.com/questions/16789288/java-lang-outofmemoryerror-unable-to-create-new-native-thread

```bash
systemctl show --property DefaultTasksMax
vim /etc/systemd/system.conf
edit DefaultTasksMax=128
```

## start docker container

```shell
docker-compose up
```

## access kibana

http://localhost:5601

## run unit tests

```shell
mvn clean test
```

## run spring

```shell
mvn spring-boot:run
```

## test runs

- factCheck training file (.ttl-output) => GET http://localhost:8080/runFileTraining
- factCheck test file (.ttl-output) => GET http://localhost:8080/runFileTest
- factCheck training file (.html-output) => GET http://localhost:8080/runFactCheck
- ner training file (.html-output) => GET http://localhost:8080/runNer

## api access

- ping => GET http://localhost:8080/api/ping
- upload => POST http://localhost:8080/api/upload
- factCheck => GET http://localhost:8080/api/factCheck?sentence={text}

## ui access

- upload => GET http://localhost:8080/upload
- factCheck => GET http://localhost:8080/factCheck

## licenses

https://wordnet.princeton.edu/license-and-commercial-use