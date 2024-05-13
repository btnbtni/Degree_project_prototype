/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class ComputerInteractionScreen implements Screen {

    final Prototype game;
    
	private Texture backgroundImage;
	private Texture decisionWindowA;
	private Texture decisionWindowB;
	private Texture vulnerabilityWindow;
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

	public ComputerInteractionScreen(final Prototype game, boolean hasError, int index) {
        this.game = game;
		this.hasError = hasError;
		screenIndex = index;
		backgroundImage = new Texture(Gdx.files.internal("emptycomputerscreenlarge.png"));
		decisionWindowA = new Texture(Gdx.files.internal("yesempty1.png"));
		decisionWindowB = new Texture(Gdx.files.internal("noempty1.png"));
		vulnerabilityWindow = new Texture(Gdx.files.internal("vulnerabilitylistbig1.png"));
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

	}
	

	@Override
	public void render(float delta) {
		
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(backgroundImage, background.x, background.y);
		game.font.draw(game.batch, game.testList.get(screenIndex).testName, 8, 761);
		if(hasError){
			game.batch.draw(game.testList.get(screenIndex).incorrectCodeImage, 10, 0);
		}else{
			game.batch.draw(game.testList.get(screenIndex).correctCodeImage, 10, 0);
		}
		if(screenPhase == 0){
			if(yesMarked){
				game.batch.draw(decisionWindowA, choiceWindowX, choiceWindowY);
			}else{
				game.batch.draw(decisionWindowB, choiceWindowX, choiceWindowY);
			}
			game.blackFont.draw(game.batch, "Would you like to\nchange this code?", choiceWindowTextX, choiceWindowTextY);
		}else if(screenPhase == 1){
			game.batch.draw(vulnerabilityWindow, (float)(game.windowSizeX*0.5) - 70, (float)(game.windowSizeY*0.7) - 300);
			for(int i = 0; i < game.vulnerabilityTypes.size; i++){
				if(selectedVulnerability != i){
					game.greyFont.draw(game.batch, game.vulnerabilityTypes.get(i), (float)(game.windowSizeX*0.5) - 50, (float)(game.windowSizeY*0.7) + 100 - (i * lineSize));
				}else{
					game.font.draw(game.batch, game.vulnerabilityTypes.get(i), (float)(game.windowSizeX*0.5) - 50, (float)(game.windowSizeY*0.7) + 100 - (i * lineSize));
				}
			}
		}else if(screenPhase == 2){
			game.batch.draw(infoTextBox, infoBoxX, infoBoxY);
			if(yesMarked){
				game.blackFont.draw(game.batch, "A developer will re-write this code \nto fix the vulnerability.\nPress ENTER to leave computer\nor R to reset task.", infoBoxX + 20, infoBoxY + 90);
			}else{
				game.blackFont.draw(game.batch, "The code is considered safe\nand will not be changed.\nPress ENTER to leave computer\nor R to reset task.", infoBoxX + 20, infoBoxY + 90);
			}
		}
		game.batch.end();


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
					selectedVulnerability = game.vulnerabilityTypes.size - 1;
				}else{
					selectedVulnerability--;
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
				selectedVulnerability++;
				if(selectedVulnerability >= game.vulnerabilityTypes.size){
					selectedVulnerability = 0;
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				handled = true;
				game.registerAnswer(game.vulnerabilityTypes.get(selectedVulnerability), screenIndex);
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
		decisionWindowA.dispose();
		decisionWindowB.dispose();
		vulnerabilityWindow.dispose();
		infoTextBox.dispose();
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
