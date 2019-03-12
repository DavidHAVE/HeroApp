package com.android.weni.pahlawan.pahlawan.object;

/**
 * Created by David on 08/01/2018.
 */

public class Quiz {
    private String number;
    private String question;
    private String point1;
    private String point2;
    private String point3;
    private String point4;
    private String answer;

    public Quiz() {
    }

    public Quiz(String number, String question, String point1, String point2,
                String point3, String point4, String answer) {
        this.number = number;
        this.question = question;
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
        this.answer = answer;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPoint1() {
        return point1;
    }

    public void setPoint1(String point1) {
        this.point1 = point1;
    }

    public String getPoint2() {
        return point2;
    }

    public void setPoint2(String point2) {
        this.point2 = point2;
    }

    public String getPoint3() {
        return point3;
    }

    public void setPoint3(String point3) {
        this.point3 = point3;
    }

    public String getPoint4() {
        return point4;
    }

    public void setPoint4(String point4) {
        this.point4 = point4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
