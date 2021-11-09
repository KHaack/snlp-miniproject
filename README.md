# snlp-miniproject

## dev installation
  - java 8
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

```powershell
docker-compose up
mvn spring-boot:run
```
