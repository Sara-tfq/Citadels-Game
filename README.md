### Citadelle
Implémentation Java du jeu Citadelle où les joueurs incarnent des personnages afin d'accumuler des richesses dans leur cité en construisant des quartiers.

### Description rapide du déroulement du jeu :
- Le but du jeu est d'avoir le plus grand score en fin de partie.
- Les joueurs peuvent piocher des personnages et utiliser le pouvoir de ces derniers.
- À chaque tour, le joueur a le choix entre piocher deux golds ou deux quartiers pour n'en garder qu'un.
- Ensuite le joueur décide oui ou non de batir un quartier.
- Celui qui se retrouve avec 8 quartier dans sa cité met fin au jeu et on passe alors au compte des points.
- Une fois les bonus affectés, on peut désigner le grand gagnant.
  
### Avancement :
- Toutes les règles du jeu ont été implémentées.
-6 robots avec stratégies différentes.
- 1 robot avec strategie suivant le forum. https://forum.trictrac.net/t/citadelles-charte-citadelles-de-base/509
- Affichage Statut / Décisions / Classement / Score .
- Démo d'une partie avec 5 robots.
- Démo 2000 parites.
- Statistiques en sortie avec .csv
  
### Pour RUN le projet :

*Pour se faire il faut Maven*

Pour tester le code :
  
        mvn clean package
   
Pour lancer une démo avec log :

        mvn exec:java -D exec.args="--demo" 
  
Pour lancer une démo 2000 parties :

        mvn exec:java -D exec.args="--2thousands"
  
Pour générer les statistiques csv :

        mvn exec:java -D exec.args="--csv" 
  
### Auteurs :
Alban FALCOZ

Nora KAYMA-KCILAR

Stacy Selom ADZAHO

Sara TAOUFIQ
