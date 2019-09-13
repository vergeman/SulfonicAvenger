package org.vergeman.sulfonicavenger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GamePlayState extends BasicGameState {

	private int stateID;
	
	//values are denoted in init();
	private int CENTIPEDE_SIZE;
	private int MAX_CENTIPEDES;
	private int NUM_SHOTS;
	private int NUM_MOLECULES;
	private int NUM_NH3;
	private int NH3SpawnInterval; // ms
	private long CentipedeInterval; // ms
	int NH3_SPEED;
	int CENTIPEDE_SPEED;
	private int OLD_CENTIPEDE_SIZE = CENTIPEDE_SIZE;
	private int OLD_MAX_CENTIPEDES = MAX_CENTIPEDES;
	private int OLD_NUM_NH3 = NUM_NH3;
	private int OLD_NH3SpawnInterval = NH3SpawnInterval; // ms
	private long OLD_CentipedeInterval = CentipedeInterval; // ms
	private int OLD_CENTIPEDE_SPEED = CENTIPEDE_SPEED;
	private int OLD_NH3_SPEED = NH3_SPEED;
	
	long lastCentipede;
	long lastNH3;

	int level_count = 0;
	
	private enum STATES {
		START_GAME_STATE, PLAY_GAME_STATE, GAME_OVER_STATE
	}

	private STATES currentState = null;

	String TITLE_MSG = "SULFONIC          AVENGER";
	float TITLE_SZ = 30f;
	String SCORE_MSG = "SCORE  ";
	float SCORE_SZ = 30f;
	String STATE_MSG;
	float STATE_SZ = 40f;
	String HIGH_SCORE_MSG = "HIGH   SCORE  ";

	WindowManager windowManager;
	AssetManager assetManager;
	TextDrawManager textDrawManager;

	Color background;
	Input input;

	Random r;

	PlayerEntity player;
	ArrayList<MoleculeEntity> molecules;
	Sprite[] sprite_molecules;
	ArrayList<NH3Entity> nh3s;
	ShotEntity[] shots;
	Sprite sprite_damage;
	Sprite sprite_centibody;
	Sprite sprite_centihead;
	Sprite sprite_shot;
	Sprite sprite_nh3;
	ArrayList<Centipede> centipedes;
	boolean newCentipede = false;

	ScoreManager scoreManager;
	List<Score> high_scores;
	Gamepad gp;

	ArrayList<Animator> animators;
	
	boolean run_once;
	
	LevelManager level_manager;
	
	public GamePlayState(int id, List<Score> high_scores, LevelManager level_manager) {
		super();
		this.stateID = id;
		this.high_scores = high_scores;
		this.level_manager = level_manager;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	
		run_once = false;
		//reinit game params
		CENTIPEDE_SIZE = 8;
		MAX_CENTIPEDES = 3;
		NUM_SHOTS = 10;
		NUM_MOLECULES = 10;
		NUM_NH3 = 2;
		NH3SpawnInterval = 45000; // ms
		CentipedeInterval = 10000; // ms
		NH3_SPEED = 170;
		CENTIPEDE_SPEED = 250;
		//reset old values
		OLD_CENTIPEDE_SIZE = CENTIPEDE_SIZE;
		OLD_MAX_CENTIPEDES = MAX_CENTIPEDES;
		OLD_NUM_NH3 = NUM_NH3;
		OLD_NH3SpawnInterval = NH3SpawnInterval; // ms
		OLD_CentipedeInterval = CentipedeInterval; // ms
		OLD_NH3_SPEED = NH3_SPEED;
		OLD_CENTIPEDE_SPEED = CENTIPEDE_SPEED;
		
		input = container.getInput();
		gp = new Gamepad(container);
		
		container.setShowFPS(false);
		container.getGraphics().setBackground(Color.black);
		
		windowManager = new WindowManager(container, game);
		assetManager = new AssetManager();
		assetManager.init();
		
		r = new Random();
		
		textDrawManager = new TextDrawManager(container.getGraphics(),
				windowManager, assetManager);
		textDrawManager.build("state", STATE_SZ, true);
		textDrawManager.build("title", TITLE_SZ, true);
		textDrawManager.build("score", SCORE_SZ, true);
		textDrawManager.build("high_score", SCORE_SZ, true);

		scoreManager = new ScoreManager(windowManager, textDrawManager, high_scores);

		animators = new ArrayList<Animator>();
		
		player = new PlayerEntity(
				container,
				assetManager,
				new Sprite(assetManager.getImage("ship")),
				(int) windowManager.getCenterX(),
				(int) (windowManager.getCenterY() + windowManager.getCenterY() / 2), animators);

		scoreManager.setScore(0);
		scoreManager.setFlash(false);
		scoreManager.setFlashTime(Sys.getTime());

		scoreManager.setLastLifeScore(0);
		scoreManager.setLastLevelScore(0);
		level_count = 0;
		
		
		/* // MOLECULES */
		sprite_molecules = new Sprite[3];
		molecules = new ArrayList<MoleculeEntity>();
		molecules = MoleculeEntity.initMolecules(container, assetManager, NUM_MOLECULES, sprite_molecules);
		
		sprite_damage = new Sprite(assetManager.getImage("damage"));
				
		/* SHOTS */
		sprite_shot = new Sprite(assetManager.getImage("shot"));
		shots = new ShotEntity[NUM_SHOTS];
		for (int s = 0; s < NUM_SHOTS; s++) {
			shots[s] = new ShotEntity(sprite_shot, -3000, -3000);
		}
		player.setShots(shots);

		/* NH3 -SPIDERS */
		sprite_nh3 = new Sprite(assetManager.getImage("nh3"));
		nh3s = new ArrayList<NH3Entity>();
		for (int z = 0; z < NUM_NH3; z++) {
			nh3s.add(new NH3Entity(container, sprite_nh3, r.nextBoolean() ? 0
				: windowManager.get_orig_width(), (int) (windowManager
				.get_orig_height() - 200 + (r.nextDouble() * 200)),  NH3_SPEED));
		}
		lastNH3 = Sys.getTime();

		
		/* CENTIPEDE */
		sprite_centibody = new Sprite(assetManager.getImage("centibody"));
		sprite_centihead = new Sprite(assetManager.getImage("centihead"));

		centipedes = new ArrayList<Centipede>();
		centipedes.add(new Centipede(container, sprite_centihead,
				sprite_centibody, CENTIPEDE_SIZE, CENTIPEDE_SPEED));
		lastCentipede = Sys.getTime();
		
		Music theme = new Music("data/audio/theme.ogg");
		theme.loop();

		input.addKeyListener(player);
	}

	/**ENTER 
	 * from Menu state**/
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {

		super.enter(container, game);
		init(container, game);

		input.initControllers();
		player.alive = true;
		player.can_collide = true;
		player.game_over = false;
		player.x = windowManager.getCenterX();
		player.y = windowManager.getCenterY() + windowManager.getCenterY() / 2;

		scoreManager.init();
		scoreManager.setInput(input, gp);
		scoreManager.setScore(0);
		
		int lvl = level_manager.getLevel().ordinal();
		
		for (int i = 0; i < 2*lvl; i++) {
			LevelUp();
		}
		
		currentState = STATES.PLAY_GAME_STATE;
	}

	
	/** UPDATE **/
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		run_once = true;
		windowManager.isResized();
		
		/* GAME_STATE */
		switch (currentState) {
			
		case PLAY_GAME_STATE:
			STATE_MSG = null;
			input.addKeyListener(player);
			scoreManager.setPauseCounter(2000);
			break;

			
		case GAME_OVER_STATE:
			input.removeKeyListener(player);
			Input.disableControllers();
			player.x = -2000;
			player.y = -2000;

			if (scoreManager.getPauseCounter() == 2000) {
				STATE_MSG = "GAME     OVER";
			}

			scoreManager.updateHighScore(container, game, delta);
			break;
		}

		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			System.exit(0);
		}

		
		
		/* begin collision detects */
		player.move(delta, molecules);
		
		/** spawn H3 */
		if (Sys.getTime() - lastNH3 > NH3SpawnInterval) {
			for (int x = 0; x<NUM_NH3; x++) {
				nh3s.add(new NH3Entity(container, sprite_nh3, r.nextBoolean() ? 0
					: windowManager.get_orig_width(), (int) (windowManager
					.get_orig_height() - 200 + (r.nextDouble() * 200)),  NH3_SPEED));

			}
			lastNH3 = Sys.getTime();
		}

		
		/* move NH3's */
		for (Iterator<NH3Entity> i = nh3s.iterator(); i.hasNext();) {
			NH3Entity n = i.next();

			n.move(delta, molecules, centipedes);

			if (n.collidesWith(player)) {
				n.collidedWith(player);
				player.collidedWith(n);
				
				if (player.isGameOver()) {
					currentState = STATES.GAME_OVER_STATE;
				}
			}

			//remove NH3
			if (n.remove_me) {
				i.remove();
			}

		}

		// move centiballs & smooth
		for (Centipede c : centipedes) {
			c.move(delta, molecules);
			if (c.checkCollisions(player)) {
				
				if (player.isGameOver()) {
					currentState = STATES.GAME_OVER_STATE;
				}
			}
		}
		
		/* Shots & Collisions*/
		for (ShotEntity s : shots) {

			// shots and molecules
			for (Iterator<MoleculeEntity> i = molecules.iterator(); i.hasNext();) {
				MoleculeEntity m = i.next();
				if (s.collidesWith(m)) {
					s.collidedWith(m);
					m.collidedWith(s);
					assetManager.getSound("hit").play();
				}
				if (m.remove) {
					updateScore(m.getScore());
					
					//agc molecule
					if (m.sprite.equals(sprite_molecules[2])) {
						animators.add(new Animator(assetManager.getSpriteSheet("agc_explosion"), m.x, m.y,
								0,0,3,3, true, 75, true));
					}
					else 
					{
						animators.add(new Animator(assetManager.getSpriteSheet("gen_explosion"), m.x, m.y,
							0,0,2,2, true, 50, true));
					}
					
					i.remove();
				}
			}

			// shots and NH3's
			for (Iterator<NH3Entity> i = nh3s.iterator(); i.hasNext();) {
				
				NH3Entity n = i.next();
				
				if (n.display && s.collidesWith(n)) {
					s.collidedWith(n);
					n.collidedWith(s);
					updateScore(n.getScore());
					
					assetManager.getSound("hit").play();
					
					animators.add(new Animator(assetManager.getSpriteSheet("gen_explosion"), n.x, n.y,
							0,0,2,2, true, 100, true));
					
					i.remove();
				}

			}

			// shots and centiballs - toggle display
			for (Iterator<Centipede> i = centipedes.iterator(); i.hasNext();) {
				
				Centipede c = i.next();

				updateScore(c.checkCollisions(s, molecules, sprite_molecules, player));
				// no centis, toggle spawn
				if (!c.isAlive) {
					i.remove();
					newCentipede = true;
				}

			}
			// Verify hit, splice and update (new) Centipede objects
			ArrayList<Centipede> new_centis = Centipede.splice_update(container, sprite_centihead, sprite_centibody, centipedes);
			
			// add new centipedes
			if (new_centis.size() > 0) {
				centipedes.addAll(new_centis);
			}

			// update only displayable shots
			if (s.isDisplay()) {
				s.move(delta);
			}

		}
		// end shots

		// spawn centipede
		if ((centipedes.size() < MAX_CENTIPEDES && newCentipede) || 
				(Sys.getTime() - lastCentipede > CentipedeInterval)) {
			
			centipedes.add(new Centipede(container, sprite_centihead,
					sprite_centibody, CENTIPEDE_SIZE, CENTIPEDE_SPEED));
			newCentipede = false;
			lastCentipede = Sys.getTime();
		}

		/*check animation states*/
		for (Iterator<Animator> i = animators.iterator(); i.hasNext();) {
			
			Animator a = i.next();
			
			if (a.isStopped()) {
				i.remove();
			}
		}		
	}

	
	/** RENDER **/
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		if (!run_once) {
			return;
		}
		
		for (Centipede c : centipedes) {
			c.draw();
		}

		for (MoleculeEntity molecule : molecules) {
			molecule.draw();
			for (String coord : molecule.damages) {
				if (! (coord.split("-")[0].equals("") || coord.split("-")[1].equals(""))) {
				sprite_damage.draw(Integer.parseInt(coord.split("-")[0]),
						Integer.parseInt(coord.split("-")[1]));
				}
			}
		}

		for (NH3Entity n : nh3s) {
			if (n.display) {
				n.draw();
			}
		}

		if (!player.isGameOver()) {
			player.draw();
		}

		for (ShotEntity s : shots) {
			if (s.isDisplay()) {
				s.draw();
			}
		}

		for (Animator a : animators) {
			a.draw();
		}
		
		/*
		 * Game Over, lets pause for gameover msg to display,
		 * then enter high scores
		 */
		if (currentState == STATES.GAME_OVER_STATE && scoreManager.getPauseCounter() <= 0) {
			scoreManager.draw(container, game, g);
		}
		
		
		// render last for z-index
		textDrawManager.draw("state", STATE_MSG, Color.white,
				(container.getWidth() / 2), (int) windowManager.getCenterY(),
				-0.5, 0, 0, 0);
		textDrawManager.draw("title", TITLE_MSG, Color.white,
				(container.getWidth() / 2), container.getHeight(), -0.5, -2, 0,
				0);
		textDrawManager.draw("score", SCORE_MSG + scoreManager.getScore(), Color.red, 20,
				container.getHeight(), 0, -2, 0, 0);

		textDrawManager.draw("high_score", HIGH_SCORE_MSG + scoreManager.getHighScore(),
				Color.blue, container.getWidth(), container.getHeight(), -1,
				-2, -20, 0);

		textDrawManager.draw("score", "LIVES  " + player.lives, Color.red,
				20 + textDrawManager.getWidth("score"), container.getHeight(),
				0, -2, 40, 0);

	}


	@Override
	public int getID() {
		return this.stateID;
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
	}

	@Override
    public void controllerButtonPressed(int controller, int button) {
        super.controllerButtonPressed(controller, button);
    }
    
	
	
	public void updateScore(int up) {

		if (currentState == STATES.PLAY_GAME_STATE) {
			scoreManager.increaseScore(up);

			if (scoreManager.isNewLife()) {
				player.lives += 1;
			}

			if (scoreManager.isNewLevel()) {
				LevelUp();
			}
		}
		scoreManager.setHighScore();
	}


	public void LevelUp() {
		//save old
		if (level_count % 5 == 2) {
			//we want a breather level every three...so we set these to zero
			MAX_CENTIPEDES = 0;
			NH3SpawnInterval = 45000; // ms
			NUM_NH3 = 2;
			NH3_SPEED = 170;
			
			for (Centipede c : centipedes) {
				c.setSpeed(200);
			}
			
		}
		
		else {
			//save "hardest" of old values
			OLD_CENTIPEDE_SIZE = Math.min(14, Math.max(CENTIPEDE_SIZE, OLD_CENTIPEDE_SIZE));
			OLD_MAX_CENTIPEDES = Math.max(MAX_CENTIPEDES, OLD_MAX_CENTIPEDES);
			OLD_NUM_NH3 = Math.max(NUM_NH3, OLD_NUM_NH3);
			
			OLD_NH3SpawnInterval = Math.min(NH3SpawnInterval, OLD_NH3SpawnInterval); // ms
			OLD_CentipedeInterval = Math.min(CentipedeInterval, OLD_CentipedeInterval); // ms
			
			OLD_NH3_SPEED = Math.max(NH3_SPEED, OLD_NH3_SPEED);
			OLD_CENTIPEDE_SPEED = Math.max(CENTIPEDE_SPEED, OLD_CENTIPEDE_SPEED);
			
			//augment by ~15%
			CENTIPEDE_SIZE = (int) (OLD_CENTIPEDE_SIZE + 1);
			MAX_CENTIPEDES = (int) (MAX_CENTIPEDES + 1);
			NUM_NH3 = (int) (OLD_NUM_NH3 + 1);
			NH3SpawnInterval -= .15 * OLD_NH3SpawnInterval; // ms
			CentipedeInterval -= .15 * OLD_CentipedeInterval; // ms
					
			//speed
			NH3_SPEED += .10 * OLD_NH3_SPEED;
			CENTIPEDE_SPEED += .10 * OLD_CENTIPEDE_SPEED;
		}
		level_count++;
		
	}


}

