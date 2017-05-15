package com.example.andrei.trafficsimulator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by andrei on 10/5/17.
 */

public class TrafficView extends View{

    //////Declare Variables/Constants
    private Paint penTrees, penCrossLines, penIncont;
    int screenWidth,screenHeight;
    static int speed=5;
    int roadWidth,x=0,y=0;
    ArrayList<Bitmap> list=new ArrayList<>();
    ArrayList<Car> carsList=new ArrayList<>();
    Resources r = getResources();
    Coordinates firstTop,secondTop,firstRight,secondRight, firstLeft,secondLeft, firstBottom, secondBottom;
    ArrayList<Coordinates> positions=new ArrayList<>();

    public static long intervalYellowColor=4000;
    public static long intervalRedGreenColor=20000;
    public static final long intervalChangeRate=500;
    public static final int pedestrianHeight=60;

    Bitmap trafficLightRed= BitmapFactory.decodeResource(r,R.drawable.semafor_rosu_h70);
    Bitmap trafficLightGalben= BitmapFactory.decodeResource(r,R.drawable.semafor_galben_h70);
    Bitmap trafficLightVerde= BitmapFactory.decodeResource(r,R.drawable.semafor_verde_h70);
    CounterClass timer=new CounterClass(intervalRedGreenColor, 1000);

    Bitmap pieton1= BitmapFactory.decodeResource(r,R.drawable.man1_h50);
    Bitmap pieton2= BitmapFactory.decodeResource(r,R.drawable.man2);
    List<Pedestrian> pedestriansList=new ArrayList<>();
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
        if(carsList.size()==0){
            initiateMotion();
        }

        refreshList();
        //checkTrafficLights();


        for(Car car:carsList){
            canvas.drawBitmap(car.getImage(),car.getCurrentPosition().getX(),car.getCurrentPosition().getY(),penTrees);
            moveCar(car);

        }
        movePedestrians();
        invalidate();
    }

    private void checkTrafficLights() {
        for (int i = 0; i <carsList.size() ; i++) {
            if(TrafficLight.horizontalRoadLightCurrentColorIsRed()){
                if(carsList.get(i).getDirection()==Constants.MOVE_LEFT || carsList.get(i).getDirection()==Constants.MOVE_RIGHT){
                    carsList.get(i).setSpeed(0);
                } else {
                    carsList.get(i).setSpeed(speed);
                }
            } else if(TrafficLight.verticalRoadLightCurrentColorIsRed()){
                if(carsList.get(i).getDirection()==Constants.MOVE_TOP || carsList.get(i).getDirection()==Constants.MOVE_DOWN){
                    carsList.get(i).setSpeed(0);
                } else {
                    carsList.get(i).setSpeed(speed);
                }
            }

        }
    }

    private void initiateMotion() {

            for (int i = 0; i <5 ; i++) {
                carsList.add(generateCar());
                //addNewCar();
            }


    }



    private void refreshList() {

        if(carsList.size()>0){
            for (int i = 0; i < carsList.size(); i++) {
                Car extractedCar=carsList.get(i);
                if(extractedCar!=null){
                    int x=extractedCar.getCurrentPosition().getX();
                    int y=extractedCar.getCurrentPosition().getY();
                    if(x<=-200 || x>screenWidth || y<=-200 || y>=screenHeight){
                        carsList.remove(i);
                        Car genCar=generateCar();
                        carsList.add(genCar);

                        //addNewCar();
                    }
                }

            }
        }




    }

    private void addNewCar() {
        Car genCar=null;
        while(true){
            genCar=generateCar();
            if(TrafficLight.horizontalRoadLightCurrentColorIsGreen()){
                if(genCar.getDirection()==Constants.MOVE_LEFT || genCar.getDirection()==Constants.MOVE_RIGHT ){
                    break;
                }
            } else if(TrafficLight.verticalRoadLightCurrentColorIsGreen()){
                if(genCar.getDirection()==Constants.MOVE_TOP || genCar.getDirection()==Constants.MOVE_DOWN ){
                    break;
                }
            }
        }
    }


    private void moveCar(Car car) {
        int x, y;
        Coordinates coord;
        switch (car.getDirection()){
            case Constants.MOVE_DOWN:
                x=car.getCurrentPosition().getX();
                y=car.getCurrentPosition().getY();
                coord=new Coordinates(x,y+speed);
                car.setCurrentPosition(coord);
                if(car.getLane()==1){
                    if(y>(screenHeight-roadWidth)/2+5 && car.getTurn()!=Constants.NO_TURN){
                        performTurning(car);
                    }
                } else if(car.getLane()==2) {
                    if(y>(screenHeight/2-roadWidth/4+5) && car.getTurn()!=Constants.NO_TURN){
                        performTurning(car);
                    }
                }

                break;
            case Constants.MOVE_TOP:
                x=car.getCurrentPosition().getX();
                y=car.getCurrentPosition().getY();
                coord=new Coordinates(x,y-speed);
                car.setCurrentPosition(coord);
                if(car.getLane()==1){
                    if(y<(screenHeight/2+roadWidth/4+20) && car.getTurn()!=Constants.NO_TURN){
                        performTurning(car);
                    }
                } else if(car.getLane()==2) {
                    if(y<(screenHeight/2+20) && car.getTurn()!=Constants.NO_TURN){
                        performTurning(car);
                    }
                }
                break;
            case Constants.MOVE_LEFT:
                x=car.getCurrentPosition().getX();
                y=car.getCurrentPosition().getY();
                coord=new Coordinates(x-speed,y);
                car.setCurrentPosition(coord);
                if(car.getLane()==1){
                    if(x<(screenWidth/2+roadWidth/4+20) && car.getTurn()!=Constants.NO_TURN){
                        performTurning(car);
                    }
                } else if(car.getLane()==2) {
                    if(x<(screenWidth/2+20) && car.getTurn()!=Constants.NO_TURN){
                        performTurning(car);
                    }
                }
                break;
            case Constants.MOVE_RIGHT:
                x=car.getCurrentPosition().getX();
                y=car.getCurrentPosition().getY();
                coord=new Coordinates(x+speed,y);
                car.setCurrentPosition(coord);
                if(car.getLane()==1){
                    if(x>(screenWidth-roadWidth)/2+5 && car.getTurn()!=Constants.NO_TURN){
                        performTurning(car);
                    }
                } else if(car.getLane()==2) {
                    if(x>(screenWidth/2-roadWidth/4+5) && car.getTurn()!=Constants.NO_TURN){
                        performTurning(car);
                    }
                }
                break;
        }

    }


    private void movePedestrians(){
        for(Pedestrian pedestrian:pedestriansList){
            /// Walks on X Axis
            if(pedestrian.isDirectionLeftRight()){
                if(TrafficLight.horizontalRoadLightCurrentColorIsGreen()){
                    pedestrian.setWalking(Boolean.TRUE);
                    if(pedestrian.getCoordinateX()<=pedestrian.getRoadToCrossCoordsMin()||pedestrian.getCoordinateX()>pedestrian.getRoadToCrossCoordsMax()) {
                        android.util.Log.i("Info", "***1x CHANGE DIREcTION="+pedestrian.getDirection()+" X="+pedestrian.getCoordinateX()+" min="+pedestrian.getRoadToCrossCoordsMin()+", max="+pedestrian.getRoadToCrossCoordsMax());
                        pedestrian.setDirection(pedestrian.getDirection()*-1);
                    }
                }else{

                    if(!pedestrian.isOnRoadX()) {
                        pedestrian.setWalking(Boolean.FALSE);
                    }
                }
            }

            /// Walks on Y Axis
            if(!pedestrian.isDirectionLeftRight()){
                if(TrafficLight.verticalRoadLightCurrentColorIsGreen()){
                    pedestrian.setWalking(Boolean.TRUE);
                    if(pedestrian.getCoordinateY()<pedestrian.getRoadToCrossCoordsMin()||pedestrian.getCoordinateY()>pedestrian.getRoadToCrossCoordsMax()+3) {
                        android.util.Log.i("Info", "***1y CHANGE DIREcTION="+pedestrian.getDirection()+" X="+pedestrian.getCoordinateY()+" min="+pedestrian.getRoadToCrossCoordsMin()+", max="+pedestrian.getRoadToCrossCoordsMax());
                        pedestrian.setDirection(pedestrian.getDirection()*-1);

                    }

                }else{

                    if(!pedestrian.isOnRoadY()){
                        pedestrian.setWalking(Boolean.FALSE);
                    }
                }
            }


            if(pedestrian.isWalking()){
                pedestrian.movePedestrianX();
                pedestrian.movePedestrianY();
            }
        }
    }

    private void performTurning(Car car){
        Bitmap img=null;
        switch(car.getTurn()){
            case Constants.TURN_RIGHT:
                switch(car.getDirection()){
                    case Constants.MOVE_DOWN:
                        if(car.getState()==Constants.SIMPLE_CAR){
                            img= BitmapFactory.decodeResource(r,R.drawable.green_car_left);
                        } else if(car.getState()==Constants.POLICE_CAR){
                            img= BitmapFactory.decodeResource(r,R.drawable.police_car_left);
                        } else {
                            img= BitmapFactory.decodeResource(r,R.drawable.ambulance_left);
                        }
                        img=getResizedBitmap(img,160,90);
                        car.setImage(img);
                        car.setTurn(Constants.NO_TURN);
                        car.setDirection(Constants.MOVE_LEFT);
                        break;
                    case Constants.MOVE_TOP:
                        if(car.getState()==Constants.SIMPLE_CAR){
                            img= BitmapFactory.decodeResource(r,R.drawable.green_car_right);
                        } else if(car.getState()==Constants.POLICE_CAR){
                            img= BitmapFactory.decodeResource(r,R.drawable.police_car_right);
                        } else{
                            img= BitmapFactory.decodeResource(r,R.drawable.ambulance_right);
                        }
                        img=getResizedBitmap(img,160,90);
                        car.setImage(img);
                        car.setTurn(Constants.NO_TURN);
                        car.setDirection(Constants.MOVE_RIGHT);
                        break;
                    case Constants.MOVE_RIGHT:
                        if(car.getState()==Constants.SIMPLE_CAR){
                            img= BitmapFactory.decodeResource(r,R.drawable.green_car_down);
                        } else if(car.getState()==Constants.POLICE_CAR){
                            img= BitmapFactory.decodeResource(r,R.drawable.police_car_down);
                        } else{
                            img= BitmapFactory.decodeResource(r,R.drawable.ambulance_down);
                        }
                        img=getResizedBitmap(img,90,160);
                        car.setImage(img);
                        car.setTurn(Constants.NO_TURN);
                        car.setDirection(Constants.MOVE_DOWN);
                        break;
                    case Constants.MOVE_LEFT:
                        if(car.getState()==Constants.SIMPLE_CAR){
                            img= BitmapFactory.decodeResource(r,R.drawable.green_car_up);
                        } else if(car.getState()==Constants.POLICE_CAR){
                            img= BitmapFactory.decodeResource(r,R.drawable.police_car_up);
                        } else{
                            img= BitmapFactory.decodeResource(r,R.drawable.ambulance_up);
                        }
                        img=getResizedBitmap(img,90,160);
                        car.setImage(img);
                        car.setTurn(Constants.NO_TURN);
                        car.setDirection(Constants.MOVE_TOP);
                        break;


                } break;
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

        generateInitialConditions();
        //generateCars();

        initPedstrians();

        //start timer
        timer.start();
    }

    private void generateInitialConditions(){
        firstTop=new Coordinates(screenWidth/2-roadWidth/2+15,-200);
        secondTop=new Coordinates(screenWidth/2-roadWidth/4+15,-200);
        firstBottom=new Coordinates(screenWidth/2+roadWidth/4+15,screenHeight);
        secondBottom=new Coordinates(screenWidth/2+15,screenHeight);
        firstLeft=new Coordinates(-200,screenHeight/2+roadWidth/4+15);
        secondLeft=new Coordinates(-200,screenHeight/2+15);
        firstRight=new Coordinates(screenWidth-170+200,screenHeight/2-roadWidth/2+15);
        secondRight=new Coordinates(screenWidth-170+200,screenHeight/2-roadWidth/4+15);
        positions.add(firstTop);
        positions.add(secondTop);
        positions.add(firstBottom);
        positions.add(secondBottom);
        positions.add(firstLeft);
        positions.add(secondLeft);
        positions.add(firstRight);
        positions.add(secondRight);

    }


    public void drawTrafficLights(Canvas canvas){

        Bitmap trafficLightHorizontal=null;

        if(TrafficLight.horizontalRoadLightCurrentColorIsRed()){
            trafficLightHorizontal=trafficLightRed;
            if(!CounterClass.waitingRG) {
                timer = new CounterClass(intervalRedGreenColor, 1000);
                timer.start();
            }
        }else if(TrafficLight.horizontalRoadLightCurrentColorIsGreen()){
            trafficLightHorizontal=trafficLightVerde;
            if(!CounterClass.waitingRG) {
                timer = new CounterClass(intervalRedGreenColor, 1000);
                timer.start();
            }
        }else{
            trafficLightHorizontal=trafficLightGalben;
            if(!CounterClass.waitingYellow) {
                timer = new CounterClass(intervalYellowColor, 1000);
                timer.start();
            }
        }

        //Horizontal Road
        Bitmap trafficLightRedRotatedRight90 =Utils.RotateBitmap(trafficLightHorizontal,90);
        canvas.drawBitmap(trafficLightRedRotatedRight90,(screenWidth-roadWidth)/2-TrafficLight.TRAFFIC_LIGHT_HEIGHT,(screenHeight-roadWidth)/2-	TrafficLight.TRAFFIC_LIGHT_WIDTH,penTrees);

        canvas.drawRect((screenWidth+roadWidth)/2,(screenHeight+roadWidth)/2,screenWidth,screenHeight,penTrees);
        Bitmap trafficLightRedRotatedRight270 =Utils.RotateBitmap(trafficLightHorizontal,270);
        canvas.drawBitmap(trafficLightRedRotatedRight270,(screenWidth+roadWidth)/2,(screenHeight+roadWidth)/2,penTrees);


        Bitmap trafficLightVertical=null;
        if(TrafficLight.verticalRoadLightCurrentColorIsRed()){
            trafficLightVertical=trafficLightRed;
        }else if(TrafficLight.verticalRoadLightCurrentColorIsGreen()){
            trafficLightVertical=trafficLightVerde;
        }else{
            trafficLightVertical=trafficLightGalben;
        }
        //Vertical Road
        Bitmap trafficLightVerdeRotatedRight180 =Utils.RotateBitmap(trafficLightVertical,180);
        canvas.drawBitmap(trafficLightVerdeRotatedRight180,(screenWidth+roadWidth)/2,(screenHeight-roadWidth)/2-TrafficLight.TRAFFIC_LIGHT_HEIGHT,penTrees);

        canvas.drawBitmap(trafficLightVertical,(screenWidth-roadWidth)/2-TrafficLight.TRAFFIC_LIGHT_WIDTH,(screenHeight+roadWidth)/2,penTrees);

    }

    public void drawPedestrians(Canvas canvas){
        for(Pedestrian pedestrian:pedestriansList){
            //if(pedestrian.isWalking()){
                canvas.drawBitmap(pedestrian.getImage(), pedestrian.getCoordinateX(), pedestrian.getCoordinateY(), penTrees);
          //  }
        }
    }


    public void drawEnviroment(Canvas canvas){
        canvas.drawRect(0,0,(screenWidth-roadWidth)/2,(screenHeight-roadWidth)/2,penTrees);
        canvas.drawRect((screenWidth+roadWidth)/2,0,screenWidth,(screenHeight-roadWidth)/2,penTrees);
        canvas.drawRect(0,(screenHeight+roadWidth)/2,(screenWidth-roadWidth)/2,screenHeight,penTrees);
        canvas.drawRect((screenWidth+roadWidth)/2,(screenHeight+roadWidth)/2,screenWidth,screenHeight,penTrees);


        //Draw Traffic Lights
        drawTrafficLights(canvas);

        //drawLines

        //top
        canvas.drawLine(screenWidth/2-5,0,screenWidth/2-5,(screenHeight-roadWidth)/2,penCrossLines);
        canvas.drawLine(screenWidth/2+5,0,screenWidth/2+5,(screenHeight-roadWidth)/2,penCrossLines);

        //top delimitators
        canvas.drawLine((screenWidth-roadWidth/2)/2-2,0,(screenWidth-roadWidth/2)/2-2,(screenHeight-roadWidth)/2,penCrossLines);
        canvas.drawLine((screenWidth/2+roadWidth/4)-2,0,(screenWidth/2+roadWidth/4)-2,(screenHeight-roadWidth)/2,penCrossLines);
        //bottom delimitator
        canvas.drawLine((screenWidth-roadWidth/2)/2-2,(screenHeight+roadWidth)/2,(screenWidth-roadWidth/2)/2-2,screenHeight,penCrossLines);
        canvas.drawLine((screenWidth/2+roadWidth/4)-2,(screenHeight+roadWidth)/2,(screenWidth/2+roadWidth/4)-2,screenHeight,penCrossLines);
        //right delimitators
        canvas.drawLine(0,screenHeight/2-roadWidth/4-2,(screenWidth-roadWidth)/2,screenHeight/2-roadWidth/4-2,penCrossLines);
        canvas.drawLine(0,screenHeight/2+roadWidth/4+2,(screenWidth-roadWidth)/2,screenHeight/2+roadWidth/4+2,penCrossLines);
        //left delimitators
        canvas.drawLine((screenWidth+roadWidth)/2,screenHeight/2-roadWidth/4-2,screenWidth,screenHeight/2-roadWidth/4-2,penCrossLines);
        canvas.drawLine((screenWidth+roadWidth)/2,screenHeight/2+roadWidth/4+2,screenWidth,screenHeight/2+roadWidth/4+2,penCrossLines);


        //bottom
        canvas.drawLine(screenWidth/2-5,(screenHeight+roadWidth)/2,screenWidth/2-5,screenHeight,penCrossLines);
        canvas.drawLine(screenWidth/2+5,(screenHeight+roadWidth)/2,screenWidth/2+5,screenHeight,penCrossLines);
        //left
        canvas.drawLine(0,screenHeight/2-5,(screenWidth-roadWidth)/2,screenHeight/2-5,penCrossLines);
        canvas.drawLine(0,screenHeight/2+5,(screenWidth-roadWidth)/2,screenHeight/2+5,penCrossLines);
        //right
        canvas.drawLine((screenWidth+roadWidth)/2,screenHeight/2-5,screenWidth,screenHeight/2-5,penCrossLines);
        canvas.drawLine((screenWidth+roadWidth)/2,screenHeight/2+5,screenWidth,screenHeight/2+5,penCrossLines);

        //Draw Pedestrians
        drawPedestrians(canvas);

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
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
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

        /*Car car=new Car(secondTop,secondTop,Constants.MOVE_DOWN,Constants.TURN_RIGHT,2);
        Bitmap img= BitmapFactory.decodeResource(r,R.drawable.green_car_down);
        img=getResizedBitmap(img,90,160);
        car.setImage(img);
        carsList.add(car);
        Car car2=new Car(firstTop,firstTop,Constants.MOVE_DOWN,Constants.TURN_RIGHT,1);
        Bitmap img2= BitmapFactory.decodeResource(r,R.drawable.green_car_down);
        img2=getResizedBitmap(img2,90,160);
        car2.setImage(img2);
        carsList.add(car2);
        Car car3=new Car(firstBottom,firstBottom,Constants.MOVE_TOP,Constants.TURN_RIGHT,1);
        Bitmap img3= BitmapFactory.decodeResource(r,R.drawable.green_car_up);
        img3=getResizedBitmap(img3,90,160);
        car3.setImage(img3);
        carsList.add(car3);
        Car car4=new Car(secondBottom,secondBottom,Constants.MOVE_TOP,Constants.TURN_RIGHT,2);
        Bitmap img4= BitmapFactory.decodeResource(r,R.drawable.green_car_up);
        img4=getResizedBitmap(img4,90,160);
        car4.setImage(img4);
        carsList.add(car4);
        Car car6=new Car(firstLeft,firstLeft,Constants.MOVE_RIGHT,Constants.TURN_RIGHT,1);
        Bitmap img6= BitmapFactory.decodeResource(r,R.drawable.green_car_right);
        img6=getResizedBitmap(img6,160,90);
        car6.setImage(img6);
        carsList.add(car6);
        Car car5=new Car(secondLeft,secondLeft,Constants.MOVE_RIGHT,Constants.TURN_RIGHT,2);
        Bitmap img5= BitmapFactory.decodeResource(r,R.drawable.green_car_right);
        img5=getResizedBitmap(img5,160,90);
        car5.setImage(img5);
        carsList.add(car5);
        Car car7=new Car(firstRight,firstRight,Constants.MOVE_LEFT,Constants.TURN_RIGHT,1);
        Bitmap img7= BitmapFactory.decodeResource(r,R.drawable.green_car_left);
        img7=getResizedBitmap(img7,160,90);
        car7.setImage(img7);
        carsList.add(car7);
        Car car8=new Car(secondRight,secondRight,Constants.MOVE_LEFT,Constants.TURN_RIGHT,2);
        Bitmap img8= BitmapFactory.decodeResource(r,R.drawable.green_car_left);
        img8=getResizedBitmap(img8,160,90);
        car8.setImage(img8);
        carsList.add(car8);*/

    }



    private Car generateCar(){
        Car car=null;
        while(true){
            int choice=new Random().nextInt(25);
            if(choice>0 && choice<=4){

                int randomCoordinates = new Random().nextInt(8);
                Coordinates coord=positions.get(randomCoordinates);
                Bitmap img;
                int randomTurn;

                switch(randomCoordinates){

                    case 0: //firstTop
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.police_car_down);
                        img=getResizedBitmap(img,90,160);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_DOWN,Constants.NO_TURN,1,Constants.POLICE_CAR,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_DOWN,Constants.TURN_RIGHT,1,Constants.POLICE_CAR,speed);
                        }
                        car.setImage(img);
                        break;

                    case 1: //secondTop
                        img= BitmapFactory.decodeResource(r,R.drawable.police_car_down);
                        img=getResizedBitmap(img,90,160);
                        car=new Car(coord,coord,Constants.MOVE_DOWN,Constants.NO_TURN,2,Constants.POLICE_CAR,speed);
                        car.setImage(img);
                        break;

                    case 2: //firstBottom
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.police_car_up);
                        img=getResizedBitmap(img,90,160);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_TOP,Constants.NO_TURN,1,Constants.POLICE_CAR,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_TOP,Constants.TURN_RIGHT,1,Constants.POLICE_CAR,speed);
                        }
                        car.setImage(img);
                        break;

                    case 3: //secondBottom
                        img= BitmapFactory.decodeResource(r,R.drawable.police_car_up);
                        img=getResizedBitmap(img,90,160);
                        car=new Car(coord,coord,Constants.MOVE_TOP,Constants.NO_TURN,2,Constants.POLICE_CAR,speed);
                        car.setImage(img);
                        break;

                    case 4: //firstLeft
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.police_car_right);
                        img=getResizedBitmap(img,160,90);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_RIGHT,Constants.NO_TURN,1,Constants.POLICE_CAR,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_RIGHT,Constants.TURN_RIGHT,1,Constants.POLICE_CAR,speed);
                        }
                        car.setImage(img);
                        break;

                    case 5: //secondLeft
                        img= BitmapFactory.decodeResource(r,R.drawable.police_car_right);
                        img=getResizedBitmap(img,160,90);
                        car=new Car(coord,coord,Constants.MOVE_RIGHT,Constants.NO_TURN,2,Constants.POLICE_CAR,speed);
                        car.setImage(img);
                        break;

                    case 6: //firstRight
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.police_car_left);
                        img=getResizedBitmap(img,160,90);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_LEFT,Constants.NO_TURN,1,Constants.POLICE_CAR,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_LEFT,Constants.TURN_RIGHT,1,Constants.POLICE_CAR,speed);
                        }
                        car.setImage(img);
                        break;

                    case 7: //secondRight
                        img= BitmapFactory.decodeResource(r,R.drawable.police_car_left);
                        img=getResizedBitmap(img,160,90);
                        car=new Car(coord,coord,Constants.MOVE_LEFT,Constants.NO_TURN,2,Constants.POLICE_CAR,speed);
                        car.setImage(img);
                        break;

                }



            }else if(choice>4 && choice<21){
                int randomCoordinates = new Random().nextInt(8);
                Coordinates coord=positions.get(randomCoordinates);
                Bitmap img;
                int randomTurn;

                switch(randomCoordinates){

                    case 0: //firstTop
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.green_car_down);
                        img=getResizedBitmap(img,90,160);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_DOWN,Constants.NO_TURN,1,Constants.SIMPLE_CAR,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_DOWN,Constants.TURN_RIGHT,1,Constants.SIMPLE_CAR,speed);
                        }
                        car.setImage(img);
                        break;

                    case 1: //secondTop
                        img= BitmapFactory.decodeResource(r,R.drawable.green_car_down);
                        img=getResizedBitmap(img,90,160);
                        car=new Car(coord,coord,Constants.MOVE_DOWN,Constants.NO_TURN,2,Constants.SIMPLE_CAR,speed);
                        car.setImage(img);
                        break;

                    case 2: //firstBottom
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.green_car_up);
                        img=getResizedBitmap(img,90,160);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_TOP,Constants.NO_TURN,1,Constants.SIMPLE_CAR,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_TOP,Constants.TURN_RIGHT,1,Constants.SIMPLE_CAR,speed);
                        }
                        car.setImage(img);
                        break;

                    case 3: //secondBottom
                        img= BitmapFactory.decodeResource(r,R.drawable.green_car_up);
                        img=getResizedBitmap(img,90,160);
                        car=new Car(coord,coord,Constants.MOVE_TOP,Constants.NO_TURN,2,Constants.SIMPLE_CAR,speed);
                        car.setImage(img);
                        break;

                    case 4: //firstLeft
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.green_car_right);
                        img=getResizedBitmap(img,160,90);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_RIGHT,Constants.NO_TURN,1,Constants.SIMPLE_CAR,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_RIGHT,Constants.TURN_RIGHT,1,Constants.SIMPLE_CAR,speed);
                        }
                        car.setImage(img);
                        break;

                    case 5: //secondLeft
                        img= BitmapFactory.decodeResource(r,R.drawable.green_car_right);
                        img=getResizedBitmap(img,160,90);
                        car=new Car(coord,coord,Constants.MOVE_RIGHT,Constants.NO_TURN,2,Constants.SIMPLE_CAR,speed);
                        car.setImage(img);
                        break;

                    case 6: //firstRight
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.green_car_left);
                        img=getResizedBitmap(img,160,90);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_LEFT,Constants.NO_TURN,1,Constants.SIMPLE_CAR,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_LEFT,Constants.TURN_RIGHT,1,Constants.SIMPLE_CAR,speed);
                        }
                        car.setImage(img);
                        break;

                    case 7: //secondRight
                        img= BitmapFactory.decodeResource(r,R.drawable.green_car_left);
                        img=getResizedBitmap(img,160,90);
                        car=new Car(coord,coord,Constants.MOVE_LEFT,Constants.NO_TURN,2,Constants.SIMPLE_CAR,speed);
                        car.setImage(img);
                        break;

                }
            } else if(choice>=21 && choice<=24){
                int randomCoordinates = new Random().nextInt(8);
                Coordinates coord=positions.get(randomCoordinates);
                Bitmap img;
                int randomTurn;

                switch(randomCoordinates){

                    case 0: //firstTop
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.ambulance_down);
                        img=getResizedBitmap(img,90,160);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_DOWN,Constants.NO_TURN,1,Constants.AMBULANCE,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_DOWN,Constants.TURN_RIGHT,1,Constants.AMBULANCE,speed);
                        }
                        car.setImage(img);
                        break;

                    case 1: //secondTop
                        img= BitmapFactory.decodeResource(r,R.drawable.ambulance_down);
                        img=getResizedBitmap(img,90,160);
                        car=new Car(coord,coord,Constants.MOVE_DOWN,Constants.NO_TURN,2,Constants.AMBULANCE,speed);
                        car.setImage(img);
                        break;

                    case 2: //firstBottom
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.ambulance_up);
                        img=getResizedBitmap(img,90,160);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_TOP,Constants.NO_TURN,1,Constants.AMBULANCE,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_TOP,Constants.TURN_RIGHT,1,Constants.AMBULANCE,speed);
                        }
                        car.setImage(img);
                        break;

                    case 3: //secondBottom
                        img= BitmapFactory.decodeResource(r,R.drawable.ambulance_up);
                        img=getResizedBitmap(img,90,160);
                        car=new Car(coord,coord,Constants.MOVE_TOP,Constants.NO_TURN,2,Constants.AMBULANCE,speed);
                        car.setImage(img);
                        break;

                    case 4: //firstLeft
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.ambulance_right);
                        img=getResizedBitmap(img,160,90);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_RIGHT,Constants.NO_TURN,1,Constants.AMBULANCE,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_RIGHT,Constants.TURN_RIGHT,1,Constants.AMBULANCE,speed);
                        }
                        car.setImage(img);
                        break;

                    case 5: //secondLeft
                        img= BitmapFactory.decodeResource(r,R.drawable.ambulance_right);
                        img=getResizedBitmap(img,160,90);
                        car=new Car(coord,coord,Constants.MOVE_RIGHT,Constants.NO_TURN,2,Constants.AMBULANCE,speed);
                        car.setImage(img);
                        break;

                    case 6: //firstRight
                        randomTurn=new Random().nextInt(2);
                        img= BitmapFactory.decodeResource(r,R.drawable.ambulance_left);
                        img=getResizedBitmap(img,160,90);
                        if(randomTurn==0){
                            car=new Car(coord,coord,Constants.MOVE_LEFT,Constants.NO_TURN,1,Constants.AMBULANCE,speed);
                        }else {
                            car=new Car(coord,coord,Constants.MOVE_LEFT,Constants.TURN_RIGHT,1,Constants.AMBULANCE,speed);
                        }
                        car.setImage(img);
                        break;

                    case 7: //secondRight
                        img= BitmapFactory.decodeResource(r,R.drawable.ambulance_left);
                        img=getResizedBitmap(img,160,90);
                        car=new Car(coord,coord,Constants.MOVE_LEFT,Constants.NO_TURN,2,Constants.AMBULANCE,speed);
                        car.setImage(img);
                        break;

                }
            }
            if(car!=null){
                break;
            }
        }


        return car;
    }

    public void initPedstrians(){
        pieton1=getResizedBitmap(pieton1,50,50);
        pieton2=getResizedBitmap(pieton2,50,50);
        Pedestrian pedestrian1=new Pedestrian(pieton1);
        pedestrian1.setWalking(false);
        pedestrian1.setCoordinateX((screenWidth+roadWidth)/2);
        pedestrian1.setCoordinateY((screenHeight+roadWidth)/2+pedestrianHeight);
        pedestrian1.setDirectionLeftRight(true);

        pedestrian1.setRoadToCrossCoordsMax((screenWidth+roadWidth)/2+pedestrianHeight);
        pedestrian1.setRoadToCrossCoordsMin((screenWidth-roadWidth)/2-(pedestrianHeight*3));
        pedestriansList.add(pedestrian1);

        Pedestrian pedestrian2=new Pedestrian(pieton2);
        pedestrian2.setWalking(true);
        pedestrian2.setCoordinateX((screenWidth-roadWidth)/2-TrafficLight.TRAFFIC_LIGHT_HEIGHT-pedestrianHeight);
        pedestrian2.setCoordinateY((screenHeight-roadWidth)/2-TrafficLight.TRAFFIC_LIGHT_WIDTH-pedestrianHeight);
        pedestrian2.setDirectionLeftRight(false);
        pedestrian2.setRoadToCrossCoordsMin((screenHeight-roadWidth)/2-TrafficLight.TRAFFIC_LIGHT_WIDTH-pedestrianHeight);
        pedestrian2.setRoadToCrossCoordsMax((screenHeight+roadWidth)/2+pedestrianHeight);
        pedestriansList.add(pedestrian2);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_VOLUME_UP:
                intervalRedGreenColor+=intervalChangeRate;
                intervalYellowColor+=intervalChangeRate;
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                intervalRedGreenColor+=intervalChangeRate;
                intervalYellowColor-=intervalChangeRate;
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
