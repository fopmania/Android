package com.example.macstudent.junshapetest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class junShpeActivity extends AppCompatActivity {
    class RenderView extends View {
        Paint paint;

        RenderView(Context context){
            super(context);
            paint = new Paint();
        }

        void drawHead(int x, int y, int s, Canvas canvas){
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(x, y, s, paint);      //  outline
            canvas.drawLine(x, y-(s/3), x, y+(s/3), paint); //  nose
            canvas.drawLine(x-(s/2), y+(s/2), x+(s/2), y+(s/2), paint); //  mouth
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x-(s/3), y-(s/3), s/7, paint);  //  left eye
            canvas.drawCircle(x+(s/3), y-(s/3), s/7, paint);  //  right eye
        }
        void drawBody(int x, int y, int s, Canvas canvas){
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x-(s/3), y, x+(s/3) ,y+s ,paint);
            canvas.drawLine(x-(s/3), y, x-(s/2), y + (s/1.5f), paint); //  Left arm
            canvas.drawLine(x+(s/3), y, x+(s/2), y + (s/1.5f), paint); //  Right arm
            canvas.drawLine(x-(s/3), y+s, x-(s/3), y+s+(s/1.5f), paint);       //  Left leg
            canvas.drawLine(x+(s/3), y+s, x+(s/3), y+s+(s/1.5f), paint);       //  Right Leg
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(255, 255, 255);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);

            int h_posx = canvas.getWidth()/2;
            int h_posy = canvas.getHeight()/4;
            int h_size = 150;
            int b_posx = h_posx;
            int b_posy = h_posy+h_size;
            int b_size = 500;


            drawHead(h_posx, h_posy, h_size, canvas);
            drawBody(b_posx, b_posy, b_size, canvas);

            invalidate();

        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new RenderView(this));
    }
}
