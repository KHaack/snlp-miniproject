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

## run tests

```shell
mvn clean test
```

## run spring

```shell
mvn spring-boot:run
```

## access

- ping => http://localhost:8080/api/ping
- factCheck training file (.ttl-output) => http://localhost:8080/runFileTraining
- factCheck test file (.ttl-output) => http://localhost:8080/runFileTest
- factCheck training file (.html-output) => http://localhost:8080/runFactCheck
- ner training file (.html-output) => http://localhost:8080/runNer

## licenses

https://wordnet.princeton.edu/license-and-commercial-use