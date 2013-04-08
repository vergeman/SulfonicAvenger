package org.vergeman.sulfonicavenger;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;

public class NH3Entity extends Entity {
	protected GameContainer container;

	protected int move_speed = 200;

	protected boolean display;

	protected Random r = new Random();
	protected boolean move_bool = true;

	private long NH3Interval = 4000; // ms
	private long lastNH3 = Sys.getTime();
	
	int score = 250;
	
	protected NH3Entity(GameContainer container, Sprite sprite, int x, int y) {
		super(sprite, x, y);
		this.container = container;
		setHorizontalMovement(0);
		setVerticalMovement(0);
		resize(container.getWidth(), container.getHeight(), 1.0f, 1.0f);
	}

	public int getScore() {
		return score;
	}
	public void move(int delta, ArrayList<MoleculeEntity> molecules) {

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

		// if y < height - 20%, we'll switch downward - scalable invisible
		// barieer
		super.move(delta);

		if (!calculateValidMove(molecules)) {
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

	public boolean calculateValidMove(ArrayList<MoleculeEntity> molecules) {
		for (MoleculeEntity m : molecules) {
			if (this.collidesWith(m)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void collidedWith(Entity other) {

		if (other instanceof ShotEntity) {
			display = false;
			this.x = -100;
			this.y = -100;
			dx = 0;
			dy = 0;
			lastNH3 = Sys.getTime() ;
		}

	}

	public void reinitialize() {
		if ( !display && (Sys.getTime() - lastNH3 > NH3Interval)) {

			this.x = (int) (r.nextBoolean() ? 0 : container.getWidth());
			this.y = (int) (container.getHeight() - 200 + (r.nextDouble() * 200));
			dy = move_speed;
			dx = move_speed;
			display = true;
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
