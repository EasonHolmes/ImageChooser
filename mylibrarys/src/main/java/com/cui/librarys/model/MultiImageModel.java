package com.cui.librarys.model;

import com.cui.librarys.bean.FolderBean;
import com.cui.librarys.bean.ImageBean;

import java.util.List;

/**
 * Created by cuiyang on 15/12/13.
 */
public interface MultiImageModel  {

    List<ImageBean> getLocalAllImg();

    List<FolderBean> getFolderList();
}
