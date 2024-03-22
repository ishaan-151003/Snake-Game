import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    Tile food;
    int tileSize = 25;
    Random random;
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    public SnakeGame(int boardWidth, int boardHeight) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Drawing grid lines
        g.setColor(Color.gray);
        for (int i = 0; i < boardWidth; i += tileSize) {
            g.drawLine(i, 0, i, boardHeight);
        }
        for (int i = 0; i < boardHeight; i += tileSize) {
            g.drawLine(0, i, boardWidth, i);
        }

        // Drawing food
        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        // Drawing snake
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        // Drawing snake body
        for (Tile part : snakeBody) {
            g.fillRect(part.x * tileSize, part.y * tileSize, tileSize, tileSize);
        }

        // Drawing score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.drawString("Game Over: " + snakeBody.size(), tileSize - 16, tileSize);
        } else {
            g.drawString("Score: " + snakeBody.size(), tileSize - 16, tileSize);
        }
    }

    private boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    private void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    private void move() {
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            if (i == 0) {
                snakeBody.get(i).x = snakeHead.x;
                snakeBody.get(i).y = snakeHead.y;
            } else {
                snakeBody.get(i).x = snakeBody.get(i - 1).x;
                snakeBody.get(i).y = snakeBody.get(i - 1).y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (Tile part : snakeBody) {
            if (collision(snakeHead, part)) {
                gameOver = true;
                break;
            }
        }

        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize || snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (velocityY != 1) {
                    velocityX = 0;
                    velocityY = -1;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (velocityY != -1) {
                    velocityX = 0;
                    velocityY = 1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (velocityX != 1) {
                    velocityX = -1;
                    velocityY = 0;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (velocityX != -1) {
                    velocityX = 1;
                    velocityY = 0;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
