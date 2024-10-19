package com.gokulsundar4545.dropu;

public class Recomented {
    private String RecomentedName;
    private String RecomentedCoverUrl;
    private String RecomentedTitle;
    private String RecomentedsubTitle;

    public Recomented() {
        // Default constructor required for calls to DataSnapshot.getValue(Movie.class)
    }

    public Recomented(String recomentedName, String recomentedCoverUrl, String recomentedTitle, String recomentedsubTitle) {
        RecomentedName = recomentedName;
        RecomentedCoverUrl = recomentedCoverUrl;
        RecomentedTitle = recomentedTitle;
        RecomentedsubTitle = recomentedsubTitle;
    }

    public String getRecomentedName() {
        return RecomentedName;
    }

    public void setRecomentedName(String recomentedName) {
        RecomentedName = recomentedName;
    }

    public String getRecomentedCoverUrl() {
        return RecomentedCoverUrl;
    }

    public void setRecomentedCoverUrl(String recomentedCoverUrl) {
        RecomentedCoverUrl = recomentedCoverUrl;
    }

    public String getRecomentedTitle() {
        return RecomentedTitle;
    }

    public void setRecomentedTitle(String recomentedTitle) {
        RecomentedTitle = recomentedTitle;
    }

    public String getRecomentedsubTitle() {
        return RecomentedsubTitle;
    }

    public void setRecomentedsubTitle(String recomentedsubTitle) {
        RecomentedsubTitle = recomentedsubTitle;
    }
}
