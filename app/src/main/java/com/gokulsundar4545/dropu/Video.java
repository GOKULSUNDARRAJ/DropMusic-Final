package com.gokulsundar4545.dropu;

public class Video {

    private String VideoName;
    private String VideoCoverUrl;
    private String VideoTitle;
    private String VideosubTitle;

    public Video() {

    }

    public Video(String videoName, String videoCoverUrl, String videoTitle, String videosubTitle) {
        VideoName = videoName;
        VideoCoverUrl = videoCoverUrl;
        VideoTitle = videoTitle;
        VideosubTitle = videosubTitle;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getVideoCoverUrl() {
        return VideoCoverUrl;
    }

    public void setVideoCoverUrl(String videoCoverUrl) {
        VideoCoverUrl = videoCoverUrl;
    }

    public String getVideoTitle() {
        return VideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        VideoTitle = videoTitle;
    }

    public String getVideosubTitle() {
        return VideosubTitle;
    }

    public void setVideosubTitle(String videosubTitle) {
        VideosubTitle = videosubTitle;
    }
}
