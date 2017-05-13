package com.example.andrei.trafficsimulator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by andrei on 10/5/17.
 */

public class TrafficView extends View{

    private Paint penTrees, penCrossLines, penIncont;
    int screenWidth,screenHeight;
    int roadWidth,x=0,y=0;
    public final int TRAFFIC_LIGHT_HEIGHT=200;
    public final int TRAFFIC_LIGHT_WIDHT=80;
    ArrayList<Bitmap> list=new ArrayList<>();
    ArrayList<Car> carsList=new ArrayList<>();
    Resources r = getResources();



    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());

    public TrafficView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setup();
    }




    @Override
    protected void onDraw(Canvas canvas) {
        drawEnviroment(canvas);

        for(Car car:carsList){
            canvas.drawBitmap(car.getImage(),car.getCurrentPosition().getX(),car.getCurrentPosition().getY(),penTrees);
            moveCar(car);
        }
        invalidate();

    }

    private void moveCar(Car car) {
        int x, y;
        Coordinates coord;
        switch (car.getDirection()){
            case Constants.MOVE_DOWN:
                x=car.getCurrentPosition().getX();
                y=car.getCurrentPosition().getY();
                coord=new Coordinates(x,y+2);
                car.setCurrentPosition(coord);
                if(y>(screenHeight-roadWidth)/2+10 && car.getTurn()!=Constants.NO_TURN){
                    performTurning(car);
                }
                break;
            case Constants.MOVE_TOP:
                 x=car.getCurrentPosition().getX();
                 y=car.getCurrentPosition().getY();
                 coord=new Coordinates(x,y-2);
                car.setCurrentPosition(coord);
                break;
            case Constants.MOVE_LEFT:
                 x=car.getCurrentPosition().getX();
                 y=car.getCurrentPosition().getY();
                 coord=new Coordinates(x-2,y);
                car.setCurrentPosition(coord);
                break;
            case Constants.MOVE_RIGHT:
                 x=car.getCurrentPosition().getX();
                 y=car.getCurrentPosition().getY();
                 coord=new Coordinates(x+2,y);
                car.setCurrentPosition(coord);
                break;
        }

    }

    private void performTurning(Car car){
        Bitmap bm;
        switch(car.getTurn()){
            case Constants.TURN_RIGHT:
                switch(car.getDirection()){
                    case Constants.MOVE_DOWN:
                        bm=car.getImage();
                        rotateBitmap(bm,90);
                        Bitmap img= BitmapFactory.decodeResource(r,R.drawable.green_car_left);
                        img=getResizedBitmap(img,160,90);
                        car.setImage(img);
                        car.setTurn(Constants.NO_TURN);
                        car.setDirection(Constants.MOVE_LEFT);
                        break;


                }
        }
    }


    public void setup(){
        screenWidth=getScreenWidth();
        screenHeight=getScreenHeight();
        penTrees=new Paint();
        penTrees.setColor(Color.parseColor("#008000"));
        penTrees.setStrokeWidth(5);
        penTrees.setStyle(Paint.Style.FILL);

        penCrossLines=new Paint();
        penCrossLines.setColor(Color.WHITE);
        penCrossLines.setStrokeWidth(5);
        roadWidth=500;

        penIncont=new Paint();
        penIncont.setColor(Color.WHITE);
        penIncont.setStyle(Paint.Style.STROKE);

        generateCars();





    }

    public void drawEnviroment(Canvas canvas){
        canvas.drawRect(0,0,(screenWidth-roadWidth)/2,(screenHeight-roadWidth)/2,penTrees);
        canvas.drawRect((screenWidth+roadWidth)/2,0,screenWidth,(screenHeight-roadWidth)/2,penTrees);
        canvas.drawRect(0,(screenHeight+roadWidth)/2,(screenWidth-roadWidth)/2,screenHeight,penTrees);
        canvas.drawRect((screenWidth+roadWidth)/2,(screenHeight+roadWidth)/2,screenWidth,screenHeight,penTrees);

        //drawLines

        //top
        canvas.drawLine(screenWidth/2-5,0,screenWidth/2-5,(screenHeight-roadWidth)/2,penCrossLines);
        canvas.drawLine(screenWidth/2+5,0,screenWidth/2+5,(screenHeight-roadWidth)/2,penCrossLines);

        canvas.drawLine((screenWidth-roadWidth/2)/2-2,0,(screenWidth-roadWidth/2)/2-2,(screenHeight-roadWidth)/2,penCrossLines);

        //bottom
        canvas.drawLine(screenWidth/2-5,(screenHeight+roadWidth)/2,screenWidth/2-5,screenHeight,penCrossLines);
        canvas.drawLine(screenWidth/2+5,(screenHeight+roadWidth)/2,screenWidth/2+5,screenHeight,penCrossLines);
        //left
        canvas.drawLine(0,screenHeight/2-5,(screenWidth-roadWidth)/2,screenHeight/2-5,penCrossLines);
        canvas.drawLine(0,screenHeight/2+5,(screenWidth-roadWidth)/2,screenHeight/2+5,penCrossLines);
        //right
        canvas.drawLine((screenWidth+roadWidth)/2,screenHeight/2-5,screenWidth,screenHeight/2-5,penCrossLines);
        canvas.drawLine((screenWidth+roadWidth)/2,screenHeight/2+5,screenWidth,screenHeight/2+5,penCrossLines);


    }

    public int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels-(int)px;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    private void generateCars() {
        Coordinates init=new Coordinates(screenWidth/2-roadWidth/2,0);
        Car car=new Car(init,init,2,7);
        Bitmap img= BitmapFactory.decodeResource(r,R.drawable.green_car_down);
        rotateBitmap(img,180);
        img=getResizedBitmap(img,90,160);
        car.setDirection(Constants.MOVE_DOWN);
        car.setTurn(Constants.TURN_RIGHT);
        car.setImage(img);
        carsList.add(car);

    }


}
