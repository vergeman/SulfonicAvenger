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
		// build centi
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
		for (CentiBallEntity c : centipede) {
			c.move(delta, molecules);
		}
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
					
					//exclude spawn point here
					molecules.add(new MoleculeEntity(sprite_molecules[type], type+1, c.getX(),
							c.getY()));

					i.remove();
					--lives;
				}

				if (lives <= 0) {
					isAlive = false;
					centi_score += 0;
				}

				if (c.getY() > container.getHeight()) {
					i.remove();
					--lives;
					//centi_score -= 50;
				}
			}

		}
		return centi_score;
	}
}
