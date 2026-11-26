const { Eureka } = require('eureka-js-client');

module.exports = function createEurekaClient(port) {
    const host = 'localhost';
    const p = Number(port);

    return new Eureka({
        instance: {
            app: 'NOTIFICATION',
            instanceId: `notification:${p}`,          // unique par instance
            hostName: host,
            ipAddr: '127.0.0.1',
            port: { $: p, '@enabled': true },
            vipAddress: 'notification',
            statusPageUrl: `http://${host}:${p}/info`,
            healthCheckUrl: `http://${host}:${p}/health`,
            dataCenterInfo: {
                '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
                name: 'MyOwn',
            },
        },
        eureka: {
            serviceUrls: {
                default: [
                    'http://peer1:8761/eureka/apps/',
                    'http://peer2:8762/eureka/apps/',
                ],
            },
            registerWithEureka: true,
            fetchRegistry: true,
        },
    });
};