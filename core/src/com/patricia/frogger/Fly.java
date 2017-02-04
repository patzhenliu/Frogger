package com.patricia.frogger;

import java.util.Random;

public class Fly {
	private int y;
	private int x;
	Random rand = new Random();
	int[] positions = {30, 174, 318, 462, 606};
	
	public Fly () {
		y = 630;
		randomizePosition();
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
	
}
