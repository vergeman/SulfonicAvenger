package org.vergeman.sulfonicavenger;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;

public class NH3Entity extends Entity {
	protected GameContainer container;

	protected int move_speed = 170;

	protected boolean display;

	protected Random r = new Random();
	protected boolean move_bool = true;

	int score = 250;

	int NH3LifeInterval = 20000;
	long spawn_time;
	boolean remove_me;

	protected NH3Entity(GameContainer container, Sprite sprite, int x, int y) {
		super(sprite, x, y);
		this.container = container;
		setHorizontalMovement(0);
		setVerticalMovement(0);
		spawn_time = Sys.getTime();
		resize(container.getWidth(), container.getHeight(), 1.0f, 1.0f);
		dx = move_speed;
		dy = move_speed;
		display = true;
		remove_me = false;
		this.x = (int) (r.nextBoolean() ? 0 : container.getWidth());
		this.y = (int) (container.getHeight() - 200 + (r.nextDouble() * 200));

	}

	public int getScore() {
		return score;
	}

	public void move(int delta, ArrayList<MoleculeEntity> molecules,
			ArrayList<Centipede> centipedes) {
		// we stay on the screen for a certain period, then head
		// back to the ether

		if (Sys.getTime() - NH3LifeInterval > spawn_time ) {
			if ((y > this.BOUNDS_BOTTOM) || (y < 0) || (x > this.BOUNDS_RIGHT) || (x < 0) ) {
				remove_me = true;
			}
		}
		else {
			if ((dx < 0) && (x < this.BOUNDS_LEFT)) {
				dx = -dx;
			}
			if ((dx > 0) && (x > this.BOUNDS_RIGHT)) {
				dx = -dx;
			}
			if ((dy < 0) && (y < this.BOUNDS_TOP)) {
				dy = -dy;
			}
			if ((dy > 0) && (y > this.BOUNDS_BOTTOM)) {
				dy = -dy;
			}
		}
		
		super.move(delta);

		if (!calculateValidMove(molecules, centipedes)) {
			super.unmove(delta);
			/* baby AI */
			move_bool = r.nextBoolean();
			if (move_bool) {
				dx = -dx;
			} else {
				dy = -dy;
			}

		}
	}

	public boolean calculateValidMove(ArrayList<MoleculeEntity> molecules,
			ArrayList<Centipede> centipedes) {
		for (MoleculeEntity m : molecules) {
			if (this.collidesWith(m)) {
				return false;
			}
		}
		/*
		 * for (Centipede c : centipedes) { if(c.checkCollisions((Entity) this))
		 * { return false; } }
		 */

		return true;
	}

	@Override
	public void collidedWith(Entity other) {

		if (other instanceof ShotEntity) {
			display = false;
			//this.x = -100;
			//this.y = -100;
			dx = 0;
			dy = 0;
		}

	}

	@Override
	public void reposition(int x, int y, float scale_rel) {
		// TODO Auto-generated method stub
		this.updateBounds(0, x - this.sprite.getWidth(), 0,
				y - this.sprite.getHeight());

	}

	public void move(long delta, ArrayList<MoleculeEntity> molecules) {
		super.move(delta);
	}
}
