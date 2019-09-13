package org.vergeman.sulfonicavenger;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class AssetManager {

	HashMap<String, Image> image_map = new HashMap<String, Image>();
	HashMap<String, Sound> sound_map = new HashMap<String, Sound>();
	HashMap<String, SpriteSheet> spritesheet_map = new HashMap<String, SpriteSheet>();
	
	InputStream inputStream, inputStream2;
	TrueTypeFont gameFont, helveticaFont;
	Font awt_gameFont, awt_helveticaFont;
	
	Image ship;
	Image molecule1;
	Image molecule2;
	Image molecule3;
	Image damage;
	Image shot;
	Image nh3;
	Image centibody;
	Image centihead;
	
	protected Image biglogo;
	
	Sound hit, shoot;

	SpriteSheet agc_explosion, gen_explosion, player_explosion;
	
	public AssetManager() {
		return;
	
	}
	
	public boolean menu_init() throws FontFormatException, IOException, SlickException {
		inputStream	= ResourceLoader.getResourceAsStream("data/fonts/arcadefont.ttf");
		awt_gameFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
		awt_gameFont = awt_gameFont.deriveFont(14f); // set font size
		gameFont = new TrueTypeFont(awt_gameFont, false);
		
		inputStream2	= ResourceLoader.getResourceAsStream("data/fonts/Helvetica.ttf");
		awt_helveticaFont = Font.createFont(Font.TRUETYPE_FONT, inputStream2);
		awt_helveticaFont = awt_helveticaFont.deriveFont(14f); // set font size
		helveticaFont = new TrueTypeFont(awt_helveticaFont, false);
		
		biglogo = new Image("data/images/biglogo.png", false, Image.FILTER_NEAREST);
		image_map.put("biglogo", biglogo);
		
		
		return true;
	}
	
	public boolean init() {
		/*load static stuff*/		
		try {
			ship = new Image("data/images/logo.png", false, Image.FILTER_NEAREST);
			nh3 = new Image("data/images/nh3.png", false, Image.FILTER_NEAREST);
			
			molecule1 = new Image("data/images/molecule_b.png", false, Image.FILTER_NEAREST);
			molecule2 = new Image("data/images/molecule_m+c.png", false, Image.FILTER_NEAREST);
			molecule3 = new Image("data/images/molecule_agc.png", false, Image.FILTER_NEAREST);
			
			damage = new Image("data/images/damage.png");
			
			centihead  = new Image("data/images/centi.png", false, Image.FILTER_NEAREST);
			centibody = new Image("data/images/centi.png", false, Image.FILTER_NEAREST);
			
			shot = new Image("data/images/shot.gif", false, Image.FILTER_NEAREST);
			
			inputStream	= ResourceLoader.getResourceAsStream("data/fonts/arcadefont.ttf");
			awt_gameFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awt_gameFont = awt_gameFont.deriveFont(14f); // set font size
			gameFont = new TrueTypeFont(awt_gameFont, false);

			image_map.put("ship", ship);
			image_map.put("molecule1", molecule1);
			image_map.put("molecule2", molecule2);
			image_map.put("molecule3", molecule3);
			image_map.put("damage", damage);
			image_map.put("shot", shot);
			image_map.put("nh3", nh3);
			image_map.put("centihead", centihead);
			image_map.put("centibody", centibody);
				
			hit = new Sound("data/audio/hit.wav");
	
			shoot = new Sound("data/audio/shot.wav");
			sound_map.put("hit", hit);
			sound_map.put("shoot", shoot);

			
			agc_explosion = new SpriteSheet("data/images/explosion_molecule_agc.png", 64, 64);
			spritesheet_map.put("agc_explosion", agc_explosion);
			
			gen_explosion = new SpriteSheet("data/images/explosion_generic.png", 32, 32);
			spritesheet_map.put("gen_explosion", gen_explosion);
			
			player_explosion = new SpriteSheet("data/images/explosion.png", 64, 64);
			spritesheet_map.put("player_explosion", player_explosion);
			
			
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
	public SpriteSheet getSpriteSheet(String element) {
		return spritesheet_map.get(element);
	}
	
	public void scale(float scale) {
		return;
	}

	public TrueTypeFont getGameFont() {
		return gameFont;

	}

	public Font getAwtGameFont() {
		return awt_gameFont; 
	}
	
	public Font getAwtHelveticaFont() {
		return awt_helveticaFont;
	}
	public TrueTypeFont getHelveticaFont() {
		return helveticaFont;
	}
}
