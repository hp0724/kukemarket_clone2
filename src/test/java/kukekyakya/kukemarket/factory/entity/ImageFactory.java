package kukekyakya.kukemarket.factory.entity;

import kukekyakya.kukemarket.entity.post.Image;

public class ImageFactory {
    public static Image createImage() {
        return new Image("origin_filename.jpg");
    }

    public static Image createImageWithOriginName(String originName) {
        return new Image(originName);
    }
}