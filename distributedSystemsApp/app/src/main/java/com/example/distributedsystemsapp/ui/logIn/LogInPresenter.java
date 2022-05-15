package com.example.distributedsystemsapp.ui.logIn;

public class LogInPresenter {

    private LogInView logInView;

    public LogInPresenter(LogInView view) {
        this.logInView = view;
    }

    public int onLogIn(){
        return logInView.logIn();
    }
}
