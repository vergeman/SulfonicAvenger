package org.vergeman.sulfonicavenger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
			Sprite sprite_centihead, int size, int speed) {
		this.container = container;
		this.sprite_centibody = sprite_centibody;
		this.sprite_centihead = sprite_centihead;
		makeCentipede(size, speed);
		lives = size;
		isAlive = true;
		

	}
	
	public void setSpeed(int speed) {
		for (CentiBallEntity cb : centipede) {
			cb.move_speed = speed;
		}
	}
	public void makeCentipede(int size, int speed) {
		centipede = new ArrayList<CentiBallEntity>();
		// build centiPEDE
		boolean start_left = r.nextBoolean();
		
		centipede.add(new CentiBallEntity(container, sprite_centihead, 
				start_left ? -10 : container.getWidth() + 10, 0,
				true, speed));

		for (int c = 1; c < size; c++) {
			centipede.add(new CentiBallEntity(container, sprite_centibody, 
					start_left ? -c * sprite_centibody.getWidth() - 10 : c * sprite_centibody.getWidth() + (container.getWidth() +  10), 0, false, speed));
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
			ArrayList<MoleculeEntity> molecules, Sprite[] sprite_molecules, PlayerEntity player) {
		
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
					//increase difficulty
					//if (player.lives <= 5) {
						type =(int) (r.nextDouble() *3 -.01);
					//}
					//else {
					//	type =(int) (r.nextDouble() *2 -.01);
					//}
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
	
	
	public static ArrayList<Centipede> splice_update(GameContainer container, Sprite sprite_centihead, Sprite sprite_centibody, ArrayList<Centipede> centipedes) {
		ArrayList<Centipede> new_centis = new ArrayList<Centipede>();
		
		for (int i = 0; i < centipedes.size(); i++) {

			Centipede c = centipedes.get(i);
			List<CentiBallEntity> CentipedeBallList = ((List<CentiBallEntity>) c.centipede);

			boolean has_new = false;
			ArrayList<CentiBallEntity> temp = new ArrayList<CentiBallEntity>();

			int q = 0;
			for (int j = 0; has_new == false
					&& j < CentipedeBallList.size(); j++) {

				CentiBallEntity cbe = CentipedeBallList.get(j);

				if (!cbe.isDisplay()) {

					List<CentiBallEntity> l = CentipedeBallList.subList(
							q + 1, CentipedeBallList.size());

					temp = new ArrayList<CentiBallEntity>(l);

					l.clear();

					CentipedeBallList.remove(j);

					has_new = true;
				}
				q++;
			}

			if (temp.size() > 0) {
				new_centis.add(new Centipede(container, sprite_centibody,
						sprite_centihead, temp));
			}
		}

		// clean ball-less centipedes
		for (Iterator<Centipede> c = centipedes.iterator(); c.hasNext();) {
			Centipede cs = c.next();

			if (cs.centipede.size() <= 0 || cs.isAlive == false) {
				c.remove();
			}
		}

		return new_centis;
	}

}
