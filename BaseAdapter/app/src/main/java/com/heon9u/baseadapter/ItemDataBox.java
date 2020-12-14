package com.heon9u.baseadapter;

import android.graphics.drawable.Drawable;

public class ItemDataBox {
    Drawable myImage;
    String[] myData;

    public ItemDataBox(Drawable myImage, String text1, String text2, String text3) {
        this.myImage = myImage;
        myData = new String[3];
        myData[0] = text1;
        myData[1] = text2;
        myData[2] = text3;
    }

    public String[] getData() {
        return myData;
    }

    public String getData(int index) {
        if(myData == null || index >= myData.length) {
            return null;
        }

        return myData[index];
    }

    public Drawable getImage() {
        return myImage;
    }
}
