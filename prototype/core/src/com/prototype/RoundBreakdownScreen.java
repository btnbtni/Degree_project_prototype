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

public class RoundBreakdownScreen implements Screen {

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

	public RoundBreakdownScreen(final Prototype game) {
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
		game.blackFont.draw(game.batch, "Test name", 10, 725);
		game.blackFont.draw(game.batch, "Result", 280, 725);
		game.blackFont.draw(game.batch, "Correct answer", 430, 725);
		game.blackFont.draw(game.batch, "Provided answer", 730, 725);
        for(int i = 0; i < game.testList.length; i++){
            yCoordinate = 700 - i*lineOffset;
            game.font.draw(game.batch, game.testList[i].testName, 10, yCoordinate);
			if(game.testList[i].testNeedsChange){
				game.font.draw(game.batch, game.testList[i].correctAnswer, 430, yCoordinate);
			}else{
				game.font.draw(game.batch, "Safe", 430, yCoordinate);
			}
			if(game.testList[i].testIsFinished){
				if(game.testList[i].providedAnswer != null){
					game.font.draw(game.batch, game.testList[i].providedAnswer, 730, yCoordinate);
				}else{
					game.font.draw(game.batch, "Safe", 730, yCoordinate);
				}
			}else{
				game.font.draw(game.batch, "N/A", 730, yCoordinate);
			}
			
            if(game.testList[i].testIsFinished){
                if(game.testList[i].testAnsweredCorrectly){
                    game.goodFont.draw(game.batch, "Correct", 280, yCoordinate);
                }else{
                    game.badFont.draw(game.batch, "Incorrect", 280, yCoordinate);
                }
            }else{
                game.neutralFont.draw(game.batch, "Unhandled", 280, yCoordinate);
            }
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
