const Notification = require('../models/Notification');

module.exports = {
  findAll: () => Notification.find().sort({ createdAt: -1 }),
  findById: id => Notification.findById(id),
  findByUserId: userId => Notification.find({ userId }).sort({ createdAt: -1 }),
  findUnreadByUserId: userId => Notification.find({ userId, read: false }).sort({ createdAt: -1 }),
  save: data => Notification.create(data),
  deleteById: id => Notification.findByIdAndDelete(id)
};
