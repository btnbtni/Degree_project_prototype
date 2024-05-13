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

public class InformationScreen implements Screen {

    final Prototype game;
	private OrthographicCamera camera;
    private Texture backgroundPicture;
    int lineOffset;
    int listTextStartX;
	int listTextStartY;
    int descriptionTextStartX;
    int descriptionTextStartY;
    int listPointer;



	public InformationScreen(final Prototype game) {
        this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);
        backgroundPicture = new Texture(Gdx.files.internal("informationscreen1.png"));
        lineOffset = 20;
        listTextStartX = 20;
        listTextStartY = 728;
        descriptionTextStartX = 312;
        descriptionTextStartY = listTextStartY;
        listPointer = 0;
	}
	

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0.2f, 0.1f, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
        game.batch.draw(backgroundPicture, 0, 0);
        game.font.draw(game.batch, "List", 20, 753);
        game.font.draw(game.batch, "Description", 312, 753);
        for(int i = 0; i < game.vulnerabilityTypes.size; i++){
            if(i == listPointer){
                game.font.draw(game.batch, game.vulnerabilityTypes.get(i), listTextStartX, listTextStartY - i*lineOffset);
            }else{
                game.greyFont.draw(game.batch, game.vulnerabilityTypes.get(i), listTextStartX, listTextStartY - i*lineOffset);
            }
        }
        game.blackFont.draw(game.batch, game.testList.get(listPointer).description, descriptionTextStartX, descriptionTextStartY, 660, Align.left, true);
		game.batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.setScreen(game.popPreviousScreen());
            dispose();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            if(listPointer == 0){
                listPointer = game.vulnerabilityTypes.size - 1;
            }else{
                listPointer--;
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            listPointer++;
            if(listPointer >= game.vulnerabilityTypes.size){
                listPointer = 0;
            }
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
