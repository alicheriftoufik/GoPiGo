package com.example.toufik.gopigo_master2_ise;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toufik on 06/01/2017.
 */

public class GraphView extends SurfaceView {

    private final int height = 2;
    private final int pixel_beginning = 500;

    private SurfaceHolder holder;
    private Paint paint = new Paint();
    private List<String> data = new ArrayList<>();

    private Handler handler = new Handler();

    public GraphView(Context context) {
        super(context);
        this.setBackgroundColor(Color.WHITE);

        autoRefresh();

        getBddData();

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    draw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

        });
    }

    public void draw(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.moveTo(0, pixel_beginning);
        Log.e(getClass().getSimpleName(),"x=0 y="+pixel_beginning);
        for (int i=0; i<data.size(); i++){
            int x=i*10+10;
            int y=(pixel_beginning-Integer.parseInt(data.get(i))*height);
            path.lineTo(x,y);
            Log.e(getClass().getSimpleName(),"x="+x+" y="+y+" data="+Integer.parseInt(data.get(i)));
        }
        canvas.drawPath(path, paint);
    }

    private void getBddData(){
        try {
            MeasureBDD measureBDD = new MeasureBDD(getContext());
            measureBDD.open();

            Cursor c = measureBDD.getBDD().rawQuery("SELECT * FROM sensor", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        data.add(c.getString(c.getColumnIndex("measure")));
                        Log.e(getClass().getSimpleName(),c.getString(c.getColumnIndex("measure")));
                    } while (c.moveToNext());
                }
                c.close();
            }
            measureBDD.close();
        }catch (Exception se){
            Log.e(getClass().getSimpleName(), "Could not create or Open the database ("+se.toString()+")");
        }
    }

    public void autoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(getClass().getSimpleName(),"autoRefresh");
                new SynchroBDD(getContext());
                getBddData();
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.WHITE);
                    draw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
                autoRefresh();
            }
        }, 5000);
    }
}