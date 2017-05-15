package com.example.andrei.trafficsimulator;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by corina on 5/13/17.
 */
public class TrafficLight {

    public final static int TRAFFIC_LIGHT_HEIGHT=180;
    public final static int TRAFFIC_LIGHT_WIDTH=80;
    public final static int LIGHT_COLOR_GREEN=0;
    public final static int LIGHT_COLOR_YELLOW=1;
    public final static int LIGHT_COLOR_RED=2;

    public static int verticalRoadLightCurrentColor=0;
    public static int horizontalRoadLightCurrentColor=2;
    
    public static  int verticalRoadLastColor=0;
    public static  int horizontalRoadLastColor=2;

    public static void setSemaforsToYellow(){

        verticalRoadLightCurrentColor=1;
        horizontalRoadLightCurrentColor=1;
    }
    
    public static void changeLightColors(){
        if(verticalRoadLastColor==LIGHT_COLOR_GREEN){
            verticalRoadLightCurrentColor=LIGHT_COLOR_RED;
        }else{
            verticalRoadLightCurrentColor=LIGHT_COLOR_GREEN;
        }

        if(horizontalRoadLastColor==LIGHT_COLOR_GREEN){
            horizontalRoadLightCurrentColor=LIGHT_COLOR_RED;
        }else{
            horizontalRoadLightCurrentColor=LIGHT_COLOR_GREEN;
        }
        verticalRoadLastColor=verticalRoadLightCurrentColor;
        horizontalRoadLastColor=horizontalRoadLightCurrentColor;
    }

    public static boolean allLightColorYellow(){
        if(verticalRoadLightCurrentColor== LIGHT_COLOR_YELLOW && horizontalRoadLightCurrentColor== LIGHT_COLOR_YELLOW){
            return true;
        }else{
            return false;
        }

    }

    public static boolean horizontalRoadLightCurrentColorIsGreen(){
        if(horizontalRoadLightCurrentColor==LIGHT_COLOR_GREEN){
            return true;
        }else{
            return false;
        }
    }
    public static boolean verticalRoadLightCurrentColorIsGreen(){
        if(verticalRoadLightCurrentColor==LIGHT_COLOR_GREEN){
            return true;
        }else{
            return false;
        }
    }

    public static boolean horizontalRoadLightCurrentColorIsRed(){
        if(horizontalRoadLightCurrentColor==LIGHT_COLOR_RED){
            return true;
        }else{
            return false;
        }
    }
    public static boolean verticalRoadLightCurrentColorIsRed(){
        if(verticalRoadLightCurrentColor==LIGHT_COLOR_RED){
            return true;
        }else{
            return false;
        }
    }

}
