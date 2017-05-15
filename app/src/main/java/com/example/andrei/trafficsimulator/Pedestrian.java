package com.example.andrei.trafficsimulator;

import android.graphics.Bitmap;

/**
 * Created by corina on 5/14/17.
 */
public class Pedestrian {

    private int id;
    private int coordinateX;
    private int coordinateY;
    private Bitmap image;
    private boolean walking;
    private boolean directionLeftRight;
    private int roadToCrossCoordsMin;
    private int roadToCrossCoordsMax;

    private int direction=1;/// 1-> normal direction -1->reverse

    public static int walkingCoordsRate=2;

    public Pedestrian() {
    }


    public void movePedestrianX() {
        if (directionLeftRight ) {
            android.util.Log.i("Info", "X="+coordinateX+" , min="+roadToCrossCoordsMin+" , max="+roadToCrossCoordsMax);
            coordinateX -= walkingCoordsRate*direction;
        }
    }

    public void movePedestrianY(){
        if(!directionLeftRight) {
            coordinateY += walkingCoordsRate*direction;
        }
    }

    public boolean isOnRoadX(){
        if(coordinateX>roadToCrossCoordsMin && coordinateX<roadToCrossCoordsMax){
            return true;
        }else{
            return false;
        }
    }

    public boolean isOnRoadY(){
        if(coordinateY>roadToCrossCoordsMin && coordinateY<roadToCrossCoordsMax){
            return true;
        }else{
            return false;
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getRoadToCrossCoordsMin() {
        return roadToCrossCoordsMin;
    }

    public void setRoadToCrossCoordsMin(int roadToCrossCoordsMin) {
        this.roadToCrossCoordsMin = roadToCrossCoordsMin;
    }

    public int getRoadToCrossCoordsMax() {
        return roadToCrossCoordsMax;
    }

    public void setRoadToCrossCoordsMax(int roadToCrossCoordsMax) {
        this.roadToCrossCoordsMax = roadToCrossCoordsMax;
    }

    public boolean isDirectionLeftRight() {
        return directionLeftRight;
    }

    public void setDirectionLeftRight(boolean directionLeftRight) {
        this.directionLeftRight = directionLeftRight;
    }

    public Pedestrian(Bitmap image) {
        this.image = image;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isWalking() {
        return walking;
    }

    public void setWalking(boolean walking) {
        this.walking = walking;
    }
}
