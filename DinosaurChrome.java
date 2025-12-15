import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class DinosaurChrome extends JPanel implements ActionListener, KeyListener {

    int boardWidth = 750;
    int boardHeight = 250;

    Image dinoRunImg, dinoDeadImg, dinoJumpImg, dinoDuckImg;
    Image cactus1Img, cactus2Img, cactus3Img, cactusBigImg;
    Image birdLowImg, birdHighImg;

    class Block {
        int x, y, width, height;
        Image img;
        Block(int x, int y, int w, int h, Image img) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
            this.img = img;
        }
    }

    int dinoWidth = 88, dinoHeight = 94, dinoDuckHeight = 55;
    int dinoX = 50;
    int dinoY = boardHeight - dinoHeight;
    Block dino;

    int cactusX = 700;
    int cactusY = boardHeight - 70;
    ArrayList<Block> obstacles = new ArrayList<>();

    int velocityX = -12;
    int velocityY = 0;
    int gravity = 1;

    boolean gameOver = false;
    int score = 0;
    static int highScore = 0;

    Timer gameLoop;
    Timer spawnTimer;

    boolean ducking = false;
    boolean jumpRequested = false;

    public DinosaurChrome() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);

        dinoRunImg = img("./assets/dino-run.gif");
        dinoDeadImg = img("./assets/dino-dead.png");
        dinoJumpImg = img("./assets/dino-jump.png");
        dinoDuckImg = img("./assets/dino-duck.png");

        cactus1Img = img("./assets/cactus1.png");
        cactus2Img = img("./assets/cactus2.png");
        cactus3Img = img("./assets/cactus3.png");
        cactusBigImg = img("./assets/cactus-big.png");

        birdLowImg = img("./assets/bird-low.png");
        birdHighImg = img("./assets/bird-high.png");

        dino = new Block(dinoX, dinoY, dinoWidth, dinoHeight, dinoRunImg);

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        spawnTimer = new Timer(1500, e -> spawnObstacle());
        spawnTimer.start();
    }

    private Image img(String path) {
        return new ImageIcon(getClass().getResource(path)).getImage();
    }

    private void spawnObstacle() {
        if (gameOver) return;

        Random r = new Random();
        double chance = r.nextDouble();

        Block b;
        if (chance < 0.05) {
            b = new Block(cactusX, cactusY, 120, 100, cactusBigImg);
        } else if (chance < 0.20) {
            b = new Block(cactusX, boardHeight - 130, 92, 70, birdLowImg);
        } else if (chance < 0.35) {
            b = new Block(cactusX, boardHeight - 190, 92, 70, birdHighImg);
        } else if (chance < 0.55) {
            b = new Block(cactusX, cactusY, 102, 70, cactus3Img);
        } else if (chance < 0.75) {
            b = new Block(cactusX, cactusY, 69, 70, cactus2Img);
        } else {
            b = new Block(cactusX, cactusY, 34, 70, cactus1Img);
        }
        obstacles.add(b);

        if (obstacles.size() > 12) obstacles.remove(0);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        int drawH = ducking ? dinoDuckHeight : dinoHeight;
        Image drawImg = ducking ? dinoDuckImg : (dino.y == dinoY ? dinoRunImg : dinoJumpImg);
        g.drawImage(drawImg, dino.x, dino.y, dino.width, drawH, null);

        for (Block o : obstacles) {
            g.drawImage(o.img, o.x, o.y, o.width, o.height, null);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier", Font.PLAIN, 28));
        g.drawString("Score: " + score, 10, 30);
        g.drawString("HI: " + highScore, boardWidth - 150, 30);

        if (gameOver) {
            g.setFont(new Font("Courier", Font.BOLD, 36));
            g.drawString("Game Over - SPACE to restart", 80, boardHeight/2);
        }
    }

    private void move() {
        if (jumpRequested && dino.y == boardHeight - (ducking ? dinoDuckHeight : dinoHeight)) {
            velocityY = -17;
            jumpRequested = false;
        }
        velocityY += gravity;
        dino.y += velocityY;

        int targetY = boardHeight - (ducking ? dinoDuckHeight : dinoHeight);
        if (dino.y > targetY) {
            dino.y = targetY;
            velocityY = 0;
        }

        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Block o = obstacles.get(i);
            o.x += velocityX;

            int dinoH = ducking ? dinoDuckHeight : dinoHeight;
            if (collision(dino.x, dino.y, dino.width, dinoH, o.x, o.y, o.width, o.height)) {
                gameOver = true;
                dino.img = dinoDeadImg;
                if (score > highScore) highScore = score;
                spawnTimer.stop();
                gameLoop.stop();
                return;
            }

            if (o.x + o.width < 0) obstacles.remove(i);
        }

        if (!gameOver) score++;
    }

    private boolean collision(int ax, int ay, int aw, int ah, int bx, int by, int bw, int bh) {
        return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
    }

    @Override public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }
    }

    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameOver) restart();
            else if (dino.y == boardHeight - (ducking ? dinoDuckHeight : dinoHeight))
                jumpRequested = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (!gameOver && dino.y == boardHeight - dinoHeight)
                ducking = true;
        }
    }

    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) ducking = false;
    }

    @Override public void keyTyped(KeyEvent e) {}

    private void restart() {
        dino.y = boardHeight - dinoHeight;
        dino.img = dinoRunImg;
        velocityY = 0;
        obstacles.clear();
        score = 0;
        gameOver = false;
        ducking = false;
        jumpRequested = false;
        gameLoop.start();
        spawnTimer.start();
    }
}
