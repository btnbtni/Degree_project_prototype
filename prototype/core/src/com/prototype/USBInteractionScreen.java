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

public class USBInteractionScreen implements Screen {

    final Prototype game;
    
	private Texture backgroundImage;
	private Texture decisionWindowA;
	private Texture decisionWindowB;
	private Texture vulnerabilityWindow;

	private OrthographicCamera camera;

	private Rectangle background;

	private String selected;
	private String notSelected;
	private QuizQuestion[] quizQuestions;
	private int result;

	int startCodeTextX;
	int startCodeTextY;
	long recentKeyStroke;
	int selectedVulnerability;
	int lineSize;
	int screenIndex;
	boolean handled;
	boolean hasError;

	int screenPhase;
	boolean yesMarked;

	String informationString;
	String resultString;

	public USBInteractionScreen(final Prototype game) {
        this.game = game;
		backgroundImage = new Texture(Gdx.files.internal("boringcomputerscreen1280.png"));
		decisionWindowA = new Texture(Gdx.files.internal("judgecodeyesbig1.png"));
		decisionWindowB = new Texture(Gdx.files.internal("judgecodenobig1.png"));
		vulnerabilityWindow = new Texture(Gdx.files.internal("vulnerabilitylistbig1.png"));
		

		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);

		background = new Rectangle();
		background.x = 0;
		background.y = 0;
		background.width = game.windowSizeX;
		background.height = game.windowSizeY;

		screenPhase = 1;
		yesMarked = true;
		selectedVulnerability = 0;
		lineSize = 20;

		startCodeTextX = game.windowSizeX/100;
		startCodeTextY = game.windowSizeY - 20;

		recentKeyStroke = 0;
		handled = false;

		

		boolean invertedQuestions;
		if(MathUtils.random(0,100) % 2 == 0){
			invertedQuestions = false;
		}
		else{
			invertedQuestions = true;
		}
		StringBuilder informationMessageBuilder = new StringBuilder();
		informationMessageBuilder.append("You've found a USB-Stick! Select the ");
		if(invertedQuestions){
			informationMessageBuilder.append("INCORRECT ");
		}
		else{
			informationMessageBuilder.append("CORRECT ");
		}
		informationMessageBuilder.append(" way(s) to handle it below:");
		informationString= informationMessageBuilder.toString();
		selected = "[X] ";
		notSelected = "[ ] ";
		quizQuestions = new QuizQuestion[2];
		quizQuestions[0] = new QuizQuestion("Plug it into a computer to see what's on it", false, invertedQuestions);
		quizQuestions[1] = new QuizQuestion("Disable autorun on a computer before plugging it in", true, invertedQuestions);
	}
	

	@Override
	public void render(float delta) {
		
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(backgroundImage, background.x, background.y);
		
		game.font.draw(game.batch, informationString, startCodeTextX, startCodeTextY);

		StringBuilder quizQuestionBuilder = new StringBuilder();
		if(screenPhase == 1){
			game.batch.draw(vulnerabilityWindow, (float)(game.windowSizeX*0.5) - 70, (float)(game.windowSizeY*0.7) - 300);
			for(int i = 0; i < quizQuestions.length; i++){
				quizQuestionBuilder.setLength(0);
				if(quizQuestions[i].getSelectedAnswer()){
					quizQuestionBuilder.append(selected);
				}
				else{
					quizQuestionBuilder.append(notSelected);
				}
				quizQuestionBuilder.append(quizQuestions[i].getQuestion());
				if(selectedVulnerability != i){
					game.greyFont.draw(game.batch, quizQuestionBuilder.toString(), startCodeTextX + 30, (float)(game.windowSizeY*0.7) + 100 - (i * lineSize));
				}else{
					game.font.draw(game.batch, quizQuestionBuilder.toString(), startCodeTextX + 30, (float)(game.windowSizeY*0.7) + 100 - (i * lineSize));
				}
			}
			if(selectedVulnerability == quizQuestions.length){
				game.font.draw(game.batch, "SUBMIT", startCodeTextX + 30, (float)(game.windowSizeY*0.7) + 100 - (quizQuestions.length * lineSize));
			}
			else{
				game.greyFont.draw(game.batch, "SUBMIT", startCodeTextX + 30, (float)(game.windowSizeY*0.7) + 100 - (quizQuestions.length * lineSize));
			}
		}else if(screenPhase == 2){

			game.font.draw(game.batch, resultString, startCodeTextX+30, (float)(game.windowSizeY*0.4));
		}
		game.batch.end();



		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			game.pushPreviousScreen(this);
			game.setScreen(new PauseScreen(game));
		}

        if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
			game.setScreen(game.popPreviousScreen());
		}

		else if(screenPhase == 1){
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
				if(selectedVulnerability == 0){
					selectedVulnerability = quizQuestions.length;
				}else{
					selectedVulnerability--;
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
				selectedVulnerability++;
				if(selectedVulnerability >= quizQuestions.length+1){
					selectedVulnerability = 0;
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				if(selectedVulnerability == quizQuestions.length){
					StringBuilder resultStringBuilder = new StringBuilder();
					for(QuizQuestion quizQuestion : quizQuestions){
						if(quizQuestion.correctQuestion()){
							this.result++;
						}
						else{
							resultStringBuilder.append(quizQuestion.getQuestion());
							resultStringBuilder.append("\n");
						}
					}
					resultStringBuilder.insert(0, "Your result is: " + String.valueOf(this.result) + "/" + String.valueOf(quizQuestions.length) + ".\n");
					
					if(result != quizQuestions.length){
						resultStringBuilder.insert(21, "The following options were marked incorrectly: \n");
					}
					resultStringBuilder.append("\n\nPress ENTER to continue");
					resultString = resultStringBuilder.toString();
					screenPhase = 2;
				}
				if(selectedVulnerability < quizQuestions.length){
					quizQuestions[selectedVulnerability].toggleSelect();
				}
				
				
			}
		}else if(screenPhase == 2){
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				game.setScreen(game.popPreviousScreen());
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
