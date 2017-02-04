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
	private boolean headingRight; //true = right. false=left
	private int x;
	private int y;
	Random rand = new Random(System.currentTimeMillis() );
	
	public Log (int laneNum, int y, Batch batch, int logNum, int speed, int length) {
		this.speed = speed; // pixel per frame
		this.y = y;
		this.batch = batch;
		logImg = new Texture(Gdx.files.internal("sprites/log.png"));
		logSprite = new Sprite(logImg);
		logSprite.setSize(length * 21, logSprite.getHeight());
		
		int randomSpace = rand.nextInt(100) + 200;
		
		headingRight = (laneNum == 2 || laneNum == 4);		
		
		if(headingRight){
			x = -10 + randomSpace * logNum;
		}else{
			this.speed *= -1;
			x = 800 - randomSpace * logNum;
		}
	}
	
	private void move () {
		x += speed;
		if (x < -10 || x > 800) {
			if (headingRight) {
				x = -10;
			}
			else {
				x = 800;
			}
		}
	}
	
	public void draw () {
		batch.begin();
		logSprite.setPosition(x, y);
		logSprite.draw(batch);
		batch.end();
		move();
	}
	
	public int getSpeed () {
		return speed;
	}
	
	public boolean collide (Sprite frog){
		Rectangle rect = new Rectangle(frog.getX() + 18, frog.getY() - 22, 15, 28);
		Rectangle logRect = new Rectangle(logSprite.getX(), logSprite.getY() - logSprite.getHeight(), logSprite.getWidth(), logSprite.getHeight() );
		return rect.overlaps(logRect);
	}
}
