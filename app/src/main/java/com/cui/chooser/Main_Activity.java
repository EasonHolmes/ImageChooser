package com.cui.chooser;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cui.librarys.bean.ImageConfigBean;
import com.cui.librarys.ui.MultiImageChooser_Activity;


public class Main_Activity extends AppCompatActivity {

    private final int select_max = 9;

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private Perview_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(com.cui.librarys.R.id.mToolbar);
        setSupportActionBar(mToolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));//为true会直接到最后一个item
        adapter = new Perview_Adapter(this, ImageConfigBean.List_checks);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new Perview_Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i = new Intent(Main_Activity.this, Ui_Activity.class);
                i.putExtra("path", ImageConfigBean.List_checks.get(position));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            Main_Activity.this, v, Ui_Activity.TRANSIT_PIC);
                    ActivityCompat.startActivity(Main_Activity.this, i, optionsCompat.toBundle());
                } else {
                    startActivity(i);
                }
            }
        });
    }

    public void choose(View v) {
        startActivityForResult(new Intent(this, MultiImageChooser_Activity.class).putExtra("MAX", select_max), ImageConfigBean.result_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageConfigBean.result_code) {
            adapter.notifyDataSetChanged();
        }
    }
}
