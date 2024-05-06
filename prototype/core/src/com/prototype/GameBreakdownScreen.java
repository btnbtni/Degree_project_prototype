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

public class GameBreakdownScreen implements Screen {

    final Prototype game;
    
	private Texture backgroundImage;

	private OrthographicCamera camera;


	int startCodeTextX;
	int startCodeTextY;
	long recentKeyStroke;
	int selectedVulnerability;
	int lineSize;
	int screenIndex;
    int lineOffset;
	boolean handled;
	boolean hasError;

	int screenPhase;
	boolean yesMarked;
    int yCoordinate;

	String codeTest;

	public GameBreakdownScreen(final Prototype game) {
        this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);
        lineOffset = 25;
	}
	

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0.2f, 0, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
        for(int i = 0; i < game.testList.length; i++){
            yCoordinate = 700 - i*lineOffset;
            game.font.draw(game.batch, game.testList[i].testName, 100, yCoordinate);
            if(game.testList[i].totalTestScore > 3){
                game.goodFont.draw(game.batch, game.testList[i].totalTestScore + " / " + game.totalRounds, 400, yCoordinate);
            }else if(game.testList[i].totalTestScore > 1){
                game.neutralFont.draw(game.batch, game.testList[i].totalTestScore + " / " + game.totalRounds, 400, yCoordinate);
            }else{
                game.badFont.draw(game.batch, game.testList[i].totalTestScore + " / " + game.totalRounds, 400, yCoordinate);
            }
        }
		if(game.round >= game.totalRounds){

		}
		game.batch.end();

		if(game.round < game.totalRounds){
			if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
				game.setScreen(game.popPreviousScreen());
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
				
			}
		}else{
            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
				game.setScreen(game.popPreviousScreen());
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
				
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
