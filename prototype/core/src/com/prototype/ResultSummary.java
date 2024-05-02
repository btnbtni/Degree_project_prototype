package com.prototype;

public class ResultSummary {
    public int totalNumberOfRounds;
    public int[] testResults;
    public int[] percentCorrect;

    public ResultSummary(int numberOfTests){
        testResults = new int[numberOfTests];
        percentCorrect = new int[numberOfTests];
        resetResults();
    }

    public void resetResults(){
        totalNumberOfRounds = 0;
        for(int i = 0; i < testResults.length; i++){
            testResults[i] = 0;
            percentCorrect[i] = 0;
        }
    }

}