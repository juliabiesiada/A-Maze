package Model;


import javafx.scene.image.Image;

public class Character {

    public String imageURL;
    public String name;

    public String getImageURL() {
        return imageURL;
    }

    public void setImage(String url) {
        this.imageURL = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character(String url, String name) {
        this.imageURL = url;
        this.name = name;
    }
}
