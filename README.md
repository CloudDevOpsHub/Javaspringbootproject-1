# 🚀 Spring Boot Demo - CI/CD Pipeline

> **CloudDevOpsHub Batch 42** - Complete Java 17 Spring Boot project with separate CI and CD pipelines

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-green?style=flat-square&logo=spring)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=flat-square&logo=docker)
![Kubernetes](https://img.shields.io/badge/Kubernetes-Ready-326CE5?style=flat-square&logo=kubernetes)

---

## 📁 Project Structure

```
spring-boot-demo/
├── src/
│   ├── main/
│   │   ├── java/com/clouddevopshub/demo/
│   │   │   ├── DemoApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── HealthController.java
│   │   │   │   └── UserController.java
│   │   │   ├── service/
│   │   │   │   └── UserService.java
│   │   │   └── model/
│   │   │       └── User.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/clouddevopshub/demo/
│           └── DemoApplicationTests.java
├── k8s/
│   └── deployment.yaml
├── .github/
│   └── workflows/
│       ├── ci.yml          # CI Pipeline (Build, Test, Push)
│       └── cd.yml          # CD Pipeline (Deploy to K8s)
├── Dockerfile
├── pom.xml
└── README.md
```

---

## 🛠️ Local Development

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker Desktop
- kubectl (for K8s deployment)

### Build & Run Locally

```bash
# Clone repository
git clone https://github.com/your-username/spring-boot-demo.git
cd spring-boot-demo

# Build the project
mvn clean package

# Run locally
mvn spring-boot:run

# Access application
curl http://localhost:8080/health
curl http://localhost:8080/api/users
```

### Run with Docker

```bash
# Build Docker image
docker build -t spring-boot-demo:latest .

# Run container
docker run -d -p 8080:8080 --name demo spring-boot-demo:latest

# Check logs
docker logs -f demo

# Test endpoints
curl http://localhost:8080/health
```

---

## 🔄 CI/CD Pipeline Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CI PIPELINE (ci.yml)                      │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────┐   ┌──────────────┐   ┌──────────────────────┐ │
│  │   Build &    │──▶│    Code      │──▶│   Docker Build      │ │
│  │    Test      │   │   Quality    │   │     & Push          │ │
│  └──────────────┘   └──────────────┘   └──────────────────────┘ │
│        │                  │                      │               │
│        ▼                  ▼                      ▼               │
│   Unit Tests        SonarQube            Push to Registry       │
│   JaCoCo            OWASP Check          Trivy Scan             │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        CD PIPELINE (cd.yml)                      │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────┐   ┌──────────────┐   ┌──────────────────────┐ │
│  │   Deploy     │──▶│  Integration │──▶│     Deploy to        │ │
│  │  to Staging  │   │    Tests     │   │    Production        │ │
│  └──────────────┘   └──────────────┘   └──────────────────────┘ │
│        │                  │                      │               │
│        ▼                  ▼                      ▼               │
│   Smoke Tests       API Tests            Blue-Green Deploy      │
│   Health Check      E2E Tests            Rollback Ready         │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📋 Step-by-Step Deployment Guide

### Step 1: Setup GitHub Repository

```bash
# Initialize git repository
git init
git add .
git commit -m "Initial commit: Spring Boot CI/CD project"

# Create GitHub repository and push
git remote add origin https://github.com/YOUR_USERNAME/spring-boot-demo.git
git branch -M main
git push -u origin main
```

### Step 2: Configure GitHub Secrets

Navigate to **Settings → Secrets and variables → Actions** and add:

| Secret Name | Description |
|-------------|-------------|
| `DOCKER_USERNAME` | Docker Hub username |
| `DOCKER_PASSWORD` | Docker Hub password/token |
| `AWS_ACCESS_KEY_ID` | AWS access key (for EKS) |
| `AWS_SECRET_ACCESS_KEY` | AWS secret key |
| `AWS_REGION` | AWS region (e.g., `ap-south-1`) |
| `EKS_CLUSTER_NAME` | EKS cluster name |
| `SLACK_WEBHOOK_URL` | Slack webhook (optional) |

**For Azure AKS:**
| Secret Name | Description |
|-------------|-------------|
| `AZURE_CREDENTIALS` | Azure service principal JSON |
| `AKS_RESOURCE_GROUP` | AKS resource group |
| `AKS_CLUSTER_NAME` | AKS cluster name |

**For GCP GKE:**
| Secret Name | Description |
|-------------|-------------|
| `GCP_SA_KEY` | GCP service account JSON key |
| `GKE_CLUSTER_NAME` | GKE cluster name |
| `GKE_CLUSTER_ZONE` | GKE cluster zone |

### Step 3: Create Kubernetes Namespaces

```bash
# Create namespaces
kubectl create namespace staging
kubectl create namespace production

# Verify
kubectl get namespaces
```

### Step 4: Trigger CI Pipeline

```bash
# Push code changes to trigger CI
git add .
git commit -m "feat: Add new feature"
git push origin main
```

**CI Pipeline Jobs:**

1. **Build & Test** - Compiles code, runs unit tests
2. **Code Quality** - SonarQube scan, OWASP dependency check
3. **Docker Build & Push** - Builds multi-arch image, pushes to registry
4. **Notify** - Sends Slack notification

### Step 5: CD Pipeline (Auto-triggered after CI)

**CD Pipeline Jobs:**

1. **Deploy to Staging** - Deploys to staging namespace
2. **Integration Tests** - Runs E2E tests
3. **Deploy to Production** - Deploys to production (manual approval)
4. **Rollback** - Available if deployment fails

### Step 6: Manual Deployment (Optional)

```bash
# Navigate to Actions tab in GitHub
# Select "CD Pipeline"
# Click "Run workflow"
# Select environment: staging/production
# Enter image tag
# Click "Run workflow"
```

---

## 🔧 Pipeline Configuration

### CI Pipeline Triggers

```yaml
on:
  push:
    branches: [main, develop, 'feature/*']
  pull_request:
    branches: [main, develop]
```

### CD Pipeline Triggers

```yaml
on:
  workflow_run:
    workflows: ["CI Pipeline"]
    types: [completed]
    branches: [main]
  workflow_dispatch:  # Manual trigger
```

---

## 🐳 Docker Commands Reference

```bash
# Build image
docker build -t spring-boot-demo:v1 .

# Run container
docker run -d -p 8080:8080 spring-boot-demo:v1

# View logs
docker logs -f <container_id>

# Push to Docker Hub
docker tag spring-boot-demo:v1 username/spring-boot-demo:v1
docker push username/spring-boot-demo:v1

# Multi-arch build
docker buildx build --platform linux/amd64,linux/arm64 \
  -t username/spring-boot-demo:v1 --push .
```

---

## ☸️ Kubernetes Commands Reference

```bash
# Apply manifests
kubectl apply -f k8s/ -n staging

# Check deployment status
kubectl get deployments -n staging
kubectl get pods -n staging
kubectl get svc -n staging

# View logs
kubectl logs -f deployment/spring-boot-demo -n staging

# Rollback deployment
kubectl rollout undo deployment/spring-boot-demo -n staging

# Scale deployment
kubectl scale deployment/spring-boot-demo --replicas=5 -n staging

# Port forward for local testing
kubectl port-forward svc/spring-boot-demo 8080:80 -n staging
```

---

## 🔍 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Welcome message |
| GET | `/health` | Health check |
| GET | `/info` | Application info |
| GET | `/api/users` | List all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

---

## 📊 Monitoring & Observability

### Actuator Endpoints

```bash
# Health check
curl http://localhost:8080/actuator/health

# Application info
curl http://localhost:8080/actuator/info

# Metrics
curl http://localhost:8080/actuator/metrics
```

### Prometheus Metrics (if enabled)

```bash
curl http://localhost:8080/actuator/prometheus
```

---

## 🛡️ Security Best Practices

- ✅ Non-root container user
- ✅ Multi-stage Docker build
- ✅ Trivy vulnerability scanning
- ✅ OWASP dependency check
- ✅ Resource limits in K8s
- ✅ Health probes configured
- ✅ Secrets management via GitHub Secrets

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing`)
5. Open Pull Request

---

## 📝 License

This project is created for educational purposes by **CloudDevOpsHub**.

---

## 📞 Support

- 📧 Email: vikas@clouddevopshub.com
- 💬 Community: [CloudDevOpsHub](https://clouddevopshub.com)
- 📺 YouTube: CloudDevOpsHub

---

**Made with ❤️ by CloudDevOpsHub - Batch 42**
