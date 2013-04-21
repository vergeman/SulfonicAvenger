package org.vergeman.sulfonicavenger;

import java.awt.FontFormatException;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameMenuState extends BasicGameState {

	private int stateId;
	String STATE_MSG = "PRESS     ENTER     TO     PLAY";
	Gamepad gp;
	Input input;
	float STATE_SZ = 40f;
	WindowManager windowManager;
	TextDrawManager textdrawManager;
	AssetManager assetManager;

	// init->render->update order. make sure we update once otherwise we FOUT
	boolean run_once = false;

	public GameMenuState(int id) {
		super();
		this.stateId = id;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		run_once = false;
		gp = new Gamepad(container);
		input = container.getInput();

		windowManager = new WindowManager(container, game);

		assetManager = new AssetManager();

		try {
			assetManager.menu_init();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		textdrawManager = new TextDrawManager(container.getGraphics(),
				windowManager, assetManager);

		textdrawManager.build("state", STATE_SZ);

	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		run_once = false;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		if (!run_once) {
			return;
		}

		textdrawManager.draw("state", STATE_MSG, Color.white,
				(container.getWidth() / 2), (int) windowManager.getCenterY(),
				-0.5, 0, 0, 0);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		run_once = true;

		gp.poll();

		if (input.isKeyPressed(Input.KEY_ENTER) || gp.isEventedButtonPressed()) {
			game.enterState(MyGame.GAMEPLAYSTATE);
		}

		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			System.exit(0);
		}

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return this.stateId;
	}

}
