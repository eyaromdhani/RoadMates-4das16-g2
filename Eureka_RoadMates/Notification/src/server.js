const mongoose = require('mongoose');
const app = require('./app');
const createEurekaClient = require('./utils/eureka');

const PORT = process.env.PORT || 8087;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/notificationdb';

mongoose.connect(MONGO_URI)
    .then(() => {
        console.log('MongoDB connecté : ' + MONGO_URI);
        app.listen(PORT, () => {
            console.log('Notification MS démarré sur le port ' + PORT);

            const eurekaClient = createEurekaClient(PORT);
            eurekaClient.start((err) =>
                console.log(err ? 'Eureka : ' + err : 'Enregistré dans Eureka'));

            // désenregistrement propre à l'arrêt
            const shutdown = () => eurekaClient.stop(() => process.exit(0));
            process.on('SIGINT', shutdown);
            process.on('SIGTERM', shutdown);
        });
    })
    .catch(err => {
        console.error('Connexion MongoDB impossible :', err.message);
        process.exit(1);
    });