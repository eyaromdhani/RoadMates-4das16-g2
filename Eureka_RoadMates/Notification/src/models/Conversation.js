const mongoose = require('mongoose');
const jsonOptions = require('../utils/jsonOptions');

// bookingId, driverId, passengerId : simples String (pas de relation)
const conversationSchema = new mongoose.Schema({
  bookingId: { type: String, required: true, index: true },
  driverId: { type: String, required: true },
  passengerId: { type: String, required: true },
  status: { type: String, enum: ['OPEN', 'CLOSED'], default: 'OPEN' },
  createdAt: { type: Date, default: Date.now }
}, jsonOptions);

module.exports = mongoose.model('Conversation', conversationSchema);
