/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class EndScreen implements Screen {

    final Prototype game;
	private OrthographicCamera camera;


	int startCodeTextX;
	int startCodeTextY;
	long recentKeyStroke;
	int selectedVulnerability;
	int lineSize;
	int screenIndex;
	boolean handled;
	boolean hasError;

	int screenPhase;
	boolean yesMarked;
	int textStartX;
	int textStartY;
	int controlTextStartX;
	int controlTextStartY;

	String codeTest;
	String resultString;
	String roundString;
	String totalScoreString;

	public EndScreen(final Prototype game) {
        this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);
		resultString = "Result: " + game.numberOfCorrectlyAnsweredTests + " / " + game.numberOfTotalTests;
		roundString = "Round " + game.round + " of " + game.totalRounds + " finished";
		totalScoreString = "Total score: " + game.totalScore;
		textStartX = (int)(game.windowSizeX*0.35);
		textStartY = (int)(game.windowSizeY*0.7);
		controlTextStartX = (int)(game.windowSizeX*0.3);
		controlTextStartY = (int)(game.windowSizeY*0.2);
	}
	

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0.2f, 0, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		
		game.font.draw(game.batch, roundString, textStartX, textStartY);
		game.font.draw(game.batch, resultString, textStartX, textStartY - 25);
		game.font.draw(game.batch, totalScoreString, textStartX, textStartY - 50);
		game.font.draw(game.batch, "Press B to view round breakdown", controlTextStartX, controlTextStartY - 25);
		
		if(game.round >= game.totalRounds){
			game.font.draw(game.batch, "Press enter to return to main menu", controlTextStartX, controlTextStartY);
			game.font.draw(game.batch, "Press G to view game breakdown", controlTextStartX, controlTextStartY - 50);
		}else{
			game.font.draw(game.batch, "Press enter to start next round", controlTextStartX, controlTextStartY);
		}
		game.batch.end();

		if(game.round < game.totalRounds){
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				game.startNewSession(game.numberOfNeededChanges, game.totalRounds);
				game.setScreen(new GameScreen(game));
				dispose();
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
				game.pushPreviousScreen(this);
				game.setScreen(new RoundBreakdownScreen(game));
			}
		}else{
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				game.updateTopTenList();
				game.fullReset();
				game.setScreen(new MainMenuScreen(game));
				dispose();
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
				game.pushPreviousScreen(this);
				game.setScreen(new RoundBreakdownScreen(game));
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
				game.pushPreviousScreen(this);
				game.setScreen(new GameBreakdownScreen(game));
			}
		}


	}
	
	@Override
	public void dispose () {
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
