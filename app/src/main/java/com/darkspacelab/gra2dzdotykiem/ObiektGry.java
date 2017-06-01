package com.darkspacelab.gra2dzdotykiem;



import android.graphics.Bitmap;

public abstract class ObiektGry {

    protected Bitmap mObraz;

    protected final int mLiczbaWierszy;
    protected final int mLiczbaKolumn;

    protected final int SZEROKOSC_GRAFIKI;
    protected final int WYSOKOSC_GRAFIKI;

    protected final int mSzerokosc;
    protected final int mWysokosc;

    protected int mX;
    protected int mY;

    public ObiektGry(Bitmap obraz, int liczbaWierszy, int liczbaKolumn, int x, int y)  {

        this.mObraz = obraz;
        this.mLiczbaWierszy = liczbaWierszy;
        this.mLiczbaKolumn = liczbaKolumn;

        this.mX = x;
        this.mY = y;

        this.SZEROKOSC_GRAFIKI = obraz.getWidth();
        this.WYSOKOSC_GRAFIKI = obraz.getHeight();

        this.mSzerokosc = this.SZEROKOSC_GRAFIKI / liczbaKolumn;
        this.mWysokosc = this.WYSOKOSC_GRAFIKI / liczbaWierszy;
    }


    protected Bitmap oddzielSkorke(int wiersz, int kolumna)  {
        // createBitmap(bitmap, mX, mY, mSzerokosc, mWysokosc).
        Bitmap skorka = Bitmap.createBitmap(mObraz, kolumna * mSzerokosc, wiersz * mWysokosc, mSzerokosc, mWysokosc);
        return skorka;
    }

    public int getX()  {
        return this.mX;
    }

    public int getY()  {
        return this.mY;
    }

    public void setmX(int mX) {
        this.mX = mX;
    }

    public void setmY(int mY) {
        this.mY = mY;
    }

    public int getWysokosc() {
        return mWysokosc;
    }

    public int getSzerokosc() {
        return mSzerokosc;
    }

}