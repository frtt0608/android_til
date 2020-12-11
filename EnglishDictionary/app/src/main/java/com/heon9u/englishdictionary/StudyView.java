package com.heon9u.englishdictionary;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class StudyView extends SurfaceView implements Callback {
    static int soundOk = 1;
    int questionNumber = 0;
    int numberOfquestion = 99;

    int textSizeForG4 = 0;
    int textSizeChanging = 0;
    int textSizeChanging2 = 0;

    int answerButton = 0;
    int answerUser = 0;

    int starIng = 0;
    int starIndex = 0;
    int starX, starY;

    int oNumber = 0;
    int xNumber = 0;

    int whatStudy = 0;

    int submenuOk = 0;
    int submenuOk2 = 0;

    int wordSave = 0;

    double rand;
    int btnPressed = 0;

    String[] wordForDelete = {"", "", "", "", ""};
    String wordToDelete = "";

    static StudyThread mThread;
    SurfaceHolder mHolder;
    static Context mContext;
    FileTable mFile;

    MyDBHelper m_helper;

    Cursor cursor;
    int dicOk = 0;
    int movePosition = 0;

    MyButton1 btnNext;
    MyButton1 btnPrevious;
    MyButton1 btnWordSelection;
    MyButton1 btnMyNote;
    MyButton1 btnExit;
    MyButton1 btnRandom;
    MyButton1 btnNum1;
    MyButton1 btnNum2;
    MyButton1 btnNum3;
    MyButton1 btnNum4;
    MyButton1 btnNum5;
    MyButton1 btnPreviousQuestion;
    MyButton1 btnSolveAgain;

    String whichSubject = "선택단어 1";

    MyButton1 btnSub1;
    MyButton1 btnSub2;
    MyButton1 btnSub3;
    MyButton1 btnSub4;
    MyButton1 btnSub5;
    MyButton1 btnSub6;
    MyButton1 btnSub7;
    MyButton1 btnSub8;

    MyButton1 btnWordSave;
    MyButton1 btnKakaoQSending;

    MyButton1 btnLeftArrow;
    MyButton1 btnRightArrow;
    MyButton1 btnClose;

    MyButton1[] btnForDictionary;

    MyButton1 btnAllDelete;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    int btnPreCount = 0;
    int btnNextCount = 0;
    int btnSelectCount = 0;
    int btnMyNoteCount = 0;
    int btnRanCount = 0;

    int btnNum1Count = 0;
    int btnNum2Count = 0;
    int btnNum3Count = 0;
    int btnNum4Count = 0;

    static int Width, Height;

    int subNumber = 1;

    Bitmap answerx;
    Bitmap answero;
    Bitmap cap;
    Bitmap explain;

    Bitmap[] star = new Bitmap[4];
    static SoundPool sdPool;
    static int dingdongdaeng, taeng;

    public StudyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        mHolder = holder;
        mContext = context;
        mThread = new StudyThread(holder, context);

        initAll();
        makeQuestion(subNumber);

        setFocusable(true);
    }

    private void initAll() {
        m_helper = new MyDBHelper(mContext, "testforeng.db", null, 1);
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Width = display.getWidth();
        Height = display.getHeight();
        textSizeChanging = (int) (Width * 64 / 1280);
        if (Width > 1700) textSizeForG4 = 120;

        mFile = new FileTable();

        btnPrevious = new MyButton1(30, 34, 0);  //previous
        btnNext = new MyButton1(btnPrevious.x + btnPrevious.w * 2, 34, 1);  //next
        btnWordSelection = new MyButton1(btnNext.x + btnPrevious.w * 2, 34, 2);  //단어선택

        btnMyNote = new MyButton1(btnWordSelection.x + btnPrevious.w * 2, 34, 4);  //내노트
        btnExit = new MyButton1(Width - btnPrevious.w * 2 - btnPrevious.w / 3, 34, 5);  //exit
        btnRandom = new MyButton1(btnMyNote.x + btnPrevious.w * 2, 34, 6); // random button

        btnNum1 = new MyButton1(btnPrevious.x + 70 + 50, btnPrevious.y + btnPrevious.h * 2 + 93, 7); // number 1
        btnNum2 = new MyButton1(btnPrevious.x + 70 + 50, btnNum1.y + btnNum1.h * 2 + 8, 8); // number 2
        btnNum3 = new MyButton1(btnPrevious.x + 70 + 50, btnNum2.y + btnNum2.h * 2 + 8, 9); // number 3
        btnNum4 = new MyButton1(btnPrevious.x + 70 + 50, btnNum3.y + btnNum3.h * 2 + 8, 10); // number 4
        btnNum5 = new MyButton1(btnPrevious.x + 70 + 50, btnNum4.y + btnNum4.h * 2 + 8, 11); // number 5  여기서는 사용안함

        btnPreviousQuestion = new MyButton1(Width - btnWordSelection.w * 6, btnNum1.y + btnNum1.h * 2 + 1, 12); //다음문제
        btnSolveAgain = new MyButton1(Width - btnWordSelection.w * 6, btnPreviousQuestion.y + btnPreviousQuestion.h * 2 + 1, 13); //다시풀기

        btnWordSave = new MyButton1(Width - btnWordSelection.w * 6, btnSolveAgain.y + btnSolveAgain.h * 2 + 1, 23); //단어장등록

        // 카카오 문제보내기 버튼
        btnKakaoQSending = new MyButton1(Width - btnWordSelection.w * 11, btnNum1.y + btnNum1.h * 2 + 1, 33);

        // sub menu 단어 선택 메뉴
        btnSub1 = new MyButton1(btnNext.x + 10, btnWordSelection.y + btnWordSelection.h * 2 + 5, 15);
        btnSub2 = new MyButton1(btnSub1.x + btnSub1.w * 2, btnSub1.y, 16);
        btnSub3 = new MyButton1(btnSub2.x + btnSub2.w * 2, btnSub1.y, 17);
        btnSub4 = new MyButton1(btnSub3.x + btnSub3.w * 2, btnSub1.y, 18);
        btnSub5 = new MyButton1(btnSub4.x + btnSub4.w * 2, btnSub1.y, 19);
        btnSub6 = new MyButton1(btnNext.x + 10, btnSub1.y + btnSub1.h * 2, 20);
        btnSub7 = new MyButton1(btnSub1.x + btnSub1.w * 2, btnSub1.y + btnSub1.h * 2, 21);
        btnSub8 = new MyButton1(btnSub2.x + btnSub2.w * 2, btnSub1.y + btnSub1.h * 2, 22);

        // 내사전에서 왼쪽, 오른쪽 버튼
        btnLeftArrow = new MyButton1(btnNext.x, Height - btnPrevious.h * 2, 26);    // left arrow
        btnRightArrow = new MyButton1(btnNext.x + 150, Height - btnPrevious.h * 2, 27);  //right arrow

        // 닫기 버튼
        btnClose = new MyButton1(Width - btnPrevious.w * 2, Height - btnPrevious.h * 2, 28); //close button in dic
        btnForDictionary = new MyButton1[5];


        // 삭제버튼
        for (int i = 0; i < 5; i++)
            btnForDictionary[i] = new MyButton1(Width - btnNum1.w*4, btnNum1.h*4 + i * btnNum1.h*2 + btnNum1.h/12 * i, 29);

        answerx = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.answerx);

        int xxx = Width / 6;
        answerx = Bitmap.createScaledBitmap(answerx, xxx, xxx, true);
        answero = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.answero);
        answero = Bitmap.createScaledBitmap(answero, xxx, xxx, true);
        explain = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.explain);

        explain = Bitmap.createScaledBitmap(explain, Width / 11, Height / 7, true);

        cap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cap);
        cap = Bitmap.createScaledBitmap(cap, Width / 12, Height / 14, true);

        for (int i = 0; i < 4; i++) {
            star[i] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.circlewhite);
            star[i] = Bitmap.createScaledBitmap(star[i], btnNum1.w * 2 + i * 2, btnNum1.w * 2 + i * 2, true);
        }

        sdPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        dingdongdaeng = sdPool.load(mContext, R.raw.dingdongdaeng, 1);
        taeng = sdPool.load(mContext, R.raw.taeng, 2);
    }

    public void makeQuestion(int x) {
        mFile.loadFile(x);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread.setRunning(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {

    }

    class StudyThread extends Thread {
        boolean canRun = true;
        boolean isWait = false;
        Paint paint = new Paint();
        Paint paint2 = new Paint();
        Paint paint3 = new Paint();

        public StudyThread(SurfaceHolder holder, Context context) {
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.create("", Typeface.BOLD));

            paint2.setColor(Color.WHITE);
            paint2.setAntiAlias(true);
            paint2.setTypeface(Typeface.create("", Typeface.BOLD));

            paint3.setColor(Color.WHITE);
            paint3.setAntiAlias(true);
            paint3.setTypeface(Typeface.create("", Typeface.BOLD));

            paint.setTextSize(TypedValue.COMPLEX_UNIT_DIP);
            paint2.setTextSize(35);
            paint3.setTextSize(40);
        }

        public void setRunning(boolean b) {

        }
    }


    class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context, String name, SQLiteDatabase
                .CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE englishWordTable (_id INTEGER PRIMARY KEY AUTOINCREMENT," + "eWord TEXT, kWord TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS person");
            onCreate(db);
        }
    }
}
