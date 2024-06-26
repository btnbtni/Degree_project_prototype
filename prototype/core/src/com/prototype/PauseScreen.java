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

public class PauseScreen implements Screen {
    
    final Prototype game;

	private Array<String> menuItems;
	OrthographicCamera camera;
	private int midAlignX;
	private int midAlignY;
	private int optionOffsetY;
	private int selectedIndex;
	private int numOptions;

	public PauseScreen(final Prototype game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);
		menuItems = new Array<String>();
		menuItems.add("Resume");
		menuItems.add("Exit to main menu");
		menuItems.add("Exit to desktop");
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
				game.setScreen(game.popPreviousScreen());
				dispose();
			}
			if(selectedIndex == 1){
				game.resetScreenStack();
				game.setScreen(new MainMenuScreen(game));
			}
			if(selectedIndex == 2){
				Gdx.app.exit();
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.setScreen(game.popPreviousScreen());
			dispose();
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

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			System.out.println(selectedIndex);
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
