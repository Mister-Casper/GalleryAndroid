package com.journaldev.mvpdagger2.activity.FileInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import com.journaldev.mvpdagger2.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FIleInfo extends Activity {

    @BindView(R.id.path)
    TextView path;
    @BindView(R.id.chandge)
    TextView chandge;
    @BindView(R.id.resolution)
    TextView resolution;
    @BindView(R.id.size)
    TextView size;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_info);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            Uri uriFile = getUri();
            viewFileInfo(uriFile);
        }
    }

    @SuppressLint("SetTextI18n")
    private void viewFileInfo(Uri file) {
        File FileByUri = new File(String.valueOf(file));
        Bitmap bmp = BitmapFactory.decodeFile(FileByUri.getPath());
        if (!FileByUri.exists()) return;
        path.setText(file.getPath());
        Calendar date = new GregorianCalendar();
        date.setTime(new Date(FileByUri.lastModified()));
        chandge.setText(date.get(Calendar.HOUR) + " : " + date.get(Calendar.MINUTE) + " , " + date.get(Calendar.DAY_OF_MONTH)  + "." + (date.get(Calendar.MONTH)+1) + "." + date.get(Calendar.YEAR));
        resolution.setText(bmp.getWidth() + "×" + bmp.getHeight());
        size.setText(convertFileLenghtToString(FileByUri.length()));
    }

    private String convertFileLenghtToString(double lenght) {
        int prefixLenght = 1024;
        int prefixId = 0;
        String[] prefix = new String[]{"Б", "КБ", "МБ"};

        while (lenght / prefixLenght >= 1) {
            prefixId++;
            lenght /= prefixLenght;
        }

        String resultLenght = ((float)Math.round(lenght * 10) / 10) + " " + prefix[prefixId];
        return resultLenght;
    }

    private Uri getUri() {
        Intent intent = getIntent();
        String uri = intent.getStringExtra("uri");
        return Uri.parse(uri);
    }


}
