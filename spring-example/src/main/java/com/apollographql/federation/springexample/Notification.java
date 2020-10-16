package com.apollographql.federation.springexample;

public class Notification {
    private Integer id;
    private String text;
    private Integer caseId;

    public Notification(Integer id, String text, Integer caseId) {
        this.id = id;
        this.text = text;
        this.caseId = caseId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }
}
