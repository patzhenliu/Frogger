package com.patricia.frogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Frogger extends ApplicationAdapter implements InputProcessor, ApplicationListener {
	SpriteBatch batch;
	
    Texture backgroundImg;
    TextureRegion background;
    Texture homeFrog;    
	Texture loseImg;
	Texture levelImg;
	Texture scoreImg;
	Texture lifeImg;
	Texture titleImg;
	Texture playImg;
	Texture playAgainImg;
	
	Frog frog;
	Fly fly;
	ArrayList<Car> cars;
	ArrayList<Log> logs;
	
	Sound hop;
	Sound plunk;
	Sound squash;
	Sound timesUp;
	Music menuMusic;
	Random rand = new Random();
	
	int time;
	int ticks;
	int levelNum;
	int points;
	int highScore;
	int lives;
	boolean onStartMenu;
	boolean[] wins;
	int[] positions = {30, 174, 318, 462, 606};
	Texture[] redNums;
	Texture[] whiteNums;
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		frog = new Frog(batch);
		fly = new Fly(batch);
		
		homeFrog = new Texture(Gdx.files.internal("sprites/homeFrog.png"));
		lifeImg = new Texture(Gdx.files.internal("sprites/life.png"));
		loseImg = new Texture(Gdx.files.internal("text/lose.png"));
		levelImg = new Texture(Gdx.files.internal("text/level.png"));
		scoreImg = new Texture(Gdx.files.internal("text/score.png"));
		titleImg = new Texture(Gdx.files.internal("text/title.png"));
		playImg = new Texture(Gdx.files.internal("text/play.png"));
		playAgainImg = new Texture(Gdx.files.internal("text/playAgain.png"));
		
		
		backgroundImg = new Texture(Gdx.files.internal("background.png"));
		background = new TextureRegion(backgroundImg, 0, 0, 690, 785);

		
		hop = Gdx.audio.newSound(Gdx.files.internal("sounds/sound-frogger-hop.wav"));
		plunk = Gdx.audio.newSound(Gdx.files.internal("sounds/sound-frogger-plunk.wav"));
		squash = Gdx.audio.newSound(Gdx.files.internal("sounds/sound-frogger-squash.wav"));
		timesUp = Gdx.audio.newSound(Gdx.files.internal("sounds/sound-frogger-time.wav"));
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/frogger-music.mp3"));
		menuMusic.play();
        
		
		onStartMenu = true;
		highScore = 0;
		resetGame();
		
		redNums = new Texture[10];
		whiteNums = new Texture[10];
		for (int i = 0; i < redNums.length; i++){
		    String fileName = "text/" + i + ".png";
		    redNums[i] = new Texture(Gdx.files.internal(fileName));
		    fileName = "text/" + i + "w.png";
		    whiteNums[i] = new Texture(Gdx.files.internal(fileName));
		}
		
		Gdx.input.setInputProcessor(this);
		
		
	}
	
	public void moveFrog() {
		if (Gdx.input.isKeyJustPressed(Keys.LEFT)){
			frog.moveLeft();			
			hop.play();
		}
		
		else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)){
			frog.moveRight();
			hop.play();
		}
		
		else if (Gdx.input.isKeyJustPressed(Keys.UP)){
			if (frog.moveUp()) {
				points += 10;
			}
			hop.play();
		}
		
		else if (Gdx.input.isKeyJustPressed(Keys.DOWN)){
			if (frog.moveDown()) {
				points -= 10;
			}
			hop.play();
		}
	}

	public void update () {
		
		if (frog.dying()) {
			 return;
		}
		
		moveFrog();
		
		int endResult = frog.isAtHome(positions);
		if (endResult < 5) {
			if (wins[endResult]) {
				lives--;
			}
			else{
				points += 50;
				points += time/30 * 10;
				if ((fly.getPosition()-30) / 144 == endResult) {
					points += 200;
				}
				fly.removePosition(endResult);
				wins[endResult] = true;
				
				resetRound(false);
			}
		}
		else if (endResult == 5){
			plunk.play();
			resetRound(true);
			//lives--;
		}
		
		if (ticks > 200) {
			fly.remove();
		}
		
		frog.move();
		
		checkWin();
        
        for (Car c: cars) {
			c.draw();
			if (c.collide(frog)) {
				squash.play();
				resetRound(true);
			}
		}
		
		boolean collided = false;
		for (Log l: logs) {
			l.draw();
			if (l.collide(frog)) {
				frog.setOnLog(l.getSpeed());
				collided = true;
			}			
		}
		if (!collided && frog.isInWater()){
			plunk.play();
			resetRound(true);
		}
		
		if (frog.getX() < 0 - frog.getWidth()*3 || frog.getX() > 690 - frog.getWidth()) {
			plunk.play();
			resetRound(true);
		}
		
		frog.draw();
		fly.draw();
		
		batch.begin();
        for (int i = 0; i < wins.length; i++) {
        	if (wins[i]) {
        		batch.draw(homeFrog, positions[i], 630);
        	}
        }
        
        for (int i = 0; i < lives - 1; i ++) {
        	batch.draw(lifeImg, i * 45 + 18, 10);
        }
        batch.end();
		
		
		if (points > highScore) {
			highScore = points;
		}
		ticks++;
		time--;

	}
	
	@Override
	public void render () {
		try{
			Thread.sleep(33);
		
			if (onStartMenu) {
				startMenu();
			}
			
			else if (lives > 0 || frog.getIsDead()){
				if (checkWin()) {
					resetLevel();
				}
				
				drawBackground();
				
				update();

				
				batch.begin();
		        drawNum(540, 13, time/30, false);
		        drawNum(120, 720, points, true);
		        drawNum(290, 720, highScore, true);
		        
		        batch.draw(levelImg, 120, 20);
		        drawNum(230, 23, levelNum, false);
		        batch.end();
		        
		        
		        if (ticks == 400) {
		        	resetTicks();
		        }
		        
		        if (time == 0) {
		        	timesUp.play();
		        	resetRound(true);
		        }
			}
			else {
		        loseScreen();
	        
			}
		}
		catch (InterruptedException  ex) {
			Thread.currentThread().interrupt();
		}
	}
	
	public void startMenu () {
		drawBackground();
		batch.begin();
        batch.draw(titleImg, 100, 733);
        batch.draw(playImg, 105, 125);
        batch.end();
        
		if (Gdx.input.isKeyPressed(Keys.ENTER)) {
			menuMusic.dispose();
			onStartMenu = false;
		}
	}
	
	public void loseScreen () {
		drawBackground();
		batch.begin();
		batch.draw(titleImg, 100, 733);
	    batch.draw(loseImg, 270, 500);
        batch.draw(scoreImg, 220, 470);
        drawScore(380, 472);
        batch.draw(playAgainImg, 105, 125);
        batch.end();
        
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)){
        	resetGame();
        }
	}
	
	public void drawBackground() {
		batch.begin();
		batch.draw(background, 0, 0);
	    batch.draw(background, 0, Gdx.graphics.getHeight());
		batch.end();
	}
	
	public void drawScore (int xDisplace, int y) {
		drawNum(xDisplace, y, points, true);
	}
	
	public void drawNum (int xDisplace, int y, int num, boolean red) {
		for (int i = 0; i < Integer.toString(num).length(); i++) {
			if (red) {
				batch.draw(redNums[Integer.parseInt(Integer.toString(num).substring(i, i + 1))], i * 20 + xDisplace, y);
			}
			else {
				batch.draw(whiteNums[Integer.parseInt(Integer.toString(num).substring(i, i + 1))], i * 20 + xDisplace, y);
			}
        }
	}
	
	
	public boolean checkWin () {
		for (boolean w: wins) {
			if (!w) {
				return false;
			}
		}
		points += 1000;
		return true;
	}
	
	public void resetRound(boolean isDead) {
		
		if (isDead) {
			frog.die();			
			lives--;
		}
		time = 60 * 30;
	}
	
	public void resetTicks () {
		ticks = 0;
    	fly.randomizePosition();

	}
	
	public void resetLevel() {
		ticks = 0;
		time = 60 * 30;
		lives = 3;
		levelNum++;
		wins = new boolean[5];
		
		createObstacles();
		
	}
	
	public void resetGame() {
		resetLevel();
		points = 0;
		levelNum= 1;
		
	}
	
	public void createObstacles () {
		cars = createCarLane(1);
		cars.addAll(createCarLane(2));
		cars.addAll(createCarLane(3));
		cars.addAll(createCarLane(4));
		cars.addAll(createCarLane(5));
		logs = createLogLane(1);
		logs.addAll(createLogLane(2));
		logs.addAll(createLogLane(3));
		logs.addAll(createLogLane(4));
		logs.addAll(createLogLane(5));
	}
	
	
	public ArrayList<Car> createCarLane (int laneNum) {
		ArrayList<Car> lane = new ArrayList<Car>();
		int carNum = rand.nextInt(1) + 2;
		int carSpeed = rand.nextInt(3) + 2;
		for (int i = 0; i < carNum; i++) {
			Car car = new Car(laneNum, laneNum * 47 + 63, batch, i, carSpeed);
			lane.add(car);
		}
		return lane;
	}
	
	public ArrayList<Log> createLogLane (int laneNum) {
		ArrayList<Log> lane = new ArrayList<Log>();
		int logNum = rand.nextInt(1) + 2;
		int logSpeed = rand.nextInt(3) + 2;
		int logLength = rand.nextInt(3) + 4;
		for (int i = 0; i < logNum; i++) {
			Log log = new Log(laneNum, laneNum * 47 + 362, batch, i, logSpeed, logLength);
			lane.add(log);
		}
		return lane;
	}
	
	@Override
    public void dispose() {
        batch.dispose();
    }
	
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
