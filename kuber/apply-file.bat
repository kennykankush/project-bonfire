kubectl apply -f bonfire-v1-backend.yml
kubectl apply -f bonfire-v1-frontend.yml
kubectl apply -f app-secrets.yml
kubectl apply -f ingress.yml
pause