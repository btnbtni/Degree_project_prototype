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

public class RoundBreakdownScreen implements Screen {

    final Prototype game;
	private OrthographicCamera camera;
	int startCodeTextX;
	int startCodeTextY;
	int lineSize;
    int lineOffset;
	boolean handled;
	boolean hasError;
	int textOneX;
	int textTwoX;
	int textThreeX;
	int textFourX;
	int textStartY;

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
		numOptions = game.testList.size;
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
        for(int i = 0; i < game.testList.size; i++){
            yCoordinate = 700 - i*lineOffset;
			if(i == selectedIndex){
				game.font.draw(game.batch, "->", 5, yCoordinate);
			}
            game.font.draw(game.batch, game.testList.get(i).testName, textOneX, yCoordinate);
			if(game.testList.get(i).testNeedsChange){
				game.font.draw(game.batch, game.testList.get(i).correctAnswer, textThreeX, yCoordinate);
			}else{
				game.font.draw(game.batch, "Safe", textThreeX, yCoordinate);
			}
			if(game.testList.get(i).testIsFinished){
				if(game.testList.get(i).providedAnswer != null){
					game.font.draw(game.batch, game.testList.get(i).providedAnswer, textFourX, yCoordinate);
				}else{
					game.font.draw(game.batch, "Safe", textFourX, yCoordinate);
				}
			}else{
				game.font.draw(game.batch, "N/A", textFourX, yCoordinate);
			}
			
            if(game.testList.get(i).testIsFinished){
                if(game.testList.get(i).testAnsweredCorrectly){
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
			dispose();
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
