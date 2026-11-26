const mongoose = require('mongoose');
const app = require('./app');

const PORT = process.env.PORT || 8087;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/notificationdb';

mongoose.connect(MONGO_URI)
  .then(() => {
    console.log('MongoDB connecté : ' + MONGO_URI);
    app.listen(PORT, () => console.log('Notification MS démarré sur le port ' + PORT));
  })
  .catch(err => {
    console.error('Connexion MongoDB impossible :', err.message);
    process.exit(1);
  });
