const express = require('express');
const errorHandler = require('./utils/errorHandler');

const app = express();
app.use(express.json());

const mongoose = require('mongoose');

app.get('/health', (req, res) => {
    const mongo = mongoose.connection.readyState === 1 ? 'UP' : 'DOWN';
    res.status(mongo === 'UP' ? 200 : 503).json({ status: mongo, mongo });
});

app.get('/info', (req, res) =>
    res.json({ app: 'Microservice Notification', version: '1.0.0', tech: 'Node.js / Express' }));

// l'ordre compte : les routes les plus spécifiques d'abord
app.use('/api/notifications/conversations', require('./routes/conversationRoutes'));
app.use('/api/notifications/messages', require('./routes/messageRoutes'));
app.use('/api/notifications/events', require('./routes/processedEventRoutes'));
app.use('/api/notifications', require('./routes/notificationRoutes'));

app.use(errorHandler);

module.exports = app;
