package org.vergeman.sulfonicavenger;

public class LevelManager {

	public enum LEVELS { 
		EASY, MEDIUM, HARD;
		
		public LEVELS increase() {
             return values()[Math.min(2, (ordinal() + 1) % values().length)];
		}
		public LEVELS decrease() {
			return  ( ( (ordinal() - 1) % values().length) < 0 ? 
					values()[2] : values()[((ordinal() - 1) % values().length)] );
		
		}
	}
	
	LEVELS level;
	
	public LevelManager() {
		level = LEVELS.EASY;
	}
	
	public void increaseLevel() {
		this.level = level.increase();
	}
	public void decreaseLevel() {
		this.level = level.decrease();
	}
	
	public LEVELS getLevel() {
		return level;
	}
}
