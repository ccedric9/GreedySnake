import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener{
    static final int SCREEN_WIDTH =600;
    static final int SCREEN_HEIGHT =600;
    static final int UNIT_SIZE =40;
    static final int GAME_UNITS = SCREEN_WIDTH * SCREEN_HEIGHT /UNIT_SIZE;
    static final int DELAY = 100;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int appleX,appleY;
    int applesEaten = 0;
    boolean running = false;
    char direction = 'R';
    Timer timer;
    Random random;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_HEIGHT,SCREEN_WIDTH));
        this.setBackground(Color.black);
        this.addKeyListener(new MyKeyAdapter());
        this.setFocusable(true);
        startGame();

    }
    
    
    public void startGame(){
        running = true;
        newApple();
        timer = new Timer(DELAY, this);
        timer.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if (running){
        	 g.setColor (Color.red);
             g.setFont(new Font("Ink Free",Font.BOLD,40));
             FontMetrics metrics1 = getFontMetrics(g.getFont());
             g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH-metrics1.stringWidth("Score: "+ applesEaten))/2,g.getFont().getSize());
        /*
             //draw grid 
            for (int i =0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
                g.setColor(Color.yellow);
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH*i,i*UNIT_SIZE);
            }
            */
        // draw apple
            g.setColor(Color.red);
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
        
        //draw snake's body
            for (int i=0;i<bodyParts;i++){
                // i==0 is snake's head - set a different color to body parts
                if(i==0){ 
                    g.setColor(new Color(155,215,0));
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }
            }
        }
        else{
            gameOver(g);
        }
    }

    public void move(){
        for (int i = bodyParts;i>0; i--){
            // head is 0 and int bodyParts is tail --tail take over the position of last part of body;
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        //check direction
        switch(direction){
            case 'U':
            y[0] = y[0] - UNIT_SIZE;
            break;
            case 'D':
            y[0] = y[0] + UNIT_SIZE;
            break;
            case 'L':
            x[0] = x[0] - UNIT_SIZE;
            break;
            case 'R':
            x[0] = x[0] + UNIT_SIZE;
            break;
        }

    }

    public void newApple(){
        appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;

    }

    public void checkApple(){
        if (x[0]==appleX && y[0]==appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }
    public void checkCollisions(){
        // hit its own body
        for (int i= bodyParts;i>0 ; i--){
            if (x[i]==x[0] && y[i]==y[0]) running = false;
        }
        // check if touches the border
        if (x[0]<0 || x[0]>SCREEN_WIDTH) running = false;
        if (y[0]<0 || y[0]>SCREEN_HEIGHT) running =false;
        if (!running) timer.stop(); 
    }

    public void gameOver(Graphics g){
        //display score in center top
        g.setColor (Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH-metrics1.stringWidth("Score: "+ applesEaten))/2,g.getFont().getSize());
        //display GameOver text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,70));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH-metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);

    }
    @Override
    public void actionPerformed (ActionEvent e){
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
                case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
                case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
                case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;

            }
        }

    }



}
