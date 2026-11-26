const service = require('../services/conversationService');
const messageService = require('../services/messageService');
const asyncHandler = require('../utils/asyncHandler');

exports.all = asyncHandler(async (req, res) => res.json(await service.findAll()));

exports.one = asyncHandler(async (req, res) => res.json(await service.findById(req.params.id)));

exports.byBooking = asyncHandler(async (req, res) => res.json(await service.findByBooking(req.params.bookingId)));

exports.byUser = asyncHandler(async (req, res) => res.json(await service.findByUser(req.params.userId)));

exports.create = asyncHandler(async (req, res) => res.status(201).json(await service.create(req.body)));

// PATCH /api/notifications/conversations/:id/close
exports.close = asyncHandler(async (req, res) => res.json(await service.close(req.params.id)));

exports.delete = asyncHandler(async (req, res) => {
  await service.remove(req.params.id);
  res.status(204).send();
});

// GET /api/notifications/conversations/:id/messages
exports.messages = asyncHandler(async (req, res) => res.json(await messageService.findByConversation(req.params.id)));

// POST /api/notifications/conversations/:id/messages
exports.send = asyncHandler(async (req, res) =>
  res.status(201).json(await messageService.send(req.params.id, req.body)));
