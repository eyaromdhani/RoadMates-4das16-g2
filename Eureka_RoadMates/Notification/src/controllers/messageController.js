const service = require('../services/messageService');
const asyncHandler = require('../utils/asyncHandler');

exports.one = asyncHandler(async (req, res) => res.json(await service.findById(req.params.id)));

// PATCH /api/notifications/messages/:id/read
exports.read = asyncHandler(async (req, res) => res.json(await service.markAsRead(req.params.id)));

exports.delete = asyncHandler(async (req, res) => {
  await service.remove(req.params.id);
  res.status(204).send();
});
