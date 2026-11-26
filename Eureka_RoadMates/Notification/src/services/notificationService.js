const repository = require('../repositories/notificationRepository');
const processedEventRepository = require('../repositories/processedEventRepository');
const HttpError = require('../utils/HttpError');

const findAll = () => repository.findAll();

const findById = async id => {
  const n = await repository.findById(id);
  if (!n) throw new HttpError(404, 'Notification introuvable : ' + id);
  return n;
};

const findByUser = userId => repository.findByUserId(userId);

const findUnreadByUser = userId => repository.findUnreadByUserId(userId);

// eventId optionnel : si fourni, l'événement ne peut être traité qu'une seule fois
const create = async (data, eventId) => {
  if (eventId) {
    if (await processedEventRepository.findByEventId(eventId)) {
      throw new HttpError(409, 'Événement déjà traité : ' + eventId);
    }
  }
  const n = await repository.save({
    userId: data.userId,
    type: data.type,
    title: data.title,
    content: data.content,
    refId: data.refId
  });
  if (eventId) {
    await processedEventRepository.save({ eventId, type: data.type });
  }
  return n;
};

const markAsRead = async id => {
  const n = await findById(id);
  n.read = true;
  return n.save();
};

const remove = async id => {
  await findById(id);
  await repository.deleteById(id);
};

module.exports = { findAll, findById, findByUser, findUnreadByUser, create, markAsRead, remove };
