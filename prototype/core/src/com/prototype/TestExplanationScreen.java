/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
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
	int sourceTextX;
	int sourceTextOneY;
	int sourceTextTwoY;
	int sourceTextThreeY;



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
		sourceTextX = 15;
		sourceTextOneY = 60;
		sourceTextTwoY = 45;
		sourceTextThreeY = 30;
	}
	

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0.2f, 0.1f, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
        game.batch.draw(backgroundPicture, 0, 0);
		if(game.testList.get(testIndex).codeSourceThree != null){
			game.sourceFont.draw(game.batch, game.testList.get(testIndex).codeSourceOne, sourceTextX, sourceTextThreeY);
		}
		if(game.testList.get(testIndex).codeSourceTwo != null){
			game.sourceFont.draw(game.batch, game.testList.get(testIndex).codeSourceTwo, sourceTextX, sourceTextTwoY);
		}
		if(game.testList.get(testIndex).codeSourceOne != null){
			game.sourceFont.draw(game.batch, game.testList.get(testIndex).codeSourceOne, sourceTextX, sourceTextOneY);
			game.sourceFont.draw(game.batch, "Code sources: ", sourceTextX, sourceTextOneY + 15);
		}
        if(game.testList.get(testIndex).testNeedsChange){
			game.batch.draw(game.testList.get(testIndex).incorrectCodeImage, 10, 0);
            game.font.draw(game.batch, game.testList.get(testIndex).explanation, textStartX, textStartY, textWidth, Align.left, true);
			game.sourceFont.draw(game.batch, "Explanation source:", textStartX, textStartY - 285);
			game.sourceFont.draw(game.batch, game.testList.get(testIndex).explanationSource, textStartX, textStartY - 300);
		}else{
			game.batch.draw(game.testList.get(testIndex).correctCodeImage, 10, 0);
            game.font.draw(game.batch, explanationIfCorrect, textStartX, textStartY, textWidth, Align.left, true);
		}
		game.batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.setScreen(game.popPreviousScreen());
			dispose();
        }



	}
	
	@Override
	public void dispose () {
		backgroundPicture.dispose();
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
