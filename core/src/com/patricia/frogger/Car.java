package com.patricia.frogger;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Car {
	private Batch batch;
	private Texture carImg;
	private Sprite carSprite;
	private int speed;
	private int type;
	private boolean headingRight; //true = right. false=left
	private int x;
	private int y;
	Random rand = new Random(System.currentTimeMillis() );
	
	public Car (int type, int y, Batch batch, int carNum) {
		speed = 5; // pixel per frame
		this.type = type;
		this.y = y;
		this.batch = batch;
		String fileName = "car" + type + ".png";
		carImg = new Texture(Gdx.files.internal(fileName));
		carSprite = new Sprite(carImg);
		
		int randomSpace = rand.nextInt(100) + 200;
		System.out.println(randomSpace);
		
		headingRight = (type == 1 || type == 5);		
		
		if(headingRight){
			x = -10 + randomSpace * carNum;
		}else{
			speed *= -1;
			x = 800 - randomSpace * carNum;
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
		carSprite.setPosition(x, y);
		carSprite.draw(batch);
		batch.end();
		move();
	}
	
	public boolean collide (Sprite object){
		Rectangle rect = new Rectangle(object.getX() + 18, object.getY() - 22, 26, 28);
		Rectangle carRect = new Rectangle(carSprite.getX(), carSprite.getY(), carSprite.getWidth(), carSprite.getHeight() );
		return rect.overlaps(carRect);
	}
	
}
