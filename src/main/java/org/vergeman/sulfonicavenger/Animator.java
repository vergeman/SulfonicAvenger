package org.vergeman.sulfonicavenger;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Animator extends Animation {

	float x, y;
	//agc_explosion.stopAt(16);
	//gen_explosion.stopAt(12);
	//make an animation manager, add, clear, draw methos
	//a lsit of AnimationLoc
	//which extends Animation, btu with coordinates
	//new Animation(, 0,0,2,3, true, 250, true);
	 //new Animation(, 0,0,3,3, true, 250, true);
	public Animator(SpriteSheet spritesheet, float pos_x, float pos_y,
			int s_x, int s_y, int s_x2, int s_y2, 
			boolean horizontal, int duration, boolean autoUpdate) {
		super(spritesheet, s_x, s_y, s_x2, s_y2, horizontal, duration, autoUpdate);
		
		this.x = pos_x;
		this.y = pos_y;
		//this.stopAt(stopIndex);
		this.setLooping(false);
	}
	

	public void update() {
		
	}
	
	public void draw() {
		super.draw(this.x,this.y);
	}
	
}
