package com.cui.librarys.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by cuiyang on 15/12/10.
 */
public class ImageBean  {
    private String path;
    private String name;

    public ImageBean(){}
    public ImageBean(String path,String name){
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
