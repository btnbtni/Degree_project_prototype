/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;

public class MainMenuScreen implements Screen {
    
    final Prototype game;
	private Array<String> menuItems;
	OrthographicCamera camera;
	private int midAlignX;
	private int midAlignY;
	private int optionOffsetY;
	private int selectedIndex;
	private int numOptions;

	public MainMenuScreen(final Prototype game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);
		menuItems = new Array<String>();
		
		menuItems.add("Start game");
		menuItems.add("How to play");
		menuItems.add("Information centre");
		menuItems.add("Results");
		menuItems.add("High scores");
		menuItems.add("Quit");
		optionOffsetY = 50;
		midAlignX = game.windowSizeX/2 - 50;
		midAlignY = game.windowSizeY/2 + 50;
		selectedIndex = 0;
		numOptions = menuItems.size;
	}

    @Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();

		for(int i = 0; i < menuItems.size; i++){
			if(i == selectedIndex){
				game.font.draw(game.batch, "->", midAlignX - 50, midAlignY - (optionOffsetY * i));
			}
			game.font.draw(game.batch, menuItems.get(i), midAlignX, midAlignY - (optionOffsetY * i));
		}
		
		game.batch.end();

		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			if(selectedIndex == 0){
				game.pushPreviousScreen(this);
				game.setScreen(new StartNewGameScreen(game));
			}
			if(selectedIndex == 1){
				game.pushPreviousScreen(this);
				game.setScreen(game.helpScreen);
			}
			if(selectedIndex == 2){
				game.pushPreviousScreen(this);
				game.setScreen(new InformationScreen(game));
			}
			if(selectedIndex == 3){
				game.pushPreviousScreen(this);
				game.setScreen(new ResultSummaryScreen(game));
			}
			if(selectedIndex == 4){
				game.pushPreviousScreen(this);
				game.setScreen(new HighScoreScreen(game));
			}
			if(selectedIndex == 5){
				Gdx.app.exit();
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			selectedIndex++;
			if(selectedIndex >= numOptions){
				selectedIndex = 0;
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			selectedIndex--;
			if(selectedIndex < 0){
				selectedIndex = numOptions - 1;
			}
		}

	}

    @Override
    public void dispose(){

    }

    @Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
