# FinFlow — Personal Finance Dashboard

A full-stack personal finance tracker built with React, Spring Boot, PostgreSQL, and a rule-based budgeting assistant.

## Features
- Financial dashboard with monthly income, expenses, balance, and transaction count
- Create, search, filter, and delete income/expense transactions
- Seven-day cash-flow chart
- FinFlow Assistant with basic spending and saving responses
- REST API, validation, PostgreSQL persistence, Actuator health endpoint
- Docker Compose local environment

## Stack
- Frontend: React, Vite, Recharts, Lucide
- Backend: Java 21, Spring Boot, Spring Web, Spring Data JPA, Bean Validation
- Database: PostgreSQL

## Fastest local run (Docker)
Install Docker Desktop, open a terminal in the project root, then run:

```bash
docker compose up --build
```

Open frontend: http://localhost:3000
API health: http://localhost:8080/actuator/health

## Run without Docker
1. Create a PostgreSQL database named `finflow`.
2. In `backend/src/main/resources/application.properties`, set the correct PostgreSQL username and password, or set `DB_USERNAME`, `DB_PASSWORD`, and `SPRING_DATASOURCE_URL` environment variables.
3. Start the backend from the `backend` folder:

```bash
mvn spring-boot:run
```

4. In another terminal, run the frontend:

```bash
cd frontend
npm install
npm run dev
```

Open the Vite URL printed in the terminal (normally http://localhost:5173). The default API URL is http://localhost:8080. For a different backend URL, create `frontend/.env` from `.env.example` and set `VITE_API_URL` before building/running Vite.

## API endpoints
- `GET /api/transactions` — list transactions; optional `search` and `type` query parameters
- `POST /api/transactions` — create transaction
- `DELETE /api/transactions/{id}` — delete transaction
- `GET /api/dashboard/summary` — monthly totals and recent transactions
- `POST /api/assistant/chat` — basic budgeting assistant
- `GET /actuator/health` — health status

Example transaction JSON:

```json
{
  "title": "Monthly salary",
  "category": "Salary",
  "amount": 45000,
  "type": "INCOME",
  "transactionDate": "2026-10-14",
  "notes": "October salary"
}
```

## GitHub
Create an empty GitHub repository, then from this folder:

```bash
git init
git add .
git commit -m "Build FinFlow full-stack finance dashboard"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/finflow.git
git push -u origin main
```

## Deployment outline
Deploy PostgreSQL first. Deploy `backend` as a Docker service using `backend/Dockerfile`, set `SPRING_DATASOURCE_URL` to the provider's JDBC PostgreSQL URL (`jdbc:postgresql://HOST:PORT/DATABASE`), `DB_USERNAME`, `DB_PASSWORD`, and `PORT=8080`. Deploy `frontend` as a static site by running `npm install && npm run build` from `frontend`, publish `frontend/dist`, and set build variable `VITE_API_URL` to the deployed backend URL. Rebuild the frontend after changing `VITE_API_URL`.

## Security note
This is a portfolio MVP. Before using real financial data, add authentication/authorization, per-user data isolation, rate limiting, and production secrets management. Never commit production credentials.
