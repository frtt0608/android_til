package com.heon9u.englishdictionary;

import java.io.IOException;
import java.io.InputStream;

public class FileTable {
    InputStream fi;

    private FileSplit0 word;
    private FileSplit1 word2;

    public void loadFile(int num) {
        fi = StudyView.mContext.getResources().openRawResource(R.raw.high01 + num);

        try {
            byte[] data = new byte[fi.available()];
            fi.read(data);
            fi.close();
            String s = new String(data, "UTF-8");
            word = new FileSplit0(s);
        } catch(IOException e) {

        }
    }

    public void loadFile2(int num) {
        fi = StudyView2.mContext.getResources().openRawResource(R.raw.test00 + num);

        try {
            byte[] data = new byte[fi.available()];
            fi.read(data);
            fi.close();
            String s = new String(data, "UTF-8");
            word2 = new FileSplit1(s);
        } catch(IOException e) {

        }
    }
}
