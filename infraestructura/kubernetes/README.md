# Local Kubernetes Deployment

This directory contains Kubernetes manifests for deploying the Franchise API locally using Minikube or Docker Desktop's built-in Kubernetes.

## Prerequisites

1. Kubernetes cluster (Minikube or Docker Desktop with Kubernetes enabled)
2. `kubectl` command-line tool
3. Docker (for building images)

## Deployment Steps

1. **Start your local Kubernetes cluster**

   - For Minikube:
     ```bash
     minikube start
     ```
   - For Docker Desktop: Enable Kubernetes in Docker Desktop settings

2. **Build the Docker image**

   ```bash
   # Build the application JAR
   ./mvnw clean package
   
   # Build and tag the Docker image
   docker build -t franchise-api:latest .
   
   # If using Minikube, load the image into the Minikube Docker daemon
   minikube image load franchise-api:latest
   ```

3. **Deploy the application**

   ```bash
   # Apply all Kubernetes manifests
   # Apply RBAC and service accounts first
   kubectl apply -f service-account.yaml
   kubectl apply -f mongo-service-account.yaml
   kubectl apply -f rbac.yaml
   
   # Then apply the rest of the resources
   kubectl apply -f namespace.yaml
   kubectl apply -f configmap.yaml
   kubectl apply -f secret.yaml
   kubectl apply -f mongo.yaml
   kubectl apply -f deployment.yaml
   kubectl apply -f service.yaml
   
   # If you want to access the API from your host, apply the ingress
   kubectl apply -f ingress.yaml
   ```

4. **Access the application**

   - For ClusterIP service (API accessible within the cluster):
     ```bash
     kubectl port-forward svc/franchise-api 8080:80 -n franchise
     ```
     Then access the API at: http://localhost:8080

   - For Ingress (if installed):
     - With Minikube:
       ```bash
       minikube addons enable ingress
       minikube tunnel
       ```
     - Access the API at: http://localhost/api

## Verifying the Deployment

```bash
# Check all resources in the franchise namespace
kubectl get all -n franchise

# Check pod logs
kubectl logs -l app=franchise-api -n franchise

# Check service details
kubectl describe svc franchise-api -n franchise
```

## Cleaning Up

To delete all resources:

```bash
kubectl delete -f .
```

## Notes

- The MongoDB data is stored in an `emptyDir` volume, which means data will be lost when the pod is deleted.
- For production, use a persistent volume for MongoDB data.
- The default MongoDB credentials are in `secret.yaml`. Change them for production use.
