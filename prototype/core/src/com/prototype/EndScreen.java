/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class EndScreen implements Screen {

    final Prototype game;
    
	private Texture backgroundImage;

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
			// String lastRoundString = "Game over!";
			// game.font.draw(game.batch, lastRoundString, controlTextStartX, controlTextStartY);
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
		
      	backgroundImage.dispose();
      	// game.batch.dispose();
	}

    @Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		//rainMusic.play();
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
