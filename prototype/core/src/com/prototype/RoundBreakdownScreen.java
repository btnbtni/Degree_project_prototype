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
	int textOneX;
	int textTwoX;
	int textThreeX;
	int textFourX;
	int textStartY;

	int screenPhase;
	boolean yesMarked;
    int yCoordinate;
	int selectedIndex;
	int numOptions;

	String codeTest;

	public RoundBreakdownScreen(final Prototype game) {
        this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);
        lineOffset = 25;
		textOneX = 35;
		textTwoX = textOneX + 270;
		textThreeX = textTwoX + 150;
		textFourX = textThreeX + 300;
		selectedIndex = 0;
		numOptions = game.testList.length;
	}
	

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0.2f, 0, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.font.draw(game.batch, "Press ENTER to view explanation of selected test", 200, 200);
		game.blackFont.draw(game.batch, "Test name", textOneX, 725);
		game.blackFont.draw(game.batch, "Result", textTwoX, 725);
		game.blackFont.draw(game.batch, "Correct answer", textThreeX, 725);
		game.blackFont.draw(game.batch, "Provided answer", textFourX, 725);
        for(int i = 0; i < game.testList.length; i++){
            yCoordinate = 700 - i*lineOffset;
			if(i == selectedIndex){
				game.font.draw(game.batch, "->", 5, yCoordinate);
			}
            game.font.draw(game.batch, game.testList[i].testName, textOneX, yCoordinate);
			if(game.testList[i].testNeedsChange){
				game.font.draw(game.batch, game.testList[i].correctAnswer, textThreeX, yCoordinate);
			}else{
				game.font.draw(game.batch, "Safe", textThreeX, yCoordinate);
			}
			if(game.testList[i].testIsFinished){
				if(game.testList[i].providedAnswer != null){
					game.font.draw(game.batch, game.testList[i].providedAnswer, textFourX, yCoordinate);
				}else{
					game.font.draw(game.batch, "Safe", textFourX, yCoordinate);
				}
			}else{
				game.font.draw(game.batch, "N/A", textFourX, yCoordinate);
			}
			
            if(game.testList[i].testIsFinished){
                if(game.testList[i].testAnsweredCorrectly){
                    game.goodFont.draw(game.batch, "Correct", textTwoX, yCoordinate);
                }else{
                    game.badFont.draw(game.batch, "Incorrect", textTwoX, yCoordinate);
                }
            }else{
                game.neutralFont.draw(game.batch, "Unhandled", textTwoX, yCoordinate);
            }
        }
		game.batch.end();

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			game.setScreen(game.popPreviousScreen());
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
			game.pushPreviousScreen(this);
			game.setScreen(new TestExplanationScreen(game, selectedIndex));
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
