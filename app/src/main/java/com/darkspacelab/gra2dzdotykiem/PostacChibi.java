package com.darkspacelab.gra2dzdotykiem;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class PostacChibi extends ObiektGry {

    private static final int WIERSZ_GORA_DO_DOL = 0;
    private static final int WIERSZ_PRAWO_DO_LEWO = 1;
    private static final int WIERSZ_LEWO_DO_PRAWO = 2;
    private static final int WIERSZ_DOL_DO_GORA = 3;

    // Row index of Image are being used.
    private int mUzytyWiersz = WIERSZ_LEWO_DO_PRAWO;

    private int uzytaKolumna;

    private Bitmap[] mSkorkaLewoDoPrawo;
    private Bitmap[] mSkorkaPrawoDoLewo;
    private Bitmap[] mSkorkaGoraDoDolu;
    private Bitmap[] mSkorkaDolDoGory;

    // Velocity of game character (pixel/millisecond)
    public float PREDKOSC = 0.1f;

    private int mPoruszajacyWektorX = 10;
    private int mPoruszajacyWektorY = 5;

    private long mOstatniCzasRysowania =-1;

    private GameSurface gameSurface;

    public PostacChibi(GameSurface gameSurface, Bitmap image, int x, int y) {
        super(image, 4, 3, x, y);

        this.gameSurface= gameSurface;

        this.mSkorkaGoraDoDolu = new Bitmap[mLiczbaKolumn]; // 3
        this.mSkorkaPrawoDoLewo = new Bitmap[mLiczbaKolumn]; // 3
        this.mSkorkaLewoDoPrawo = new Bitmap[mLiczbaKolumn]; // 3
        this.mSkorkaDolDoGory = new Bitmap[mLiczbaKolumn]; // 3

        for(int col = 0; col < this.mLiczbaKolumn; ++col) {
            for (int row = 0; row < 4; ++row) {
                Bitmap skorka = this.oddzielSkorke(row, col);
                if (row == WIERSZ_GORA_DO_DOL) this.mSkorkaGoraDoDolu[col] = skorka;
                if (row == WIERSZ_PRAWO_DO_LEWO) this.mSkorkaPrawoDoLewo[col] = skorka;
                if (row == WIERSZ_LEWO_DO_PRAWO) this.mSkorkaLewoDoPrawo[col] = skorka;
                if (row == WIERSZ_DOL_DO_GORA) this.mSkorkaDolDoGory[col] = skorka;
            }
        }
    }

    public Bitmap[] getSkorkeRuchu()  {
        switch (mUzytyWiersz)  {
            case WIERSZ_DOL_DO_GORA:
                return  this.mSkorkaDolDoGory;
            case WIERSZ_LEWO_DO_PRAWO:
                return this.mSkorkaLewoDoPrawo;
            case WIERSZ_PRAWO_DO_LEWO:
                return this.mSkorkaPrawoDoLewo;
            case WIERSZ_GORA_DO_DOL:
                return this.mSkorkaGoraDoDolu;
            default:
                return null;
        }
    }

    public Bitmap getAktualnaSkorka()  {
        Bitmap[] bitmapy = this.getSkorkeRuchu();
        return bitmapy[this.uzytaKolumna];
    }

    public void aktualizuj()  {
        this.uzytaKolumna++;
        if(uzytaKolumna >= this.mLiczbaKolumn)  {
            this.uzytaKolumna = 0;
        }
        // Aktualny czas w nanosekundach
        long teraz = System.nanoTime();

        // Nigdy nie odrysowany.
        if(mOstatniCzasRysowania == -1) {
            mOstatniCzasRysowania = teraz;
        }
        // Konwersja nanosekund do milisekund
        int roznicaCzasu = (int) ((teraz - mOstatniCzasRysowania) / 1000000 );

        float droga = PREDKOSC * roznicaCzasu;

        double dlugoscWektoraRuchu = Math.sqrt(mPoruszajacyWektorX * mPoruszajacyWektorX + mPoruszajacyWektorY * mPoruszajacyWektorY);

        // Oblicz nową pozycję postacji
        mX += (int)(droga * mPoruszajacyWektorX / dlugoscWektoraRuchu);
        mY += (int)(droga * mPoruszajacyWektorY / dlugoscWektoraRuchu);

        // Odbicia przy brzegach ekranu

        if (mX < 0 )  {
            mX = 0;
            mPoruszajacyWektorX *= -1;
        } else if (mX > gameSurface.getWidth() - mSzerokosc)  {
            mX = this.gameSurface.getWidth() - mSzerokosc;
            mPoruszajacyWektorX *= -1;
        }

        if (mY < 0 )  {
            mY = 0;
            mPoruszajacyWektorY *= -1;
        } else if (mY > gameSurface.getHeight() - mWysokosc)  {
            mY = this.gameSurface.getHeight()- mWysokosc;
            mPoruszajacyWektorY *= -1;
        }

        // mUzytyWiersz
        if ( mPoruszajacyWektorX > 0 )  {
            if(mPoruszajacyWektorY > 0 && Math.abs(mPoruszajacyWektorX) < Math.abs(mPoruszajacyWektorY)) {
                mUzytyWiersz = WIERSZ_GORA_DO_DOL;
            } else if (mPoruszajacyWektorY < 0 && Math.abs(mPoruszajacyWektorX) < Math.abs(mPoruszajacyWektorY)) {
                mUzytyWiersz = WIERSZ_DOL_DO_GORA;
            } else  {
                mUzytyWiersz = WIERSZ_LEWO_DO_PRAWO;
            }
        } else {
            if (mPoruszajacyWektorY > 0 && Math.abs(mPoruszajacyWektorX) < Math.abs(mPoruszajacyWektorY)) {
                mUzytyWiersz = WIERSZ_GORA_DO_DOL;
            } else if (mPoruszajacyWektorY < 0 && Math.abs(mPoruszajacyWektorX) < Math.abs(mPoruszajacyWektorY)) {
                mUzytyWiersz = WIERSZ_DOL_DO_GORA;
            } else  {
                mUzytyWiersz = WIERSZ_PRAWO_DO_LEWO;
            }
        }
    }

    public void rysuj(Canvas canvas)  {
        Bitmap bitmap = this.getAktualnaSkorka();
        canvas.drawBitmap(bitmap, mX, mY, null);
        // Last rysuj time.
        mOstatniCzasRysowania = System.nanoTime();
    }

    public void setmPoruszajacyWektor(int poruszajacyWektorX, int poruszajacyWektorY)  {
        mPoruszajacyWektorX = poruszajacyWektorX;
        mPoruszajacyWektorY = poruszajacyWektorY;
    }
}