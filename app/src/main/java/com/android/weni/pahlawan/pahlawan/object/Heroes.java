package com.android.weni.pahlawan.pahlawan.object;

/**
 * Created by David on 15/11/2017.
 */

public class Heroes {

    private String heroesName;
    private String description;
    private String imageUri;

    public Heroes(){
    }

    public Heroes(String heroesName, String description, String imageUri) {
        this.heroesName = heroesName;
        this.description = description;
        this.imageUri = imageUri;
    }

    public String getHeroesName() {
        return heroesName;
    }

    public void setHeroesName(String heroesName) {
        this.heroesName = heroesName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
