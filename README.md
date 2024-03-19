### Citadel
Java implementation of the Citadel game where players embody characters to accumulate wealth in their city by building districts.

### Quick overview of the game flow:
- The goal of the game is to have the highest score at the end.
- Players can draw characters and use their powers.
- Each turn, the player has the choice between drawing two gold or two districts to keep only one.
- Then the player decides whether or not to build a district.
- The player who ends up with 8 districts in their city ends the game, and then we move on to counting the points.
- Once the bonuses are allocated, we can designate the grand winner.

### Progress:
- All game rules have been implemented.
- 6 robots with different strategies.
- 1 robot with strategy following the forum. https://forum.trictrac.net/t/citadelles-charte-citadelles-de-base/509
- Status / Decisions / Ranking / Score display.
- Demo of a game with 5 robots.
- Demo of 2000 games.
- Output statistics with .csv

### To RUN the project:

*To do this, you need Maven*

To test the code:
  
        mvn clean package
   
To launch a demo with logs:

        mvn exec:java -D exec.args="--demo" 
  
To launch a demo of 2000 games:

        mvn exec:java -D exec.args="--2thousands"
  
To generate csv statistics:

        mvn exec:java -D exec.args="--csv" 
  
### Authors:
Alban FALCOZ

Nora KAYMA-KCILAR

Stacy Selom ADZAHO

Sara TAOUFIQ

