# Porting Manual

1. Stacks
2. Build & Distribute
3. Deployment Command
4. MySQL WorkBench Connection
5. Nginx default
6. EC2 Setting
7. Settings or Tips

## 1. Stacks

### 1.1 Web

<img src="ProfileImage/WebArci.png">

### 1.1.1 Back-End

1. JVM 11
2. Spring Boot 2.7.13
3. IntelliJ Ultimate LTS
4. Gradle
5. Spring Data JPA
6. MySQL 8.0.33
7. Workbench 8.0.33
8. Spring Boot Starter 3.0.0
9. thymeleaf
10. mqtt
11. jackson 2.13.4.1
12. Spring Security
13. JWT 0.11.5
14. Lombok
15. Logger
16. AWS IoT Device sdk java 1.3.9

### 1.1.2 Front-End

1. Vue 3.2.13
2. axios 1.4.0
3. Vuex 4.0.2
4. Vuetify 3.0.0-beta.0
5. vue-router 4.0.13
6. chart.js 3.8.3
7. vue-chart.js 5.2.0
8. vuex-persistedstate 4.1.0
9. fortawesome 6.4.2
10. Node.js 18.16.1

### 1.2 IoT

### 1.2.1 HW
- esp12E(NodeMCU V3)
- esp12F(NodeMCU V2)
- esp8266(NodeMCU devkit V0.9)
- esp32
- 아두이노 D1 R1
- 서보모터 mg996r
- Rasberry Pi4
- Rasberry Pi Camera Rev 1.3


### 1.2.2 SW

## 2. Build & Distribute

### 2.1 Nginx

```
user nginx;
worker_processes  auto;
error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;

events {
  worker_connections  1024;
}

http {
  include       /etc/nginx/mime.types;
  default_type  application/octet-stream;

  # 백엔드 upstream 설정
  upstream myweb-api {
      server {DOMAIN}:{PORT};
  }

  # 프론트엔드 upstream 설정
  upstream next-server {
      server {DOMAIN}:{PORT};
  }

  # HTTP 서버 설정
  server {
      listen 80;
      server_name {DOMAIN};
      server_tokens off;

      location /.well-known/acme-challenge/ {
      allow all;
      root /var/www/certbot;
      }

      location / {
            return 301 https://$host$request_uri;
      }
  }

  # HTTPS 설정
  server {
      listen 443 ssl;
      server_name {DOMAIN};
      server_tokens off;

      ssl_certificate /etc/letsencrypt/live/{DOMAIN}/fullchain.pem;
      ssl_certificate_key /etc/letsencrypt/live/{DOMAIN}/privkey.pem;
      include /etc/letsencrypt/options-ssl-nginx.conf;
      ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

      location /turtle {
          proxy_pass         http://myweb-api/turtle;
      }

      location / {
          proxy_pass         http://next-server/;
          proxy_http_version 1.1;
          proxy_set_header Upgrade $http_upgrade;
          proxy_set_header Connection 'upgrade';
          proxy_set_header Host $host;
          proxy_cache_bypass $http_upgrade;
      }
  }

  log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';
  access_log  /var/log/nginx/access.log  main;

  sendfile        on;
  keepalive_timeout  65;
  # include /etc/nginx/conf.d/*.conf;
}

```

### 2.2 docker-compose.yml

```
version: '2.20.2'

networks:
  {NETWORK}:
    driver: bridge

services:

  nginx:
    container_name: nginx
    image: nginx:1.21.5-alpine
    restart: unless-stopped
    volumes:
      - ./mount/nginx.conf:/etc/nginx/nginx.conf
      - ./mount/nginx/sites-available:/etc/nginx/sites-available
      - ./data/certbot/conf:/etc/letsencrypt				# 인증서 공유를 위한 바인딩
      - ./data/certbot/www:/var/www/certbot
    ports:
      - 80:80
      - 443:443
    environment:
      - TZ=Asia/Seoul
    networks:
      - turtle
    command: "/bin/sh -c 'while :; do sleep 6h & wait $${!}; nginx -s reload; done & nginx -g \"daemon off;\"'"

  certbot:
    container_name: certbot
    image: certbot/certbot
    restart: unless-stopped
    volumes:
      - ./data/certbot/conf:/etc/letsencrypt
      - ./data/certbot/www:/var/www/certbot
    environment:
      - TZ=Asia/Seoul  # Jenkins Container 설정
    entrypoint: "/bin/sh -c 'trap exit TERM; while :; do certbot renew; sleep 12h & wait $${!}; done;'"

  jenkins:
    build:
      context: ./
      dockerfile: Dockerfile-jenkins
    container_name: jenkins
    restart: unless-stopped
    ports:
      - "{PORT}:8080"
    volumes:
      - ./mount/jenkins:/var/jenkins_home
      - /var/run/docker.sock:/var/run/docker.sock
      - ./mount/jenkins-html:/var/lib/jenkins
#      - ./jenkins-docker-install.sh:/jenkins-docker-install.sh
    user: root
    environment:
      - TZ=Asia/Seoul
    networks:
      - turtle

  mysql:
    container_name: mysql-container
    image: mysql:latest
    restart: unless-stopped
    ports:
      - "{PORT}:3306"
    volumes:
      - ./mount/mysqldata:/var/lib/mysql
    environment:
      MYSQL_ROOT_PASSWORD: {PASSWORD}
```

## 3. Deployment Command

### 3.1 FRONTEND

#### 3.1.2 nginx.conf

```
server {
    listen 80;
    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri $uri/ /index.html =404;
    }
}
```

#### 3.1.2 Dockerfile

```
# 첫 번째 빌드 단계
# Node.js를 설치한 베이스 이미지로부터 시작
FROM node:18.16.1 as build

# /app을 작업 디렉토리로 설정
WORKDIR /app

# 프로젝트의 package.json과 package-lock.json을 복사
COPY package*.json ./

RUN npm install -g npm@9.5.1

RUN npm install

# 나머지 소스 코드와 리소스를 복사
COPY . .

# 프로젝트 빌드
RUN npm run build

# 두 번째 빌드 단계
# Nginx를 베이스 이미지로 사용
FROM nginx:1.25.1-alpine

# 첫 번째 빌드 단계에서 빌드한 소스를 Nginx가 서빙할 수 있게 복사
COPY --from=build /app/dist /usr/share/nginx/html

# nginx.conf를 Nginx 설정 디렉토리에 복사
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 포트80에서 트래픽 listen 하겠다는 뜻
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

#### 3.2.3 jenkins Excute shell

```
CONTAINER_ID=$(docker ps -aqf name=front-backup)
if [ "$CONTAINER_ID" != "" ]
then
    docker stop front-backup || true
    docker rm front-backup || true
fi

cd /var/jenkins_home/workspace/FRONTEND_Lab/Web/FE/frontend_part

docker build -t front-backup -f Dockerfile .

docker run -d -p 8102:80 --network ubuntu_turtle -e TZ=Asia/Seoul --name front-backup front-backup
```

### 3.2 BACKEND

#### 3.2.1 Dockerfile

```
FROM adoptopenjdk:11-jre-hotspot

WORKDIR /app

# 환경변수 정의

ARG DB_USER
ARG DB_PASSWORD
ENV DB_USER ${DB_USER}
ENV DB_PASSWORD ${DB_PASSWORD}

COPY ./build/libs/a204-1.3.2-SNAPSHOT.jar /app
CMD java -jar a204-1.3.2-SNAPSHOT.jar
```

#### 3.2.2 jenkins Excute shell

```
CONTAINER_ID=$(docker ps -aqf name=backup)
if [ "$CONTAINER_ID" != "" ]
then
    docker stop backup || true
fi

cd /var/jenkins_home/workspace/BACKEND_Lab/Web/BE/a204
docker build -t backup -f Dockerfile .
docker run -it -d --rm -p 8081:8080 --network ubuntu_turtle -e TZ=Asia/Seoul -e DB_USER=${DB_USER} -e DB_PASSWORD=${DB_PASSWORD} --name backup backup
```

## 4. MySQL WorkBench Connection

### 4.2 Spring Boot

```
spring.datasource.url=jdbc:mysql://{DOMAIN}/{SCHEMA}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username={ID}
spring.datasource.password={PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## 5. Nginx default

```
server {
     listen 80;
     listen [::]:80;

     server_name {DOMAIN};

     location /.well-known/acme-challenge/ {
             allow all;
             root /var/www/certbot;
     }
}
```

## 6. EC2 Setting

내부 오픈 포트: 22,80,443,{젠킨스 포트},{백엔드포트},{프론트엔드포트}

## 7. Files ignore

### 7.1 IoT

```
_Src/
```

### 7.2 BackEnd

```
HELP.md
.gradle
build/
!gradle/wrapper/gradle-wrapper.jar
!**/src/main/**/build/
!**/src/test/**/build/

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache
bin/
!**/src/main/**/bin/
!**/src/test/**/bin/

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr
out/
!**/src/main/**/out/
!**/src/test/**/out/

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/

### VS Code ###
.vscode/
# Created by https://www.toptal.com/developers/gitignore/api/windows,macos,gradle,intellij,java
# Edit at https://www.toptal.com/developers/gitignore?templates=windows,macos,gradle,intellij,java

### Intellij ###
# Covers JetBrains IDEs: IntelliJ, RubyMine, PhpStorm, AppCode, PyCharm, CLion, Android Studio, WebStorm and Rider
# Reference: https://intellij-support.jetbrains.com/hc/en-us/articles/206544839

# User-specific stuff
.idea/**/workspace.xml
.idea/**/tasks.xml
.idea/**/usage.statistics.xml
.idea/**/dictionaries
.idea/**/shelf

# AWS User-specific
.idea/**/aws.xml

# Generated files
.idea/**/contentModel.xml

# Sensitive or high-churn files
.idea/**/dataSources/
.idea/**/dataSources.ids
.idea/**/dataSources.local.xml
.idea/**/sqlDataSources.xml
.idea/**/dynamic.xml
.idea/**/uiDesigner.xml
.idea/**/dbnavigator.xml

# Gradle
.idea/**/gradle.xml
.idea/**/libraries

# Gradle and Maven with auto-import
# When using Gradle or Maven with auto-import, you should exclude module files,
# since they will be recreated, and may cause churn.  Uncomment if using
# auto-import.
# .idea/artifacts
# .idea/compiler.xml
# .idea/jarRepositories.xml
# .idea/modules.xml
# .idea/*.iml
# .idea/modules
# *.iml
# *.ipr

# CMake
cmake-build-*/

# Mongo Explorer plugin
.idea/**/mongoSettings.xml

# File-based project format
*.iws

# IntelliJ
out/

# mpeltonen/sbt-idea plugin
.idea_modules/

# JIRA plugin
atlassian-ide-plugin.xml

# Cursive Clojure plugin
.idea/replstate.xml

# SonarLint plugin
.idea/sonarlint/

# Crashlytics plugin (for Android Studio and IntelliJ)
com_crashlytics_export_strings.xml
crashlytics.properties
crashlytics-build.properties
fabric.properties

# Editor-based Rest Client
.idea/httpRequests

# Android studio 3.1+ serialized cache file
.idea/caches/build_file_checksums.ser

### Intellij Patch ###
# Comment Reason: https://github.com/joeblau/gitignore.io/issues/186#issuecomment-215987721

# *.iml
# modules.xml
# .idea/misc.xml
# *.ipr

# Sonarlint plugin
# https://plugins.jetbrains.com/plugin/7973-sonarlint
.idea/**/sonarlint/

# SonarQube Plugin
# https://plugins.jetbrains.com/plugin/7238-sonarqube-community-plugin
.idea/**/sonarIssues.xml

# Markdown Navigator plugin
# https://plugins.jetbrains.com/plugin/7896-markdown-navigator-enhanced
.idea/**/markdown-navigator.xml
.idea/**/markdown-navigator-enh.xml
.idea/**/markdown-navigator/

# Cache file creation bug
# See https://youtrack.jetbrains.com/issue/JBR-2257
.idea/$CACHE_FILE$

# CodeStream plugin
# https://plugins.jetbrains.com/plugin/12206-codestream
.idea/codestream.xml

# Azure Toolkit for IntelliJ plugin
# https://plugins.jetbrains.com/plugin/8053-azure-toolkit-for-intellij
.idea/**/azureSettings.xml

### Java ###
# Compiled class file
*.class

# Log file
*.log

# BlueJ files
*.ctxt

# Mobile Tools for Java (J2ME)
.mtj.tmp/

# Package Files #
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# virtual machine crash logs, see http://www.java.com/en/download/help/error_hotspot.xml
hs_err_pid*
replay_pid*

### macOS ###
# General
.DS_Store
.AppleDouble
.LSOverride

# Icon must end with two \r
Icon


# Thumbnails
._*

# Files that might appear in the root of a volume
.DocumentRevisions-V100
.fseventsd
.Spotlight-V100
.TemporaryItems
.Trashes
.VolumeIcon.icns
.com.apple.timemachine.donotpresent

# Directories potentially created on remote AFP share
.AppleDB
.AppleDesktop
Network Trash Folder
Temporary Items
.apdisk

### macOS Patch ###
# iCloud generated files
*.icloud

### Windows ###
# Windows thumbnail cache files
Thumbs.db
Thumbs.db:encryptable
ehthumbs.db
ehthumbs_vista.db

# Dump file
*.stackdump

# Folder config file
[Dd]esktop.ini

# Recycle Bin used on file shares
$RECYCLE.BIN/

# Windows Installer files
*.cab
*.msi
*.msix
*.msm
*.msp

# Windows shortcuts
*.lnk

### Gradle ###
.gradle
**/build/
!src/**/build/

# Ignore Gradle GUI config
gradle-app.setting

# Avoid ignoring Gradle wrapper jar file (.jar files are usually ignored)
!gradle-wrapper.jar

# Avoid ignore Gradle wrappper properties
!gradle-wrapper.properties

# Cache of project
.gradletasknamecache

# Eclipse Gradle plugin generated files
# Eclipse Core
.project
# JDT-specific (Eclipse Java Development Tools)
.classpath

### Gradle Patch ###
# Java heap dump
*.hprof

application.properties

# End of https://www.toptal.com/developers/gitignore/api/windows,macos,gradle,intellij,java
```

### 7.3 FrontEnd

```
.DS_Store
node_modules
/dist


# local env files
.env.development
.env.production
.env.local
.env.*.local

# Log files
npm-debug.log*
yarn-debug.log*
yarn-error.log*
pnpm-debug.log*

# Editor directories and files
.idea
.vscode
*.suo
*.ntvs*
*.njsproj
*.sln
*.sw?
```

## 8. Settings or Tips

### 8.1 init-letsencrypt.sh

```
#!/bin/bash

if ! [ -x "$(command -v docker-compose)" ]; then
  echo 'Error: docker-compose is not installed.' >&2
  exit 1
fi

domains=({DOMAIN})
rsa_key_size=4096
data_path="./data/certbot"
email="{EMAIL}" # Adding a valid address is strongly recommended
staging=0 # Set to 1 if you're testing your setup to avoid hitting request limits

if [ -d "$data_path" ]; then
  read -p "Existing data found for $domains. Continue and replace existing certificate? (y/N) " decision
  if [ "$decision" != "Y" ] && [ "$decision" != "y" ]; then
    exit
  fi
fi


if [ ! -e "$data_path/conf/options-ssl-nginx.conf" ] || [ ! -e "$data_path/conf/ssl-dhparams.pem" ]; then
  echo "### Downloading recommended TLS parameters ..."
  mkdir -p "$data_path/conf"
  curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot-nginx/certbot_nginx/_internal/tls_configs/options-ssl-nginx.conf > "$data_path/conf/options-ssl-nginx.conf"
  curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot/certbot/ssl-dhparams.pem > "$data_path/conf/ssl-dhparams.pem"
  echo
fi

echo "### Creating dummy certificate for $domains ..."
path="/etc/letsencrypt/live/$domains"
mkdir -p "$data_path/conf/live/$domains"
docker-compose run --rm --entrypoint "\
  openssl req -x509 -nodes -newkey rsa:$rsa_key_size -days 1\
    -keyout '$path/privkey.pem' \
    -out '$path/fullchain.pem' \
    -subj '/CN=localhost'" certbot
echo


echo "### Starting nginx ..."
docker-compose up --force-recreate -d nginx
echo

echo "### Deleting dummy certificate for $domains ..."
docker-compose run --rm --entrypoint "\
  rm -Rf /etc/letsencrypt/live/$domains && \
  rm -Rf /etc/letsencrypt/archive/$domains && \
  rm -Rf /etc/letsencrypt/renewal/$domains.conf" certbot
echo


echo "### Requesting Let's Encrypt certificate for $domains ..."
#Join $domains to -d args
domain_args=""
for domain in "${domains[@]}"; do
  domain_args="$domain_args -d $domain"
done

# Select appropriate email arg
case "$email" in
  "") email_arg="--register-unsafely-without-email" ;;
  *) email_arg="--email $email" ;;
esac

# Enable staging mode if needed
if [ $staging != "0" ]; then staging_arg="--staging"; fi

docker-compose run --rm --entrypoint "\
  certbot certonly --webroot -w /var/www/certbot \
    $staging_arg \
    $email_arg \
    $domain_args \
    --rsa-key-size $rsa_key_size \
    --agree-tos \
    --force-renewal" certbot
echo

echo "### Reloading nginx ..."
docker-compose exec nginx nginx -s reload

```

### 8.2 Docker-jenkins

```
 # 베이스 이미지 설정
FROM jenkins/jenkins:lts

# Docker CLI 설치
USER root
RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://get.docker.com -o get-docker.sh && \
    sh get-docker.sh && \
    rm get-docker.sh

USER jenkins
```
