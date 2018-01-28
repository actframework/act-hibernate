package com.techempower.ee7;

import act.Act;

import javax.ws.rs.core.Application;

public class MyApplication extends Application {
    public static void main(String[] args) throws Exception {
        Act.start();
    }
}
