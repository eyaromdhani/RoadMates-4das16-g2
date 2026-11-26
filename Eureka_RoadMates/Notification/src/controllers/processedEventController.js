const service = require('../services/processedEventService');
const asyncHandler = require('../utils/asyncHandler');

exports.all = asyncHandler(async (req, res) => res.json(await service.findAll()));

exports.one = asyncHandler(async (req, res) => res.json(await service.findByEventId(req.params.eventId)));

exports.create = asyncHandler(async (req, res) => res.status(201).json(await service.create(req.body)));

exports.delete = asyncHandler(async (req, res) => {
  await service.remove(req.params.eventId);
  res.status(204).send();
});
