package com.cui.librarys.bean;

import java.util.List;

/**
 * Created by cuiyang on 15/12/10.
 */
public class FolderBean {

    public long bucketId;
    public String bucketName;
    public String thumbPath;
    public int photoCount;

    /**
     * <p>
     * 重写该方法
     * <p>
     * 使只要图片所在的文件夹名称(dirName)相同就属于同一个图片组
     */
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof FolderBean)) {
//            return false;
//        }
//        return bucketName.equals(((FolderBean)o).bucketName);
//    }
}
