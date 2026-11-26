const router = require('express').Router();
const c = require('../controllers/messageController');

router.get('/:id', c.one);
router.patch('/:id/read', c.read);
router.delete('/:id', c.delete);

module.exports = router;
