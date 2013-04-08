package org.vergeman.sulfonicavenger;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class TextDrawer {
	TrueTypeFont gameFont;
	Font awt_gameFont;
	Graphics g;
	WindowManager windowManager;
	int height;
	int width;
	int draw_x, draw_y;
	Color bg; 

	String last_message;
	
	//drawing vars
	int x,y;
	double w_offset_coef,h_offset_coef,w_offset,h_offset;
	boolean center;
	
	public TextDrawer(TrueTypeFont gameFont, Font awt_gameFont, 
			float font_size, Graphics g, WindowManager windowManager) {
	
		this.gameFont = gameFont;
		this.awt_gameFont = awt_gameFont;
		this.g = g;
		this.windowManager = windowManager;
		setFontSize(font_size);

		this.height = gameFont.getHeight();

	}
	
	
	public void update() {
		
	}
	
	public void resize() {
		//repoisition
	}
	
	public void setFontSize(float size) {
		this.awt_gameFont = awt_gameFont.deriveFont(size);
		gameFont = new TrueTypeFont(awt_gameFont, false);
	}
	
	public void draw(String message, Color text_color,
			int x, int y, double w_offset_coef, double h_offset_coef, double w_offset, double h_offset) {

		if (message != null) {
			//move left to offset message
			draw_x= (int) (x + w_offset_coef * gameFont.getWidth(message) + w_offset);
			draw_y= (int) (y + h_offset_coef * height + h_offset);
			
			if (center) {
				x= (int) (windowManager.getCenterX() - gameFont.getWidth(message)) / 2;
				y= (int) (windowManager.getCenterY() - gameFont.getHeight() / 2);
			}
				//g.fillRect(x,y, width, height);
			last_message = message;
			
			gameFont.drawString(draw_x, draw_y, message, text_color);
		}
	}
	
	
}
