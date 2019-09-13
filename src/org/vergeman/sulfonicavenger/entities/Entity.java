package org.vergeman.sulfonicavenger.entities;

import org.vergeman.sulfonicavenger.Sprite;

import java.awt.Rectangle;

/**
 * An entity represents any element that appears in the game. The
 * entity is responsible for resolving collisions and movement
 * based on a set of properties defined either by subclass or externally.
 *
 * Note that doubles are used for positions. This may seem strange
 * given that pixels locations are integers. However, using double means
 * that an entity can move a partial pixel. It doesn't of course mean that
 * they will be display half way through a pixel but allows us not lose
 * accuracy as we move.
 */

public abstract class Entity {

	/** The current x location of this entity */
	public float	x;

	/** The current y location of this entity */
	public float	y;

	public float old_x;
	public float old_y;
	/** The sprite that represents this entity */
	public Sprite sprite;

	/** The current speed of this entity horizontally (pixels/sec) */
	public float	dx;

	/** The current speed of this entity vertically (pixels/sec) */
	public float	dy;

	public int BOUNDS_LEFT;
	public int BOUNDS_RIGHT;
	public int BOUNDS_TOP;
	public int BOUNDS_BOTTOM;
	
	/** The rectangle used for this entity during collisions  resolution */
	private Rectangle	me	= new Rectangle();

	/** The rectangle used for other entities during collision resolution */
	private Rectangle	him	= new Rectangle();	
	
	/**
	 * Construct a entity based on a sprite image and a location.
	 *
	 * @param sprite The reference to the image to be displayed for this entity
	 * @param x The initial x location of this entity
	 * @param y The initial y location of this entity
	 */
	protected Entity(Sprite sprite, int x, int y) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
	}

	/**
	 * Request that this entity move itself based on a certain ammount
	 * of time passing.
	 *
	 * @param delta The amount of time that has passed in milliseconds
	 */
	public void move(long delta) {
		// update the location of the entity based on move speeds
		old_x = x;
		old_y = y;
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	}
	
	public void unmove(long delta) {
		x -= (delta * dx) / 1000;
		y -= (delta * dy) / 1000;
	}
	
	
	/**
	 * Set the horizontal speed of this entity
	 *
	 * @param dx The horizontal speed of this entity (pixels/sec)
	 */
	public void setHorizontalMovement(float dx) {
		this.dx = dx;
	}

	/**
	 * Set the vertical speed of this entity
	 *
	 * @param dy The vertical speed of this entity (pixels/sec)
	 */
	public void setVerticalMovement(float dy) {
		this.dy = dy;
	}

	/**
	 * Get the horizontal speed of this entity
	 *
	 * @return The horizontal speed of this entity (pixels/sec)
	 */
	public float getHorizontalMovement() {
		return dx;
	}

	/**
	 * Get the vertical speed of this entity
	 *
	 * @return The vertical speed of this entity (pixels/sec)
	 */
	public float getVerticalMovement() {
		return dy;
	}

	/**
	 * Draw this entity to the graphics context provided
	 */
	public void draw() {
		sprite.draw((int) x, (int) y);
	}

	/**
	 * Do the logic associated with this entity. This method
	 * will be called periodically based on game events
	 */
	public void doLogic() {
	}

	/**
	 * Get the x location of this entity
	 *
	 * @return The x location of this entity
	 */
	public int getX() {
		return (int) x;
	}

	/**
	 * Get the y location of this entity
	 *
	 * @return The y location of this entity
	 */
	public int getY() {
		return (int) y;
	}

	/**
	 * Check if this entity collised with another.
	 *
	 * @param other The other entity to check collision against
	 * @return True if the entities collide with each other
	 */
	public boolean collidesWith(Entity other) {
		me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
		him.setBounds((int) other.x, (int) other.y, other.sprite.getWidth(), other.sprite.getHeight());

		return me.intersects(him);
	}
	
	/**
	 * Notification that this entity collided with another.
	 *
	 * @param other The entity with which this entity collided.
	 */
	public abstract void collidedWith(Entity other);

	public abstract void reposition(int x, int y, float scale_rel);
	
	public void rescale(int x, int y, float scale_abs) {
		
	}
	
	public void resize(int x, int y, float scale_abs, float scale_rel) {
		rescale(x,y,scale_abs);
		reposition(x,y,scale_rel);
	}
	
	public void updateBounds(int left, int right, int top, int bottom) {
		this.BOUNDS_LEFT = left;
		this.BOUNDS_RIGHT = right;
		this.BOUNDS_TOP = top;
		this.BOUNDS_BOTTOM = bottom;
	}

}