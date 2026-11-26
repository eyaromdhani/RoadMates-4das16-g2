const mongoose = require('mongoose');
const jsonOptions = require('../utils/jsonOptions');

// idempotence : un événement déjà traité n'est jamais retraité
const processedEventSchema = new mongoose.Schema({
  eventId: { type: String, required: true, unique: true },
  type: String,
  processedAt: { type: Date, default: Date.now }
}, jsonOptions);

module.exports = mongoose.model('ProcessedEvent', processedEventSchema);
