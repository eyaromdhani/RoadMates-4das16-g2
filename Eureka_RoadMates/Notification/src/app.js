const express = require('express');
const errorHandler = require('./utils/errorHandler');

const app = express();
app.use(express.json());

// l'ordre compte : les routes les plus spécifiques d'abord
app.use('/api/notifications/conversations', require('./routes/conversationRoutes'));
app.use('/api/notifications/messages', require('./routes/messageRoutes'));
app.use('/api/notifications/events', require('./routes/processedEventRoutes'));
app.use('/api/notifications', require('./routes/notificationRoutes'));

app.use(errorHandler);

module.exports = app;
