package org.vergeman.sulfonicavenger;

import org.newdawn.slick.Image;

/**
 * Implementation of sprite that uses an OpenGL quad and a texture to render a
 * given image to the screen.
 * 
 */
public class Sprite {

	/** The texture that stores the image for this sprite */
	private Image image;

	/** The width in pixels of this sprite */
	@SuppressWarnings("unused")
	private int width;

	/** The height in pixels of this sprite */
	@SuppressWarnings("unused")
	private int height;

	/**
	 * Create a new sprite from a specified image.
	 * 
	 * @param loader
	 *            the texture loader to use
	 * @param ref
	 *            A reference to the image on which this sprite should be based
	 */
	public Sprite(Image img) {
		image = img;
		setWidth(image.getWidth());
		setHeight(image.getHeight());
	}

	/**
	 * Get the width of this sprite in pixels
	 * 
	 * @return The width of this sprite in pixels
	 */
	public int getWidth() {
		return image.getWidth();
	}

	/**
	 * Get the height of this sprite in pixels
	 * 
	 * @return The height of this sprite in pixels
	 */
	public int getHeight() {
		return image.getHeight();
	}

	/**
	 * Draw the sprite at the specified location
	 * 
	 * @param x
	 *            The x location at which to draw this sprite
	 * @param y
	 *            The y location at which to draw this sprite
	 */
	public void draw(int x, int y) {
		image.draw(x, y);
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}