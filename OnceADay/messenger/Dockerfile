FROM maven:3.8.7-openjdk-18-slim AS DEPS
ENV HOME=/usr/src/app
ENV SERVICE=messenger

WORKDIR $HOME

COPY pom.xml $HOME/pom.xml
COPY ./eureka_server/pom.xml $HOME/eureka_server/pom.xml
COPY ./sso/pom.xml $HOME/sso/pom.xml
COPY ./sso/sso_api/pom.xml $HOME/sso/sso_api/pom.xml
COPY ./sso/sso_server/pom.xml $HOME/sso/sso_server/pom.xml
COPY ./gateway/pom.xml $HOME/gateway/pom.xml
COPY ./database/pom.xml $HOME/database/pom.xml
COPY ./file_storage/pom.xml $HOME/file_storage/pom.xml
COPY ./file_storage/file_storage_api/pom.xml $HOME/file_storage/file_storage_api/pom.xml
COPY ./file_storage/file_storage_app/pom.xml $HOME/file_storage/file_storage_app/pom.xml
COPY ./commons/pom.xml $HOME/commons/pom.xml
COPY ./core/pom.xml $HOME/core/pom.xml
COPY ./core/core_api/pom.xml $HOME/core/core_api/pom.xml
COPY ./core/core_app/pom.xml $HOME/core/core_app/pom.xml
COPY ./messenger/pom.xml $HOME/messenger/pom.xml
COPY ./messenger/messenger_api/pom.xml $HOME/messenger/messenger_api/pom.xml
COPY ./messenger/messenger_app/pom.xml $HOME/messenger/messenger_app/pom.xml
COPY ./messenger/messenger_common/pom.xml $HOME/messenger/messenger_common/pom.xml
COPY ./messenger/messenger_common/pom.xml $HOME/messenger/messenger_common/pom.xml

RUN mvn dependency:go-offline -pl :${SERVICE}

FROM maven:3.8.7-openjdk-18-slim AS BUILDER
ENV HOME=/usr/src/app
ENV APP=messenger_app

WORKDIR $HOME

COPY --from=deps /root/.m2/ /root/.m2/
COPY --from=deps $home/ $home/

COPY . $HOME/

RUN mvn -Dmaven.test.skip package -T 1.5C -am -q -pl :${APP}

FROM openjdk:18 AS BUILD
ENV HOME=/usr/src/app
ENV SERVICE=messenger
COPY --from=builder $HOME/${SERVICE}/${SERVICE}_app/target/${SERVICE}.jar /app/${SERVICE}.jar
WORKDIR /app
ENTRYPOINT java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5091 -Djava.security.egd=file:/dev/./urandom -jar ${SERVICE}.jar

EXPOSE 5091
EXPOSE 8091