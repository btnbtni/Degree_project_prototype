package com.prototype;

public class QuizQuestion {
    
    private String question;
    private boolean correctAnswer;
    private boolean selectedAnswer;
    private boolean inverted;
    private boolean result;

    public QuizQuestion(String question, boolean correctAnswer, boolean inverted){
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.selectedAnswer = false;
        this.inverted = inverted;
    }

    public String getQuestion(){
        return this.question;
    }

    public boolean correctQuestion(){
        if(this.inverted && this.correctAnswer == !this.selectedAnswer){
            this.result = true;
            return true;
        }
        else if(!this.inverted && this.correctAnswer == this.selectedAnswer){
            this.result = true;
            return true;
        }
        else{
            this.result = false;
            return false;
        }
    }

    public boolean getSelectedAnswer(){
        return this.selectedAnswer;
    }

    public void toggleSelect(){
        this.selectedAnswer = !this.selectedAnswer;
    }

    public boolean getResult(){
        return this.result;
    }

}
