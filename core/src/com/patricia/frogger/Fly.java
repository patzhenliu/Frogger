package com.patricia.frogger;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Fly {
	private Batch batch;
	private int y;
	private int x;
	private Texture flyImg;
	private Random rand = new Random();
	private int[] positions = {30, 174, 318, 462, 606};
	
	public Fly (Batch batch) {
		this.batch = batch;
		y = 630;
		randomizePosition();
		flyImg = new Texture(Gdx.files.internal("sprites/fly.png"));
	}
	
	public void randomizePosition () {
		int chance = rand.nextInt(positions.length);
		x = positions[chance];
	}
	
	public int getPosition () {
		return x;
	}
	
	public void remove () {
		x = 1000;
	}
	
	public void removePosition(int index) {
		int newIndex;
		int oldPos = positions[index];
		if (index != positions.length - 1) {
			newIndex = index + 1;
		}
		else{
			newIndex = 0;
		}
		for (int i = 0; i < positions.length; i++) {
			if (positions[i] == oldPos) {
				positions[i] = positions[newIndex];
			}
		}

	}
	
	public void draw () {
		batch.begin();
		batch.draw(flyImg, x, 630);
		batch.end();
	}
	
}
