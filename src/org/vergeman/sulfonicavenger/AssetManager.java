package org.vergeman.sulfonicavenger;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class AssetManager {

	HashMap<String, Image> image_map = new HashMap<String, Image>();
	HashMap<String, Sound> sound_map = new HashMap<String, Sound>();
	
	InputStream inputStream;
	TrueTypeFont gameFont;
	Font awt_gameFont;
	
	Image ship;
	Image molecule1;
	Image molecule2;
	Image molecule3;
	Image damage;
	Image shot;
	Image nh3;
	Image centibody;
	Image centihead;
	
	Image explosion;
	
	Sound hit, shoot;

	/*hash or lists...hmm*/
	/*sonuds as well*/
	
	public AssetManager() {
		return;
	
	}
	
	public boolean init() {
		/*load static stuff*/		
		try {
			ship = new Image("data/logo.png", false, Image.FILTER_NEAREST);
			nh3 = new Image("data/nh3.png", false, Image.FILTER_NEAREST);
			
			molecule1 = new Image("data/molecule_b.png", false, Image.FILTER_NEAREST);
			molecule2 = new Image("data/molecule_m+c.png", false, Image.FILTER_NEAREST);
			molecule3 = new Image("data/molecule_agc.png", false, Image.FILTER_NEAREST);
			
			damage = new Image("data/damage.png");
			explosion = new Image("data/explosion.png");
			
			centihead  = new Image("data/centi.png", false, Image.FILTER_NEAREST);
			centibody = new Image("data/centi.png", false, Image.FILTER_NEAREST);
			
			shot = new Image("data/shot.gif", false, Image.FILTER_NEAREST);
			
			inputStream	= ResourceLoader.getResourceAsStream("data/arcadeclassic.ttf");			 
			awt_gameFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awt_gameFont = awt_gameFont.deriveFont(14f); // set font size
			gameFont = new TrueTypeFont(awt_gameFont, false);

			image_map.put("ship", ship);
			image_map.put("molecule1", molecule1);
			image_map.put("molecule2", molecule2);
			image_map.put("molecule3", molecule3);
			image_map.put("damage", damage);
			image_map.put("explosion", explosion);
			image_map.put("shot", shot);
			image_map.put("nh3", nh3);
			image_map.put("centihead", centihead);
			image_map.put("centibody", centibody);
				
			hit = new Sound("data/hit.wav");
	
			shoot = new Sound("data/shot.wav");
			sound_map.put("hit", hit);
			sound_map.put("shoot", shoot);
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public Image getImage(String element) {
		return image_map.get(element);
	}
	public Sound getSound(String element) {
		return sound_map.get(element);
	}
	
	public void scale(float scale) {
		return;
	}

	public TrueTypeFont getFont() {
		return gameFont;

	}

	public Font get_awtFont() {
		return awt_gameFont; 
	}
}
