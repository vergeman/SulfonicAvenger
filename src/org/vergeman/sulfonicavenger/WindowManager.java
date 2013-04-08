package org.vergeman.sulfonicavenger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class WindowManager {
	private int _WIDTH;
	private int _HEIGHT;
	private int old_width;
	private int old_height;
	private int new_width;
	private int new_height;

	private int center_x;
	private int center_y;
	
	private GameContainer container;
	//private StateBasedGame game;

	private float scale_abs = 1.0f;
	private float scale_rel = 1.0f;
	/**
	 * WindowManager: responsible for checking dimensions of the application and
	 * on resize making sure everything else follows with the appropriate scale
	 * factor
	 * 
	 * @param orig_width
	 * @param orig_height
	 * @param container
	 * @param game
	 */

	public WindowManager(GameContainer container, StateBasedGame game) {

		this._WIDTH = container.getWidth();
		this._HEIGHT = container.getHeight();
		this.old_width = this._WIDTH;
		this.old_height = this._HEIGHT;
		this.center_x = this._WIDTH / 2;
		this.center_y = this._HEIGHT / 2;
		
		this.container = container;
		
	}

	public boolean isResized() {
		/* update */
		new_height = this.container.getHeight();
		new_width = this.container.getWidth();
		
		if (new_height != old_height || new_width != old_width) {

			scale_abs = Math.min((float) new_width / _WIDTH, (float) new_height / _HEIGHT);
			scale_rel = Math.min((float) new_width / old_width, (float) new_height / old_height);

			
			old_height = new_height;
			old_width = new_width;
			
			center_x = new_width / 2;
			center_y = new_height /2;
			
			return true;
		}
		return false;
	}

	public float getScaleAbsFactor() {
		return scale_abs;
	}

	public float getScaleRelFactor() {
		return scale_rel;
	}

	
	public float getCenterX() {
		return center_x;
	}
	public float getCenterY() {
		return center_y;
	}
	
	public int get_orig_width() {
		return _WIDTH;
	}
	public int get_orig_height() {
		return _HEIGHT;
	}
}


/* old resize code didn't do anything
isFirstLoad = false;
scale_abs = windowManager.getScaleAbsFactor();
scale_rel = windowManager.getScaleRelFactor();
int x = container.getWidth();
int y = container.getHeight();

for (MoleculeEntity m : molecules) {
	m.resize(x, y, scale_abs, scale_rel);
}

for (NH3Entity n : nh3s) {
	n.resize(x, y, scale_abs, scale_rel);
}

for (Centipede c : centipedes) {
	c.resize(x, y, scale_abs, scale_rel);
}

player.resize(x, y, scale_abs, scale_rel);

*/