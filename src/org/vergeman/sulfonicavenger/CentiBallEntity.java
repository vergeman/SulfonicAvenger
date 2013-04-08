package org.vergeman.sulfonicavenger;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;

public class CentiBallEntity extends Entity {
	protected GameContainer container;
	
	protected boolean isHead;
	
	protected int move_speed = 250;

	protected int goal_x;
	protected int goal_y;
	protected float old_dx;

	protected boolean display;
	
	protected CentiBallEntity(GameContainer container, Sprite sprite, 
				int x, int y, boolean isHead) {
		super(sprite, x, y);
		this.isHead = isHead;
		this.display = true;
		this.container = container;
		setHorizontalMovement(move_speed);
		setVerticalMovement(0);
		resize(container.getWidth(), container.getHeight(), 1.0f, 1.0f);

		// TODO Auto-generated constructor stub
	}
	
	
	public void move(long delta, ArrayList<MoleculeEntity> molecules) {
		/*TODO: framerate problem; moving down at one delta while another moves left,
		 * then both move left at different delta; there's a slight X gap
		 */
		//BOUNDS CHECK
		if ((dx < 0) && (x < this.BOUNDS_LEFT)) {
			old_dx = dx;
			goal_y =  getY() + sprite.getHeight();
			
			//goal_x = getX();
			goal_x = 0;
			
			dx = 0;
			dy = move_speed;
		}

		if ((dx > 0) && (x > this.BOUNDS_RIGHT)) {
			old_dx = dx;
			goal_y = getY() + sprite.getHeight();
			//goal_x = getX();
			goal_x =container.getWidth() - sprite.getWidth();
			dx = 0;
			dy = move_speed;
		}

		super.move(delta);
//we've moved far enough, lets go back to horizontal
		if (goal_y > getY()) { 
			y = goal_y;
			x = goal_x;
			dx = -old_dx;
			dy = 0;
		}
		
//molecule collision? adjust our movement		
		if (!calculateValidMove(molecules)) {
			super.unmove(delta);
			//horizontal collide, so move down
			if (dy == 0) {
				dy = move_speed;
				old_dx = dx;
				dx=0;
				
				goal_y = getY() + sprite.getHeight();
				goal_x = getX();	
				//use moelecule to place X
			}
			//else vertical collide, so move horizontal opposite
			else {
				dy = 0;
				dx = -old_dx;
				goal_y = getY(); 
				goal_x = getX() + sprite.getWidth();	
				
			}
		}
		
	}

	//status for off screen - re-initialize
	
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
		// TODO Auto-generated method stub
		if (other instanceof ShotEntity) {
			this.display = false;
			dx = 0;
			dy = 0;
		}
		


	}

	public boolean isDisplay() {
		return display;
	}
	@Override
	public void reposition(int x, int y, float scale_rel) {
		// TODO Auto-generated method stub
		this.updateBounds(0, x - this.sprite.getWidth(), 0,
				y + this.sprite.getHeight());

	}
	

}
