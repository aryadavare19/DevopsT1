# 🔧 Worker Finder — DevOps T1 Project

A full-stack **Worker Finder** web application demonstrating a complete CI/CD pipeline using **GitHub Actions**, **Docker**, and **Render**. The project consists of a Spring Boot REST API backend and a React (Vite) frontend, with automated build, test, containerization, and deployment stages.

---

## 📁 Project Structure

```
DevopsT1-main/
├── .github/
│   └── workflows/
│       └── ci.yml              # GitHub Actions CI/CD pipeline
├── rest-api/                   # Spring Boot backend (Java 17)
│   ├── src/
│   │   ├── main/java/com/workerfinder/restapi/
│   │   │   ├── controller/     # JobController, WorkerController
│   │   │   ├── model/          # Job, Worker, Booking models
│   │   │   └── service/        # WorkerService, WorkerServiceImpl
│   │   └── resources/
│   │       └── application.properties
│   ├── Dockerfile              # Multi-stage Docker build for backend
│   └── pom.xml
├── frontend/                   # React + Vite frontend
│   ├── src/
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── Dockerfile.frontend     # Multi-stage Docker build for frontend
│   └── package.json
└── rmi-server/                 # (RMI server module)
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2, Maven |
| Frontend | React 18, Vite, Node.js 20 |
| Containerization | Docker (multi-stage builds) |
| CI/CD | GitHub Actions |
| Deployment | Render (via Deploy Hook) |
| Registry | Docker Hub |

---

## ⚙️ CI/CD Pipeline Overview

The pipeline is defined in `.github/workflows/ci.yml` and runs on every push or pull request to `main` or `dev` branches.

### Pipeline Stages

```
Push to main/dev
       │
       ├──► Job 1: Backend Build & Test  (Maven compile + JUnit)
       │
       ├──► Job 2: Frontend Build         (npm ci + npm run build)
       │
       └──► Job 3: Docker Build & Push    (only on push to main)
                        │
                        └──► Job 4: Deploy to Render (only on push to main)
```

### Job 1 — Backend Build & Test
- Sets up **Java 17** (Temurin distribution) with Maven cache
- Runs `mvn clean compile` to build the Spring Boot project
- Runs `mvn test` to execute JUnit tests
- Uploads Surefire test reports as artifacts

### Job 2 — Frontend Build
- Sets up **Node.js 20** with npm cache
- Runs `npm ci` for clean dependency installation
- Runs `npm run build` to produce the Vite production bundle
- Uploads the `dist/` folder as a build artifact

### Job 3 — Docker Build & Push *(main branch only)*
- Depends on Jobs 1 and 2 passing
- Packages the Spring Boot JAR (`mvn package -DskipTests`)
- Logs in to **Docker Hub** using secrets
- Builds and pushes the backend image: `<DOCKERHUB_USERNAME>/worker-finder-api:latest`

### Job 4 — Deploy to Render *(main branch only)*
- Depends on Job 3 completing
- Triggers the **Render deploy hook** via a `curl` POST request
- Gracefully skips if the secret is not configured

---

## 🐳 Docker — Multi-Stage Builds

### Backend (`rest-api/Dockerfile`)
```
Stage 1 (builder): maven:3.9.6-eclipse-temurin-17
  → Downloads dependencies, builds JAR

Stage 2 (runtime): eclipse-temurin:17-jre-alpine
  → Copies JAR, exposes port 8081, runs the app
```

### Frontend (`frontend/Dockerfile.frontend`)
```
Stage 1 (builder): node:20-alpine
  → Installs deps, builds React app

Stage 2 (server): nginx:alpine
  → Serves /dist on port 80 with SPA routing support
```

---

## 🚀 REST API Endpoints

Base URL: `http://localhost:8081/api`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/jobs` | Post a new job (customerId, skill, area, description) |
| `GET` | `/api/jobs/open` | Get all open jobs |
| `POST` | `/api/jobs/assign` | Assign a job to a worker |
| `POST` | `/api/jobs/complete` | Mark a booking as complete |
| `POST` | `/api/jobs/rate` | Rate a worker after job completion |
| `GET` | `/api/jobs/bookings/customer` | Get bookings for a customer |
| `GET` | `/api/jobs/bookings/worker` | Get bookings for a worker |
| `GET` | `/api/workers` | Get all workers |

---

## 🔐 GitHub Secrets Required

Configure these in your repository under **Settings → Secrets and Variables → Actions**:

| Secret | Description |
|---|---|
| `DOCKERHUB_USERNAME` | Your Docker Hub username |
| `DOCKERHUB_TOKEN` | Docker Hub access token (not your password) |
| `RENDER_DEPLOY_HOOK_URL` | Render deploy hook URL for the backend service |

---

## 🏃 Running Locally

### Backend
```bash
cd rest-api
mvn clean spring-boot:run
# API runs on http://localhost:8081
```

### Frontend
```bash
cd frontend
npm install
npm run dev
# App runs on http://localhost:5173
```

### With Docker
```bash
# Backend
cd rest-api
docker build -t worker-finder-api .
docker run -p 8081:8081 worker-finder-api

# Frontend
cd frontend
docker build -f Dockerfile.frontend -t worker-finder-frontend .
docker run -p 80:80 worker-finder-frontend
```

---

## 🧪 Running Tests

```bash
cd rest-api
mvn test
# Reports saved to: rest-api/target/surefire-reports/
```

Test classes:
- `RestApiApplicationTests` — context load test
- `WorkerControllerLogicTest` — unit tests for worker business logic

---

## 📌 Notes

- CORS is enabled globally (`@CrossOrigin(origins = "*")`) for development; restrict in production.
- Data is stored in-memory (no database); all data resets on restart.
- The Render deploy step is **skipped gracefully** if `RENDER_DEPLOY_HOOK_URL` is not set, so the pipeline won't fail during initial setup.

---

*DevOps T1 Project — Cummins College of Engineering for Women, Pune*
