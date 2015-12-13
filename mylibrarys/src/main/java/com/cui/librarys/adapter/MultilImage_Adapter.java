package com.cui.librarys.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.cui.librarys.R;
import com.cui.librarys.bean.ImageConfigBean;
import com.cui.librarys.bean.CropSquareTansformation;
import com.cui.librarys.bean.ImageBean;
import com.cui.librarys.ui.MultiImageChooser_Activity;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by cuiyang on 15/10/15.
 */
public class MultilImage_Adapter extends RecyclerView.Adapter<MultilImage_Adapter.ViewHolder> {

    private List<ImageBean> path_list;
    private MultiImageChooser_Activity mContext;

    private CropSquareTansformation cropSquareTransformation;

    public MultilImage_Adapter(List<ImageBean> path_list, MultiImageChooser_Activity mContext) {
        this.path_list = path_list;
        this.mContext = mContext;
        cropSquareTransformation = new CropSquareTansformation();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_chooser, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String path = path_list.get(position).getPath();

        Changecheck(holder, path);

        holder.item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(holder, path);
            }
        });

        Picasso.with(mContext).load(new Uri.Builder().scheme("file").path(path).build())
                .resize(200, 200)
                .centerCrop()
                .transform(cropSquareTransformation)
                .config(Bitmap.Config.RGB_565)
                .into(holder.item_img);
    }

    /**
     * 设置是否选中
     * 当然你可以在此之前判断业务需要的最大数量
     *
     * @param holder
     * @param path
     */
    private void setCheck(ViewHolder holder, String path) {
        //大于设置数量则不加入
        if ( ImageConfigBean.List_checks.size() < mContext.Max_select) {
            if (Changecheck(holder, path)) {//如果包括就删除
                int i = ImageConfigBean.List_checks.indexOf(path);
                ImageConfigBean.List_checks.remove(i);
                Changecheck(holder, path);
                setTitleNum();
            } else {
                ImageConfigBean.List_checks.add(path);
                Changecheck(holder, path);
                setTitleNum();
            }
        }else{
            //do someting
        }

    }

    private void setTitleNum() {
        mContext.setToolbarTitle(ImageConfigBean.List_checks.size() + "/" + mContext.Max_select);
    }

    /**
     * 判断是否以选
     *
     * @param hoder
     * @param path
     * @return
     */
    private boolean Changecheck(ViewHolder hoder, String path) {
        if (ImageConfigBean.List_checks.contains(path)) {
            hoder.img_check.setVisibility(View.VISIBLE);
            return true;
        } else {
            hoder.img_check.setVisibility(View.GONE);
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return path_list != null ? path_list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_img;
        FrameLayout img_check;

        public ViewHolder(View itemView) {
            super(itemView);
            item_img = (ImageView) itemView.findViewById(R.id.img);
            img_check = (FrameLayout) itemView.findViewById(R.id.img_check);
        }
    }
}
