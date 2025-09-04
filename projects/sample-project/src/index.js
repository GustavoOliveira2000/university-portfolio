import express from 'express';

const app = express();
app.use(express.json());

app.get('/health', (_req, res) => res.json({ ok: true }));

const items = new Map();
let id = 1;

app.get('/items', (_req, res) => {
  res.json({ items: Array.from(items.entries()).map(([id, v]) => ({ id, ...v })) });
});

app.post('/items', (req, res) => {
  const body = req.body || {};
  const newId = String(id++);
  items.set(newId, { ...body });
  res.status(201).json({ id: newId, ...body });
});

app.put('/items/:id', (req, res) => {
  const { id } = req.params;
  if (!items.has(id)) return res.status(404).json({ error: 'not_found' });
  items.set(id, { ...req.body });
  res.json({ id, ...req.body });
});

app.delete('/items/:id', (req, res) => {
  const { id } = req.params;
  if (!items.has(id)) return res.status(404).json({ error: 'not_found' });
  items.delete(id);
  res.status(204).send();
});

const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Sample API listening on :${port}`));