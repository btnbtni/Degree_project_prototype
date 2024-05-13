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
	public Screen npcInteractionScreen;
	public Screen helpScreen;
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
	// private int numberOfInteractions;
	public int numberOfTests;
	public int numberOfTotalTests;
	public int numberOfNeededChanges;
	public int numberOfVulnerabilities;

	int windowSizeX;
	int windowSizeY;
	int tileSize;
	String[] vulnerabilityTypes;

	public Prototype(int windowSizeX, int windowSizeY){
		this.windowSizeX = windowSizeX;
		this.windowSizeY = windowSizeY;
		this.tileSize = 64;
		// numberOfInteractions = 10;
		numberOfTests = 5;
		numberOfVulnerabilities = 4;
		numberOfTotalTests = numberOfTests + 2;
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
		interactionScreens = new Screen[numberOfTests];
		vulnerabilityTypes = new String[numberOfVulnerabilities];
		screenStack = new Screen[screenStackCapacity];
		topTenScores = new int[10];
		resultSummary = new ResultSummary(numberOfVulnerabilities);
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
		testList[0] = new TestScenario(0, "sqlcorrect.png", "sqlincorrect.png", "SQL Injection", "Database handler",
		"Explanation of how SQL injections work. Filler text to see how wrapping works, hello, goodbye.", vulnerabilityTypes,
		"This code takes an unchecked string from the user and appends it to the query string. While the program asks for a single parameter, a " +
		"malicious user could enter a string which ends the intended query, then they could start their own custom command, for instance inserting " +
		"or deleting data from the database.");
		testList[1] = new TestScenario(1, "buffercorrect.png", "bufferincorrect.png", "Buffer overflow", "User input handler",
		"Here will soon be an explanation of how buffer overflow works, as well as how to avoid them.", vulnerabilityTypes,
		"This program defines an input limit of size 100, then uses this limit when reading input into a buffer of size 25. This means that the user can " +
		"write data outside the buffer, affecting parts of the memory which can belong to other variables or even code to be executed.");
		testList[2] = new TestScenario(1, "sidecorrect.png", "sideincorrect.png", "Side channel attack", "Authenticator",
		"Easy to overlook etc.", vulnerabilityTypes,
		"This code example has a subtle vulnerability. The function examines the provided password character by character and returns the value representing an " +
		"incorrect password as soon as a check fails. This means that there is a correlation between the execution time and the number of correct starting characters in the " +
		"provided password.");
		testList[3] = new TestScenario(1, "memorycorrect.png", "memoryincorrect.png", "Memory leak", "Memory handler",
		"Will mention that it is not usually a very dangerous vulnerability but it can crash servers etc.", vulnerabilityTypes,
		"In this example, the program allocates memory 10 times within a loop, and stores the pointer to that memory in the same variable each time. " +
		"After the loop has finished, the allocated memory pointed to by the variable is freed. This will only be the memory allocated in the last loop iteration, " +
		"however, which means that the program will leak memory every time this function is called.");
		testList[4] = new TestScenario(1, "memorycorrect.png", "memoryincorrect.png", "Memory leak", "Memory handler 2",
		"Will mention that it is not usually a very dangerous vulnerability but it can crash servers etc.", vulnerabilityTypes,
		"In this example, the program allocates memory 10 times within a loop, and stores the pointer to that memory in the same variable each time. " +
		"After the loop has finished, the allocated memory pointed to by the variable is freed. This will only be the memory allocated in the last loop iteration, " +
		"however, which means that the program will leak memory every time this function is called.");
		// for(int i = 4; i < numberOfTests; i++){
		// 	vulnerabilityTypes[i] = "Placeholder " + i;
		// 	testList[i] = new TestScenario(i, "correctcodeexample1.png", "incorrectcodeexample1.png",
		// 	 "Placeholder " + i, "Sample handler " + i, "Test description for vulnerability number " + i + ".");
		// }
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
		if(numErrors > numberOfTests){
			numErrors = numberOfTests;
		}
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
		npcInteractionScreen = new NPCInteractionScreen(this);
		helpScreen = new HelpScreen(this);
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
			resultSummary.totalTestInstances[testList[i].vulnerabilityIndex]++;
			if(testList[i].testAnsweredCorrectly){
				testList[i].totalTestScore++;
				resultSummary.testResults[testList[i].vulnerabilityIndex]++;
			}
		}
		for(int i = 0; i < numberOfVulnerabilities; i++){
			resultSummary.percentCorrect[i] = (int)((resultSummary.testResults[i]*100) / resultSummary.totalTestInstances[i]);
		}
	}

}
