package com.cui.librarys.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cui.librarys.R;
import com.cui.librarys.bean.CropSquareTansformation;
import com.cui.librarys.bean.FolderBean;
import com.cui.librarys.bean.GetTransformat;
import com.cui.librarys.bean.ImageBean;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuiyang on 15/12/10.
 */
public class PopView_Adapter extends BaseAdapter {

    private List<FolderBean> parent_pathList;
    private Context mContext;
    private CropSquareTansformation cropSquareTansformation;

    public PopView_Adapter(List<FolderBean> parent_pathList) {
        this.parent_pathList = parent_pathList;
        cropSquareTansformation = GetTransformat.getCropsquareTansformation();

    }

    @Override
    public int getCount() {
        return parent_pathList == null ? 0 : parent_pathList.size();
    }

    @Override
    public Object getItem(int position) {
        return parent_pathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pop, parent, false);
            holder.img_folder = (ImageView) convertView.findViewById(R.id.image_folder);
            holder.title = (TextView) convertView.findViewById(R.id.folder_name);
            holder.number = (TextView) convertView.findViewById(R.id.folder_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FolderBean bean = parent_pathList.get(position);
        Picasso.with(mContext).load(new Uri.Builder().scheme("file").path(bean.thumbPath).build())
                .resize(200, 200)
                .centerCrop()
                .transform(cropSquareTansformation)
                .config(Bitmap.Config.RGB_565)
                .into(holder.img_folder);
        holder.title.setText(bean.bucketName + "");
        holder.number.setText(bean.photoCount + "");
        return convertView;
    }

    public class ViewHolder {
        ImageView img_folder;
        TextView title, number;
    }
}
