package com.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class ResultSummaryScreen implements Screen {

    final Prototype game;
    OrthographicCamera camera;
    int lineOffset;
    int yCoordinate;

    public ResultSummaryScreen(final Prototype game){
        this.game = game;
        camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);
        lineOffset = 25;
    }

    @Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();

		for(int i = 0; i < game.numberOfVulnerabilities; i++){
            yCoordinate = 700 - i*lineOffset;
            game.font.draw(game.batch, game.testList.get(i).correctAnswer + ": ", 100, yCoordinate);
            game.font.draw(game.batch, game.resultSummary.testResults.get(i) + " / " + game.resultSummary.totalTestInstances.get(i) , 400, yCoordinate);
            if(game.resultSummary.percentCorrect.get(i) > 69){
                game.goodFont.draw(game.batch, game.resultSummary.percentCorrect.get(i) + "%", 600, yCoordinate);
            }else if(game.resultSummary.percentCorrect.get(i) > 30){
                game.neutralFont.draw(game.batch, game.resultSummary.percentCorrect.get(i) + "%", 600, yCoordinate);
            }else{
                game.badFont.draw(game.batch, game.resultSummary.percentCorrect.get(i) + "%", 600, yCoordinate);
            }
        }
		
		game.batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.setScreen(game.popPreviousScreen());
            dispose();
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
