package com.patricia.frogger;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Crocodile {
	private Batch batch;
	private int y;
	private int x;
	private Texture crocImg;
	private Random rand = new Random();
	private int levelAppear;
	private int[] positions = {30, 174, 318, 462, 606};
	
	public Crocodile(Batch batch, int levelNum) {
		this.batch = batch;
		levelAppear = 2; //level of first appearance
		y = 630;
		randomizePosition(levelNum);
		crocImg = new Texture(Gdx.files.internal("sprites/crocMouth.png"));
	}
	
	public void randomizePosition(int levelNum) {
		if(levelNum >= levelAppear) {
			int chance = rand.nextInt(positions.length);
			x = positions[chance];
		}
	}
	
	public int getPosition() {
		return x;
	}
	
	public void remove() {
		x = 1000;
	}
	
	public void setPositionArray(int[] posArray) {
		positions = posArray;
	}
	
	public void draw() {
		batch.begin();
		batch.draw(crocImg, x, 630);
		batch.end();
	}
}
