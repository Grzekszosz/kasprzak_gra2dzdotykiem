package com.darkspacelab.gra2dzdotykiem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {


    private GameThread gameThread;

    private PostacChibi chibi1,chibi2;
    private Wybuch piwpaw;
    private  boolean wybuchlo = false;

    public GameSurface(Context context)  {
        super(context);

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
    }

    public void update()  {
        this.chibi1.aktualizuj();
        this.chibi2.aktualizuj();
        if(wybuchlo){
            this.piwpaw.update();
        }

    }


    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);

        this.chibi1.rysuj(canvas);
        this.chibi2.rysuj(canvas);

        if(wybuchlo){
            this.piwpaw.draw(canvas);
        }

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        Bitmap chibiBitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        Bitmap piwpawBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.bom);
        this.chibi1 = new PostacChibi(this,chibiBitmap1,100,50);
        this.chibi1.PREDKOSC=0.4f;
        this.chibi2 = new PostacChibi(this,chibiBitmap1,200,500);
        this.chibi2.PREDKOSC=0.1f;
        piwpaw = new Wybuch(this,piwpawBitmap,200,500);
        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionevent) {
        int x = (int) motionevent.getX();
        int y = (int) motionevent.getY();
        x=x-chibi1.getX();
        y=y-chibi1.getY();
        chibi1.setmPoruszajacyWektor(x,y);
        x = (int) motionevent.getX();
        y = (int) motionevent.getY();
        x=x-chibi2.getX();
        y=y-chibi2.getY();
        chibi2.setmPoruszajacyWektor(x,y);

        if (motionevent.getAction() == MotionEvent.ACTION_DOWN) {
            wybuchlo = true;
            piwpaw.setFinish(false);
            piwpaw.setmX((int)motionevent.getX());
            piwpaw.setmY((int)motionevent.getY());

            return true;
        }
        System.out.println("x:" + x + " y:" + y);
        return false;
    }
}