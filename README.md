# ♟️ Java Chess Game

A fully functional chess game built with Java Swing, featuring a graphical user interface with smooth piece movement, valid move highlighting, and complete chess rule implementation.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)


## Features

- Full chess rule implementation (all piece movements, special moves, win conditions)
- Clean graphical interface with piece images
- Click-to-select, click-to-move gameplay
- Visual indicators for valid moves
- Real-time check/checkmate detection
- Turn-based play with proper state management


## Quick Start

**Prerequisites:** Java 8+ and Maven

**Dependencies:** This uses [chesslib](https://github.com/bhlangonijr/chesslib) for chess logic validation.

1. Clone and navigate to the repo:
```bash
   git clone https://github.com/yourusername/java-chess-game.git
   cd java-chess-game
```

2. Add chesslib to your `pom.xml`:
```xml
   
       com.github.bhlangonijr
       chesslib
       1.3.3
   
```

3. Run it:
```bash
   mvn clean compile exec:java -Dexec.mainClass="chess.ChessGame"
```

**Assets:** Make sure you have chess piece PNG images in an `assets/` directory named like `white_king.png`, `black_queen.png`, etc.

## How to Play

- Click a piece to select it (yellow highlight appears)
- Valid moves show as green dots
- Click a green dot to move there
- Game enforces all rules automatically

White moves first, then players alternate. The game will notify you of check and checkmate.

## Technical Stack

- **Java Swing** for the GUI
- **Java 2D Graphics** for rendering
- **chesslib** for move validation and game state
- Custom board rendering with 80x80px squares

## Potential Improvements

Some ideas I'm considering:

- AI opponent with adjustable difficulty
- Move history panel showing algebraic notation
- Undo/redo functionality
- Chess clock for timed games
- Move animations
- Sound effects