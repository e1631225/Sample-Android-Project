package com.example.infolinetestapplication.entity;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by suleyman on 31.3.2016.
 * Kullanıcının listeye eklediği yeni bir kayıt burada tutulur
 */
public class Record {
    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    String snapshotPath;

    @DatabaseField
    String videoPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSnapshotPath() {
        return snapshotPath;
    }

    public void setSnapshotPath(String snapshotPath) {
        this.snapshotPath = snapshotPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
