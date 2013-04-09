package org.vergeman.sulfonicavenger;

import java.util.ArrayList;
import java.util.Random;

public class MoleculeEntity extends Entity {

	int lives;
	boolean remove = false;
	int score;
	
	ArrayList<String> damages;
	Random r;
	
	protected MoleculeEntity(Sprite sprite,  int type, int x, int y) {
		super(sprite, x, y);
		
		//Molecule Type M+C / B / AGC
		switch (type) {
		case 3: //AGC
			lives = 20;
			score = 500;
			break;
		default:
			score = 50;
			lives = 10;
			break;
		
		}
		damages = new ArrayList<String>();
		r = new Random();
	}

	public int getScore() {
		return score;
	}
	@Override
	public void collidedWith(Entity other) {
		
		if (other instanceof ShotEntity) {
			--lives;
			if (lives <= 0) {
				remove = true;
			}
			damages.add(
					(int) (this.x + r.nextDouble() * sprite.getWidth())  + "-" 
			+ (int) (this.y + (r.nextDouble() * sprite.getHeight())));
		}
		
		
		
	}

	@Override
	public void reposition(int x, int y, float scale_rel) {
		// TODO Auto-generated method stub
		// bounds
		this.updateBounds(0, x - this.sprite.getWidth(), 0,
				y - this.sprite.getHeight());

		// placement - TODO: fucked up needs wrk
		this.x = Math.min(Math.abs(this.x * scale_rel), this.BOUNDS_RIGHT);
		this.y = Math.min(Math.abs(this.y * scale_rel), this.BOUNDS_BOTTOM);

	}

}
