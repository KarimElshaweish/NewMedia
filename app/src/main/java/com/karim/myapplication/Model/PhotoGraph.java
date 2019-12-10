package com.karim.myapplication.Model;

import java.util.List;

public class PhotoGraph {
    String name,price,image;
    List<TypesItems>items;

    public PhotoGraph() {
    }
    public PhotoGraph(String name, String price, List<TypesItems> items,String image) {
        this.name = name;
        this.price = price;
        this.items = items;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<TypesItems> getItems() {
        return items;
    }

    public void setItems(List<TypesItems> items) {
        this.items = items;
    }
}
