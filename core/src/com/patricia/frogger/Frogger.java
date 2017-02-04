package com.patricia.frogger;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Frogger extends ApplicationAdapter implements InputProcessor, ApplicationListener {
	SpriteBatch batch;
	
    Texture backgroundImg;
    TextureRegion background;
    Texture homeFrog;
    Texture flyImg;
	Texture winImg;
	Texture loseImg;
	Texture scoreImg;
	Texture frogImg;
	
	Sprite frogSprite;
	
	Frog frog;
	Fly fly;
	ArrayList<Car> cars;
	ArrayList<Log> logs;
	
	Sound hop;
	ShapeRenderer shapeRenderer;
	
	int time;
	int ticks;
	int points;
	int lives;
	boolean facingLeft;
	boolean facingDown;
	boolean[] wins;
	int[] positions = {30, 174, 318, 462, 606};
	Texture[] numbers;
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		frog = new Frog();
		cars = createCarLane(1, 3);
		cars.addAll(createCarLane(2, 3));
		cars.addAll(createCarLane(3, 2));
		cars.addAll(createCarLane(4, 3));
		cars.addAll(createCarLane(5, 2));
		//logs = createLogLane(1);
		//logs.addAll(createLogLane(2));
		//logs.addAll(createLogLane(3));
		//logs.addAll(createLogLane(4));
		//logs.addAll(createLogLane(5));
		
		fly = new Fly();
		flyImg = new Texture(Gdx.files.internal("fly.png"));
		homeFrog = new Texture(Gdx.files.internal("homeFrog.png"));
		winImg = new Texture(Gdx.files.internal("win.png"));
		loseImg = new Texture(Gdx.files.internal("lose.png"));
		scoreImg = new Texture(Gdx.files.internal("score.png"));
		
		backgroundImg = new Texture(Gdx.files.internal("background.png"));
		background = new TextureRegion(backgroundImg, 0, 0, 690, 785);
		
		
		frogImg = new Texture(Gdx.files.internal("frogSprites.png"));
		frogSprite = new Sprite(frogImg, 20, 20, 50, 50);
		//frogSprite.setRotation(90);
		
		hop = Gdx.audio.newSound(Gdx.files.internal("sound-frogger-hop.wav"));
                
		ticks = 0;
		time = 120;
		points = 0;
		lives = 3;
		facingLeft = false;
		facingDown = false;
		wins = new boolean[5];
		
		numbers = new Texture[10];
		for (int i = 0; i < numbers.length; i++){
		    String fileName = i + ".png";
		    numbers[i] = new Texture(Gdx.files.internal(fileName));
		}
		
		Gdx.input.setInputProcessor(this);
		
		
	}

	public void update () {
		
		if (Gdx.input.isKeyJustPressed(Keys.LEFT)){
			frog.moveLeft(facingLeft, facingDown);
			frogSprite.setRotation(90f);
			hop.play();
			facingLeft = true;
			facingDown = false;
		}
		
		else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)){
			frog.moveRight(facingLeft, facingDown);
			frogSprite.setRotation(270f);
			hop.play();
			facingLeft = false;
			facingDown = false;
		}
		
		else if (Gdx.input.isKeyJustPressed(Keys.UP)){
			if (frog.moveUp(facingLeft, facingDown)) {
				points += 10;
			}
			frogSprite.setRotation(0f);
			hop.play();
			facingLeft = false;
			facingDown = false;
		}
		
		else if (Gdx.input.isKeyJustPressed(Keys.DOWN)){
			if (frog.moveDown(facingLeft, facingDown)) {
				points -= 10;
			}
			frogSprite.setRotation(180f);
			hop.play();
			facingLeft = false;
			facingDown = true;
		}
		//System.out.println("Y:" + frog.getY());
		
		int endResult = frog.isAtHome(positions);
		if (endResult < 5) {
			points += 50;
			if ((fly.getPosition()-30) / 144 == endResult) {
				points += 200;
			}
			fly.removePosition(endResult);
			wins[endResult] = true;
			
			reset();
		}
		else if (endResult == 5){
			lives--;
		}
		
		if (ticks > 200) {
			fly.remove();
		}
		
		checkWin();
		for (Car c: cars) {
			c.draw();
			if (c.collide(frogSprite)) {
				frog.newFrog();
				//lives--;
				//System.out.println("DEATH TO FROGS");
			}
		}
		/*for (Log l: logs) {
			l.draw();
			if (l.collide(frogSprite)) {
				frog.newFrog();
				//lives--;
				//System.out.println(frogSprite.getX());
			}
		}*/
		ticks++;
		//System.out.println(points);
		frogSprite.setPosition(frog.getX(), frog.getY());
		
		
		//frog.drowned();
	}
	
	@Override
	public void render () {
		try{
		Thread.sleep(33);
		
		
		if (!isGameOver()){
			batch.begin();
			batch.draw(background, 0, 0);
		    batch.draw(background, 0, Gdx.graphics.getHeight());
			batch.end();
			
			update();
			//shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
			
			batch.begin();
			frogSprite.draw(batch);
	        //batch.draw(frogSprite, frog.getX(), frog.getY());
	        batch.draw(flyImg, fly.getPosition(), 630);
	        for (int i = 0; i < wins.length; i++) {
	        	if (wins[i]) {
	        		batch.draw(homeFrog, positions[i], 630);
	        	}
	        }
	        for (int i = 0; i < lives - 1; i ++) {
	        	batch.draw(frogImg, i * 40, -3);
	        }
	        
	        drawScore(120, 720);
	        batch.end();
	        
	        /*shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.identity();
            shapeRenderer.rect(0, 0, 300, 20);
            shapeRenderer.end();*/
	        
	        if (ticks == 400) {
	        	reset();
	        }
		}
		else {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			if (checkWin()) {
				batch.begin();
		        batch.draw(winImg, 270, 370);
		        batch.end();
			}
			else {
				batch.begin();
		        batch.draw(loseImg, 270, 370);
		        batch.end();
			}
			batch.begin();
	        batch.draw(scoreImg, 220, 340);
	        drawScore(360, 342);
	        batch.end();
		}
		}catch(InterruptedException  ex){
			Thread.currentThread().interrupt();
		}
	}
	
	public void drawScore (int xDisplace, int y) {
		for (int i = 0; i < Integer.toString(points).length(); i++) {
        	batch.draw(numbers[Integer.parseInt(Integer.toString(points).substring(i, i + 1))], i * 20 + xDisplace, y);
        }
	}
	
	public boolean isGameOver () {
		if (checkWin() || lives == 0) {
			return true;
		}
		return false;
	}
	
	public boolean checkWin () {
		for (boolean w: wins) {
			if (!w) {
				return false;
			}
		}
		//wins = new boolean[5];
		points += 1000;
		return true;
	}
	
	public void reset () {
		ticks = 0;
    	fly.randomizePosition();
	}
	
	public ArrayList<Car> createCarLane (int laneNum, int carNum) {
		ArrayList<Car> lane = new ArrayList<Car>();
		for (int i = 0; i < carNum; i++) {
			Car car = new Car(laneNum, laneNum * 47 + 52, batch, i);
			lane.add(car);
		}
		return lane;
	}
	
	public ArrayList<Log> createLogLane (int laneNum) {
		ArrayList<Log> lane = new ArrayList<Log>();
		for (int i = 0; i < 3; i++) {
			Log log = new Log(laneNum, laneNum * 47 + 362, batch, i);
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
