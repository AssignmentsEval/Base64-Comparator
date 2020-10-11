package com.waes.base64comp.model;

import java.util.ArrayList;

/**
 * The Result class is to save the comparison result.
 */
public class Result {
    /**
     * The string to specify the comparison results.
     */
    String status;

    /**
     * The string to describe the error reason.
     */
    String message;

    /**
     * The ArrayList to store the difference of two strings.
     */
    ArrayList<Diff> diffList;

    public Result() {

    }

    public Result(String status, String message, ArrayList<Diff> diffList) {
        this.status = status;
        this.message = message;
        this.diffList = diffList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Diff> getDiffList() {
        return diffList;
    }

    public void setDiffList(ArrayList<Diff> diffList) {
        this.diffList = diffList;
    }


    @Override
    public String toString() {
        return "Result{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
