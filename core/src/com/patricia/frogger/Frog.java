package com.patricia.frogger;

public class Frog {
	private int x;
	private int y;
	
	public Frog () {
		x = 332;
		y = 47;
	}
	
	public void drowned () {
		if (isInWater()) {
			newFrog();
		}
	}
	
	public int isAtHome (int[] homePositions) {
		if (y >= 600) {
			for (int p: homePositions) {
				if (x - 14 == p) {
					int index = (x - 44) / 144;
					System.out.println("win");
					newFrog();
					return index;
				}
			}
			newFrog();
			return 5;
		}
		return 6;
	}
	
	public boolean isInWater () {
		if (y >= 350 && y <= 700) {
			return true;
		}
		
		return false;
	}
	
	public void newFrog () {
		x = 332;
		y = 47;
	}
	
	public void moveLeft (boolean facingLeft, boolean facingDown) {
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
	
	public int getX () {
		return x;
	}
	
	public int getY () {
		return y;
	}
}
