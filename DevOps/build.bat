docker build -t core -f core/Dockerfile .
docker build -t eureka -f eureka_server/Dockerfile .
docker build -t file_storage -f file_storage/Dockerfile .
docker build -t gateway -f gateway/Dockerfile .
docker build -t sso -f sso/Dockerfile .
docker build -t keycloak -f ./IDP/SPI/Dockerfile ./IDP