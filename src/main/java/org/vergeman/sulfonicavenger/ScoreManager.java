package org.vergeman.sulfonicavenger;

import java.util.Collections;
import java.util.List;
import org.lwjgl.Sys;
import org.lwjgl.input.Controllers;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ScoreManager {
	WindowManager windowManager;
	TextDrawManager textDrawManager;
	Input input;
	Gamepad gp;

	int score;
	int high_score;
	
	int last_life_score = 0;
	int last_level_score = 0;
	
	private int NUM_HIGH_SCORES = 5;
	private int LIFE_SCORE = 5000;
	private int LEVEL_SCORE = 3000;
	
	List<Score> high_scores;
	Character[] score_name = {'A', 'A', 'A'};
	int score_index;
	boolean score_flash;
	long score_flash_time;
	long SCORE_FLASH_INTERVAL = 250;
	
	
	boolean is_entering_score;
	int pause_counter;
	
	public ScoreManager(WindowManager windowManager, TextDrawManager textDrawManager, List<Score> high_scores) {
		this.windowManager = windowManager;
		this.textDrawManager = textDrawManager;
	
		this.high_scores = high_scores;
		this.high_score = high_scores.size() > 0 ? high_scores.get(0).score : 0;
		this.score_index = 0;
		this.is_entering_score = false;
		this.pause_counter = 0;
	}

	public void init() {
		score_name = new Character[]{'A', 'A', 'A'};
	}
	
	public int getPauseCounter() {
		return pause_counter;
	}
	public void setPauseCounter(int time) {
		this.pause_counter = time;
	}
	public void setInput(Input input, Gamepad gp) {
		this.input = input;
		this.gp = gp;
	}
	public void setFlash(boolean flash) {
		this.score_flash = flash;
	}
	public void setFlashTime(long time) {
		this.score_flash_time = time;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getScore() {
		return score;
	}
	
	public int getHighScore() {
		return high_score;
	}
	public void setHighScore() {
		this.high_score = Math.max(getScore(), getHighScore());
	}
	
	public void setLastLifeScore(int score) {
		this.last_life_score = score;
	}
	public int getLastLifeScore() {
		return this.last_life_score;
	}
	public void increaseLastLifeScore(int score) {
		this.last_life_score += score;
	}
	
	public int getLastLevelScore() {
		return this.last_level_score;
	}
	public void setLastLevelScore(int score) {
		this.last_level_score = score;
	}
	public void increaseLastLevelScore(int score) {
		this.last_level_score += score;
	}
	
	public void increaseScore(int up) {
		this.score += up;
	}
	
	
	public boolean isNewLife() {
		 if (getScore() - getLastLifeScore()  >= LIFE_SCORE)  {
			 increaseLastLifeScore(LIFE_SCORE); 
			 return true;
		 }
		 return false;
	}
	
	public boolean isNewLevel() {
		if (getScore() - getLastLevelScore() >= LEVEL_SCORE) {
			increaseLastLevelScore(LEVEL_SCORE);
			return true;
		}
		return false;
	}
	
	
	
	public void updateHighScore(GameContainer container, StateBasedGame game, long delta) throws SlickException {
		this.pause_counter -=delta;
		
		if (!is_entering_score && pause_counter <= 0) {
			input.initControllers();
			Controllers.clearEvents();
			Collections.sort(high_scores);
			/* find our score on the high score list if it qualifies
			 * and add it
			 */
			for (int s = 0; !is_entering_score && s < high_scores.size(); s++) {
		
				if (score > high_scores.get(s).score) {
					high_scores.add(s, new Score(score, null));
					is_entering_score = true;
				}
			}
			//make sure also add for size 0
			if (!is_entering_score && high_scores.size() <= NUM_HIGH_SCORES ) {
				high_scores.add(new Score(score, null));
				is_entering_score = true;
			}
			//truncate list 
			if (high_scores.size() > NUM_HIGH_SCORES ) {
				high_scores.remove(high_scores.size()-1);
			}
			
		}
		
		//flash render high score
		if (Sys.getTime() - score_flash_time > SCORE_FLASH_INTERVAL) {
			score_flash = !score_flash;
			score_flash_time = Sys.getTime();
		}

		if (is_entering_score) {
			gp.poll();
			
			if (input.isKeyPressed(Input.KEY_LEFT) || gp.isEventedControllerLeft()) {
				if (score_index <= 0) {
					score_index = score_name.length-1;
				}
				else {
					score_index = (score_index-1) % score_name.length;
				}
			}
			if (input.isKeyPressed(Input.KEY_RIGHT) || gp.isEventedControllerRight()) {
				score_index = (score_index+1) % score_name.length;
			}
			if (input.isKeyPressed(Input.KEY_DOWN) || gp.isEventedControllerDown()) {
				if (score_name[score_index] < 'Z') {
					score_name[score_index]++;
				}
			}
			if (input.isKeyPressed(Input.KEY_UP) || gp.isEventedControllerUp()) {
				if (score_name[score_index] > 'A') {
					score_name[score_index]--;
				}
			}
		}
		
		//case where there may be no high score, we just wait at game over until input
		if (!is_entering_score && (pause_counter < 0 || (input.isKeyPressed(Input.KEY_ENTER) || 
				gp.isEventedButtonPressed()))) {
			is_entering_score =false;			
			game.enterState(MyGame.GAMEMENUSTATE);
		}
		
	}
	
	
	
	
	public void draw(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

			g.setColor(Color.lightGray);
			g.fillRect(container.getWidth() / 4 -1, 
					(int) windowManager.getCenterY() / 8 -1, 
					container.getWidth() / 2 + 2, 
					(int) windowManager.getCenterY() + (int) windowManager.getCenterY() / 4 + 2);
		
			g.setColor(Color.black);
			g.fillRect(container.getWidth() / 4, 
					(int) windowManager.getCenterY() / 8, 
					container.getWidth() / 2, 
					(int) windowManager.getCenterY() + (int) windowManager.getCenterY() / 4);
			
			
			g.setColor(Color.white);
		
			textDrawManager.draw("state", "HIGH   SCORES", Color.white,
					container.getWidth() / 2,
					(int) windowManager.getCenterY() / 4, -0.5, 0.5, 0, 0);

			/* FLASH LETTERS */
			int q = 1;
			int score_pos = high_scores.size();

			//multiple scores are being edited
			for (Score s : high_scores) {

				if (s.name == null) {
					score_pos = q-1;
					textDrawManager.draw("score", "A", Color.white,
							container.getWidth() + 200, 0, 0, 0, 0, 0);

					int w = textDrawManager.getWidth("score");
					
					//For each letter
					for (int x = 0; x < score_name.length; x++) {
						// because our class sucks, we need to load a last message size of 1
						
						if (x == score_index && score_flash) {
							// time is set in update()

						} 
						else {
							textDrawManager
									.draw("score",
											score_name[x].toString(),
											Color.white,
											container.getWidth() / 2,
											(int) windowManager.getCenterY() / 4,
											0, 2 * q, -textDrawManager.getWidth("state") / 2 + x * w, 20);
						}
					}
				}
				else {
					//drawn name
					textDrawManager.draw("score", s.name, Color.white,
							container.getWidth() / 2,
							(int) windowManager.getCenterY() / 4, 0, 2 * q,
							-textDrawManager.getWidth("state") / 2, 20);

				}
				//draw score
				textDrawManager.draw("score", "" + s.score, Color.white,
						container.getWidth() / 2,
						(int) windowManager.getCenterY() / 4, 0, 2 * q, 0, 20);

				q++; // vertical spacing of high scores increment

			}

			//we'll allow input logic in the render space since it's not "mission critical"
			if (input.isKeyPressed(Input.KEY_ENTER) || gp.isEventedButtonPressed()) {
				
				is_entering_score = false;
				
				if (score_pos < high_scores.size() ) {
					high_scores.get(score_pos).name = 
							new String("" + score_name[0].charValue() + score_name[1].charValue() + score_name[2].charValue());
				}
				game.enterState(MyGame.GAMEMENUSTATE);
				
			}
	}
}
