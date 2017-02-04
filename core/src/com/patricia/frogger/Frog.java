package com.patricia.frogger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Frog {
	private Batch batch;
	private int x;
	private int y;
	Sprite frogSprite;
	Texture frogImg;
	
	public Frog (Batch batch) {
		this.batch = batch;
		newFrog();
		frogImg = new Texture(Gdx.files.internal("sprites/frog.png"));
		frogSprite = new Sprite(frogImg, 0, 0, 27, 29);
		
		
	}

	
	public int isAtHome (int[] homePositions) {
		if (y >= 600) {
			for (int i = 0; i < homePositions.length; i++) {
				if (((x) > (homePositions[i] - 20)) && ((x) < (homePositions[i] + 20))) {
					newFrog();
					return i;
				}
			}
			newFrog();
			return 5;
		}
		return 6;
	}
	
	public boolean isInWater () {
		if (y >= 390) {
			return true;
		}
		
		return false;
	}
	
	public void newFrog () {
		x = 332;
		y = 65;
	}
	
	public void moveLeft (boolean facingLeft, boolean facingDown) {
		frogSprite.setRotation(90f);
		if (x - 72 > 0) {
			x -= 72;
			
			/*if (!facingLeft){
				y += 20;
			}
			if (facingDown){
				y -= 20;
				x += 25;
			}*/
		}
	}
	
	public void moveRight (boolean facingLeft, boolean facingDown) {
		frogSprite.setRotation(270f);
		if (x + 72 < 650){
			x += 72;
			/*if (facingLeft){
				y -= 20;
			}
			if (facingDown){
				if (y > 80){
					y -= 20;
				}
				x += 25;
			}*/
		}
	}
	
	public boolean moveUp (boolean facingLeft, boolean facingDown) {
		frogSprite.setRotation(0f);
		if (y + 48 < 650){
			y += 48;
			/*if (facingLeft){
				y -= 20;
			}
			if (facingDown){
				y -= 20;
				x += 25;
			}*/
			return true;
		}
		return false;
	}
	
	public boolean moveDown (boolean facingLeft, boolean facingDown) {
		frogSprite.setRotation(180f);
		if (y - 48 > 0){
			y -= 48;
			/*if (facingLeft){
				y -= 20;
			}
			if (!facingDown){
				y += 20;
				x -= 25;
			}*/
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
}
