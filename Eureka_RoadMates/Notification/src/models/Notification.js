const mongoose = require('mongoose');
const NOTIFICATION_TYPES = require('./NotificationType');
const jsonOptions = require('../utils/jsonOptions');

const notificationSchema = new mongoose.Schema({
  userId: { type: String, required: true, index: true },
  type: { type: String, enum: NOTIFICATION_TYPES, required: true },
  title: { type: String, required: true },
  content: String,
  refId: String,                       // référence métier (ex. bookingId)
  read: { type: Boolean, default: false },
  createdAt: { type: Date, default: Date.now }
}, jsonOptions);

module.exports = mongoose.model('Notification', notificationSchema);
