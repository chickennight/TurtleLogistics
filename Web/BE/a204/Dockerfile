FROM adoptopenjdk:11-jre-hotspot

WORKDIR /app

# 환경변수 정의

ARG DB_USER
ARG DB_PASSWORD
ENV DB_USER ${DB_USER}
ENV DB_PASSWORD ${DB_PASSWORD}

COPY ./build/libs/a204-1.3.2-SNAPSHOT.jar /app
CMD java -jar a204-1.3.2-SNAPSHOT.jar