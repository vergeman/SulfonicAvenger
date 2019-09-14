package org.vergeman.sulfonicavenger;

import org.newdawn.slick.Color;
import java.util.HashMap;
import org.newdawn.slick.Graphics;


public class TextDrawManager {

	HashMap<String, TextDrawer> text_drawer_map;
	AssetManager assetManager;
	WindowManager windowManager;
	Graphics g;
	
	
	public TextDrawManager(Graphics g, WindowManager windowManager, AssetManager assetManager) {
		this.assetManager = assetManager;
		this.windowManager = windowManager;
		this.g = g;
		
		text_drawer_map = new HashMap<String, TextDrawer>();	
	}
	
	/* dirty: we'll just use true for default (arcarde-style), false for helvetica */
	public void build(String msg_name, float font_size, boolean font_name) {

		text_drawer_map.put(msg_name,
				new TextDrawer(assetManager.getGameFont(),
						font_name ? assetManager.getAwtGameFont()
								: assetManager.getAwtHelveticaFont(),
						font_size, g, windowManager));
	}
	
	public TextDrawer get(String message) {
		return text_drawer_map.get(message);
	}
	
	public int getWidth(String message) {
		String last_message = text_drawer_map.get(message).last_message;
		return text_drawer_map.get(message).ttfont.getWidth(last_message);
	}
	public int getHeight(String message) {
		String last_message = text_drawer_map.get(message).last_message;
		return text_drawer_map.get(message).ttfont.getHeight(last_message);
	}
	
	public void draw(String key, String message, Color text_color,
			int x, int y, double w_offset_coef, double h_offset_coef, double w_offset, double h_offset) {
		
		this.text_drawer_map.get(key).draw(message, text_color,
				x, y, w_offset_coef,h_offset_coef,w_offset,h_offset);
	}
}
