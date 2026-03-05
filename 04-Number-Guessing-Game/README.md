# Number Guessing Game

## Overview

Number Guessing Game is a console-based Java application where players attempt to guess a randomly generated number within a defined range.  
The system provides feedback for each guess and tracks the number of attempts required to find the correct number.

The project demonstrates fundamental Java concepts including modular design, game logic separation, console-based user interaction, and persistent leaderboard tracking using CSV storage.

---

## Folder Structure

```
Number-Guessing-Game
|
+--- bin/                               (Compiled Java files)
|
+--- src/                               (Source code)
|      NumberGuessLab.java              (Application entry point)
|      NGEngine.java                    (Game logic and round management)
|      NGGameUI.java                    (Console UI interaction)
|
+--- task2_leaderboard.csv              (Leaderboard and player scores)
+--- README.md                          (Project documentation)
+--- .gitignore                         (Git ignore configuration)
```

---

## Features

### Game Mechanics

• Random number generation for each game round  
• User input-based guessing system  
• Feedback for each guess (too high / too low)  
• Tracks number of attempts taken to guess correctly  

### Leaderboard System

• Stores player scores in `task2_leaderboard.csv`  
• Maintains ranking based on performance  
• Persistent score storage across sessions  

### Console Interface

• Simple and interactive console UI  
• Clear feedback messages for player guidance  
• Modular UI and logic separation  

---

## How to Run

### 1. Open terminal in project root

```
cd Number-Guessing-Game
```

### 2. Compile the project

```
javac -d bin src/com/venkat/numberguess/*.java
```

### 3. Run the game

```
java -cp bin com.venkat.numberguess.NumberGuessLab
```

---

## Technologies Used

Java SE  
Object-Oriented Programming  
File Handling  
Console-based UI  

---

## Developer

Venkatram  
GitHub: https://github.com/VENKATRAM2005
