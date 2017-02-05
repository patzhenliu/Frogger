package com.patricia.frogger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Frog {
	private Batch batch;
	private int x;
	private int y;
	private int dyingSpeed;
	private boolean isDead;
	private Sprite frogSprite;
	private Texture frogImg;
	private Texture deathImg;
	
	public Frog (Batch batch) {
		this.batch = batch;
		newFrog();
		frogImg = new Texture(Gdx.files.internal("sprites/frog.png"));
		frogSprite = new Sprite(frogImg, 0, 0, 27, 29);
		deathImg = new Texture(Gdx.files.internal("sprites/death.png"));
		
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
	}
	
	public void moveLeft () {
		frogSprite.setRotation(90f);
		if (x - 72 > 0) {
			x -= 72;
		}
	}
	
	public void moveRight () {
		frogSprite.setRotation(270f);
		if (x + 72 < 650){
			x += 72;
		}
	}
	
	public boolean moveUp () {
		frogSprite.setRotation(0f);
		if (y + 48 < 650){
			y += 48;
			return true;
		}
		return false;
	}
	
	public boolean moveDown () {
		frogSprite.setRotation(180f);
		if (y - 48 > 20){
			y -= 48;
			return true;
		}
		return false;
	}
	
	public void move () {
		frogSprite.setPosition(x, y);
	}
	
	public void draw () {
		batch.begin();
		frogSprite.draw(batch);
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
