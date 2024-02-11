kubectl delete -f db-config.yaml
kubectl delete -f db-secret.yaml
kubectl delete -f deployment-core.yaml
kubectl delete -f deployment-eureka.yaml
kubectl delete -f deployment-fs.yaml
kubectl delete -f deployment-gateway.yaml
kubectl delete -f deployment-keycloak.yaml
kubectl delete -f deployment-sso.yaml
kubectl delete -f ingress.yaml
kubectl delete -f keycloak-config.yaml
kubectl delete -f keycloak-secret.yaml
kubectl delete -f deployment-zoo-keeper.yaml
kubectl delete -f deployment-kafka.yaml

kubectl apply -f deployment-zoo-keeper.yaml
kubectl apply -f deployment-kafka.yaml
kubectl apply -f db-config.yaml
kubectl apply -f db-secret.yaml
kubectl apply -f deployment-core.yaml
kubectl apply -f deployment-eureka.yaml
kubectl apply -f deployment-fs.yaml
kubectl apply -f deployment-gateway.yaml
kubectl apply -f deployment-keycloak.yaml
kubectl apply -f deployment-sso.yaml
kubectl apply -f ingress.yaml
kubectl apply -f keycloak-config.yaml
kubectl apply -f keycloak-secret.yaml

kubectl get pods