package com.patricia.frogger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Frog {
	private Batch batch;
	private int x;
	private int y;
	private int dx;
	private int dy;
	private int dyingSpeed;
	private boolean isDead;
	private Sprite frogSprite;
	private Texture frogImg;
	private Texture deathImg;
	private Sprite[] frogSprites;
	
	private int spriteCount;
	private int animationSpeed;
	private int animationCount;
	
	private boolean isMoving;
	
	public Frog (Batch batch) {
		this.batch = batch;
		
		frogImg = new Texture(Gdx.files.internal("sprites/frogSprites.png"));
		frogSprite = new Sprite(frogImg, 0, 0, 30, 32);

		frogSprites = new Sprite[3];
		frogSprites[0] = frogSprite;
		frogSprites[1] = new Sprite(frogImg, 120, 2, 30, 23);
		frogSprites[2] = new Sprite(frogImg, 62, 0, 25, 35);
		
		spriteCount = 0;
		animationSpeed = 3;
		animationCount = animationSpeed;
		
		deathImg = new Texture(Gdx.files.internal("sprites/death.png"));
		
		newFrog();
		
	}

	
	public int isAtHome (int[] homePositions) {
		if (y >= 600) {
			for (int i = 0; i < homePositions.length; i++) {
				if (((x) > (homePositions[i] - 20)) && ((x) < (homePositions[i] + 20))) {
					//newFrog();
					return i;
				}
			}
			//newFrog();
			return 5;
		}
		return 6;
	}
	
	public boolean isInWater () {
		if (y >= 390 && y <= 630) {
			return true;
		}
		
		return false;
	}
	
	public void newFrog () {
		x = 332;
		y = 65;
		isDead = false;
		dyingSpeed = 20;
		dx = 0;
		dy = 0;
		rotateSprites(0f);
		isMoving = false;
	}
	
	public boolean moveLeft () {
		if (!isMoving) {
			rotateSprites(90f);
			
			if (x - 72 > 0) {
				spriteCount = 2;
				dy = 0;
				dx = -24;
				x += dx;
				y += dy;
				return true;
			}
		}
		return false;
	}
	
	public boolean moveRight () {
		if (!isMoving) {
			rotateSprites(270f);
			
			if (x + 72 < 650){
				spriteCount = 2;
				dy = 0;
				dx = 24;
				x += dx;
				y += dy;
				return true;
			}
		}
		return false;
	}
	
	public boolean moveUp () {
		if (!isMoving) {
			rotateSprites(0f);
			
			if (y + 48 < 650){
				spriteCount = 2;
				dy = 16;
				dx = 0;
				x += dx;
				y += dy;
				return true;
			}
		}
		return false;
	}
	
	public boolean moveDown () {
		if (!isMoving) {
			rotateSprites(180f);
			
			if (y - 48 > 30){
				spriteCount = 2;
				dy = -16;
				dx = 0;
				x += dx;
				y += dy;
				return true;
			}
		}
		return false;
	}
	
	public void rotateSprites (float angle) {
		frogSprites[0].setRotation(angle);
		frogSprites[1].setRotation(angle);
		frogSprites[2].setRotation(angle);
	}
	
	public void move () {
		
		if (spriteCount > 0) {
			isMoving = true;
			//batch.begin();
			//frogSprites[spriteCount].draw(batch);
			//batch.end();
			animationCount--;
			if (animationCount == 0) {
				x += dx;
				y += dy;
				spriteCount--;
				animationCount = animationSpeed;
			}
		}
		else {
			dx = 0;
			dy = 0;
			isMoving = false;
		}
		frogSprites[spriteCount].setPosition(x, y);
		
	}
	
	public void draw () {
		batch.begin();
		frogSprites[spriteCount].draw(batch);
		batch.end();
	}
	
	public Sprite getSprite () {
		return frogSprite;
	}
	
	public int getX () {
		return x;
	}
	
	public int getY () {
		return y;
	}
	
	public int getWidth () {
		return Math.round(frogSprite.getWidth());
	}
	
	public void setOnLog (int speed) {
		x += speed;
	}
	
	public boolean getIsDead () {
		return isDead;
	}
	
	public void die(){
		isDead = true;
	}
	
	public boolean dying () {
		
		if(isDead){
			if (dyingSpeed > 0) {
				dyingSpeed--;
				batch.begin();
				batch.draw(deathImg, x, y);
				batch.end();	
			}
			else {
				newFrog();
			}
			return true;
			
		}
		return false;
		
	}
	
}
