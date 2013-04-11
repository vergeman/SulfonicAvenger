package org.vergeman.sulfonicavenger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MyGame extends StateBasedGame {
	/* original dims from which we scale off of */
	private final static int WIDTH = 1024;
	private final static int HEIGHT = 768;

	public final static int MAINMENUSTATE = 0;
	public final static int GAMEPLAYSTATE = 1;
	public final static int EXITSTATE = 2;

	public static void main(String[] args) throws SlickException {

		try {

			AppGameContainer app = new AppGameContainer(new MyGame());

			app.setDisplayMode(WIDTH, HEIGHT, false);
			app.setResizable(true);
			app.start();
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public MyGame() {
		super("Sulfonic Avenger -- PROTOTYPE");
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		// addState(new MainMenuGameState(MAINMENUSTATE));
		GamePlayState state = new GamePlayState(1);
		addState(state);

	}

}