package com.example.myapplication;

public class FetchStatus {
    private enum Status {INITIAL, LOADING, SUCCESS}
    private Status status;
    public FetchStatus() {
        status = Status.INITIAL;
    }
    public void success() {
        status = Status.SUCCESS;
    }
    public void loading() {
        status = Status.LOADING;
    }
}