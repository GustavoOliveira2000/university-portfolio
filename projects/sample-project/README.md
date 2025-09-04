# Sample Project — Mini REST API

Concise REST API to demonstrate clean project structure, tests, and documentation.

## Why it exists
University assignment to build a small CRUD API. I used this to showcase:
- Sensible folder structure
- Unit tests with coverage
- A short Postman collection

## Tech Stack
- Node.js 20, Express
- Jest + Supertest
- ESLint + Prettier

## Architecture (High-Level)
```
Client (Insomnia/Postman) → Express Routes → Controllers → Services → In-memory Repo
                              |-- tests/ (unit + request)
```

## Setup
```bash
cd projects/sample-project
npm install
npm run dev
# Visit http://localhost:3000/health
```

## Scripts
```json
{
  "scripts": {
    "dev": "node src/index.js",
    "test": "jest --coverage --passWithNoTests"
  }
}
```

## Endpoints
- `GET /health` → `{ ok: true }`
- `GET /items`
- `POST /items`
- `PUT /items/:id`
- `DELETE /items/:id`

## Tests
```bash
npm test
```
Outputs coverage summary in terminal.

## Demo
Add a short GIF under `/assets/` and embed it here:
![demo](../../assets/sample-demo.gif)

## What I learned
- Designing minimal, readable APIs
- Writing request-level tests
- Keeping READMEs as living docs