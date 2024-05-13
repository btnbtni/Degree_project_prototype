/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Input;

public class HelpScreen implements Screen {
    
    final Prototype game;

	OrthographicCamera camera;
	private Texture helpScreenIcons;
	private String arrowText;
	private String enterText;
	private String escText;
	private String helpText;
	private String informationText;

	public HelpScreen(final Prototype game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);

		arrowText = "Use arrow keys to navigate character or menu items";
		enterText = "Press ENTER to interact with objects, NPCs or menu items";
		escText = "Press ESCAPE to go back";
		helpText = "Press H to see this screen";
		informationText = "Game structure:\nSolve cyber security problems by interacting with objects or NPCs that have exclamation marks above them. Collect USB-sticks and handle them correctly. To finish a round, use the door.\n\nAny other keys will be explained on screen.";

	
		helpScreenIcons = new Texture(Gdx.files.internal("helpscreenicons.png"));
	}

    @Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(helpScreenIcons, 0, 0);
		game.font.draw(game.batch, arrowText, 360, 640, 500, Align.left, true);
		game.font.draw(game.batch, enterText, 360, 537, 500, Align.left, true);
		game.font.draw(game.batch, escText, 360, 445, 500, Align.left, true);
		game.font.draw(game.batch, helpText, 360, 373, 500, Align.left, true);
		game.font.draw(game.batch, informationText, 164, 278, 800, Align.left, true);
		
		game.batch.end();


		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.setScreen(game.popPreviousScreen());
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
