package com.prototype;

public class ResultSummary {
    public int totalNumberOfRounds;
    public int[] testResults;
    public int[] totalTestInstances;
    public int[] percentCorrect;

    public ResultSummary(int numberOfVulnerabilities){
        testResults = new int[numberOfVulnerabilities];
        totalTestInstances = new int[numberOfVulnerabilities];
        percentCorrect = new int[numberOfVulnerabilities];
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
