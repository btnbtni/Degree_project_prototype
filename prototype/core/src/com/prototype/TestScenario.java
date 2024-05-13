package com.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

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

    public TestScenario(int index, String correctPath, String incorrectPath, String correctAnswer, String name, String description, String[] vulnList, String explanation){
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
        for(int i = 0; i < vulnList.length; i++){
            if(correctAnswer.equals(vulnList[i])){
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
