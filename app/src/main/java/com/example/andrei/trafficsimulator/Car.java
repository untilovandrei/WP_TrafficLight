package com.example.andrei.trafficsimulator;

import android.graphics.Bitmap;

/**
 * Created by andrei on 5/10/17.
 */

public class Car {

    private Bitmap image;
    private Coordinates initialPosition;
    private Coordinates currentPosition;
    private int direction;
    private int turn;
    private int lane;
    private int state;
    private int speed;


    public Car( Coordinates initialPosition, Coordinates currentPosition, int direction, int turn,int lane,int state, int speed) {
        //this.image = image;
        this.initialPosition = initialPosition;
        this.currentPosition = currentPosition;
        this.direction = direction;
        this.turn = turn;
        this.lane= lane;
        this.state= state;
        this.speed=speed;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Coordinates getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(Coordinates initialPosition) {
        this.initialPosition = initialPosition;
    }

    public Coordinates getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Coordinates currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}