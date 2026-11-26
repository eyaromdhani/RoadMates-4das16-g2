const repository = require('../repositories/messageRepository');
const conversationService = require('./conversationService');
const HttpError = require('../utils/HttpError');

const findByConversation = async conversationId => {
  await conversationService.findById(conversationId);
  return repository.findByConversationId(conversationId);
};

const findById = async id => {
  const m = await repository.findById(id);
  if (!m) throw new HttpError(404, 'Message introuvable : ' + id);
  return m;
};

const send = async (conversationId, data) => {
  const conversation = await conversationService.findById(conversationId);
  if (conversation.status === 'CLOSED') {
    throw new HttpError(400, 'Conversation fermée');
  }
  if (data.senderId !== conversation.driverId && data.senderId !== conversation.passengerId) {
    throw new HttpError(400, "L'expéditeur ne fait pas partie de la conversation");
  }
  return repository.save({
    conversationId,
    senderId: data.senderId,
    content: data.content
  });
};

const markAsRead = async id => {
  const m = await findById(id);
  if (!m.readAt) {
    m.readAt = new Date();
    await m.save();
  }
  return m;
};

const remove = async id => {
  await findById(id);
  await repository.deleteById(id);
};

module.exports = { findByConversation, findById, send, markAsRead, remove };
