package com.example.upark.DAO;

public class SecurityQuestion {

    private int questionID;
    private String questionText;
    private String answerText;

    public SecurityQuestion(int questionID, String questionText, String answerText) {
        this.questionID = questionID;
        this.questionText = questionText;
        this.answerText = answerText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
