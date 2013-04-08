package org.vergeman.sulfonicavenger;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public class PlayerEntity extends Entity implements KeyListener {
	boolean alive, can_collide;
	boolean game_over;
	int lives = 3;
	int spawn_x, spawn_y;

	protected int move_speed = 300;

	protected ShotEntity[] shots;
	protected int shot_index = 0;

	int PAUSE_SPEED = 1000;
	int pause_counter = 0;

	AssetManager assetManager;

	protected PlayerEntity(GameContainer container, AssetManager assetManager,
			Sprite sprite, int x, int y) {
		super(sprite, x, y);
		this.spawn_x = x;
		this.spawn_y = y;

		this.alive = true;
		this.can_collide = true;
		this.game_over = false;
		this.pause_counter = 0;

		setVerticalMovement(0);
		setHorizontalMovement(0);
		resize(container.getWidth(), container.getHeight(), 1.0f, 1.0f);
		this.assetManager = assetManager;
	}

	public void setShots(ShotEntity[] shots) {
		this.shots = shots;
	}

	public void move(int delta, ArrayList<MoleculeEntity> molecules) {

		if ((dx < 0) && (x < this.BOUNDS_LEFT)) {
			dx = 0;
		}
		if ((dx > 0) && (x > this.BOUNDS_RIGHT)) {
			dx = 0;
		}
		if ((dy < 0) && (y < this.BOUNDS_TOP)) {
			dy = 0;
		}
		if ((dy > 0) && (y > this.BOUNDS_BOTTOM)) {
			dy = 0;
		}

		// System.out.println("alive: " + alive);
		// System.out.println("can_collide: " + can_collide);
		// System.out.println("lives: " + lives);

		if (alive) {

			super.move(delta);

			if (!calculateValidMove(molecules)) {
				super.unmove(delta);
			}
		}

		if (pause_counter >= 0) {
			pause_counter -= delta;
		}

		if (!alive && pause_counter < 0) {
			reinitialize(); // respawn, set to alive (moveable) but not
							// collidable
			pause_counter = PAUSE_SPEED + 500; // repause to give user time to
												// orient
		} else if (alive && pause_counter <= 0) {
			can_collide = true;
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
	// resolution relative
	public void reposition(int x, int y, float scale_rel) {
		// bounds
		this.updateBounds(0, x - this.sprite.getWidth(), 0,
				y - this.sprite.getHeight());

		// placement - TODO: fucked up needs wrk
		this.x = Math.min(Math.abs(this.x * scale_rel), this.BOUNDS_RIGHT);
		this.y = Math.min(Math.abs(this.y * scale_rel), this.BOUNDS_BOTTOM);

	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isGameOver() {
		return game_over;
	}

	public int paused() {
		return pause_counter;
	}

	@Override
	public void draw() {
		if (pause_counter <= 0) {
			super.draw();
		} else {
			/*flash*/
			switch ((int) (pause_counter / (PAUSE_SPEED / 4))) {

			case 5:
				break;
			case 3:
				break;
			case 1:
				break;
			default:
				super.draw();
			}
		}
	}

	@Override
	public void collidedWith(Entity other) {
		// TODO Auto-generated method stub
		if (other instanceof CentiBallEntity || other instanceof NH3Entity) {

			if (alive && can_collide) {
				alive = false;
				can_collide = false;
				pause_counter = PAUSE_SPEED;
				--lives;
				assetManager.getSound("hit").play();

				if (lives <= 0) {
					game_over = true;
				}
			}
		}
	}

	public void reinitialize() {
		this.x = spawn_x;
		this.y = spawn_y;
		this.alive = true;
	}

	public void shoot() {
		// if we are allowed to shoot (in between time)
		if (!shots[shot_index].isDisplay()) {
			shots[shot_index].reinitalize(x, y);
			shot_index++;
			shot_index = shot_index % shots.length;
			assetManager.getSound("shoot").play();
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {

		case Input.KEY_LEFT:
			setHorizontalMovement(-move_speed);
			break;
		case Input.KEY_RIGHT:
			setHorizontalMovement(move_speed);
			break;
		case Input.KEY_SPACE:
			shoot();
			break;
		case Input.KEY_UP:
			setVerticalMovement(-move_speed);
			break;
		case Input.KEY_DOWN:
			setVerticalMovement(move_speed);
			break;
		default:
			break;

		}
	}

	@Override
	public void keyReleased(int key, char c) {
		// TODO Auto-generated method stub
		if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT) {
			setHorizontalMovement(0);
		}
		if (key == Input.KEY_UP || key == Input.KEY_DOWN) {
			setVerticalMovement(0);
		}

	}

	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAcceptingInput() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setInput(Input arg0) {
		// TODO Auto-generated method stub
	}

}
