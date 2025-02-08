# Leitner System Backend

## Auteurs

### Groupe 3 - 4AL2

- BERDRIN Tristan - [tberdrin@myges.fr](mailto\:tberdrin@myges.fr)
- SCHWAGER Antoine - [aschwager2@myges.fr](mailto\:aschwager2@myges.fr)

## Description du Projet

Ce projet est une implémentation du système de Leitner basé sur la répétition espacée et l'auto-évaluation. Il s'agit d'une API REST développée en **Java 21** avec **Spring Boot**, en suivant les principes **DDD (Domain-Driven Design)**, l'**architecture Hexagonale**, et les **principes SOLID**.

L'API est conçue pour gérer des fiches d'apprentissage permettant aux utilisateurs d'améliorer leur mémorisation en fonction de la fréquence de leurs réponses correctes ou incorrectes.

Ce projet respecte les spécifications décrites dans le fichier **Swagger.yml**.

## Schéma d'Architecture

*(A venir...)*

## Prérequis

- **Java 21** installé
- **Maven** installé

## Installation et Démarrage

### 1. Cloner le dépôt

```sh
 git clone <https://github.com/Antschw/leitner-system-backend>
 cd leitner-system-backend
```

### 2. Construire le projet

```sh
 mvn clean install
```

### 3. Lancer l'application

```sh
 mvn spring-boot:run
```

L'application démarre sur [**http://localhost:8080**](http://localhost:8080).

### 4. Tester avec Swagger

Accédez à la documentation interactive sur :

```
http://localhost:8080/swagger-ui/index.html
```

## API Endpoints

L'API expose les endpoints suivants (extraits du fichier Swagger) :

- **GET /cards** : Récupère toutes les fiches d'apprentissage (avec possibilité de filtrer par tags)
- **POST /cards** : Crée une nouvelle fiche d'apprentissage
- **GET /cards/quizz** : Récupère les fiches à réviser du jour
- **PATCH /cards/{cardId}/answer** : Répondre à une question et modifier son statut en fonction de la réponse

*(Voir Swagger.yml pour plus de détails)*

## Tests

Exécutez les tests unitaires avec la commande :

```sh
 mvn test
```




