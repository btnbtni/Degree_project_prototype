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

public class HighScoreScreen implements Screen {

    final Prototype game;
	private OrthographicCamera camera;
    int lineOffset;
    int textStartX;
	int textStartY;



	public HighScoreScreen(final Prototype game) {
        this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);
        lineOffset = 25;
        textStartX = (int)(game.windowSizeX*0.35);
		textStartY = (int)(game.windowSizeY*0.7);
	}
	

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0.2f, 0.1f, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
        game.font.draw(game.batch, "Highscore table", 400, 650);
        for(int i = 0; i < 10; i++){
            if(i < 9){
                game.font.draw(game.batch, (i+1) + ".", textStartX, textStartY - i*lineOffset);
            }else{
                game.font.draw(game.batch, (i+1) + ".", textStartX - 12, textStartY - i*lineOffset);
            }
            if(game.topTenScores[i] >= 0){
                game.font.draw(game.batch, game.topTenScores[i] + "", textStartX + 120, textStartY - i*lineOffset);
            }else{
                game.greyFont.draw(game.batch, "N/A", textStartX + 120, textStartY - i*lineOffset);
            }
        }
		if(game.round >= game.totalRounds){

		}
		game.batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.setScreen(game.popPreviousScreen());
			dispose();
        }


	}
	
	@Override
	public void dispose() {
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
