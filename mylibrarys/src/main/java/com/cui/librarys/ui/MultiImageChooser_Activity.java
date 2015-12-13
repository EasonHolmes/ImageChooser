package com.cui.librarys.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cui.librarys.R;
import com.cui.librarys.adapter.MultilImage_Adapter;
import com.cui.librarys.adapter.PopView_Adapter;
import com.cui.librarys.bean.FolderBean;
import com.cui.librarys.bean.ImageBean;
import com.cui.librarys.bean.ImageConfigBean;
import com.cui.librarys.common.ScreenUtils;
import com.cui.librarys.presenter.MultiImagePresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by cuiyang on 15/12/10.
 */
public class MultiImageChooser_Activity extends MultiImagePresenter implements View.OnClickListener {


    private Toolbar mToolbar;
    private PopView_Adapter Albumn_Adapter;
    private MultilImage_Adapter adapter;
    private LinearLayout layout_bottom;
    private TextView txt_current_folder;
    private RecyclerView recyclerView;
    private ListPopupWindow mFolderPopupWindow;


    /**
     * 每一个图片的路径
     */
    private ArrayList<ImageBean> image_list = new ArrayList<ImageBean>();
    /**
     * 文件夹封面
     */
    private ArrayList<FolderBean> folders_list = new ArrayList<FolderBean>();

    private Context mContext;
    /**
     * 最大选择数
     */
    public  int Max_select = 0;

    public final  String getMax_Extra = "MAX";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multilimage_act);
        mContext = this;
        Max_select = getIntent().getIntExtra(getMax_Extra, 0);

        initView();

        createPopView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setToolbarTitle("0/" + Max_select);//设置title要放在前面否则会不起作用.被坑了十几分钟才知道.........
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        txt_current_folder = (TextView) findViewById(R.id.txt_current_folder_name);
        txt_current_folder.setOnClickListener(this);


        image_list.addAll(getLocalAllImg());

        recyclerView = (RecyclerView) findViewById(R.id.photo_list);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new MultilImage_Adapter(image_list, MultiImageChooser_Activity.this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * popwindow
     */
    private void createPopView() {
        folders_list.addAll(getFolderList());

        Albumn_Adapter = new PopView_Adapter(folders_list);
        mFolderPopupWindow = new ListPopupWindow(MultiImageChooser_Activity.this);
        mFolderPopupWindow.setAdapter(Albumn_Adapter);
        mFolderPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mFolderPopupWindow.setHeight(ScreenUtils.getScreenH(MultiImageChooser_Activity.this) * 5 / 8);
        mFolderPopupWindow.setAnchorView(layout_bottom);
        mFolderPopupWindow.setModal(true);

        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                image_list.clear();
                FolderBean bean = folders_list.get(position);
                image_list.addAll(getMediaThumbnailsPathByCategroy(bean.bucketId));
                txt_current_folder.setText(bean.bucketName);
                adapter.notifyDataSetChanged();
                showOrDissPop();
            }
        });
    }


    public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.check_title) {
            setResult(ImageConfigBean.result_code);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放picasso的持有
        Picasso.with(this).cancelTag(this);
    }

    private void showOrDissPop() {
        if (mFolderPopupWindow.isShowing()) {
            mFolderPopupWindow.dismiss();
        } else {
            Albumn_Adapter.notifyDataSetChanged();
            mFolderPopupWindow.show();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.txt_current_folder_name) {
            showOrDissPop();
        }
    }
}
