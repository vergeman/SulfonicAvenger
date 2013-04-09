package org.vergeman.sulfonicavenger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.GameContainer;

public class Centipede {
	ArrayList<CentiBallEntity> centipede;
	GameContainer container;
	Sprite sprite_centibody;
	Sprite sprite_centihead;
	Random r = new Random();
	
	boolean isAlive;
	int lives;
	int start_pos;

	public Centipede(GameContainer container, Sprite sprite_centibody,
			Sprite sprite_centihead, ArrayList<CentiBallEntity> centiballs) {
		this.container = container;
		this.sprite_centibody = sprite_centibody;
		this.sprite_centihead = sprite_centihead;
		this.centipede = centiballs; 
		lives = centiballs.size();
		isAlive = true;
	}

	public Centipede(GameContainer container, Sprite sprite_centibody,
			Sprite sprite_centihead, int size) {
		this.container = container;
		this.sprite_centibody = sprite_centibody;
		this.sprite_centihead = sprite_centihead;
		makeCentipede(size);
		lives = size;
		isAlive = true;

	}
	
	public void makeCentipede(int size) {
		centipede = new ArrayList<CentiBallEntity>();
		// build centiPEDE
		boolean start_left = r.nextBoolean();
		
		centipede.add(new CentiBallEntity(container, sprite_centihead, 
				start_left ? -10 : container.getWidth() + 10, 0,
				true));

		for (int c = 1; c < size; c++) {
			centipede.add(new CentiBallEntity(container, sprite_centibody, 
					start_left ? -c * sprite_centibody.getWidth() - 10 : c * sprite_centibody.getWidth() + (container.getWidth() +  10), 0, false));
		}

	}

	public void resize(int x, int y, float scale_abs, float scale_rel) {
		for (CentiBallEntity c : centipede) {
			c.resize(x, y, scale_abs, scale_rel);
		}
	}

	public void move(long delta, ArrayList<MoleculeEntity> molecules) {
		
		centipede.get(0).move(delta, molecules);
		
		for (int c = centipede.size()-1; c > 0; c--) {
			//nudge fix for deltas
			if (centipede.get(c).y == centipede.get(0).y) {
				
				if (centipede.get(c).dx >= 0) {
					centipede.get(c).x = centipede.get(0).x - c*centipede.get(c).sprite.getWidth();
				}
				else {
					centipede.get(c).x = centipede.get(0).x + c*centipede.get(c).sprite.getWidth();
				}
			}	
			else {
				centipede.get(c).move(delta, molecules);
			}
		}
		//off screen, toggle death
		if (centipede.get(centipede.size() - 1).getY() > centipede.get(centipede.size()-1).BOUNDS_BOTTOM) {
			isAlive =false;
		}
		/*
		for (CentiBallEntity c: centipede) {
			c.move(delta, molecules);
		}
	*/
	 
	}

	public void draw() {
		for (CentiBallEntity c : centipede) {
			if (c.isDisplay()) {
				c.draw();
			}
		}

	}

	public boolean checkCollisions(Entity p) {
		for (Iterator<CentiBallEntity> i = centipede.iterator(); i.hasNext();) {
			CentiBallEntity c = i.next();

			if (p.collidesWith(c)) {
				p.collidedWith(c);
				c.collidedWith(p);
				return true;
			}
		}
		return false;
	}

	//centipede is this centipede
	//centipedes is our list of centipedes
	public int checkCollisions(ShotEntity s,
			ArrayList<MoleculeEntity> molecules, Sprite[] sprite_molecules) {
		
		int centi_score = 0;

		for (Iterator<CentiBallEntity> i = centipede.iterator(); i.hasNext();) {
			CentiBallEntity c = i.next();

			if (s.collidesWith(c)) {
				s.collidedWith(c);
				c.collidedWith(s);
				centi_score += 25;

				// replace centibody w/ molecule
				int type;
				if (!c.isDisplay()) {
					type =(int) (r.nextDouble() *3 -.01);
					
					molecules.add(new MoleculeEntity(sprite_molecules[type], type+1, c.getX(),
							c.getY()));

					--lives;
				}

				if (c.getY() > container.getHeight()) {
					i.remove();
					--lives;
				}

				if (lives <= 0) {
					isAlive = false;
				}

			}

		}
		return centi_score;
	}
}
