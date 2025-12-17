# Hotel Reservation System

Ce projet est un système simple de réservation d'hôtels en Java. Il permet de gérer des chambres, des utilisateurs, et des réservations avec des validations de disponibilité et de budget.

## Structure du projet

- `entities/` : Contient les classes `Room`, `User`, et `Booking` représentant les entités du système.
- `enums/` : Contient les énumérations comme `RoomType`.
- `services/` : Contient la classe `HotelReservationService` qui gère la logique métier.
- `test/` : Contient les tests unitaires pour valider le comportement du service.

## Comment tester

Le projet utilise JUnit pour les tests unitaires. Voici comment exécuter les tests :

1. Assurez-vous d'avoir Java et Maven installés sur votre machine.

2. Depuis la racine du projet (`SkypayTest2`), lancez la commande suivante pour exécuter les tests :

```bash
mvn test
```

3. Les tests sont dans `src/test/java/org/skypaytest/HotelReservationTest.java`. Ils couvrent :
   - La création et mise à jour des chambres (`setRoom`).
   - La création des utilisateurs (`setUser`).
   - La réservation de chambres (`bookRoom`) avec gestion des cas de succès et d'échec (dates invalides, solde insuffisant, chambre déjà réservée, etc).

4. Vous pouvez consulter les sorties dans la console pour voir les messages de succès ou d'échec des réservations.

## Notes

- Le service gère les exceptions en interne et affiche les erreurs sans interrompre l'exécution.

---