package com.frauddetector.backend;

import com.frauddetector.frontend.controllers.AlertViewController;
public class AlertViewControllerSingleton {
    private static AlertViewController instance;
    public static synchronized void setInstance(AlertViewController controller) {
        instance = controller;
    }
    public static AlertViewController getInstance() {
        return instance;
    }
}
