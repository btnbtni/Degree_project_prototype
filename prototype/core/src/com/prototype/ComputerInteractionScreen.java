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

public class ComputerInteractionScreen implements Screen {

    final Prototype game;
    private Screen previousScreen;
    
	private Texture backgroundImage;
	private Texture decisionWindowA;
	private Texture decisionWindowB;

	private OrthographicCamera camera;

	private Rectangle background;

	int startCodeTextX;
	int startCodeTextY;
	long recentKeyStroke;

	int screenPhase;
	boolean yesMarked;

	String codeTest;

	public ComputerInteractionScreen(final Prototype game, Screen screen) {
        this.game = game;
        previousScreen = screen;

		backgroundImage = new Texture(Gdx.files.internal("boringcomputerscreen2.png"));
		decisionWindowA = new Texture(Gdx.files.internal("judgecodeyes1.png"));
		decisionWindowB = new Texture(Gdx.files.internal("judgecodeno1.png"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);

		background = new Rectangle();
		background.x = 0;
		background.y = 0;
		background.width = game.windowSizeX;
		background.height = game.windowSizeY;

		screenPhase = 0;
		yesMarked = true;

		startCodeTextX = game.windowSizeX/100;
		startCodeTextY = game.windowSizeY - (game.windowSizeY / 20);

		recentKeyStroke = 0;

		codeTest = "Test test test test \nTEST TEST \nasdasd";
	}
	

	@Override
	public void render(float delta) {
		
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(backgroundImage, background.x, background.y);
		game.font.draw(game.batch, codeTest, startCodeTextX, startCodeTextY);
		if(screenPhase == 0){
			if(yesMarked){
				game.batch.draw(decisionWindowA, (float)(game.windowSizeX*0.7), (float)(game.windowSizeY*0.7));
			}else{
				game.batch.draw(decisionWindowB, (float)(game.windowSizeX*0.7), (float)(game.windowSizeY*0.7));
			}
		}
		game.batch.end();



		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			game.setScreen(new PauseScreen(game, this));
		}

        if(Gdx.input.isKeyPressed(Input.Keys.O)){
			game.setScreen(previousScreen);
		}

		if(screenPhase == 0){
			if((Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))){
				yesMarked = !yesMarked;
				recentKeyStroke = TimeUtils.nanoTime();
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
