package com.darkspacelab.gra2dzdotykiem;


import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by samsung on 2017-06-01.
 */


public class Wybuch extends ObiektGry {
    private int rowIndex = 0 ;
    private int colIndex = -1 ;

    private boolean finish= false;
    private GameSurface gameSurface;

    public Wybuch(GameSurface GameSurface, Bitmap image, int x, int y) {
        super(image, 5, 5, x, y);

        this.gameSurface= GameSurface;
    }

    public void update()  {
        this.colIndex++;

        if(this.colIndex >= this.mLiczbaWierszy)  {
            this.colIndex =0;
            this.rowIndex++;

            if(this.rowIndex>= this.mLiczbaKolumn)  {
                this.finish= true;
                this.colIndex = -1;
                this.rowIndex = 0;
            }
        }
    }

    public void draw(Canvas canvas)  {
        if(!finish)  {
            Bitmap bitmap= this.oddzielSkorke(rowIndex,colIndex);
            canvas.drawBitmap(bitmap, this.mX, this.mY,null);
        }
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
