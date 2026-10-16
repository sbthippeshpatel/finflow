# FinFlow deployment checklist

## 1. PostgreSQL
Create a managed PostgreSQL database at your preferred host. Keep its host, port, database name, username, and password private.

## 2. Backend service
- Root directory: `backend`
- Build method: Docker using `backend/Dockerfile` (or Java 21 + Maven with `mvn clean package`; run `java -jar target/finflow-api-1.0.0.jar`)
- Environment variables:
  - `PORT=8080` (the service provider may inject its own port)
  - `SPRING_DATASOURCE_URL=jdbc:postgresql://HOST:PORT/DATABASE`
  - `DB_USERNAME=your_database_username`
  - `DB_PASSWORD=your_database_password`
- After deploy, check `/actuator/health` on the backend domain.

## 3. Frontend
- Static-site root/build directory: `frontend`
- Build command: `npm install && npm run build`
- Publish directory: `dist`
- Build environment variable: `VITE_API_URL=https://YOUR-BACKEND-DOMAIN`
- Redeploy/rebuild frontend after setting the variable.

## 4. Verify
- Open the frontend URL.
- Add an income transaction and an expense transaction.
- Confirm dashboard totals and transaction list update.
- Open AI Assistant and ask “Summarize my expenses”.
- Confirm `https://YOUR-BACKEND-DOMAIN/actuator/health` returns `{"status":"UP"}`.

## Important
The assistant is rule-based, not connected to a hosted LLM. This project currently has no login or multi-user isolation, so do not expose real personal financial data publicly. Keep credentials in provider environment settings, never in GitHub.
