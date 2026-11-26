const Conversation = require('../models/Conversation');

module.exports = {
  findAll: () => Conversation.find().sort({ createdAt: -1 }),
  findById: id => Conversation.findById(id),
  findByBookingId: bookingId => Conversation.findOne({ bookingId }),
  findByUser: userId => Conversation.find({ $or: [{ driverId: userId }, { passengerId: userId }] }),
  save: data => Conversation.create(data),
  deleteById: id => Conversation.findByIdAndDelete(id)
};
