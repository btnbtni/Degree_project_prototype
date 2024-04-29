/*
*	Basic code setup inspired by the following tutorials:
*		https://libgdx.com/wiki/start/simple-game-extended
*		https://libgdx.com/wiki/start/a-simple-game
*/


package com.prototype;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Prototype extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public BitmapFont greyFont;
	public Screen[] interactionScreens;
	private Screen[] screenStack;
	private boolean[] testIsFinished;
	private boolean[] testNeedsChange;
	private boolean[] testAnsweredCorrectly;
	public String[] correctVersion;
	public String[] incorrectVersion;
	private String[] correctAnswers;
	private String[] providedAnswers;
	public int numberOfAnsweredTests;
	public int numberOfCorrectlyAnsweredTests;
	public int totalScore;
	public int round;
	public int totalRounds;

	private int[] indicesOfNeededChanges;
	private int screenStackPointer;
	private int screenStackCapacity;
	private int numberOfInteractions;
	public int numberOfTests;
	public int numberOfNeededChanges;

	int windowSizeX;
	int windowSizeY;
	int tileSize;
	String[] vulnerabilityTypes;

	public Prototype(int windowSizeX, int windowSizeY){
		this.windowSizeX = windowSizeX;
		this.windowSizeY = windowSizeY;
		this.tileSize = 64;
		numberOfInteractions = 10;
		numberOfTests = 10;
		numberOfNeededChanges = 5;
		screenStackPointer = -1;
		screenStackCapacity = 10;
		numberOfAnsweredTests = 0;
		totalScore = 0;
		round = 0;
		numberOfCorrectlyAnsweredTests = 0;
		indicesOfNeededChanges = new int[numberOfNeededChanges];
		interactionScreens = new Screen[numberOfInteractions];
		vulnerabilityTypes = new String[10];
		testIsFinished = new boolean[numberOfTests];
		testNeedsChange = new boolean[numberOfTests];
		testAnsweredCorrectly = new boolean[numberOfTests];
		correctVersion = new String[numberOfTests];
		incorrectVersion = new String[numberOfTests];
		correctAnswers = new String[numberOfTests];
		providedAnswers = new String[numberOfTests];
		screenStack = new Screen[screenStackCapacity];
	}

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		greyFont = new BitmapFont();
		greyFont.setColor(Color.DARK_GRAY);
		vulnerabilityTypes[0] = "SQL Injection";
		correctAnswers[0] = "SQL Injection";
		vulnerabilityTypes[1] = "Buffer overflow";
		correctAnswers[1] = "Buffer overflow";
		for(int i = 2; i < 10; i++){
			vulnerabilityTypes[i] = "Placeholder " + i;
			correctAnswers[i] = "Placeholder " + i;
		}
		setTexts();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	public void startNewSession(int numErrors, int rounds){
		numberOfNeededChanges = numErrors;
		totalRounds = rounds;
		indicesOfNeededChanges = new int[numberOfNeededChanges];
		numberOfCorrectlyAnsweredTests = 0;
		round++;
		setRandomValues();
		setLists();
		resetScreenStack();
		for(int i = 0; i < interactionScreens.length; i++){
			interactionScreens[i] = new ComputerInteractionScreen(this, testNeedsChange[i], i);
		}
	}

	public void fullReset(){
		numberOfAnsweredTests = 0;
		totalScore = 0;
		round = 0;
	}

	public Screen popPreviousScreen(){
		if(screenStackPointer < 0){
			return null;
		}
		Screen poppedScreen = screenStack[screenStackPointer];
		screenStack[screenStackPointer] = null;
		screenStackPointer--;
		return poppedScreen;
	}

	public void pushPreviousScreen(Screen screen){
		screenStackPointer++;
		if(screenStackPointer >= screenStackCapacity){
			screenStackPointer--;
			return;
		}
		screenStack[screenStackPointer] = screen;
	}

	public void resetScreenStack(){
		for(int i = 0; i < screenStackCapacity; i++){
			screenStack[i] = null;
			screenStackPointer = -1;
		}
	}

	private void setRandomValues(){
		int randomNumber;
		boolean uniqueNumber;
		for(int i = 0; i < numberOfNeededChanges; i++){
			uniqueNumber = false;
			while(!uniqueNumber){
				randomNumber = MathUtils.random((numberOfTests - 1));
				uniqueNumber = true;
				for(int j = 0; j < i; j++){
					if(indicesOfNeededChanges[j] == randomNumber){
						uniqueNumber = false;
					}
				}
				if(uniqueNumber){
					indicesOfNeededChanges[i] = randomNumber;
				}
			}
		}
	}

	private void setLists(){
		for(int i = 0; i < numberOfTests; i++){
			testNeedsChange[i] = false;
			testIsFinished[i] = false;
			testAnsweredCorrectly[i] = false;
			providedAnswers[i] = null;
		}
		for(int i = 0; i < numberOfNeededChanges; i++){
			testNeedsChange[indicesOfNeededChanges[i]] = true;
		}
	}

	private void setTexts(){
		for(int i = 0; i < numberOfTests; i++){
			correctVersion[i] = "public function DoSomething(){\n    CORRECT CODE " + i + "\n}";
			incorrectVersion[i] = "public function DoSomething(){\n    INCORRECT CODE " + i + "\n}";
		}
	}

	public void registerAnswer(String answer, int index){
		providedAnswers[index] = answer;
		if(testNeedsChange[index] && answer.equals(correctAnswers[index])){
			testAnsweredCorrectly[index] = true;
			numberOfCorrectlyAnsweredTests++;
		}
		testIsFinished[index] = true;
		numberOfAnsweredTests++;
	}

	public void registerAnswer(int index){
		if(!testNeedsChange[index]){
			testAnsweredCorrectly[index] = true;
			numberOfCorrectlyAnsweredTests++;
		}
		testIsFinished[index] = true;
		numberOfAnsweredTests++;
	}

	public void finishRound(){
		totalScore += numberOfCorrectlyAnsweredTests;
	}

}
