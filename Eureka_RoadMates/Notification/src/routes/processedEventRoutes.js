const router = require('express').Router();
const c = require('../controllers/processedEventController');

router.get('/', c.all);
router.get('/:eventId', c.one);
router.post('/', c.create);
router.delete('/:eventId', c.delete);

module.exports = router;
