const Message = require('../models/Message');

module.exports = {
  findById: id => Message.findById(id),
  findByConversationId: conversationId => Message.find({ conversationId }).sort({ sentAt: 1 }),
  save: data => Message.create(data),
  deleteById: id => Message.findByIdAndDelete(id),
  deleteByConversationId: conversationId => Message.deleteMany({ conversationId })
};
