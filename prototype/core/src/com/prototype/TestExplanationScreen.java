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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class TestExplanationScreen implements Screen {

    final Prototype game;
	private OrthographicCamera camera;
    private Texture backgroundPicture;
    private int testIndex;
    int textStartX;
    int textStartY;
    int textWidth;
    String explanationIfCorrect;



	public TestExplanationScreen(final Prototype game, int index) {
        this.game = game;
        testIndex = index;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);
        backgroundPicture = new Texture(Gdx.files.internal("emptycomputerscreenlarge.png"));
        textStartX = 520;
		textStartY = 650;
        textWidth = 450;
        explanationIfCorrect = "This code does not have a known vulnerability";
	}
	

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0.2f, 0.1f, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
        game.batch.draw(backgroundPicture, 0, 0);
        if(game.testList[testIndex].testNeedsChange){
			game.batch.draw(game.testList[testIndex].incorrectCodeImage, 10, 0);
            game.font.draw(game.batch, game.testList[testIndex].explanation, textStartX, textStartY, textWidth, Align.left, true);
		}else{
			game.batch.draw(game.testList[testIndex].correctCodeImage, 10, 0);
            game.font.draw(game.batch, explanationIfCorrect, textStartX, textStartY, textWidth, Align.left, true);
		}
		game.batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.setScreen(game.popPreviousScreen());
        }



	}
	
	@Override
	public void dispose () {
		backgroundPicture.dispose();
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
