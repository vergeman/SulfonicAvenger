package org.vergeman.sulfonicavenger;

import java.awt.FontFormatException;
import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Controllers;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameMenuState extends BasicGameState {

	private int stateId;
	
	String STATE_MSG = "SELECT   DIFFICULTY";
	String TITLE_MSG = "SULFONIC     AVENGER";
	String COMPANY_MSG = "PERMA  PURE";
	String PRESENTS_MSG = "presents";
	String AVENGERS_MSG = "TOP    AVENGERS";
	
	String[] LEVELS_MSGS = {"NORMAL", "KINETIC", "SULFONIC"};
	
	float STATE_SZ = 30f;
	float TITLE_SZ = 60f;
	float COMPANY_SZ = 70f;
	float PRESENTS_SZ = 20f;
	float LEVELS_SZ = 26f;
	float AVENGERS_SZ = 26f;	

	List<Score> high_scores;
	
	Gamepad gp;
	Input input;
	WindowManager windowManager;
	TextDrawManager textdrawManager;
	AssetManager assetManager;
	
	LevelManager level_manager;
	
	Sprite biglogo;
	Color cursor;
	
	// init->render->update order. make sure we update once otherwise we FOUT
	boolean run_once = false;

	public GameMenuState(int id, List<Score> high_scores, LevelManager level_manager) {
		super();
		this.stateId = id;
		this.high_scores = high_scores;
		this.level_manager = level_manager;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		run_once = false;

        assetManager = new AssetManager();

		gp = new Gamepad(container);

		input = container.getInput();

		windowManager = new WindowManager(container, game);


		try {
			assetManager.menu_init();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		biglogo = new Sprite(assetManager.getImage("biglogo"));
		
		textdrawManager = new TextDrawManager(container.getGraphics(),
				windowManager, assetManager);

		textdrawManager.build("state", STATE_SZ, true);
		textdrawManager.build("title_menu", TITLE_SZ, true);
		textdrawManager.build("levels", LEVELS_SZ, true);
		textdrawManager.build("avengers", AVENGERS_SZ, true);
		
		textdrawManager.build("company", COMPANY_SZ, false);
		textdrawManager.build("presents", PRESENTS_SZ, false);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		run_once = false;
		Controllers.clearEvents();
		level_manager.resetLevel();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		if (!run_once) {
			return;
		}
		
		/*draw this centered, as if in a relative box*/
		biglogo.draw(Math.max(20, (int) windowManager.getCenterX() - biglogo.getWidth() - 120), 
				Math.max(20, (int) windowManager.getCenterY() - biglogo.getHeight() / 2) );
		

		textdrawManager.draw("company", COMPANY_MSG, Color.blue,
				(container.getWidth() / 2 + 60), 
				Math.max(20, (int) windowManager.getCenterY() - biglogo.getHeight() / 2) ,
				-0.25, 0, 0, 0);

		textdrawManager.draw("presents", PRESENTS_MSG, Color.blue,
				(container.getWidth() / 2 + 60), 
				Math.max(20, (int) windowManager.getCenterY() - biglogo.getHeight() / 2 ) ,
				-1.4, 4, -1, 0);

		textdrawManager.draw("title_menu", TITLE_MSG, Color.red,
				(container.getWidth() / 2) +75, 
				Math.max(20, (int) windowManager.getCenterY() - biglogo.getHeight() / 2),
				-0.25, 10, 0, 0);

		textdrawManager.draw("state", STATE_MSG, Color.white,
				(container.getWidth() / 2), 
				Math.max(20, (int) windowManager.getCenterY()- biglogo.getHeight() / 2 ),
				0, 15, -57, 50);

		/*draw menu*/
		for (int i = 0; i < 3; i++ ) {
			cursor = Color.white;
			
			if (level_manager.getLevel().ordinal() == i) {
				cursor = Color.red;
			}
			
			textdrawManager.draw("levels", LEVELS_MSGS[i], cursor,
					(container.getWidth() / 2) + 25, 
					Math.max(20, (int) windowManager.getCenterY()- biglogo.getHeight() / 2 + 75),
					0, 17+2*i, 0, 0);
		}
		
		
		/*draw high scores if they exist*/
		if (high_scores.size() > 0) {
		
			textdrawManager.draw("avengers", "TOP  AVENGERS", Color.blue,
					(container.getWidth() / 2 ), 
					Math.max(20, (int) windowManager.getCenterY()- biglogo.getHeight() / 2 ),
					0, 21, 310, -35);
	
			for (int i = 0; i < high_scores.size(); i++) {
				textdrawManager.draw("avengers", high_scores.get(i).name + "        " + high_scores.get(i).score, Color.blue,
						(container.getWidth() / 2 ), 
						Math.max(20, (int) windowManager.getCenterY()- biglogo.getHeight() / 2 ),
						0, 23 + 2*i, 325, -35);		
			}
			
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		run_once = true;
		
		input.initControllers();
		
		gp.poll();
		
		if (input.isKeyPressed(Input.KEY_C)) {
			game.enterState(MyGame.GAMEPADTESTSTATE);			
		}

		if (input.isKeyPressed(Input.KEY_UP) || gp.isEventedControllerUp()) {
			level_manager.decreaseLevel();
		}
		if (input.isKeyPressed(Input.KEY_DOWN) || gp.isEventedControllerDown()) {
			level_manager.increaseLevel();
		}
		
		if (input.isKeyPressed(Input.KEY_ENTER) || gp.isEventedButtonPressed()) {
			game.enterState(MyGame.GAMEPLAYSTATE);
		}

		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			System.exit(0);
		}
		
		windowManager.isResized();

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return this.stateId;
	}

}
