import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

	static final int WIDTH = 600;
	static final int HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (WIDTH*HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int food;
	int foodX;
	int foodY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		nextFood();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		
		if(running) {
			for(int i=0;i<HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, WIDTH, i*UNIT_SIZE);
			}
				g.setColor(Color.red);
				g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
	
			for(int i=0;i< bodyParts;i++) {
			if(i==0) {
				g.setColor(Color.green);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}else {
				g.setColor(new Color(45,110,0));
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		}
			g.setColor(Color.red);
			g.setFont(new Font("MV Boli",Font.BOLD,75));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + food, (WIDTH - metrics.stringWidth("Score: " + food))/2,
				g.getFont().getSize());
	}
		else {
			gameOver(g);
		}
}
	public void nextFood() {
		foodX = random.nextInt((int)(WIDTH/UNIT_SIZE))*UNIT_SIZE;
		foodY = random.nextInt((int)(HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		for(int i= bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
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
	public void checkEat() {
		if((x[0] == foodX) && (y[0] == foodY)) {
			bodyParts++;
			food++;
			nextFood();
		}
	}
	public void checkColission() {
		//sprawdzanie czy glowa uderza w ciało
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&&(y[0] == y[i])) {
				running = false;
			}
		}
		//sprawdzanie czy glowa dotyka lewej sciany
		if(x[0] < 0) {
			running = false;
		}
		//sprawdzanie czy glowa dotyka prawej sciany
		if(x[0] > WIDTH) {
			running = false;
		}
		//sprawdzanie czy glowa dotyka gornej sciany
		if(y[0] < 0) {
			running = false;
		}
		//sprawdzanie czy glowa dotyka prawej sciany
		if(y[0] > HEIGHT) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("MV Boli",Font.BOLD,75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + food, (WIDTH - metrics.stringWidth("Score: " + food))/2,
			g.getFont().getSize());

		g.setColor(Color.red);
		g.setFont(new Font("MV Boli",Font.BOLD,75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (WIDTH - metrics1.stringWidth("Game Over"))/2,
			HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	if(running) {
		move();
		checkEat();
		checkColission();
	}
	repaint();
	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}
