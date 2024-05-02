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
    
	private Texture backgroundImage;
	private Texture decisionWindowA;
	private Texture decisionWindowB;
	private Texture vulnerabilityWindow;
	private Texture codeImage;
	private Texture infoTextBox;

	private OrthographicCamera camera;

	private Rectangle background;

	int startCodeTextX;
	int startCodeTextY;
	long recentKeyStroke;
	int selectedVulnerability;
	int lineSize;
	int screenIndex;
	boolean handled;
	boolean hasError;
	int choiceWindowX;
	int choiceWindowY;
	int choiceWindowTextX;
	int choiceWindowTextY;
	int infoBoxX;
	int infoBoxY;

	int screenPhase;
	boolean yesMarked;

	String codeTest;

	public ComputerInteractionScreen(final Prototype game, boolean hasError, int index) {
        this.game = game;
		this.hasError = hasError;
		screenIndex = index;
		backgroundImage = new Texture(Gdx.files.internal("emptycomputerscreenlarge.png"));
		decisionWindowA = new Texture(Gdx.files.internal("yesempty1.png"));
		decisionWindowB = new Texture(Gdx.files.internal("noempty1.png"));
		vulnerabilityWindow = new Texture(Gdx.files.internal("vulnerabilitylistbig1.png"));
		codeImage = new Texture(Gdx.files.internal("imagetexttest2.png"));
		infoTextBox = new Texture(Gdx.files.internal("infotext1.png"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);

		background = new Rectangle();
		background.x = 0;
		background.y = 0;
		background.width = game.windowSizeX;
		background.height = game.windowSizeY;

		choiceWindowX = 700;
		choiceWindowY = 500;
		choiceWindowTextX = choiceWindowX + 25;
		choiceWindowTextY = choiceWindowY + 85;
		infoBoxX = (int)(game.windowSizeX*0.5) - 100;
		infoBoxY = (int)(game.windowSizeY*0.5);


		screenPhase = 0;
		yesMarked = true;
		selectedVulnerability = 0;
		lineSize = 20;

		startCodeTextX = game.windowSizeX/100;
		startCodeTextY = game.windowSizeY - 40;

		recentKeyStroke = 0;
		handled = false;

		codeTest = "Test test test test \nTEST TEST \nasdasd";
	}
	

	@Override
	public void render(float delta) {
		
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(backgroundImage, background.x, background.y);
		if(hasError){
			game.font.draw(game.batch, game.incorrectVersion[screenIndex], startCodeTextX, startCodeTextY);
		}else{
			game.font.draw(game.batch, game.correctVersion[screenIndex], startCodeTextX, startCodeTextY);
		}
		// game.batch.draw(codeImage, 10, 0);
		// game.batch.draw(codeImage, startCodeTextX, startCodeTextY - 300);
		if(screenPhase == 0){
			if(yesMarked){
				game.batch.draw(decisionWindowA, choiceWindowX, choiceWindowY);
			}else{
				game.batch.draw(decisionWindowB, choiceWindowX, choiceWindowY);
			}
			game.blackFont.draw(game.batch, "Would you like to\nchange this code?", choiceWindowTextX, choiceWindowTextY);
		}else if(screenPhase == 1){
			game.batch.draw(vulnerabilityWindow, (float)(game.windowSizeX*0.5) - 70, (float)(game.windowSizeY*0.7) - 300);
			for(int i = 0; i < game.vulnerabilityTypes.length; i++){
				if(selectedVulnerability != i){
					game.greyFont.draw(game.batch, game.vulnerabilityTypes[i], (float)(game.windowSizeX*0.5) - 50, (float)(game.windowSizeY*0.7) + 100 - (i * lineSize));
				}else{
					game.font.draw(game.batch, game.vulnerabilityTypes[i], (float)(game.windowSizeX*0.5) - 50, (float)(game.windowSizeY*0.7) + 100 - (i * lineSize));
				}
			}
		}else if(screenPhase == 2){
			game.batch.draw(infoTextBox, infoBoxX, infoBoxY);
			if(yesMarked){
				game.blackFont.draw(game.batch, "A developer will re-write this code \nto fix the vulnerability.\nPress ENTER to leave computer\nor R to reset task.", infoBoxX + 20, infoBoxY + 90);
			}else{
				game.blackFont.draw(game.batch, "The code is considered safe\nand will not be changed.\nPress ENTER to leave computer\nor R to reset task.", infoBoxX + 20, infoBoxY + 90);
			}
			// game.font.draw(game.batch, "Press ENTER to go back\nor R to reset task.", (float)(game.windowSizeX*0.5) - 100, (float)(game.windowSizeY*0.4));
		}
		game.batch.end();



		// if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
		// 	game.pushPreviousScreen(this);
		// 	game.setScreen(new PauseScreen(game));
		// }

        if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
			game.setScreen(game.popPreviousScreen());
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			game.setScreen(game.popPreviousScreen());
		}

		if(screenPhase == 0){
			if((Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))){
				yesMarked = !yesMarked;
				recentKeyStroke = TimeUtils.nanoTime();
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				if(yesMarked){
					screenPhase = 1;
				}else{
					game.registerAnswer(screenIndex);
					screenPhase = 2;
					handled = true;
				}
			}
		}else if(screenPhase == 1){
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
				if(selectedVulnerability == 0){
					selectedVulnerability = game.vulnerabilityTypes.length - 1;
				}else{
					selectedVulnerability--;
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
				selectedVulnerability++;
				if(selectedVulnerability >= game.vulnerabilityTypes.length){
					selectedVulnerability = 0;
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				handled = true;
				game.registerAnswer(game.vulnerabilityTypes[selectedVulnerability], screenIndex);
				screenPhase = 2;
			}
		}else if(screenPhase == 2){
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				game.setScreen(game.popPreviousScreen());
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
				game.resetAnswer(screenIndex);
				screenPhase = 0;
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
