# RAPPORT CITADELLE

## I. Point d’avancement

### 1) Fonctionnalités réalisées

   1. Règles du jeu Citadelle

Toutes les règles du jeu citadelle ont été implémentées

**But du jeu :**

- Chaque robot possède une cité qu’il doit rendre prospère avec des quartiers.

- Le but est de construire 8 quartiers pour mettre fin au jeu.

- Ensuite, les points sont comptabilisés pour déterminer le vainqueur.

**Initialisation :**

- Distribuer 2 pièces d'or à chaque robot.

- Distribuer 4 cartes quartier à chaque robot.

- Désigner le premier Roi au hasard.

**Action dans le jeu :**

- Chaque robot choisit un personnage parmi les cartes disponibles.

- À la fin de chaque tour, les cartes personnages sont mélangées et le nouveau Roi commence le processus.

**Choix des personnages :**

- Mélanger les cartes personnages.

- Détruire des personnages avant le choix du personnage des robots

- Le robot avec la couronne choisit secrètement sa carte.

- Passer les cartes au joueur suivant qui fait de même.

- Une fois que tous les joueurs ont choisi leur personnage le tour de jeu commence

**Tour de jeu :**

- Le robot avec la couronne commence en appelant les robots par ordre croissant.

- Lorsqu'un joueur est appelé, il se révèle aux autres et joue son tour selon le personnage qu’il avait choisi

- Si aucun robot n'a le chiffre appelé, la carte n'est pas jouée et on passe au chiffre suivant.

**Actions lorsqu’on est appelé :**

- Piocher deux cartes quartier ou prendre deux pièces d’or.

- Si le joueur choisit de piocher les deux cartes quartier, il peut en choisir une et remettre l'autre sous le paquet.

- Utiliser le pouvoir de sa carte personnage.

- Le joueur peut décider de construire un quartier dans sa cité en payant le coût du quartier.

* Attention : Il ne peut pas construire le même quartier dans sa cité.

**Fin du tour :**

- Réinitialiser le jeu en mélangeant les cartes personnages et en recommençant le processus.

2. Fonctionnalités additionnelles semaine spécifique

1) Fonctionnalités additionnelles 1

- Simulation de 1000 parties du meilleur bot (dans notre cas il s’agit du Robot Discret) contre le second (avec d’autres bots pour compléter) 

-  Simulation de 1000 parties de du meilleur bot contre lui-même

- Affichage des statistiques sur la sortie standard

- Utilisation de Jcommander pour les options permettant le passage en mode démo 2 x 1000 parties ou une seule partie avec des logs 

- 2 x 1000 parties : mvn exec:java -D exec.args="--2thousands" 

- Mode démo d’un seule partie avec log complet : mvn exec:java -D exec.args="--demo" 

2. Fonctionnalités additionnelles 2

- Lancement d’une simulation à plusieurs parties avec relecture de "stats/gamestats.csv” s’il existe et ajout des nouvelles statistiques : mvn exec:java -D exec.args="--csv" 

3. Fonctionnalités additionnelles 3

- Implémentation du robot Richard suivant les séries de conseils su le forum qui va être expliqué dans la suite du rapport <https://www.trictrac.net/forum/sujet/citadelles-charte-citadelles-de-base>

### 2. Fonctionnalités non réalisées

- Certains scénarios spécifiques ne fonctionnent pas comme prévu:

Exemple: Pour l’architecte, lorsqu’il construit le district école de magie au dernier tour, l’avantage de ce district est pris en compte ce qui ne devrait pas être le cas 

- Le scénario endGame(fin de jeu) des conseils de Richard sur le forum a été implémenté mais n’a pas été push sur le master par manque de tests pouvant nous assurer qu'il fonctionne correctement

### 3. Affichage à partir des logs

- Une classe ActionOfBotDuringARound a été ajouté pour tout les affichages tout au long du jeu

- un variable logger a été ajouté  à toutes les classes où on aura un affichage

<!---->

    private static final Logger logger = Logger.getLogger(ActionOfBotDuringARound.class.getName());

Nous avons aussi deux lignes importantes pour les loggers

    System.setProperty("java.util.logging.SimpleFormatter.format", "\u001B[37m %5$s%6$s%n \u001B[0m");
    if (!systemPrint) logger.setLevel(Level.OFF);

Le System.setProperty permet d’enlever l’affichage de la date, du package, etc… lorsque les loggers sont appelés

Le boolean systemPrint permet de savoir si l’on doit afficher ou non les logs. Dans le cas où l’on doit les désactiver, la ligne “logger.setLevel(Level.OFF);” permet de ne plus afficher les loggers

### 4. Prise en compte de l’ajout des statistiques dans le fichier CSV

- Ajout de deux classes principales pour l’écriture des statistiques dans le fichier gamestats.csv et leur affichage:

  - DisplayFullGameStats: qui permet d’afficher dans la sortie standard les statistiques se trouvant dans le fichier gamestats.csv

  - WriteStatsByLine: qui prend un tableau de statistiques et ajoute les statistiques de chaque robot dans gamestats.csv

  - Utilisation des paths dans les deux méthodes pour éviter les problèmes de chemin sur des systèmes d’exploitation différents

- Le fichier gamestats.csv est créé lorsqu’il n’existe pas

- Ce fichier est mis à jour à chaque ajout de nouvelles stratégies

### 5. Résumé de ce qui a été fait pour le bot spécifique demandé

Pour le bot Richard (que nous avons affectueusement renommé Richardo dans notre projet dans Intellij) , nous avons commencé par implémenter les différentes stratégies Bâtisseur, Agressif et Opportuniste. Ces stratégies ont été implémentées grâce aux classes StrategyBatisseur, StrategyAgressif et StrategyOpportuniste, qui permettent au bot Richard de suivre le bon chemin de pensées selon s’il est agressif, bâtisseur ou opportuniste à un instant T. Les 3 classes ont bien été testées pour vérifier que leur comportement était correct.

Pour les conseils de Richard, nous avons aussi implémenté les scénarios Assassin, Architecte et Roi dans le RobotRichardo. Si jamais le robot Richard reconnait un de ces scénarios, il agira en conséquence, en suivant bien sûr les recommandations de Richard. Malheureusement, nous n’avons pas implémenté dans master  la partie DernierTour des conseils de Richard. Nous l’avions bien codé, mais par manque de temps, nous n’avons pas pu la tester pour être sûr de son fonctionnement. Nous avons préféré la laisser sur la branche richardo pour ne pas casser notre projet juste à la fin

Malheureusement, malgré tout notre travail sur Richardo, il n’est pas notre meilleur robot, palme qui revient au Robot Discret. En nous basant sur les statistiques des 1000 parties à 5 joueurs, notre RobotDiscret gagne 31% des parties avec un score moyen de 22,311 tandis que notre robot Richardo ne gagne que 12% des parties avec un score moyen de 19,241.

Le Robot Discret est considéré comme le meilleur en raison de sa stratégie bien définie et optimisée, qui lui permet de maximiser ses chances de gagner dans le jeu. Voici un résumé de ses principales stratégies et pourquoi elles sont efficaces :

1. **Priorisation des personnages :** Le Robot Discret privilégie les personnages ROI, MARCHAND, ÉVÊQUE et CONDOTTIERE par rapport aux autres, car ils offrent des avantages significatifs tels que la génération de revenus, la possibilité de piocher des cartes ou de détruire les quartiers des adversaires.

2. **Choix entre gagner des pièces et piocher des districts :**

   - Lorsque la main du robot est vide ou que les districts qu'il possède sont déjà présents dans sa cité, il choisit de piocher des districts pour diversifier ses options et augmenter ses chances de construire des quartiers précieux.

   - Si le robot n'a pas suffisamment d'or pour construire le quartier le moins coûteux dans sa main, il prend des pièces d'or pour s'assurer qu'il pourra toujours effectuer des constructions lors de ses prochains tours.

3. **Stratégie de piochage des districts :**

   - Le Robot Discret suit des conditions spécifiques pour choisir les districts à piocher. Il privilégie d'abord les districts du même type que son personnage, puis les districts spéciaux (noble, marchand, évêque ou militaire) pour maximiser les avantages potentiels.

   - En l'absence de districts du même type que son personnage, il opte pour les districts spéciaux ayant le plus grand score (coût) pour tirer parti des bonus associés.

   - Si aucune de ces conditions n'est remplie, il choisit simplement le district ayant le plus grand score (coût) disponible.

4. **Choix du district à construire :** Le Robot Discret priorise la construction de districts du même type que son personnage, puis des districts spéciaux, et enfin les autres districts. Il choisit toujours le district le plus cher qu'il peut construire, ce qui lui permet d'accumuler des quartiers de grande valeur dans sa cité.

En suivant cette stratégie, le Robot Discret maintient un flux constant de revenus et de districts dans sa main, ce qui lui permet de construire efficacement sa cité et de maximiser ses chances de terminer le jeu en premier avec 8 quartiers. Cette approche proactive et stratégique lui confère un avantage significatif sur ses adversaires, ce qui explique pourquoi il est considéré comme le meilleur bot pour ce jeu spécifique.

## II. Architecture et qualité

### 1. Architecture du projet

Tout d’abord, la classe GameEngine gère le fonctionnement du jeu et veille au bon déroulement de la partie. Elle fait choisir leur personnage aux robots, gère un peu l’affichage et continue la partie tant qu’un personnage n’a pas construit 8 quartiers. Pour le tour de jeu de chaque personnage, elle laisse cette responsabilité à la classe Round.

La classe Round fait jouer aux robots leur tour de jeu. C’est elle qui leur fait piocher leur cartes, construire leurs quartiers, utiliser le pouvoir de leur personnage.

À la fin du jeu, c’est la classe Winner qui permet de déterminer le ou les gagnants de la partie.

Nous avons les classes CharactersType et DistrictsType qui permet de simuler les quartiers et les personnages tandis que les classes DeckDistrict et DeckChacacters nous servent comme leur nom l’indique comme deck de jeu.

De plus, nous avons la classe abstraite Robot, qui est la pierre angulaire de tous les bots puisque tous les différents robots que l’on a créé hérite de cette classe. La classe Robot implémente les fonctions qui sont communes au fonctionnement de tous les bots. Par exemple, l’affichage du statut de chaque robot ou piocher 2 à 4 cartes depuis la pioche. Ces fonctions ne changent pas d’un robot à un autre, c’est pourquoi nous les définissons dans Robot. Mais Robot reste une classe abstraite, elle a donc des méthodes abstraites. Ces méthodes sont celles qui changent d’un robot à un autre, et qu’on change dans chaque classe robot qui hérite de Robot. Par exemple, savoir quel district construire ou encore quel personnage le robot doit choisir. Ces fonctions sont différentes pour chaque bot, c’est pourquoi elles sont abstraites dans la classe Robot et qu’elles sont définies dans chaque bot.

Ainsi, les différents bots héritent de la classe Robot et changent les fonctions qu’ils doivent changer pour suivre leur fonctionnement.

Nous avons aussi le Robot Richard, qui suit les conseils donnés par Richard sur le forum. Les classes StrategyBatisseur, StrategyAgressif et StrategyOpportuniste permettent au bot Richard de suivre la stratégie associée à la classe. Ces classes sont en dehors de la classe RobotRichardo pour éviter de transformer cette classe en god classe.

De plus, les classes ParseFullGameStats et WriteStatsByLine permettent d’update le fichier gamestast.csv pour qu’il contienne les statistiques des 2\*1000 parties. La classe CitadelleArguments permet quand à elle de gérer les différents arguments “--2thousands”, “--demo”, “--csv”

Par ailleurs, la classe Pouvoir permet de faire les actions correspondant au pouvoir du personnage. 

Enfin, la classe ActionOfBotDuringARound a été créée pour éviter que plusieurs classes ne gèrent l’affichage. Si jamais une classe veut afficher un log, elle passe par cette classe. Ainsi, elle porte la quasi responsabilité pour les entrées/sorties

### 2. Où trouver les infos ?

Dans chaque classe, pour les fonctions importantes dont le fonctionnement peut sembler obscur au premier abord, nous avons ajouté de la javadoc au-dessus desdites fonctions pour vous permettre de mieux comprendre leur fonctionnement. 

### 3. Etat de la base de code

a) Parties de bonnes qualités : 

Notre code est loin d’être le plus optimal , ceci dit nous avons tout de même quelques parties intéressantes à commencer par les classes de bases (CharacterType , DistrictType , DeckDistrict) qui ont facilité l’avancement du code.\
Notre moteur de jeu qu’on peut retrouver dans le package game est assez bien architecturé dans le sens ou les classes sont cohésives et assure le bon déroulement de la partie.

b) Quelles parties sont à refactor et pourquoi ?

Tout d’abord, notre classe CharactersType n’est pas optimale. En effet, pour appeler les différents pouvoirs des personnages, nous aurions pu utiliser une seule méthode et l’override dans chacun des personnages de CharactersType. Ainsi, un petit refactor de la classe CharactersType pourrait simplifier l’utilisation des pouvoirs, et peut être enlever la classe Power.

De plus, l’architecture que l’on a choisie pour implémenter les stratégies des personnages pourrait être refactor. Tous les bots doivent définir les méthodes abstraites de la classe Robot. Cependant, la plupart des robots n’ont pas besoin de redéfinir toutes les méthodes, et donc beaucoup d'entre elles ressemblent aux méthodes définies dans RobotRandom. Ce choix d’implémentation nous conduit à avoir quelques duplications dans notre code.

c) Comment la sortie SONAR le montre-elle (ou pas) ?

Notre sortie SONAR montre bien que notre code possède pas mal de duplications. En effet, notre code présente plus de 2% de duplications à cause de notre choix d’architecture pour les stratégies des robots. Et plusieurs classes présentent une complexité assez élevée dont:

- Robot : 89%

- RobotRichardo : 75%

- ActionOfBotDuringARound : 74%

## III. Processus 

### 1. Qui est responsable de quoi / qui a fait quoi ?

**Partie commune :** 

- Implémentation des classes de bases et des fonctionnalités principales du jeu

- Implémentation du Robot Random dont les choix sont faits de manière aléatoire

- Refactoring et corrections des bugs

- Implémentation de Power pour le pouvoir des personnages

- Enfin , en ce qui concerne les tests, chacun a pris le temps de tester ses méthodes et stratégies.




**Partie d'Alban FALCOZ :** 

- Refactor de la classe GameEngine en 3 classes différentes: GameEngine, Round et ActionOfBotDuringARound pour simplifier l’affichage

- Création du robotChoiceOfCharacter : ce robot essaie de prendre l’assassin s’il peut finir ce tour, il prend le roi, pour piocher l’assassin au prochain tour, s’il peut finir au prochain tour. Il choisit de prendre le condottière si quelqu’un est proche de finir. 

- Mis en place des loggers pour remplacer les system.out.println et mise en place des 1000 parties

- Implémentation de la stratégie Batisseur et du scénario Architecte pour le robotRichard

- Correction des bugs pour l’assassin et le voleur



**Partie de Nora KAYMA-KCILAR :**

- Stratégie et implémentation du RobotRush : une implémentation personnalisée d'un robot dans le jeu, héritant des fonctionnalités de base de la classe Robot. 

Le but de ce robot est de prioriser une stratégie de construction rapide pour gagner, en utilisant différents personnages du jeu à son avantage et en choisissant de construire des districts dès que possible.

- Stratégie et implémentation du RobotAnalyzer : une implémentation personnalisée d'un robot dans le jeu, héritant des fonctionnalités de base de la classe Robot.

Elle utilise l'analyse de l'état actuel du jeu et l'historique des actions pour prendre ses décisions sur la sélection des personnages, la construction de districts, et le choix des cartes districts.

- Refactor des fonctionnalités pour les robots dans le but de la création de la classe Robot.

  

**Partie de Selom Ami ADZAHO :** 

- Implémentation de Robot discret: son but premier est de se faire le plus discret possible donc il priorise les personnages dits non agressifs du jeu(Roi, Marchand, Évêque, Condottiere)

- Implémentation de Stratégie Opportuniste du robot Richard: cette stratégie priorise le choix des personnages Évêque, Condottiere et Voleur.

- Mise en place du fichier CSV et des classes permettant de le mettre à jour avec les statistiques

- Refactor de la plupart des classes (GameEngine en trois classes : GameEngine, Round et Winner; RobotRichardo pour suivre un strategy pattern etc…)
  


**Partie de Sara TAOUFIQ :** 

- Implémentation des premiers enums et classes 

- Création du Robot Agressif : Ce robot priorise les personnages agressifs , son but est de gagner par destruction des autres joueurs.

- Création d’une stratégie agressive pour Richard : implémentation de la stratégie agressive décrite dans le forum.



### 2. Quel est le process de l'équipe (comment git est utilisé, branching strategy…)

Nous avons découpé le projet en milestones, puis chaque milestone en issue, l’objectif étant de finir un milestone par semaine. Chaque personne de l’équipe pouvait s’occuper de plusieurs issues à la fois. Chaque issue apportait un peu de fonctionnalité verticale au projet.

De plus, lorsque quelqu’un voulait implémenter une nouvelle fonctionnalité, cette personne devait créer une nouvelle branche et travailler dessus. Une fois son travail fini, la branche de la personne est merge sur master grâce à une pull request. Il faut également savoir que github action vérifie que “mvn clean package” s’effectue sans soucis à chaque PR ou push sur master.

Nous nous sommes ainsi basés sur la stratégie github flow pour notre stratégie de branche.
