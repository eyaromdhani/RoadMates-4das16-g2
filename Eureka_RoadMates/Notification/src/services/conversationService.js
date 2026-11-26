const repository = require('../repositories/conversationRepository');
const messageRepository = require('../repositories/messageRepository');
const HttpError = require('../utils/HttpError');

const findAll = () => repository.findAll();

const findById = async id => {
  const c = await repository.findById(id);
  if (!c) throw new HttpError(404, 'Conversation introuvable : ' + id);
  return c;
};

const findByBooking = async bookingId => {
  const c = await repository.findByBookingId(bookingId);
  if (!c) throw new HttpError(404, 'Aucune conversation pour la réservation : ' + bookingId);
  return c;
};

const findByUser = userId => repository.findByUser(userId);

// chat ouvert après BookingConfirmed : une seule conversation par réservation
const create = async data => {
  if (await repository.findByBookingId(data.bookingId)) {
    throw new HttpError(409, 'Une conversation existe déjà pour la réservation : ' + data.bookingId);
  }
  return repository.save({
    bookingId: data.bookingId,
    driverId: data.driverId,
    passengerId: data.passengerId,
    status: 'OPEN'
  });
};

const close = async id => {
  const c = await findById(id);
  c.status = 'CLOSED';
  return c.save();
};

const remove = async id => {
  await findById(id);
  await messageRepository.deleteByConversationId(id);   // suppression des messages liés
  await repository.deleteById(id);
};

module.exports = { findAll, findById, findByBooking, findByUser, create, close, remove };
