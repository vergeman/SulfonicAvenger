package org.vergeman.sulfonicavenger;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class TextDrawer {
	TrueTypeFont ttfont;
	Font awt_Font;
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
	
	public TextDrawer(TrueTypeFont ttfont, Font awt_Font, 
			float font_size, Graphics g, WindowManager windowManager) {
	
		this.ttfont = ttfont;
		this.awt_Font = awt_Font;
		this.g = g;
		this.windowManager = windowManager;
		setFontSize(font_size);

		this.height = ttfont.getHeight();

	}
	
	
	public void update() {
		
	}
	
	public void resize() {
		//repoisition
	}
	
	public void setFontSize(float size) {
		this.awt_Font = awt_Font.deriveFont(size);
		ttfont = new TrueTypeFont(awt_Font, false);
	}
	
	public void draw(String message, Color text_color,
			int x, int y, double w_offset_coef, double h_offset_coef, double w_offset, double h_offset) {

		if (message != null) {
			//move left to offset message
			draw_x= (int) (x + w_offset_coef * ttfont.getWidth(message) + w_offset);
			draw_y= (int) (y + h_offset_coef * height + h_offset);
			
			if (center) {
				x= (int) (windowManager.getCenterX() - ttfont.getWidth(message)) / 2;
				y= (int) (windowManager.getCenterY() - ttfont.getHeight() / 2);
			}
				//g.fillRect(x,y, width, height);
			last_message = message;
			
			ttfont.drawString(draw_x, draw_y, message, text_color);
		}
	}
	
	
}
