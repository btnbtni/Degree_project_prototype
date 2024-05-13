package com.prototype;

import com.badlogic.gdx.utils.Array;

public class ResultSummary {
    public int totalNumberOfRounds;
    public Array<Integer> testResults;
    public Array<Integer> totalTestInstances;
    public Array<Integer> percentCorrect;

    public ResultSummary(int numberOfVulnerabilities){
        testResults = new Array<Integer>();
        totalTestInstances = new Array<Integer>();
        percentCorrect = new Array<Integer>();
        for(int i = 0; i < numberOfVulnerabilities; i++){
            testResults.add(0);
            totalTestInstances.add(0);
            percentCorrect.add(0);
        }
        resetResults();
    }

    public void resetResults(){
        totalNumberOfRounds = 0;
        for(int i = 0; i < testResults.size; i++){
            testResults.set(i, 0);
            percentCorrect.set(i, 0);
        }
    }

}
