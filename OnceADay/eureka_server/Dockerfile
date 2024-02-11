FROM maven:3.8.7-openjdk-18-slim AS DEPS
ENV HOME=/usr/src/app

WORKDIR $HOME

COPY ./eureka_server/pom.xml $HOME/pom.xml

RUN mvn dependency:go-offline

FROM maven:3.8.7-openjdk-18-slim AS BUILDER
ENV HOME=/usr/src/app
WORKDIR $HOME

COPY --from=deps /root/.m2/ /root/.m2/
COPY --from=deps $home/ $home/

COPY ./eureka_server $HOME/

RUN mvn package -T 1.5C -am -q

FROM openjdk:18 AS BUILD
ENV HOME=/usr/src/app

COPY --from=builder $HOME/target/eureka.jar /app/eureka.jar
WORKDIR /app
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka.jar"]