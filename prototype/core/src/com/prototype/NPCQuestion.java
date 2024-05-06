package com.prototype;

import java.util.ArrayList;

public class NPCQuestion {
    
    private String question;
    private Integer correctOption;
    private Integer selectedOption;
    private ArrayList<String> options;
    private boolean result;

    public NPCQuestion(String question){
        this.question = question;
        this.result = false;
        this.options = new ArrayList<>();
    }

    public String getQuestion(){
        return this.question;
    }

    public boolean correctQuestion(){
        if(this.selectedOption != null && this.correctOption != null && this.selectedOption == this.correctOption){
            result = true;
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<String> getOptions(){
        return this.options;
    }

    public Integer getSelectedOption(){
        return this.selectedOption;
    }

    public void setSelectedOption(int option){
        this.selectedOption = option;
    }

    public void putOption(String option, boolean correct){
        this.options.add(option);
        if(correct){
            this.correctOption = this.options.size()-1;
        }
    }

    public boolean getResult(){
        return this.result;
    }

}
