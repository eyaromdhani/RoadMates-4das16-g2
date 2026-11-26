module.exports = (err, req, res, next) => {
  let status = err.status || 500;
  if (err.name === 'ValidationError' || err.name === 'CastError') status = 400;
  if (err.type === 'entity.parse.failed') status = 400;
  if (status === 500) console.error(err);
  res.status(status).json({
    timestamp: new Date().toISOString(),
    status,
    message: err.message,
    path: req.originalUrl
  });
};
