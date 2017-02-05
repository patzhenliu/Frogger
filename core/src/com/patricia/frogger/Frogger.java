//Frogger.java
//Patricia Liu
//Contain all the game rules, logic, and interaction with user

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
	Crocodile crocodile;
	ArrayList<Car> cars;
	ArrayList<Log> logs;
	
	Sound hop;
	Sound plunk;
	Sound squash;
	Sound timesUp;
	Music menuMusic;
	Random rand = new Random();
	
	int time;
	int crocTime;
	int flyTicks;
	int crocTicks;
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
	public void create() {
		batch = new SpriteBatch();
		
		frog = new Frog(batch);
		fly = new Fly(batch);
		crocodile = new Crocodile(batch, levelNum);
		
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
        
		crocTime = rand.nextInt(500) + 600; //amount of ticks before croc reappears
		onStartMenu = true;
		highScore = 0;
		resetGame();
		levelNum = 1;
		
		redNums = new Texture[10];
		whiteNums = new Texture[10];
		for(int i = 0; i < redNums.length; i++){
		    String fileName = "text/" + i + ".png";
		    redNums[i] = new Texture(Gdx.files.internal(fileName));
		    fileName = "text/" + i + "w.png";
		    whiteNums[i] = new Texture(Gdx.files.internal(fileName));
		}
		
		Gdx.input.setInputProcessor(this);
		
		
	}	

	public void update() {
		//frog movement and death
		//crocodile and fly movement
		//car and log movement
		
		if(frog.dying()) {
			//nothing else happens while frog is dead
			 return;
		}
		
		moveFrog();
		checkFrogAtHome();
		removeHomeObstacles();

		checkWin();
		resetTicks();
        checkTime();
        
        for(Car c: cars) {
			c.draw();
			carCollision(c);
		}
		
		boolean collided = false;
		for(Log l: logs) {
			l.draw();
			collided = logCollision(l);		
		}
		
		if(!collided && frog.isInWater()){
			resetRound(true, true);
		}
		
		if(frog.leftScreen()) {
			resetRound(true, true);
		}
		
		drawEntities();
		checkScore();
			
		flyTicks++;
		crocTicks++;
		time--;

	}
	
	@Override
	public void render() {
		//draws all text, start menu, losing screen
		try{
			Thread.sleep(33);
		
			if(onStartMenu) {
				startMenu();
			}
			
			else if(lives > 0 || frog.getIsDead()){
				if(checkWin()) {
					resetLevel();
				}
				
				drawBackground();
				update();
				drawText();
				
		        
			}
			else {
		        loseScreen();
	        
			}
		}
		catch(InterruptedException  ex) {
			Thread.currentThread().interrupt();
		}
	}
	
	public void moveFrog() {
		if(Gdx.input.isKeyJustPressed(Keys.LEFT)){
			if(frog.moveLeft()) {			
				hop.play();
			}
		}
		
		else if(Gdx.input.isKeyJustPressed(Keys.RIGHT)){
			 if(frog.moveRight()) {
				 hop.play();
			 }
		}
		
		else if(Gdx.input.isKeyJustPressed(Keys.UP)){
			if(frog.moveUp()) {
				points += 10;
				hop.play();
			}
			
		}
		
		else if(Gdx.input.isKeyJustPressed(Keys.DOWN)){
			if(frog.moveDown()) {
				points -= 10;
				hop.play();
			}
			
		}
		
		frog.move();
	}
	
	public void drawEntities() {
		//draw animals in the game
		frog.draw();
		
		if(fly.getPosition() != crocodile.getPosition()) { //fly and croc can't show up in the same spot
			fly.draw();
			crocodile.draw();
		}
		else {
			crocodile.randomizePosition(levelNum);
		}
		
		drawHomeFrogs();
		drawFrogLives();
	}
	
	public void drawHomeFrogs() {
		batch.begin();
        for(int i = 0; i < wins.length; i++) {
        	if(wins[i]) {
        		batch.draw(homeFrog, positions[i], 630);
        	}
        }
        batch.end();
	}
	
	public void drawFrogLives() {
		batch.begin();
		for(int i = 0; i < lives - 1; i ++) {
        	batch.draw(lifeImg, i * 45 + 18, 10);
        }
        batch.end();
	}
	
	public void drawBackground() {
		batch.begin();
		batch.draw(background, 0, 0);
	    batch.draw(background, 0, Gdx.graphics.getHeight());
		batch.end();
	}
	
	public void drawText() {
		//draws all text (numbers) present on screen
		batch.begin();
		batch.draw(levelImg, 120, 20);
		drawNum(230, 23, levelNum, false);
        drawNum(540, 13, time/30, false);
        drawNum(120, 720, points, true);
        drawNum(290, 720, highScore, true);
        batch.end();
	}
	
	public void drawScore(int xDisplace, int y) {
		drawNum(xDisplace, y, points, true);
	}
	
	public void drawNum(int xDisplace, int y, int num, boolean red) {
		//draws numbers
		for(int i = 0; i < Integer.toString(num).length(); i++) {
			if(red) {
				batch.draw(redNums[Integer.parseInt(Integer.toString(num).substring(i, i + 1))], i * 20 + xDisplace, y);
			}
			else {
				batch.draw(whiteNums[Integer.parseInt(Integer.toString(num).substring(i, i + 1))], i * 20 + xDisplace, y);
			}
        }
	}
	
	public void startMenu() {
		//draws start menu
		//checks if player hits ENTER - play
		drawBackground();
		batch.begin();
        batch.draw(titleImg, 100, 733);
        batch.draw(playImg, 105, 125);
        batch.end();
        
		if(Gdx.input.isKeyPressed(Keys.ENTER)) {
			menuMusic.dispose();
			onStartMenu = false;
		}
	}
	
	public void loseScreen() {
		//draws screen when player loses
		//checks if player hits ENTER - play again
		drawBackground();
		batch.begin();
		batch.draw(titleImg, 100, 733);
	    batch.draw(loseImg, 270, 500);
        batch.draw(scoreImg, 220, 470);
        drawScore(380, 472);
        batch.draw(playAgainImg, 105, 125);
        batch.end();
        
        if(Gdx.input.isKeyJustPressed(Keys.ENTER)){
        	resetGame();
        }
	}
	
	public void checkFrogAtHome() {
		//checks if frog has reached the end
		//checks if frog is in the same position as fly or croc
		int homeIndex = frog.getHomePos(positions);
		if(homeIndex < 5) {
			if(wins[homeIndex]) {
				lives--;
			}
			else if((crocodile.getPosition()-30) / 144 == homeIndex) {
				resetRound(true, false);
			}
			else{
				points += 50;
				points += time/30 * 10;
				eatFly(homeIndex);
				fly.removePosition(homeIndex);
				crocodile.setPositionArray(fly.getPositionArray());
				wins[homeIndex] = true;
				
				resetRound(false, true);
			}
		}
		else if(homeIndex == 5){
			//missed the home position
			resetRound(true, true);
		}
	}
	
	public boolean checkWin() {
		//check if all homes are filled
		for(boolean w: wins) {
			if(!w) {
				return false;
			}
		}
		points += 1000;
		return true;
	}
	
	public void checkScore() {
		if(points > highScore) {
			highScore = points;
		}
	}
	
	public void checkTime() {
		//checks if close to/is times up
		if(time == 30) {
        	timesUp.play();
        }
        if(time == 0) {
        	resetRound(true, false);
        }
	}
	
	public void carCollision(Car c) {
		//frog dies when colliding with car
		if(c.collide(frog)) {
			resetRound(true, false);
		}
	}
	
	public boolean logCollision(Log l) {
		//frog follows log on collision
		if(l.collide(frog)) {
			frog.setOnLog(l.getSpeed());
			return true;
		}	
		return false;
	}
	
	public void eatFly(int index) {
		if((fly.getPosition()-30) / 144 == index) {
			points += 200;
		}
	}
	
	public void resetTicks() {
		//resets fly and croc ticks when each reaches a specified value
		if(flyTicks == 400) {
        	resetFlyTicks();
        }
        
        if(crocTicks == crocTime) {
        	crocTime = rand.nextInt(500) + 600;
        	resetCrocTicks();
        }
	}
	
	public void resetFlyTicks() {
		flyTicks = 0;
    	fly.randomizePosition();

	}
	
	public void resetCrocTicks() {
		crocTicks = 0;
    	crocodile.randomizePosition(levelNum);

	}
	
	public void resetRound(boolean isDead, boolean inWater) {
		//resets variables for the round
		//takes care of frog death
		
		if(isDead) {
			playDeathSound(inWater);
			frog.die();			
			lives--;
		}
		else {
			frog.newFrog();
		}
		resetFlyTicks();
		crocTicks = 501; //crocodile doesn't appear right when the round begins
		time = 60 * 30;
	}
	
	
	public void resetLevel() {
		//resets all variables
		//points remains the same, level increases by one
		resetRound(false, true);
		lives = 3;
		levelNum++;
		wins = new boolean[5];
		
		createRoadObstacles();
		
	}
	
	public void resetGame() {
		//resets all variables
		//high score remains the same
		resetLevel();
		points = 0;
		levelNum= 1;
		
	}
	
	public void playDeathSound(boolean inWater) {
		if (inWater) {
			plunk.play();
		}
		else {
			squash.play();
		}
	}
	
	public void removeHomeObstacles() {
		//checks if fly and crocodile should be removed
		if(flyTicks > 200) {
			fly.remove();
		}
		if(crocTicks > 500) {
			crocodile.remove();
		}
	}
	
	public void createRoadObstacles() {
		//creates all cars and logs
		cars = createCarLane(1);
		for(int i = 2; i < 6; i++) {
			cars.addAll(createCarLane(i));
		}
		
		logs = createLogLane(1);
		for(int i = 2; i < 6; i++) {
			logs.addAll(createLogLane(i));
		}
	}
	
	public ArrayList<Car> createCarLane(int laneNum) {
		//create car objects
		ArrayList<Car> lane = new ArrayList<Car>();
		int carNum = 2; //cars per lane
		int carSpeed = rand.nextInt(3) + 2; //random speed
		for(int i = 0; i < carNum; i++) {
			Car car = new Car(laneNum, laneNum * 47 + 63, batch, i, carSpeed);
			lane.add(car);
		}
		return lane;
	}
	
	public ArrayList<Log> createLogLane(int laneNum) {
		//create log objects
		ArrayList<Log> lane = new ArrayList<Log>();
		int logNum = 2; //logs per lane
		int logSpeed = rand.nextInt(3) + 2; //random speed
		int logLength = rand.nextInt(3) + 4; //random length
		for(int i = 0; i < logNum; i++) {
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
