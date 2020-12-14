package com.heon9u.baseadapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemView extends LinearLayout {

    ImageView myImage;
    TextView myText1;
    TextView myText2;
    TextView myText3;

    public ItemView(Context context, ItemDataBox aItem) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listlayout, this, true);

        myImage = (ImageView) findViewById(R.id.imageItem);
        myImage.setImageDrawable(aItem.getImage());

        myText1 = (TextView) findViewById(R.id.dataItem01);
        myText1.setText(aItem.getData(0));

        myText2 = (TextView) findViewById(R.id.dataItem02);
        myText2.setText(aItem.getData(1));

        myText3 = (TextView) findViewById(R.id.dataItem03);
        myText3.setText(aItem.getData(2));
    }

    public void setText(int index, String data) {
        if(index == 0) {
            myText1.setText(data);
        } else if(index == 1) {
            myText2.setText(data);
        } else if(index == 2) {
            myText3.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setIcon(Drawable icon) {
        myImage.setImageDrawable(icon);
    }
}
