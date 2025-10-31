# Chrome Dinosaur Game (Java Swing)

A fast-paced Chrome Dinosaur clone built with **Java Swing**.  
Jump over cacti, duck under birds, and beat your high score!

---

## Features

- **Jump** – `SPACE`  
- **Duck** – Hold `↓` (Down Arrow)  
- **Obstacles** – Small, medium, large, and big cacti + low/high birds  
- **High Score** – Persists across restarts  
- **60 FPS** smooth gameplay  
- **Instant restart** on Game Over  

---

## Controls

| Key        | Action            |
|------------|-------------------|
| `SPACE`    | Jump / Restart    |
| `↓`        | Duck (hold)       |

---

## Project Structure

```
ChromeDinoGame/
├── App.java
├── DinosaurChrome.java
└── assets/
    ├── dino-run.gif
    ├── dino-jump.png
    ├── dino-duck.png
    ├── dino-dead.png
    ├── cactus1.png
    ├── cactus2.png
    ├── cactus3.png
    ├── cactus-big.png
    ├── bird-low.png
    └── bird-high.png
```

> Images are loaded with `getClass().getResource()` – no external paths needed.

---

## How to Run

```bash
git clone https://github.com/yourusername/ChromeDinoGame.git
cd ChromeDinoGame
javac *.java
java App
```

*Requires Java 8+*

---

## License

MIT © 2025
