FROM maven:3.8.7-openjdk-18-slim AS DEPS
ENV HOME=/usr/src/app

WORKDIR $HOME

COPY ./pom.xml $HOME/pom.xml
COPY ./SPI/pom.xml $HOME/SPI/pom.xml
COPY ./idp_sidecar/pom.xml $HOME/idp_sidecar/pom.xml

RUN mvn dependency:go-offline -pl :SPI


FROM maven:3.8.7-openjdk-18-slim AS BUILDER
ENV HOME=/usr/src/app
ENV APP=sso_server

WORKDIR $HOME

COPY --from=deps /root/.m2/ /root/.m2/
COPY --from=deps $home/ $home/

COPY ./SPI $HOME/SPI

RUN mvn package -T 1.5C -am -q -pl :SPI

FROM bitnami/keycloak:21.1.2
ENV HOME=/usr/src/app

ENV DB_VENDOR=POSTGRES
ENV DB_ADDR=postgres
ENV DB_DATABASE=keycloak
ENV DB_USER=oad
ENV DB_PASSWORD=oad
ENV KEYCLOAK_ADMIN_USER=oad
ENV KEYCLOAK_ADMIN_PASSWORD=oad
ENV KC_HEALTH_ENABLED=true

COPY --from=builder $HOME/SPI/target/spi.jar /opt/bitnami/keycloak/providers/spi.jar
EXPOSE 8080