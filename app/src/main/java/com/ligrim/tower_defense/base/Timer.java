package com.ligrim.tower_defense.base;

public class Timer {
    private double timeCounter;
    private double lastAlarmTime;
    private final double alarmTime;

    public Timer(double alarmTime) {
        this.timeCounter = 0;
        this.lastAlarmTime = 0;
        this.alarmTime = alarmTime;
    }

    public void tick() {
        timeCounter += alarmTime;
    }

    public boolean alarm(double atTime) {
        if (atTime < lastAlarmTime + alarmTime) return false;
        lastAlarmTime = atTime;
        return true;
    }

    public double getTime() {
        return this.timeCounter;
    }

}
