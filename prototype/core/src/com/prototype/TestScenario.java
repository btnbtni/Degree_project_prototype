package com.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class TestScenario implements Screen{

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
    public int vulnerabilityIndex;
    public String explanation;
    public String codeSourceOne;
    public String codeSourceTwo;
    public String codeSourceThree;
    public String infoSource;
    public String explanationSource;

    public TestScenario(int index, String correctPath, String incorrectPath, String correctAnswer, String name, String description, Array<String> vulnList, String explanation,
    String codeSourceOne, String codeSourceTwo, String codeSourceThree, String infoSource, String explanationSource){
        testIndex = index;
        correctCodeImage = new Texture(Gdx.files.internal(correctPath));
        incorrectCodeImage = new Texture(Gdx.files.internal(incorrectPath));
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
        testName = name;
        testIsFinished = false;
        testNeedsChange = false;
        totalTestScore = 0;
        testAnsweredCorrectly = false;
        providedAnswer = null;
        this.description = description;
        vulnerabilityIndex = 0;
        this.codeSourceOne = codeSourceOne;
        this.codeSourceTwo = codeSourceTwo;
        this.codeSourceThree = codeSourceThree;
        this.explanationSource = explanationSource;
        this.infoSource = infoSource;
        for(int i = 0; i < vulnList.size; i++){
            if(correctAnswer.equals(vulnList.get(i))){
                vulnerabilityIndex = i;
                break;
            }
        }
    }

    @Override
    public void render(float delta){

    }

    public void resetValues(){
        testIsFinished = false;
        testNeedsChange = false;
        testAnsweredCorrectly = false;
        providedAnswer = null;
    }

    @Override
	public void dispose () {
		correctCodeImage.dispose();
        incorrectCodeImage.dispose();
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
