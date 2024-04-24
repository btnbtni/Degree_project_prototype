/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Prototype extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public BitmapFont greyFont;
	public Screen[] interactionScreens;
	public Screen[] screenStack;
	private int screenStackPointer;
	private int screenStackCapacity;

	int windowSizeX;
	int windowSizeY;
	int tileSize;
	String[] vulnerabilityTypes;

	public Prototype(int windowSizeX, int windowSizeY){
		this.windowSizeX = windowSizeX;
		this.windowSizeY = windowSizeY;
		this.tileSize = 64;
		interactionScreens = new Screen[10];
		vulnerabilityTypes = new String[10];
		screenStackPointer = -1;
		screenStackCapacity = 10;
		screenStack = new Screen[screenStackCapacity];
		vulnerabilityTypes[0] = "SQL Injection";
		vulnerabilityTypes[1] = "Buffer overflow";
		for(int i = 2; i < 10; i++){
			vulnerabilityTypes[i] = "Placeholder " + i;
		}
		
	}

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(); // use libGDX's default Arial font
		greyFont = new BitmapFont();
		greyFont.setColor(Color.DARK_GRAY);
		for(int i = 0; i < interactionScreens.length; i++){
			interactionScreens[i] = new ComputerInteractionScreen(this);
		}
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	public Screen popPreviousScreen(){
		if(screenStackPointer < 0){
			return null;
		}
		Screen poppedScreen = screenStack[screenStackPointer];
		screenStack[screenStackPointer] = null;
		screenStackPointer--;
		return poppedScreen;
	}

	public void pushPreviousScreen(Screen screen){
		screenStackPointer++;
		if(screenStackPointer >= screenStackCapacity){
			screenStackPointer--;
			return;
		}
		screenStack[screenStackPointer] = screen;
	}

	public void resetScreenStack(){
		for(int i = 0; i < screenStackCapacity; i++){
			screenStack[i] = null;
			screenStackPointer = -1;
		}
	}
}
