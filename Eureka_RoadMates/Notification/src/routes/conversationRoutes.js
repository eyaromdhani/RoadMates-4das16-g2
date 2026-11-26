const router = require('express').Router();
const c = require('../controllers/conversationController');

router.get('/', c.all);
router.get('/booking/:bookingId', c.byBooking);
router.get('/user/:userId', c.byUser);
router.get('/:id', c.one);
router.post('/', c.create);
router.patch('/:id/close', c.close);
router.delete('/:id', c.delete);
router.get('/:id/messages', c.messages);
router.post('/:id/messages', c.send);

module.exports = router;
