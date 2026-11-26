const ProcessedEvent = require('../models/ProcessedEvent');

module.exports = {
  findAll: () => ProcessedEvent.find().sort({ processedAt: -1 }),
  findByEventId: eventId => ProcessedEvent.findOne({ eventId }),
  save: data => ProcessedEvent.create(data),
  deleteByEventId: eventId => ProcessedEvent.findOneAndDelete({ eventId })
};
