
**Implemented Tasks Description:**

1. Basic Android application has been created, having one Main Activity. Which extends AppCompatActivity class. Extending this class provides the posibility to implement/use some very important methods as (OnCreate, findViewById...)

2. In the middle of the screen was set the following text: "Done with Pride and Prejudice by Corina Tilea", as a TextView, which was added to MainActivityLayout.

3. On screen "resize"(actually on screen rotation),the text remains in the middle (vertically and horizontally). For this was used setGravity(Gravity.CENTER) method.

4.  2 Buttons had been added to the activity. One with default and other with custom style.
Buttons where created using Button class, from android.widget.Button package. 
For customization of the button were used methods like: 
setText, setBackgroundColor, setTextColor, setPadding, set Typeface. In order to set button width in pixels, was necessary to convert display units to pixels has been used:  
```java
int customBtnWidthPx=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, resourcesRef.getDisplayMetrics());
```

5.  2 TextView elements had been added to the activity (one with default styles, one with custom style).

6.  2 elements had been made to interact:
 
6.1. On button(Custom Button) click - the Custom Text size, color and value is changed.)
In order to hadle the button click event, was used buttonCustom.setOnClickListener(in this method is implemented OnClickListener from Button class, with custom activity).
[Example]
```java
	buttonCustom.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View viewId){
                        textViewCustom.setTextColor(Color.RED);
                        textViewCustom.setText("Good Job, The Text Has Been Changed!");
                    }

                }
        );
```


6.2. On Long click Event has been implemented over default text element, by setOnLongClickListener() method.

In order to arrange the items on the activity, RelativeLayout was used. 
 mainActivityLayout.addView metdhos recieves as parameters the widget to be added and the Layout according to which to arrange it on the activity.



