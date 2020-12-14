package com.heon9u.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    MyAdapter mAdapter;

    String eWord[] = {"sunny", "cloudy", "rainny", "snowy", "windy",
            "giraffe", "elephant", "turtle", "rabbit", "zebra",
            "baseball", "soccer", "basketball", "volleyball", "badminton",
            "KOREA", "JAPAN", "CHINA", "USA", "THAILAND"};

    String kWord[] = {"햇볕이 잘 드는", "구름이 있는", "비오는", "눈이 내리는",
            "바람이 센", "기린", "코끼리", "거북이", "토끼",
            "얼룩말", "야구", "축구", "농구", "배구", "배드민턴",
            "대한민국", "일본", "중국", "미국", "태국"};

    public GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview = (RecyclerView) findViewById(R.id.view);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerview.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter();
        recyclerview.setAdapter(mAdapter);

        makeData();

        gestureDetector = new GestureDetector(getApplicationContext(),
                                        new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerview.addOnItemTouchListener(onItemTouchListener);
    }

    public void makeData() {
        for(int i=0; i<20; i++) {
            OriginalData item = new OriginalData();
            item.image = ContextCompat.getDrawable(this, R.drawable.pic00 + i);
            item.text = eWord[i];
            mAdapter.add(item);
        }
    }

    RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if(childView != null && gestureDetector.onTouchEvent(e)) {
                int currentPos = rv.getChildAdapterPosition(childView);
                Toast.makeText(getApplicationContext(), kWord[currentPos], Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };
}