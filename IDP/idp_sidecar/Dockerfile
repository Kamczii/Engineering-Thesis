FROM maven:3.8.6-openjdk-11-slim AS DEPS
ENV HOME=/usr/src/app

WORKDIR $HOME

COPY ./pom.xml $HOME/pom.xml
COPY ./SPI/pom.xml $HOME/SPI/pom.xml
COPY ./idp_sidecar/pom.xml $HOME/idp_sidecar/pom.xml

RUN mvn dependency:go-offline -pl :idp_sidecar


FROM maven:3.8.6-openjdk-11-slim AS BUILDER
ENV HOME=/usr/src/app
ENV APP=sso_server

WORKDIR $HOME

COPY --from=deps /root/.m2/ /root/.m2/
COPY --from=deps $home/ $home/

COPY ./idp_sidecar $HOME/idp_sidecar

RUN mvn package -T 1.5C -am -q -pl :idp_sidecar

FROM openjdk:11 AS BUILD
ENV HOME=/usr/src/app

COPY --from=builder $HOME/idp_sidecar/target/sidecar.jar /app/sidecar.jar
WORKDIR /app
EXPOSE 8084
EXPOSE 5085
ENTRYPOINT java -jar sidecar.jar