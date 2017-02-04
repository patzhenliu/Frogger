package com.patricia.frogger;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Car {
	private Batch batch;
	private Texture carImg;
	private int speed;
	private int type;
	private boolean headingRight; //true = right. false=left
	private int x;
	private int y;
	Random rand = new Random(System.currentTimeMillis() );
	
	public Car (int type, int y, Batch batch) {
		speed = 5; // pixel per frame
		this.type = type;
		this.y = y;
		this.batch = batch;
		String fileName = "car" + type + ".png";
		carImg = new Texture(Gdx.files.internal(fileName));
		int randomSpace = rand.nextInt(500) + 200;
		
		headingRight = (type == 1 || type == 5);		
		
		if(headingRight){
			x = -10 + randomSpace;
		}else{
			speed *= -1;
			x = 800 - randomSpace;
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
		batch.draw(carImg, x, y);
		batch.end();
		move();
	}
	
	//public bool colide(   ){
		
		
	//}
	
}
