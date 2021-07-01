package com.gian.gethome.Clases;

public class SliderAnimalDetailScreen {

    private String image;
    private String title;


    public SliderAnimalDetailScreen(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
