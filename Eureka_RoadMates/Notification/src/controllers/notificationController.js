const service = require('../services/notificationService');
const asyncHandler = require('../utils/asyncHandler');

exports.all = asyncHandler(async (req, res) => res.json(await service.findAll()));

exports.one = asyncHandler(async (req, res) => res.json(await service.findById(req.params.id)));

exports.byUser = asyncHandler(async (req, res) => res.json(await service.findByUser(req.params.userId)));

exports.unreadByUser = asyncHandler(async (req, res) => res.json(await service.findUnreadByUser(req.params.userId)));

// POST /api/notifications?eventId=<id>   (eventId optionnel)
exports.create = asyncHandler(async (req, res) =>
  res.status(201).json(await service.create(req.body, req.query.eventId)));

// PATCH /api/notifications/:id/read
exports.read = asyncHandler(async (req, res) => res.json(await service.markAsRead(req.params.id)));

exports.delete = asyncHandler(async (req, res) => {
  await service.remove(req.params.id);
  res.status(204).send();
});
