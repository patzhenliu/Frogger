//Log.java
//Patricia Liu
////Contains all information and methods regarding the logs

package com.patricia.frogger;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Log {
	private Batch batch;
	private Texture logImg;
	private Sprite logSprite;
	private int speed;
	private boolean headingRight;
	private int x;
	private int y;
	private Random rand = new Random(System.currentTimeMillis() );
	
	public Log(int laneNum, int y, Batch batch, int logNum, int speed, int length) {
		this.speed = speed; // pixel per frame
		this.y = y;
		this.batch = batch;
		logImg = new Texture(Gdx.files.internal("sprites/log.png"));
		logSprite = new Sprite(logImg);
		logSprite.setSize(length * 21, logSprite.getHeight());
		
		int randomSpace = rand.nextInt(100) + 200; //determines the amount of space between each log
		
		headingRight =(laneNum == 2 || laneNum == 4); //logs in the specified lanes go right
		
		if(headingRight){
			x = -10 + randomSpace * logNum;
		}else{
			this.speed *= -1;
			x = 800 - randomSpace * logNum;
		}
	}
	
	private void move() {
		x += speed;
		if(x < -10 || x > 800) {
			if(headingRight) {
				x = -10;
			}
			else {
				x = 800;
			}
		}
	}
	
	public void draw() {
		batch.begin();
		logSprite.setPosition(x, y);
		logSprite.draw(batch);
		batch.end();
		move();
	}
	
	public boolean collide(Frog frog){
		Sprite frogSprite = frog.getSprite();
		Rectangle rect = new Rectangle(frogSprite.getX(), frogSprite.getY(), frogSprite.getWidth()/2, frogSprite.getHeight());
		Rectangle logRect = new Rectangle(logSprite.getX(), logSprite.getY(), logSprite.getWidth(), logSprite.getHeight() );
		return rect.overlaps(logRect);
	}
	
	public int getSpeed() {
		return speed;
	}
}
