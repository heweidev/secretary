package com.hewei.secretary.note;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by fengyinpeng on 2018/1/4.
 *
 *

 一个Note由那条数据组成
 数据有以下几种：
 1. 字符串
 2. 文件（图片、视频、文档等）
 3. List
     Number
     字符串
     文件（图片、视频、文档等）

 4. Map
    key: string
    value: string， number, 文件
 */

public class Note {
    public Note(String title, String desc, List<String> tags) {
        this.title = title;
        this.desc = desc;
        this.tags = tags;
    }

    @SerializedName("_id")
    public String id;
    public String title;
    public String desc;
    public List<String> tags;
    public Date date;

    public List<NoteTemplate> data;
}
