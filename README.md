# Worker Finder — DevOps CI/CD Project

> **Course:** Software Engineering and Design Architectures — DevOps  
> **Institute:** Cummins College of Engineering for Women, Pune  
> **Team:** [Person 1 Name] · [Person 2 Name] · [Person 3 Name]

---

## Problem Statement

Finding reliable local workers (plumbers, electricians, carpenters, etc.) is difficult for households in India. The **Worker Finder** platform connects customers with skilled workers in their area. Customers can search for workers by skill and location, post jobs, assign workers, and rate them after completion.

This project implements a complete **CI/CD pipeline** around the Worker Finder application, demonstrating automated build, test, containerization, and deployment practices.

---

## Architecture

```
┌──────────────┐     HTTP/REST     ┌──────────────────┐     Java RMI     ┌─────────────────┐
│  React        │ ──────────────▶  │  Spring Boot      │ ──────────────▶ │  RMI Server      │
│  Frontend     │   port 3000      │  REST API         │   port 1099     │  (Data + Logic)  │
│  (Vite)       │                  │  port 8081        │                 │                  │
└──────────────┘                   └──────────────────┘                  └─────────────────┘
```

**Tech Stack:**

| Layer        | Technology              |
|--------------|-------------------------|
| Frontend     | React + Vite            |
| REST API     | Spring Boot 3.2, Java 17|
| Data/Logic   | Java RMI Server         |
| Build        | Maven (backend), npm (frontend) |
| CI/CD        | GitHub Actions + Jenkins|
| Container    | Docker                  |
| Deploy       | Render.com              |

---

## Repository Structure

```
workerfinder/
├── .github/
│   └── workflows/
│       └── ci.yml              ← GitHub Actions CI/CD pipeline
├── frontend/                   ← React + Vite app
│   ├── src/
│   ├── Dockerfile.frontend
│   └── package.json
├── rest-api/                   ← Spring Boot REST API
│   ├── src/
│   │   ├── main/java/com/workerfinder/restapi/
│   │   │   ├── controller/     ← WorkerController, JobController
│   │   │   ├── model/          ← Worker, Job, Booking
│   │   │   └── service/        ← WorkerService (RMI interface)
│   │   └── test/java/com/workerfinder/restapi/
│   │       ├── WorkerModelTest.java         ← Unit tests
│   │       └── WorkerControllerLogicTest.java
│   ├── Dockerfile
│   └── pom.xml
├── rmi-server/                 ← Java RMI data server
├── docker-compose.yml          ← Run full stack locally
├── Jenkinsfile                 ← Jenkins pipeline definition
└── README.md
```

---

## Getting Started (Local Setup)

### Prerequisites
- Java 17+
- Maven 3.9+
- Node.js 20+
- Docker Desktop

### Step 1 — Start the RMI Server
```bash
cd rmi-server
javac -d out src/*.java
java -cp out RmiServer
# Should print: RMI Server started on port 1099
```

### Step 2 — Start the Spring Boot REST API
```bash
cd rest-api
mvn spring-boot:run
# Runs on http://localhost:8081
```

### Step 3 — Start the React Frontend
```bash
cd frontend
npm install
npm run dev
# Opens at http://localhost:5173
```

### Step 4 — Run with Docker Compose (all services)
```bash
docker-compose up --build
# Frontend: http://localhost:3000
# API:      http://localhost:8081
```

---

## Running Tests

```bash
cd rest-api
mvn test
```

Test reports are generated at: `rest-api/target/surefire-reports/`

### Test Coverage

| Test Class                    | What it tests                          | Tests |
|-------------------------------|----------------------------------------|-------|
| `WorkerModelTest`             | Worker, Job, Booking model classes     | 12    |
| `WorkerControllerLogicTest`   | Filtering, rating, job logic           | 11    |

---

## CI/CD Pipeline

### GitHub Actions (automatic on every push)

Pipeline file: `.github/workflows/ci.yml`

```
Push to GitHub
      │
      ├── Job 1: Backend Build & Test (Maven + JUnit)
      │         mvn clean compile → mvn test → upload reports
      │
      ├── Job 2: Frontend Build (npm)
      │         npm ci → npm run build → upload dist
      │
      ├── Job 3: Docker Build & Push (main branch only)
      │         mvn package → docker build → docker push
      │
      └── Job 4: Deploy to Render (main branch only)
                curl deploy hook → staging live
```

### Jenkins (local/demo)

Pipeline file: `Jenkinsfile`

Setup:
1. Install Jenkins (download from jenkins.io)
2. Install plugins: Git, Maven Integration, Docker Pipeline
3. Configure tools: JDK 17, Maven 3.9
4. Add credentials: `dockerhub-creds` (username/password)
5. Create Pipeline job → point to this repo → Jenkinsfile

---

## Docker

### Build manually
```bash
cd rest-api
docker build -t worker-finder-api .
docker run -p 8081:8081 worker-finder-api
```

### Docker Hub
Image: `[your-dockerhub-username]/worker-finder-api:latest`

---

## Deployment

**Platform:** Render.com (free tier)

Steps:
1. Go to render.com → New → Web Service
2. Connect your Docker Hub image
3. Set port to `8081`
4. Copy the Deploy Hook URL → add as `RENDER_DEPLOY_HOOK_URL` in GitHub Secrets

Live URL: `https://worker-finder-api.onrender.com` *(update after deploy)*

---

## API Endpoints

| Method | Endpoint                          | Description                  |
|--------|-----------------------------------|------------------------------|
| GET    | `/api/workers`                    | Search workers (skill, area) |
| GET    | `/api/workers/all`                | Get all workers              |
| POST   | `/api/jobs`                       | Post a new job               |
| GET    | `/api/jobs/open`                  | Get open jobs                |
| POST   | `/api/jobs/assign`                | Assign worker to job         |
| POST   | `/api/jobs/complete`              | Mark job as completed        |
| POST   | `/api/jobs/rate`                  | Rate a worker                |
| GET    | `/api/jobs/bookings/customer`     | Get bookings by customer     |
| GET    | `/api/jobs/bookings/worker`       | Get bookings by worker       |

---

## Team Contributions

| Person   | Role              | Responsibilities                                                          |
|----------|-------------------|---------------------------------------------------------------------------|
| Person 1 | DevOps Engineer   | GitHub repo setup, GitHub Actions CI/CD, Dockerfile, Docker Hub, Render deploy |
| Person 2 | Backend Engineer  | Spring Boot API, unit tests (WorkerModelTest, WorkerControllerLogicTest)  |
| Person 3 | RMI + Docs        | RMI server, README, project report, architecture diagram, demo screenshots |

---

## GitHub Secrets Required

| Secret Name              | Description                          |
|--------------------------|--------------------------------------|
| `DOCKERHUB_USERNAME`     | Your Docker Hub username             |
| `DOCKERHUB_TOKEN`        | Docker Hub access token              |
| `RENDER_DEPLOY_HOOK_URL` | Render deploy hook URL               |
