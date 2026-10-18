# Native Warrior Game

**A three-level Java 2D action-platformer developed with IntelliJ IDEA and the CityEngine physics library.**

Native Warrior challenges players to navigate platform-based levels, fight armed enemies, manage ammunition and health, and reach the end-of-level flag. The project combines Java object-oriented programming, collision-based gameplay, animated characters, audio, user-interface screens, and score tracking.

> [!IMPORTANT]
> **CityEngine is required to run this project.** The game depends on the external Java `city.cs.engine` library (configured as `CityEngine` in the IntelliJ module). **The library is not included in this repository.** Reviewers can browse the source code without it, but must obtain and configure a compatible CityEngine distribution and its dependencies to compile and play. This is **not a standalone executable**.

## Features

- **Three levels** with distinct backgrounds, platforms, enemies, hazards and flag-based progression.
- **Combat:** player shooting, enemy projectiles, a limited magazine, reserve ammunition, manual reloading and ammunition pickups.
- **Movement:** left/right controls, double jump and moving platforms.
- **Enemies and hazards:** patrolling and stationary soldiers, enemy fire and explosive bombs.
- **Health and progress:** limited lives, health pickups, a score counter and elapsed-time tracking.
- **Persistent leaderboard:** saves player names, scores and completion times to `leaderboard.txt` and displays ranked results.
- **Menus and sound:** main menu, instructions, level selection, settings, adjustable volume, sound effects and music.

## Technologies

| Technology | Use |
| --- | --- |
| Java | Game logic and object-oriented application structure |
| CityEngine (`city.cs.engine`) | Physics bodies, world simulation and collision handling |
| JBox2D (`org.jbox2d`) | Vector and underlying physics types used by the game |
| Java Swing/AWT | Window, menus, input and interface elements |
| IntelliJ IDEA | Development and project configuration |

## How to run

### Prerequisites

- A compatible **Java Development Kit (JDK)** and **IntelliJ IDEA**.
- The **CityEngine Java library**, including any dependencies distributed with it. Obtain these from your course resources or another authorised source. The project's `JavaGame.iml` refers to an application-level IntelliJ library named `CityEngine`, but does not bundle its JAR files.

### Setup

1. Clone this repository:

   ```bash
   git clone https://github.com/Gursimer-Dhaliwal/Native-Warrior-Game.git
   ```

2. Open the cloned folder as a project in IntelliJ IDEA.
3. Set up an appropriate JDK under **File → Project Structure → Project**.
4. Go to **File → Project Structure → Libraries → + → Java**, select the appropriate CityEngine JAR file(s), and attach the library to the game's module. If you have a preconfigured library named `CityEngine`, you may use it instead.
5. Make sure `org.jbox2d` classes and other dependencies supplied with CityEngine resolve correctly; missing imports mean that the required libraries are not fully configured.
6. Ensure `src` is marked as a source root. Run **`game.Game`** (the class with `public static void main(String[] args)`).
7. Run the game with the **repository root as its working directory**, so relative paths such as `data/...` and `leaderboard.txt` resolve as expected.

**Troubleshooting:** An error such as `package city.cs.engine does not exist` means CityEngine is missing from the module's classpath. `package org.jbox2d.common does not exist` indicates a missing physics dependency. Missing images or audio may indicate an incorrect working directory or filename case mismatch on case-sensitive systems.

## Controls

| Key | Action |
| --- | --- |
| **A / D** | Move left / right |
| **W** | Jump (supports double jump) |
| **Space** | Shoot during gameplay; advance at level transitions |
| **R** | Reload |
| **Esc** | Return to menu (or handle the current game screen) |

Complete each level by defeating the enemies and reaching the flag. Ammo boxes replenish ammunition; hearts restore lives. After completing the campaign, the game offers the option to save a score to the leaderboard.

## Project structure

```text
Native-Warrior-Game/
├── src/game/             # Gameplay, levels, character logic, UI and leaderboard classes
│   ├── Game.java         # Application entry point and game state
│   ├── MainMenu.java     # Menus, settings and instructions
│   ├── Level1.java       # Level one
│   ├── Level2.java       # Level two
│   ├── Level3.java       # Level three
│   ├── WASDMovement.java # Player input, movement and ammunition controls
│   └── ...
├── data/                 # Images, sprites, sound effects and music
├── JavaGame.iml          # IntelliJ module configuration (CityEngine reference)
├── leaderboard.txt       # Saved leaderboard results
├── Use of AI.txt         # AI-use declaration
└── README.md
```

## Code highlights for reviewers

- `Game.java` coordinates menus, level transitions, player lives, scoring and overall game state.
- `LevelBuilder.java` creates platforms, moving platforms, enemy placements, pickups, bombs and finish flags.
- `SoldierPatrol.java` and `SoldierShoot.java` contain enemy movement and combat behaviour.
- `WASDMovement.java` handles keyboard input, double jumping, shooting and reloading.
- `LeaderboardManager.java` reads and writes results and ranks players by score, then completion time.

## Development transparency

A separate [`Use of AI.txt`](Use%20of%20AI.txt) documents how ChatGPT was used as a learning and debugging aid during development, including explanations of Java concepts, implementation guidance and creative inspiration. The source code and that declaration are available for review.

## Author

**Gursimer Dhaliwal**  
Computer Science student, City St George's, University of London  
[GitHub profile](https://github.com/Gursimer-Dhaliwal)
