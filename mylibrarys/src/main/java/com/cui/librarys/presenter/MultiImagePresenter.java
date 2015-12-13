package com.cui.librarys.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.cui.librarys.bean.FolderBean;
import com.cui.librarys.bean.ImageBean;
import com.cui.librarys.model.MultiImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuiyang on 15/12/13.
 */
public class MultiImagePresenter extends AppCompatActivity implements MultiImageModel {

    /**
     * 查找本地所有图片
     */
    @Override
    public List<ImageBean> getLocalAllImg() {
        final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,// 图片绝对路径
                MediaStore.Images.Media.DISPLAY_NAME,// 图片文件名
                MediaStore.Images.Media.DATE_ADDED,};
        //只查询jpeg和png的图片
        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                null, null, IMAGE_PROJECTION[2] + " desc");

        List<ImageBean> list = new ArrayList<ImageBean>();
        if (cursor != null) {
            String path;
            String name;
            while (cursor.moveToNext()) {
                //图片的路径
                path = cursor.getString(cursor.getColumnIndex(IMAGE_PROJECTION[0]));
                name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                //获取该图片的所在文件夹的路径
//                File file = new File(path);
//                String parentName = file.getParentFile().getName();
//                String parentPath = file.getParentFile().getAbsolutePath();

                ImageBean iamge = new ImageBean(path, name);
                list.add(iamge);
            }
            cursor.close();
            return list;
        } else {
            return null;
        }
    }

    /**
     * 获取文件夹分类信息
     *
     * @return
     */
    @Override
    public List<FolderBean> getFolderList() {
        ContentResolver resolver = getContentResolver();

        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID};
        String orderBy = MediaStore.Images.Media.BUCKET_ID;

        Cursor albumCursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, orderBy);

        int bucketColumnId = albumCursor
                .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

        int bucketColumn = albumCursor
                .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        long previouSid = 0;
        ArrayList<FolderBean> albumInfos = new ArrayList<>();
        FolderBean albumInfo = new FolderBean();
        albumInfo.bucketId = 0;
        albumInfo.bucketName = "所有图片";
        albumInfo.photoCount = 0;
        albumInfos.add(albumInfo);
        int photoCount = 0;
        while (albumCursor.moveToNext()) {

            photoCount++;
            if (albumCursor.isLast()) {
                albumInfos.get(0).photoCount = photoCount;
            }

            long bucketId = albumCursor.getInt(bucketColumnId);
            if (previouSid != bucketId) {
                FolderBean album = new FolderBean();
                album.bucketId = bucketId;
                album.bucketName = albumCursor.getString(bucketColumn);
                album.photoCount++;
                albumInfos.add(album);
                previouSid = bucketId;
            } else {
                if (albumInfos.size() > 0) {
                    albumInfos.get(albumInfos.size() - 1).photoCount++;
                }
            }

        }

        if (albumCursor != null) {
            albumCursor.close();
        }

        if (photoCount == 0) {
            albumInfos.clear();
        }

        if (albumInfos.size() > 0) {
            for (int i = 0; i < albumInfos.size(); i++) {
                String thumbPath = getMediaThumbnailPath(this, albumInfos.get(i).bucketId);
                albumInfos.get(i).thumbPath = thumbPath;
            }
        }
        return albumInfos;
    }

    /**
     * 获取文件夹封面图
     *
     * @param context
     * @param id
     * @return
     */
    private static String getMediaThumbnailPath(Context context, long id) {
        String path = "";
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketId = String.valueOf(id);
        String sort = MediaStore.Images.Thumbnails._ID + " DESC";
        String[] selectionArgs = {bucketId};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor;
        if (!bucketId.equals("0")) {
            cursor = context.getContentResolver().query(images, null,
                    selection, selectionArgs, sort);
        } else {
            cursor = context.getContentResolver().query(images, null,
                    null, null, sort);
        }
        if (cursor != null && cursor.moveToNext()) {
            selection = MediaStore.Images.Media._ID + " = ?";
            String photoID = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            selectionArgs = new String[]{photoID};

            images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
            Cursor pathCursor = context.getContentResolver().query(images, null,
                    selection, selectionArgs, sort);
            if (pathCursor != null && pathCursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            } else
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            if (pathCursor != null)
                pathCursor.close();
        }
        if (cursor != null)
            cursor.close();
        return path;
    }

    protected ArrayList<ImageBean> getMediaThumbnailsPathByCategroy( long id) {
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketId = String.valueOf(id);
        String sort = MediaStore.Images.Media._ID + " DESC";
        String[] selectionArgs = {bucketId};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor;
        if (!bucketId.equals("0")) {
            cursor = getContentResolver().query(images, null,
                    selection, selectionArgs, sort);
        } else {
            cursor = getContentResolver().query(images, null,
                    null, null, sort);
        }
        int pathColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

        ArrayList<ImageBean> imageInfos = new ArrayList<>();

        while (cursor.moveToNext()) {
            String path = cursor.getString(pathColumn);
            ImageBean imageInfo = new ImageBean();
            imageInfo.setPath(path);
            imageInfos.add(imageInfo);
        }
        cursor.close();
        return imageInfos;
    }
}
