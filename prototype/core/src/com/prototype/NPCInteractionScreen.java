/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class NPCInteractionScreen implements Screen {

    final Prototype game;
    
	private Texture backgroundImage;

	private OrthographicCamera camera;

	private Rectangle background;

	private String selected;
	private String notSelected;

	int startCodeTextX;
	int startCodeTextY;
	long recentKeyStroke;
	int selectedVulnerability;
	int lineSize;
	int screenIndex;
	boolean hasError;

	int screenPhase;

	String informationString;
	String resultString;

	NPCQuestion npcQuestion;
	private Texture phishingImage;
	private boolean phishingQuestion;
	private Texture npcImage;

	public NPCInteractionScreen(final Prototype game) {
        this.game = game;
		backgroundImage = new Texture(Gdx.files.internal("npcinteractionbackground.png"));
		npcImage = new Texture(Gdx.files.internal("interaction/girlnpc96.png"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.windowSizeX, game.windowSizeY);

		background = new Rectangle();
		background.x = 0;
		background.y = 0;
		background.width = game.windowSizeX;
		background.height = game.windowSizeY;

		screenPhase = 1;
		selectedVulnerability = 0;
		lineSize = 20;

		startCodeTextX = game.windowSizeX/100;
		startCodeTextY = game.windowSizeY - 40;

		recentKeyStroke = 0;

		

		
		
		StringBuilder informationMessageBuilder = new StringBuilder();
		informationMessageBuilder.append("Hi!\n");
		
		informationMessageBuilder.append("Could you please help me with something?\n");

		
		selected = "[X] ";
		notSelected = "[ ] ";

		ArrayList<NPCQuestion> socialEngineeringQuestions = new ArrayList<>();

		String questionString = "Carl stood outside the building when I arrived at work this morning. He had forgotten his access credentials for the building and asked if I could let him in. I said no since that's against the rules.";
		NPCQuestion question = new NPCQuestion(questionString);
		question.putOption("You should've let him in, Carl is our friend!", false);
		question.putOption("You acted correctly.", true);
		question.putOption("Who's Carl?", false);
		question.putOption("Who are you?", false);
		question.setExplanation("Social engineering attacks are often carried out by exploiting the trust of others. People that have lost access to the building are a security threat.");
		
		socialEngineeringQuestions.add(question);

		questionString = "The new intern, Vera, called in this morning. Her supervisor forgot to give her access to the shared company drive so she wasn't able to work on her assigned project. No problem though, I gave her temporary credentials so that she could get to work.";
		question = new NPCQuestion(questionString);
		question.putOption("Great work!", false);
		question.putOption("You're very nice.", false);
		question.putOption("Oh, no, you shouldn't have done that.", true);
		question.setExplanation("Social engineering attacks are often carried out by exploiting the trust of others. Company credentials should only be given out through proper channels.");
		socialEngineeringQuestions.add(question);


		ArrayList<NPCQuestion> phishingQuestions = new ArrayList<>();
		question = new NPCQuestion("phishing/phish1.png");
		question.putOption("This looks like a phishing email.", true);
		question.putOption("This email looks legitimate.", false);
		question.setExplanation("You should always verify the email address of the sender!");

		phishingQuestions.add(question);
		question = new NPCQuestion("phishing/phish3.png");
		question.putOption("This looks like a phishing email.", true);
		question.putOption("This email looks legitimate.", false);
		question.setExplanation("You should always verify the email address of the sender!");
		phishingQuestions.add(question);

		if(MathUtils.random(0,100) % 2 == 0){
			phishingQuestion = false;
			npcQuestion = socialEngineeringQuestions.get(MathUtils.random(0, socialEngineeringQuestions.size()-1));
			phishingImage = null;
			informationMessageBuilder.append("\n");
			informationMessageBuilder.append(npcQuestion.getQuestion());
		}else{
			informationMessageBuilder.append("I got this email this morning, what do you think of it?");
			npcQuestion = phishingQuestions.get(MathUtils.random(0, phishingQuestions.size()-1));
			phishingQuestion = true;
			phishingImage = new Texture(Gdx.files.internal(npcQuestion.getQuestion()));
		}
		informationString= informationMessageBuilder.toString();
	}
	

	@Override
	public void render(float delta) {
		
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(backgroundImage, background.x, background.y);
		
		game.blackFont.draw(game.batch, informationString, startCodeTextX, startCodeTextY, 800, Align.left, true);

		StringBuilder quizQuestionBuilder = new StringBuilder();
		if(screenPhase == 1){
			game.batch.draw(npcImage, (float)(game.windowSizeX*0.5) - 70 + 400, (float)(game.windowSizeY*0.7) - 300 + 200);
			float yOffset = 150;
			if(phishingQuestion){
				yOffset = 500;
				game.batch.draw(phishingImage, startCodeTextX+30, (float)(game.windowSizeY*0.7) - 350);
			}
			
			ArrayList<String> options = npcQuestion.getOptions();
			for(int i = 0; i < options.size(); i++){
				quizQuestionBuilder.setLength(0);
				if(npcQuestion.getSelectedOption() != null && npcQuestion.getSelectedOption() == i){
					quizQuestionBuilder.append(selected);
				}
				else{
					quizQuestionBuilder.append(notSelected);
				}
				quizQuestionBuilder.append(options.get(i));
				if(selectedVulnerability != i){
					game.blackFont.draw(game.batch, quizQuestionBuilder.toString(), startCodeTextX + 30, (float)(game.windowSizeY*0.7) + 100 - (i * lineSize) - yOffset, 700, Align.left, true);
				}else{
					game.font.draw(game.batch, quizQuestionBuilder.toString(), startCodeTextX + 30, (float)(game.windowSizeY*0.7) + 100 - (i * lineSize) - yOffset, 700, Align.left, true);
				}
			}
			if(selectedVulnerability == options.size()){
				game.font.draw(game.batch, "SUBMIT", startCodeTextX + 30, (float)(game.windowSizeY*0.7) + 100 - (npcQuestion.getOptions().size() * lineSize)- yOffset);
			}
			else{
				game.greyFont.draw(game.batch, "SUBMIT", startCodeTextX + 30, (float)(game.windowSizeY*0.7) + 100 - (npcQuestion.getOptions().size() * lineSize)- yOffset);
			}
		}else if(screenPhase == 2){

			game.blackFont.draw(game.batch, resultString, startCodeTextX+30, (float)(game.windowSizeY*0.4), 700, Align.left, true);
		}
		game.batch.end();



		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			game.setScreen(game.popPreviousScreen());
		}

		else if(screenPhase == 1){
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
				if(selectedVulnerability == 0){
					selectedVulnerability = npcQuestion.getOptions().size();
				}else{
					selectedVulnerability--;
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
				selectedVulnerability++;
				if(selectedVulnerability >= npcQuestion.getOptions().size()+1){
					selectedVulnerability = 0;
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				if(selectedVulnerability == npcQuestion.getOptions().size()){
					StringBuilder resultStringBuilder = new StringBuilder();
					if(npcQuestion.correctQuestion()){
						resultStringBuilder.append("You answered correctly\n");
						game.numberOfCorrectlyAnsweredTests++;
					}
					else{
						resultStringBuilder.append("You answered incorrectly\n");
					}
					if(npcQuestion.getExplanation() != null)
						resultStringBuilder.append(npcQuestion.getExplanation());
					resultStringBuilder.append("\nPress ENTER to continue");
					resultString = resultStringBuilder.toString();
					game.numberOfAnsweredTests++;
					screenPhase = 2;
				}
				if(selectedVulnerability < npcQuestion.getOptions().size()){
					npcQuestion.setSelectedOption(selectedVulnerability);
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
		if(phishingImage != null){
			phishingImage.dispose();
		}
		npcImage.dispose();

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
