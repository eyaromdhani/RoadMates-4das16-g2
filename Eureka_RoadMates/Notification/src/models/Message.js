const mongoose = require('mongoose');
const jsonOptions = require('../utils/jsonOptions');

const messageSchema = new mongoose.Schema({
  conversationId: { type: String, required: true, index: true },
  senderId: { type: String, required: true },
  content: { type: String, required: true },
  sentAt: { type: Date, default: Date.now },
  readAt: { type: Date, default: null }
}, jsonOptions);

module.exports = mongoose.model('Message', messageSchema);
