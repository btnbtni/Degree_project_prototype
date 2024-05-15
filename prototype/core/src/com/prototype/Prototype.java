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
import com.badlogic.gdx.utils.Array;

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
	public BitmapFont sourceFont;
	public BitmapFont blackSourceFont;
	public Array<Screen> interactionScreens;
	public Screen usbInteractionScreen;
	public Screen npcInteractionScreen;
	public Screen helpScreen;
	public Array<Screen> screenStack;
	public int numberOfAnsweredTests;
	public int numberOfCorrectlyAnsweredTests;
	public int totalScore;
	public int round;
	public int totalRounds;
	public ResultSummary resultSummary;
	public Array<Integer> topTenScores;
	public Array<TestScenario> testList;
	public int numberOfComputerScreens;

	private Array<Integer> indicesOfNeededChanges;
	public Array<Integer> indicesOfScreens;
	public Array<Integer> indicesOfTests;
	private int screenStackPointer;
	private int screenStackCapacity;
	public int numberOfTests;
	public int numberOfTotalTests;
	public int numberOfNeededChanges;
	public int numberOfVulnerabilities;

	public int windowSizeX;
	public int windowSizeY;
	public int tileSize;
	public Array<String> vulnerabilityTypes;

	public Prototype(int windowSizeX, int windowSizeY){
		this.windowSizeX = windowSizeX;
		this.windowSizeY = windowSizeY;
		this.tileSize = 64;
		numberOfTests = 4;
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

		testList = new Array<TestScenario>();

		indicesOfNeededChanges = new Array<Integer>();
		interactionScreens = new Array<Screen>();
		vulnerabilityTypes = new Array<String>();
		screenStack = new Array<Screen>();
		topTenScores = new Array<Integer>();
		resultSummary = new ResultSummary(numberOfVulnerabilities);
		indicesOfScreens = new Array<Integer>();
		indicesOfTests = new Array<Integer>();

		for(int i = 0; i < numberOfTests; i++){
			indicesOfScreens.add(0);
		}

		for(int i = 0; i < 10; i++){
			topTenScores.add(-1);
		}

		for(int i = 0; i < numberOfComputerScreens; i++){
			indicesOfTests.add(0);
		}

		for(int i = 0; i < screenStackCapacity; i++){
			screenStack.add(null);
		}

	}

	public void create() {
		batch = new SpriteBatch();
		/*
		 * Usage of FreeTypeFontGenerator and FreeTypeFontParameter following this guide:
		 * https://libgdx.com/wiki/extensions/gdx-freetype
		 * 
		 * Font file joystix monospace from:
		 * https://www.1001fonts.com/pixel-fonts.html
		 * https://typodermicfonts.com/proportional-joystix/
		 */
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("joystix monospace.otf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		sourceFont = new BitmapFont();
		blackSourceFont = new BitmapFont();
		blackSourceFont.setColor(Color.BLACK);
        font = generator.generateFont(parameter);
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
		vulnerabilityTypes.add("SQL Injection");
		vulnerabilityTypes.add("Buffer overflow");
		vulnerabilityTypes.add("Side channel attack");
		vulnerabilityTypes.add("Memory leak");
		/*
		 * Sources for info, code example and explanation below
		 * https://www.programiz.com/sql/injection
		 * https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
		 */
		testList.add(new TestScenario(0, "sqlcorrect.png", "sqlincorrect.png", "SQL Injection", "Database handler",
		"SQL Injection can happen when SQL statements are produced by concatenating inputs from sources. This can be exploited by users when they include SQL commands in their input.\n"
		+ "SQL Injection can be prevented when programmers sanitize their inputs to check for illegal characters.\n" + 
		"In the scenario where a user is to input their username and inputs are not sanitized they could potentially receive a full list of usernames by typing in: username\" OR \"1\" = \"1", 
		vulnerabilityTypes,
		"This code takes an unchecked string from the user and appends it to the query string. While the program asks for a single parameter, a " +
		"malicious user could enter a string which ends the intended query, then they could start their own custom command, for instance inserting " +
		"or deleting data from the database.",
		"https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html",
		null,
		null,
		"https://www.programiz.com/sql/injection",
		"https://www.programiz.com/sql/injection"));


		/*
		 * Sources for info, code example and explanation below
		 * https://owasp.org/www-community/vulnerabilities/Buffer_Overflow
		 * https://www.tutorialspoint.com/c_standard_library/c_function_fgets.htm
		 * https://www.w3schools.com/c/c_user_input.php
		 */
		testList.add(new TestScenario(1, "buffercorrect.png", "bufferincorrect.png", "Buffer overflow", "User input handler",
		"Buffer overflow is when a program attempts to write to memory outside the intended range. Many programming languages, such as Java, will prevent the data from actually " + 
		"being written outside the allocated memory if attempted. Languages such as C, however, will not prevent this. The memory after the intended range ends can belong to other " + 
		"variables or even the code that will be executed.\n" + 
		"This vulnerability can be exploited in order to crash programs, bypass authentication, or in other ways alter the way the program works.", vulnerabilityTypes,
		"This program defines an input limit of size 100, then uses this limit when reading input into a buffer of size 25. This means that the user can " +
		"write data outside the buffer, affecting parts of the memory which can belong to other variables or even code to be executed.",
		"https://owasp.org/www-community/vulnerabilities/Buffer_Overflow",
		"https://www.tutorialspoint.com/c_standard_library/c_function_fgets.htm",
		"https://www.w3schools.com/c/c_user_input.php",
		"https://owasp.org/www-community/vulnerabilities/Buffer_Overflow",
		"https://owasp.org/www-community/vulnerabilities/Buffer_Overflow"));


		/*
		 * Sources for info, code example and explanation below
		 * https://youtu.be/-D1gf3omRnw?si=WVvQ6YGOPDWpGeIn
		 */
		testList.add(new TestScenario(1, "sidecorrect.png", "sideincorrect.png", "Side channel attack", "Authenticator",
		"A side channel attack is when information surrounding the program can be used to gain information. " + 
		"Examples of types of information that can be used are time and sound. The execution time of a program can, in some cases, " + 
		"reveal information about confidential data. One way to prevent this is by making sure that the execution time does not depend on " + 
		"the confidential data, for instance by making it constant.", vulnerabilityTypes,
		"This code example has a subtle vulnerability. The function examines the provided password character by character and returns the value representing an " +
		"incorrect password as soon as a check fails. This means that there is a correlation between the execution time and the number of correct starting characters in the " +
		"provided password.",
		"https://youtu.be/-D1gf3omRnw?si=WVvQ6YGOPDWpGeIn",
		null,
		null,
		"https://youtu.be/-D1gf3omRnw?si=WVvQ6YGOPDWpGeIn",
		"https://youtu.be/-D1gf3omRnw?si=WVvQ6YGOPDWpGeIn"));



		/*
		 * Sources for info, code example and explanation below
		 * https://owasp.org/www-community/vulnerabilities/Memory_leak
		 * https://www.geeksforgeeks.org/what-is-memory-leak-how-can-we-avoid/
		 */
		testList.add(new TestScenario(1, "memorycorrect.png", "memoryincorrect.png", "Memory leak", "Memory handler",
		"Memory leaks happen when allocated memory is not freed after its use. This can be exploited if the vulnerability can be recreated by malicious users.\n" + 
		"Memory leaks can crash systems, lead to denial of service attacks and lead to monetary losses.", vulnerabilityTypes,
		"In this example, the program allocates memory 10 times within a loop, and stores the pointer to that memory in the same variable each time. " +
		"After the loop has finished, the allocated memory pointed to by the variable is freed. This will only be the memory allocated in the last loop iteration, " +
		"however, which means that the program will leak memory every time this function is called.",
		"https://owasp.org/www-community/vulnerabilities/Memory_leak",
		null,
		null,
		"https://www.geeksforgeeks.org/what-is-memory-leak-how-can-we-avoid/",
		"https://owasp.org/www-community/vulnerabilities/Memory_leak"));
		resetTopTenList();
		helpScreen = new HelpScreen(this);
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		if(batch != null){
			batch.dispose();
		}
		if(font != null){
			font.dispose();
		}
		if(greyFont != null){
			greyFont.dispose();
		}
		if(blackFont != null){
			blackFont.dispose();
		}
		if(goodFont != null){
			goodFont.dispose();
		}
		if(neutralFont != null){
			neutralFont.dispose();
		}
		if(badFont != null){
			badFont.dispose();
		}
		if(sourceFont != null){
			sourceFont.dispose();
		}
		if(helpScreen != null){
			helpScreen.dispose();
		}
		if(npcInteractionScreen != null){
			npcInteractionScreen.dispose();
		}
		if(usbInteractionScreen != null){
			usbInteractionScreen.dispose();
		}
		resetScreenStack();
		for(int i = 0; i < testList.size; i++){
			if(testList.get(i) != null){
				testList.get(i).dispose();
			}
		}

		for(int i = 0; i < interactionScreens.size; i++){
			if(interactionScreens.get(i) != null){
				interactionScreens.get(i).dispose();
			}
		}
	}

	public void startNewSession(int numErrors, int rounds){
		if(numErrors > numberOfTests){
			numErrors = numberOfTests;
		}
		numberOfNeededChanges = numErrors;
		indicesOfNeededChanges = new Array<Integer>();
		for(int i = 0; i < numberOfNeededChanges; i++){
			indicesOfNeededChanges.add(0);
		}
		numberOfAnsweredTests = 0;
		totalRounds = rounds;
		numberOfCorrectlyAnsweredTests = 0;
		round++;
		setRandomValues();
		setLists();
		resetScreenStack();
		generateIndicesForScreens();

		for(int i = 0; i < interactionScreens.size; i++){
			if(interactionScreens.get(i) != null){
				interactionScreens.get(i).dispose();
			}
		}
		interactionScreens = new Array<Screen>();
		for(int i = 0; i < numberOfTests; i++){
			interactionScreens.add(new ComputerInteractionScreen(this, testList.get(i).testNeedsChange, i));
		}
		if(helpScreen != null){
			helpScreen.dispose();
		}
		if(npcInteractionScreen != null){
			npcInteractionScreen.dispose();
		}
		if(usbInteractionScreen != null){
			usbInteractionScreen.dispose();
		}
		helpScreen = new HelpScreen(this);
		usbInteractionScreen = new USBInteractionScreen(this);
		npcInteractionScreen = new NPCInteractionScreen(this);
	}

	public void updateTopTenList(){
		int newIndex = -1;
		for(int i = 0; i < 10; i++){
			if(totalScore > topTenScores.get(i)){
				newIndex = i;
				for(int j = 9; j > i; j--){
					topTenScores.set(j, topTenScores.get(j-1));
				}
				break;
			}
		}
		if(newIndex >= 0){
			topTenScores.set(newIndex, totalScore);
		}
	}

	public void resetTopTenList(){
		for(int i = 0; i < 10; i++){
			topTenScores.set(i, -1);
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
		Screen poppedScreen = screenStack.get(screenStackPointer);
		screenStack.set(screenStackPointer, null);
		screenStackPointer--;
		return poppedScreen;
	}

	public void pushPreviousScreen(Screen screen){
		screenStackPointer++;
		if(screenStackPointer >= screenStackCapacity){
			screenStackPointer--;
			resetScreenStack();
			Gdx.app.exit();
		}
		screenStack.set(screenStackPointer, screen);
	}

	public void resetScreenStack(){
		for(int i = 0; i < screenStackCapacity; i++){
			if(screenStack.get(i) != null){
				screenStack.get(i).dispose();
			}
			screenStack.set(i, null);
		}
		screenStackPointer = -1;
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
					if(indicesOfNeededChanges.get(j) == randomNumber){
						uniqueNumber = false;
					}
				}
				if(uniqueNumber){
					indicesOfNeededChanges.set(i, randomNumber);
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
					if(indicesOfScreens.get(j) == randomNumber){
						uniqueNumber = false;
					}
				}
				if(uniqueNumber){
					indicesOfScreens.set(i, randomNumber);
				}
			}
		}
		for(int i = 0; i < numberOfTests; i++){
			indicesOfTests.set(indicesOfScreens.get(i), i);
		}
	}

	private void setLists(){
		for(int i = 0; i < testList.size; i++){
			testList.get(i).resetValues();
		}
		for(int i = 0; i < numberOfNeededChanges; i++){
			testList.get(indicesOfNeededChanges.get(i)).testNeedsChange = true;
		}
		for(int i = 0; i < numberOfComputerScreens; i++){
			indicesOfTests.set(i, null);
		}
	}


	public void registerAnswer(String answer, int index){
		testList.get(index).providedAnswer = answer;
		if(testList.get(index).testNeedsChange && answer.equals(testList.get(index).correctAnswer)){
			testList.get(index).testAnsweredCorrectly = true;
			numberOfCorrectlyAnsweredTests++;
		}
		testList.get(index).testIsFinished = true;
		numberOfAnsweredTests++;
	}

	public void registerAnswer(int index){
		if(!testList.get(index).testNeedsChange){
			testList.get(index).testAnsweredCorrectly = true;
			numberOfCorrectlyAnsweredTests++;
		}
		testList.get(index).testIsFinished = true;
		numberOfAnsweredTests++;
	}

	public void resetAnswer(int index){
		if(testList.get(index).testAnsweredCorrectly){
			testList.get(index).testAnsweredCorrectly = false;
			numberOfCorrectlyAnsweredTests--;
		}
		testList.get(index).testIsFinished = false;
		numberOfAnsweredTests--;
	}

	public void finishRound(){
		totalScore += numberOfCorrectlyAnsweredTests;
		resultSummary.totalNumberOfRounds++;
		for(int i = 0; i < numberOfTests; i++){
			resultSummary.totalTestInstances.set(testList.get(i).vulnerabilityIndex, resultSummary.totalTestInstances.get(testList.get(i).vulnerabilityIndex) + 1);
			if(testList.get(i).testAnsweredCorrectly){
				testList.get(i).totalTestScore++;
				resultSummary.testResults.set(testList.get(i).vulnerabilityIndex, resultSummary.testResults.get(testList.get(i).vulnerabilityIndex));
			}
		}
		for(int i = 0; i < numberOfVulnerabilities; i++){
			resultSummary.percentCorrect.set(i, (int)((resultSummary.testResults.get(i)*100) / resultSummary.totalTestInstances.get(i)));
		}
	}

}
