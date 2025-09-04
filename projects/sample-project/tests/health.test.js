import request from 'supertest';
import express from 'express';

// Inline app to keep the template minimal
const app = express();
app.use(express.json());
app.get('/health', (_req, res) => res.json({ ok: true }));

test('GET /health works', async () => {
  const res = await request(app).get('/health');
  expect(res.status).toBe(200);
  expect(res.body.ok).toBe(true);
});