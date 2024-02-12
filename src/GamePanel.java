import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 100;
    BagliListe snake;
    int size;
    int appleX;
    int appleY;
    int bombX;
    int bombY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        size = 5;
        snake = new BagliListe();
        int tempx = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        int tempy = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        for(int i = 0;i<5;i++){
            snake.sonaEkle(tempx,tempy);
        }
        startGame();
    }
    public void startGame(){
        newApple();
        newBomb();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.GREEN);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.RED);
            g.fillOval(bombX,bombY,UNIT_SIZE,UNIT_SIZE);

            Node iter = snake.head;
            Color color = new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
            while(iter != null) {
                if (iter == snake.head) {
                    g.setColor(Color.green);
                    g.fillRect(iter.x, iter.y, UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(color);
                    g.fillRect(iter.x, iter.y, UNIT_SIZE, UNIT_SIZE);
                }
                iter = iter.next;
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Size: "+ size,(SCREEN_WIDTH-metrics.stringWidth("Score: "+ size))/2,g.getFont().getSize());
        }
        else {
            gameover(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
    }
    public void newBomb(){
        bombX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        bombY = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move() {
        Node iter = snake.tail;
        while (iter != snake.head) {
            iter.x = iter.prev.x;
            iter.y = iter.prev.y;
            iter = iter.prev;
        }
        switch (direction) {
            case 'U':
                snake.head.y = snake.head.y - UNIT_SIZE;
                break;
            case 'D':
                snake.head.y = snake.head.y + UNIT_SIZE;
                break;
            case 'L':
                snake.head.x = snake.head.x - UNIT_SIZE;
                break;
            case 'R':
                snake.head.x = snake.head.x + UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if( snake.head.x == appleX && snake.head.y == appleY ){
            snake.sonaEkle(snake.tail.x,snake.tail.y);
            size++;
            newApple();
        }
    }
    public void checkBomb(){
        if( snake.head.next.next.x == bombX && snake.head.next.next.y == bombY ){
            snake.aradanSil(snake.head.next.next);
            size--;
            newBomb();
        }
        if(size <= 3){
            running = false;
        }
    }
    public void checkCollisions() {
        if (snake.head.x < 0) {
            snake.head.x = SCREEN_WIDTH - UNIT_SIZE;
        }
        if (snake.head.x >= SCREEN_WIDTH) {
            snake.head.x = 0;
        }
        if (snake.head.y < 0) {
            snake.head.y = SCREEN_HEIGHT - UNIT_SIZE;
        }
        if (snake.head.y >= SCREEN_HEIGHT) {
            snake.head.y = 0;
        }

        Node iter = snake.tail;
        while (iter != snake.head) {
            if (snake.head.x == iter.x && snake.head.y == iter.y) {
                running = false;
            }
            iter = iter.prev;
        }

        if (!running) {
            timer.stop();
        }
    }
    public void gameover(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Size: "+ size,(SCREEN_WIDTH-metrics1.stringWidth("Score: "+ size))/2,g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH-metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkBomb();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
            }
        }
    }
}
