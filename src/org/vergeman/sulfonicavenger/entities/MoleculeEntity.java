package org.vergeman.sulfonicavenger.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.vergeman.sulfonicavenger.AssetManager;
import org.vergeman.sulfonicavenger.Sprite;

public class MoleculeEntity extends Entity {

	int lives;
	int score;
	public boolean remove = false;
	public ArrayList<String> damages;
	Random r;
	
	public MoleculeEntity(Sprite sprite, int type, int x, int y) {
		super(sprite, x, y);
		
		//Molecule Type M+C / B / AGC
		switch (type) {
		case 3: //AGC
			lives = 20;
			score = 500;
			break;
		default:
			score = 50;
			lives = 10;
			break;
		
		}
		damages = new ArrayList<String>();
		r = new Random();
	}

	public int getScore() {
		return score;
	}
	@Override
	public void collidedWith(Entity other) {
		
		if (other instanceof ShotEntity) {
			--lives;
			if (lives <= 0) {
				remove = true;
			}
			damages.add(
					(int) (this.x + r.nextDouble() * sprite.getWidth())  + "-" 
			+ (int) (this.y + (r.nextDouble() * sprite.getHeight())));
		}
		
	}

	@Override
	public void reposition(int x, int y, float scale_rel) {
		// TODO Auto-generated method stub
		// bounds
		this.updateBounds(0, x - this.sprite.getWidth(), 0,
				y - this.sprite.getHeight());

		// placement - TODO: fucked up needs wrk
		this.x = Math.min(Math.abs(this.x * scale_rel), this.BOUNDS_RIGHT);
		this.y = Math.min(Math.abs(this.y * scale_rel), this.BOUNDS_BOTTOM);

	}

	
	public static ArrayList<MoleculeEntity> initMolecules(GameContainer container, AssetManager assetManager,
			int NUM_MOLECULES, Sprite[] sprite_molecules) {
		ArrayList<MoleculeEntity> molecules = new ArrayList<MoleculeEntity>();
		Random r = new Random();
		
		Sprite sprite_molecule1 = new Sprite(assetManager.getImage("molecule1"));
		Sprite sprite_molecule2 = new Sprite(assetManager.getImage("molecule2"));
		Sprite sprite_molecule3 = new Sprite(assetManager.getImage("molecule3"));
		sprite_molecules[0] = sprite_molecule1;
		sprite_molecules[1] = sprite_molecule2;
		sprite_molecules[2] = sprite_molecule3;

		
		int w, h;
		int s_w = sprite_molecule3.getWidth();
		int s_h = sprite_molecule3.getHeight();
		HashMap<String, Boolean> molecule_pos = new HashMap<String, Boolean>();

		// spawn evenly on sprite dimensions
		while (molecule_pos.size() < NUM_MOLECULES) {
			w = ((int) (r.nextDouble() * container.getWidth()) / s_w) * s_w;

			h = ((int) (r.nextDouble() * (container.getHeight() - container
					.getHeight() / 3)) / s_h) * s_h;

			if (!(h == 0 && w < 5 * s_w)) {
				molecule_pos.put(w + "-" + h, true);
			}
		}

		int type;
		for (String pos : molecule_pos.keySet()) {
			type = (int) (r.nextDouble() * 3 - .1);
			molecules.add(new MoleculeEntity(sprite_molecules[type], type + 1,
					Integer.valueOf(pos.split("-")[0]), Integer.valueOf(pos
							.split("-")[1])));
		}

		return molecules;
	}
	
}
