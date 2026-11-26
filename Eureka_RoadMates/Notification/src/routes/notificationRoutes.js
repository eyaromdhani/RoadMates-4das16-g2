const router = require('express').Router();
const c = require('../controllers/notificationController');

router.get('/', c.all);
router.get('/user/:userId', c.byUser);
router.get('/user/:userId/unread', c.unreadByUser);
router.get('/:id', c.one);
router.post('/', c.create);
router.patch('/:id/read', c.read);
router.delete('/:id', c.delete);

module.exports = router;
