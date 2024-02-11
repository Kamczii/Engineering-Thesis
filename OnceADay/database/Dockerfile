FROM liquibase/liquibase:latest
ADD database/src/main/resources/liquibase /home/oad
EXPOSE 6543
#ENTRYPOINT ["/liquibase/liquibase","--defaults-file=/home/oad/liquibase-local.properties", "--log-level=INFO", "drop-all"]
ENTRYPOINT ["/liquibase/liquibase","--defaults-file=/home/oad/liquibase-local.properties", "--log-level=INFO", "update"]