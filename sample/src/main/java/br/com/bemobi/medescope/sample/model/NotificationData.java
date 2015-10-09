package br.com.bemobi.medescope.sample.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by bkosawa on 30/06/15.
 */
public class NotificationData {

    private String id;

    private String title;

    private String desc;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public NotificationData(String id) {
        this.id = id;
    }

    public NotificationData(String id, String title, String desc) {
        this.id = id;
        this.title = title;
        this.desc = desc;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}
