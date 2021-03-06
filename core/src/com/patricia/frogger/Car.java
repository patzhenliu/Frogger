//Car.java
//Patricia Liu
////Contains all information and methods regarding the cars

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
	private boolean headingRight;
	private int x;
	private int y;
	private Random rand = new Random(System.currentTimeMillis() );
	
	public Car(int type, int y, Batch batch, int carNum, int speed) {
		this.speed = speed; // pixel per frame
		this.y = y;
		this.batch = batch;
		String fileName = "sprites/car" + type + ".png";
		carImg = new Texture(Gdx.files.internal(fileName));
		carSprite = new Sprite(carImg);
		
		int randomSpace = rand.nextInt(100) + 200; //determines the amount of space between each car
		
		headingRight =(type == 1 || type == 5);	//cars in the specified lanes go right
		
		if(headingRight){
			x = -10 + randomSpace * carNum;
		}else{
			this.speed *= -1;
			x = 800 - randomSpace * carNum;
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
		carSprite.setPosition(x, y);
		carSprite.draw(batch);
		batch.end();
		move();
	}
	
	public boolean collide(Frog frog){
		Sprite frogSprite = frog.getSprite();
		Rectangle rect = new Rectangle(frogSprite.getX(), frogSprite.getY(), frogSprite.getWidth(), frogSprite.getHeight());
		Rectangle carRect = new Rectangle(carSprite.getX(), carSprite.getY(), carSprite.getWidth(), carSprite.getHeight() );
		return rect.overlaps(carRect);
	}
	
}
