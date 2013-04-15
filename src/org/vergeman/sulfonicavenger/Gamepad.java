package org.vergeman.sulfonicavenger;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Gamepad {
	enum OS {
		WINDOWS, LINUX, MACOSX
	}

	int index;
	float x_value, y_value;
	boolean next, button;
	
	OS os;
	GameContainer container;
	Input input;
	int count;
	int x_axis, y_axis;
	long PRESS_INTERVAL = 2;
	long last_press;
	
	public Gamepad(GameContainer container) {
		if (LWJGLUtil.getPlatformName().equals("windows")) {
			os = OS.WINDOWS;
		}
		if (LWJGLUtil.getPlatformName().equals("linux")) {
			os = OS.LINUX;
		}
		if (LWJGLUtil.getPlatformName().equals("macosx")) {
			os = OS.MACOSX;
		}
	
		this.container = container;
		this.input = container.getInput();
		try {
			input.initControllers();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		switch (os) {
			case WINDOWS:
				y_axis = 0;
				x_axis = 1;
				break;
			case LINUX:
				x_axis = 3;
				y_axis = 4;
				break;
		}
		
		this.count = input.getControllerCount();
		this.last_press = Sys.getTime();
	}
	
	public boolean detect() {
		if (input.getControllerCount() > 0) {
			return true;
		}
		return false;
	}
	
	public void poll() {

		next = false;
		if (Controllers.next()) {
			next = true;
			Controller c = Controllers.getEventSource();
			x_value = c.getAxisValue(x_axis);
			y_value = c.getAxisValue(y_axis);
			button = c.isButtonPressed(0) || c.isButtonPressed(1) || c.isButtonPressed(2);
			
		}
		
	}
	
	
	public boolean isControllerRight() {
		return count == 0 ? false : input.getAxisValue(0, x_axis) >= 0.5f ? true : false;				
	}
	//works but we consume next in first call to left before we verify it's correwct
	public boolean isEventedControllerRight() {
		if (next) {
			return count == 0 ? false : x_value >= 0.5f ? true : false;			
		}
		return false;
	}
	
	public boolean isControllerLeft() {
		return count == 0 ? false : input.getAxisValue(0, x_axis) <= -0.5f ? true : false;				
	}

	public boolean isEventedControllerLeft() {
		if (next) {
			return count == 0 ? false : x_value <= -0.5f ? true : false;			
		}
		return false;
	}
	
	public boolean isControllerUp() {
		return count == 0 ? false : input.getAxisValue(0, y_axis) <= -0.5f ? true : false;				
	}
	
	public boolean isEventedControllerUp() {
		if (next) {
			return count == 0 ? false : y_value <= -0.5f ? true : false;			
		}
		return false;
	}
	
	public boolean isControllerDown() {
		return count == 0 ? false : input.getAxisValue(0, y_axis) >= 0.5f ? true : false;				
	}
	
	public boolean isEventedControllerDown() {
		if (next) {
			return count == 0 ? false : y_value >= 0.5f ? true : false;			
		}
		return false;
	}
	
	
	public boolean isControllerNone() {
		return count == 0 ? false : !(isControllerUp() || isControllerDown() || isControllerLeft() || isControllerRight());
	}
	
	
	public boolean isButtonPressed() {
		return count == 0? false : input.isButton1Pressed(Input.ANY_CONTROLLER) || input.isButton2Pressed(Input.ANY_CONTROLLER) || input.isButton3Pressed(Input.ANY_CONTROLLER);
	}
	
	public boolean isEventedButtonPressed() {
		if (next && button) {
			return count == 0? false : button;
		}
		return false;
	}
}