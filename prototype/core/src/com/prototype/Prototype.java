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

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.Gdx;

public class Prototype extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public BitmapFont greyFont;
	public BitmapFont blackFont;
	public BitmapFont goodFont;
	public BitmapFont neutralFont;
	public BitmapFont badFont;
	public Screen[] interactionScreens;
	public Screen usbInteractionScreen;
	private Screen[] screenStack;
	public boolean[] testIsFinished;
	public boolean[] testNeedsChange;
	public boolean[] testAnsweredCorrectly;
	public String[] correctVersion;
	public String[] incorrectVersion;
	public String[] correctAnswers;
	public String[] providedAnswers;
	public String[] testNames;
	public int[] totalTestScores;
	public int numberOfAnsweredTests;
	public int numberOfCorrectlyAnsweredTests;
	public int totalScore;
	public int round;
	public int totalRounds;
	public ResultSummary resultSummary;
	public int[] topTenScores;
	public TestScenario[] testList;
	public int numberOfComputerScreens;

	private int[] indicesOfNeededChanges;
	public int[] indicesOfScreens;
	public Integer[] indicesOfTests;
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
		numberOfComputerScreens = 18;

		testList = new TestScenario[numberOfTests];

		indicesOfNeededChanges = new int[numberOfNeededChanges];
		interactionScreens = new Screen[numberOfInteractions];
		vulnerabilityTypes = new String[10];
		screenStack = new Screen[screenStackCapacity];
		topTenScores = new int[10];
		resultSummary = new ResultSummary(numberOfTests);
		indicesOfScreens = new int[numberOfTests];
		indicesOfTests = new Integer[numberOfComputerScreens];

	}

	public void create() {
		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("joystix monospace.otf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		//font = new BitmapFont();
        font = generator.generateFont(parameter);
		//greyFont = new BitmapFont();
		parameter.color = Color.DARK_GRAY;
        greyFont = generator.generateFont(parameter);
		parameter.color = Color.YELLOW;
		neutralFont = generator.generateFont(parameter);
		parameter.color = Color.GREEN;
		goodFont = generator.generateFont(parameter);
		parameter.color = Color.RED;
		badFont = generator.generateFont(parameter);
		parameter.color = Color.BLACK;
		blackFont = generator.generateFont(parameter);
		//greyFont.setColor(Color.DARK_GRAY);
		vulnerabilityTypes[0] = "SQL Injection";
		vulnerabilityTypes[1] = "Buffer overflow";
		vulnerabilityTypes[2] = "Side channel attack";
		vulnerabilityTypes[3] = "Memory leak";
		testList[0] = new TestScenario(0, "correctcodeexample1.png", "incorrectcodeexample1.png", "SQL Injection", "Database handler",
		"Explanation of how SQL injections work. Filler text to see how wrapping works, hello, goodbye.");
		testList[1] = new TestScenario(1, "correctcodeexample1.png", "incorrectcodeexample1.png", "Buffer overflow", "User input handler",
		"Here will soon be an explanation of how buffer overflow works, as well as how to avoid them.");
		testList[2] = new TestScenario(1, "correctcodeexample1.png", "incorrectcodeexample1.png", "Side channel attack", "Password authenticator",
		"Easy to overlook etc.");
		testList[3] = new TestScenario(1, "correctcodeexample1.png", "incorrectcodeexample1.png", "Memory leak", "Memory handler",
		"Will mention that it is not usually a very dangerous vulnerability but it can crash servers etc.");
		for(int i = 4; i < numberOfTests; i++){
			vulnerabilityTypes[i] = "Placeholder " + i;
			testList[i] = new TestScenario(i, "correctcodeexample1.png", "incorrectcodeexample1.png",
			 "Placeholder " + i, "Sample handler " + i, "Test description for vulnerability number " + i + ".");
		}
		resetTopTenList();
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
		numberOfAnsweredTests = 0;
		totalRounds = rounds;
		indicesOfNeededChanges = new int[numberOfNeededChanges];
		numberOfCorrectlyAnsweredTests = 0;
		round++;
		setRandomValues();
		setLists();
		resetScreenStack();
		generateIndicesForScreens();
		for(int i = 0; i < interactionScreens.length; i++){
			interactionScreens[i] = new ComputerInteractionScreen(this, testList[i].testNeedsChange, i);
		}
		usbInteractionScreen = new USBInteractionScreen(this);
		System.out.println("START OF ARRAY");
		for(int i = 0; i < indicesOfTests.length; i++){
			System.out.println(indicesOfTests[i]);
		}
		System.out.println("END OF ARRAY");
	}

	public void updateTopTenList(){
		int newIndex = -1;
		for(int i = 0; i < 10; i++){
			if(totalScore > topTenScores[i]){
				newIndex = i;
				for(int j = 9; j > i; j--){
					topTenScores[j] = topTenScores[j-1];
				}
				break;
			}
		}
		if(newIndex >= 0){
			topTenScores[newIndex] = totalScore;
		}
	}

	public void resetTopTenList(){
		for(int i = 0; i < 10; i++){
			topTenScores[i] = -1;
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

	private void generateIndicesForScreens(){
		int randomNumber;
		boolean uniqueNumber;
		for(int i = 0; i < numberOfTests; i++){
			uniqueNumber = false;
			while(!uniqueNumber){
				randomNumber = MathUtils.random((numberOfComputerScreens - 1));
				uniqueNumber = true;
				for(int j = 0; j < i; j++){
					if(indicesOfScreens[j] == randomNumber){
						uniqueNumber = false;
					}
				}
				if(uniqueNumber){
					indicesOfScreens[i] = randomNumber;
				}
			}
		}
		for(int i = 0; i < numberOfTests; i++){
			indicesOfTests[indicesOfScreens[i]] = i;
		}
	}

	private void setLists(){
		for(int i = 0; i < testList.length; i++){
			testList[i].resetValues();
		}
		for(int i = 0; i < numberOfNeededChanges; i++){
			testList[indicesOfNeededChanges[i]].testNeedsChange = true;
		}
		for(int i = 0; i < numberOfComputerScreens; i++){
			indicesOfTests[i] = null;
		}
	}


	public void registerAnswer(String answer, int index){
		testList[index].providedAnswer = answer;
		if(testList[index].testNeedsChange && answer.equals(testList[index].correctAnswer)){
			testList[index].testAnsweredCorrectly = true;
			numberOfCorrectlyAnsweredTests++;
		}
		testList[index].testIsFinished = true;
		numberOfAnsweredTests++;
	}

	public void registerAnswer(int index){
		if(!testList[index].testNeedsChange){
			testList[index].testAnsweredCorrectly = true;
			numberOfCorrectlyAnsweredTests++;
		}
		testList[index].testIsFinished = true;
		numberOfAnsweredTests++;
	}

	public void resetAnswer(int index){
		if(testList[index].testAnsweredCorrectly){
			testList[index].testAnsweredCorrectly = false;
			numberOfCorrectlyAnsweredTests--;
		}
		testList[index].testIsFinished = false;
		numberOfAnsweredTests--;
	}

	public void finishRound(){
		totalScore += numberOfCorrectlyAnsweredTests;
		resultSummary.totalNumberOfRounds++;
		for(int i = 0; i < numberOfTests; i++){
			if(testList[i].testAnsweredCorrectly){
				testList[i].totalTestScore++;
				resultSummary.testResults[i]++;
			}
			resultSummary.percentCorrect[i] = (int)((resultSummary.testResults[i]*100) / resultSummary.totalNumberOfRounds);
		}
	}

}
