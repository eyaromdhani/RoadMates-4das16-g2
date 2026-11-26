const repository = require('../repositories/processedEventRepository');
const HttpError = require('../utils/HttpError');

const findAll = () => repository.findAll();

const findByEventId = async eventId => {
  const e = await repository.findByEventId(eventId);
  if (!e) throw new HttpError(404, 'Événement introuvable : ' + eventId);
  return e;
};

const create = async data => {
  if (!data.eventId) throw new HttpError(400, 'eventId obligatoire');
  if (await repository.findByEventId(data.eventId)) {
    throw new HttpError(409, 'Événement déjà traité : ' + data.eventId);
  }
  return repository.save({ eventId: data.eventId, type: data.type });
};

const remove = async eventId => {
  await findByEventId(eventId);
  await repository.deleteByEventId(eventId);
};

module.exports = { findAll, findByEventId, create, remove };
