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

## test if the server is running

http://localhost:8080/api/ping