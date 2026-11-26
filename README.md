# 🚗 RoadMates – Covoiturage Web

Architecture Web Distribuée – Atelier Eureka & Actuator
Année universitaire : 2026–2027


## 👥 Équipe :

Eya Allah Romdhani       --> Recherche & Réservation

Omaima Zaghouani         --> Trajets

Mohamed Aziz Bouslama    --> Avis & Réputation

Rayen Samoud             --> Fidélité & Récompenses

Mohamed Hedi Mehdoui     --> Incidents & Accidents

Fedi Mouelhi             --> Paiement

Eya Allah Romdhani       --> Notification & Messagerie (microservice partagé)

## ⚙️ Prérequis

JDK 17 ou supérieur

Maven (ou le wrapper mvnw fourni)

IntelliJ IDEA

Docker Desktop

PostgreSQL

MongoDB

Spring Boot 3.5.7

Spring Cloud 2025.0.0

## 🚀 Lancer le projet

Ordre de démarrage

Démarrer Docker Desktop et les conteneurs pg et mongo.

Lancer le serveur Eureka (EurekaRoadMatesApplication) sur le port 8761.

Lancer les microservices dans n'importe quel ordre.

Dashboard Eureka

Accédez au dashboard depuis votre navigateur :

http://localhost:8761

## 📸 Eureka Dashboard

![Eureka Dashboard](images/Dashboard_Eureka.png)


## Test du CRUD – Recherche & Réservation
![CRUD_MS_Reservation](images/CRUD_MS_Reservation.png)


## 📁 Structure du dépôt

```text
Covoiturage-web/
├── BackEnd/
├── FrontEnd/
├── Documentation/
├── images/
└── Eureka_RoadMates/
    ├── Trajets/
    ├── Reservation/
    ├── Paiement/
    ├── Fidelite/
    ├── Avis/
    ├── Incidents/
    └── Eureka_RoadMates/
```

## Paiement (microservice 3)

Le service est un client Eureka et expose les endpoints Actuator `health`, `info`, `metrics`, `env` et `mappings`. Les routes métier attendent l'en-tête `X-User-Id` pour identifier l'utilisateur de démonstration.

### Démarrer la base et les services

Avec Docker Desktop, lancer PostgreSQL depuis la racine du dépôt :

```powershell
docker compose up -d postgres
```

Dans deux terminaux PowerShell distincts, démarrer Eureka et Paiement :

```powershell
cd Eureka_RoadMates\Eureka_RoadMates
.\mvnw.cmd spring-boot:run
```

```powershell
cd Eureka_RoadMates\Paiement
.\mvnw.cmd spring-boot:run
```

Ports par défaut : Eureka `8761` et Paiement `8083`. La base `paiementdb` utilise PostgreSQL (`postgres` / `postgres`). Ouvrir ensuite [le dashboard Eureka](http://localhost:8761) et vérifier `PAIEMENT`.

### Tester Paiement dans PowerShell

```powershell
$payment = 'http://localhost:8083'
$userId = 'user-test-' + [guid]::NewGuid().ToString()
$user = @{ 'X-User-Id' = $userId }

# Créer un wallet avec 50 TND pour le test
$walletBody = @{ ownerId = $userId; initialBalance = 50 } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "$payment/wallets" -ContentType 'application/json' -Body $walletBody

# GET /wallets/me
Invoke-RestMethod -Method Get -Uri "$payment/wallets/me" -Headers $user

# POST /payments (déduit 12,50 TND du wallet)
$paymentBody = @{ recipientId = 'driver-2'; amount = 12.50; description = 'Reservation test' } | ConvertTo-Json
$createdPayment = Invoke-RestMethod -Method Post -Uri "$payment/payments" -Headers $user -ContentType 'application/json' -Body $paymentBody
$paymentId = $createdPayment.id

# Lire une transaction, puis modifier ses informations (le montant reste immuable)
Invoke-RestMethod -Method Get -Uri "$payment/payments/$paymentId" -Headers $user
$updateBody = @{ recipientId = 'driver-2'; description = 'Reservation confirmée' } | ConvertTo-Json
Invoke-RestMethod -Method Put -Uri "$payment/payments/$paymentId" -Headers $user -ContentType 'application/json' -Body $updateBody

# GET /transactions et solde restant (37,50 TND)
Invoke-RestMethod -Method Get -Uri "$payment/transactions" -Headers $user
Invoke-RestMethod -Method Get -Uri "$payment/wallets/me" -Headers $user

# DELETE rembourse le paiement et conserve sa trace avec le statut REFUNDED
Invoke-RestMethod -Method Delete -Uri "$payment/payments/$paymentId" -Headers $user
Invoke-RestMethod -Method Get -Uri "$payment/wallets/me" -Headers $user
```

`POST /wallets/me/deposit` permet aussi de créditer un wallet existant pour les essais. `GET /payments` renvoie l'historique de paiements de l'utilisateur. `DELETE /payments/{id}` rembourse un paiement terminé au lieu d'effacer la trace financière.

### Lancer les tests automatiquement

Les tests utilisent une base H2 temporaire et ne nécessitent ni PostgreSQL ni Eureka :

```powershell
cd Eureka_RoadMates\Paiement
.\mvnw.cmd test
```

L'endpoint de santé est `http://localhost:8083/actuator/health`. Pour `GET /wallets/me` et `GET /transactions`, envoyer l'en-tête `X-User-Id` avec Postman ou l'exemple PowerShell ci-dessus. Cette identité par en-tête sert uniquement à la démonstration du CRUD ; elle ne remplace pas une authentification.
