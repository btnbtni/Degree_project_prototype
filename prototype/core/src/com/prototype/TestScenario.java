package com.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TestScenario {

    public String testName;
    public boolean testIsFinished;
	public boolean testNeedsChange;
    public int totalTestScore;
    public boolean testAnsweredCorrectly;
    public String correctAnswer;
    public String providedAnswer;
    public int testIndex;
    public Texture correctCodeImage;
    public Texture incorrectCodeImage;
    public String description;

    public TestScenario(int index, String correctPath, String incorrectPath, String correctAnswer, String name, String description){
        testIndex = index;
        correctCodeImage = new Texture(Gdx.files.internal(correctPath));
        incorrectCodeImage = new Texture(Gdx.files.internal(incorrectPath));
        this.correctAnswer = correctAnswer;
        testName = name;
        testIsFinished = false;
        testNeedsChange = false;
        totalTestScore = 0;
        testAnsweredCorrectly = false;
        providedAnswer = null;
        this.description = description;
    }

    public void resetValues(){
        testIsFinished = false;
        testNeedsChange = false;
        testAnsweredCorrectly = false;
        providedAnswer = null;
    }
}
