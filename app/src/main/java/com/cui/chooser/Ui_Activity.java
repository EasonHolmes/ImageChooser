package com.cui.chooser;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.cui.librarys.common.ScreenUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by cuiyang on 15/12/13.
 */
public class Ui_Activity extends AppCompatActivity {

    public static final String TRANSIT_PIC = "picture";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_act);
        imageView = (ImageView) findViewById(R.id.img_ui);

        String path = getIntent().getStringExtra("path");
        Picasso.with(this).load(new Uri.Builder().scheme("file").path(path).build())
                .into(imageView);

        ViewCompat.setTransitionName(imageView, TRANSIT_PIC);

    }
}
