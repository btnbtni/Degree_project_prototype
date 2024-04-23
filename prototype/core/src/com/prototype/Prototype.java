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
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Prototype extends Game {
	public SpriteBatch batch;
	public BitmapFont font;

	int windowSizeX;
	int windowSizeY;
	int tileSize;
	String[] vulnerabilityTypes;

	public Prototype(int windowSizeX, int windowSizeY){
		this.windowSizeX = windowSizeX;
		this.windowSizeY = windowSizeY;
		this.tileSize = 64;
		vulnerabilityTypes = new String[10];
		vulnerabilityTypes[0] = "SQL Injection";
		vulnerabilityTypes[1] = "Buffer overflow";
		for(int i = 2; i < 10; i++){
			vulnerabilityTypes[i] = "Placeholder " + i;
		}
	}

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(); // use libGDX's default Arial font
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
