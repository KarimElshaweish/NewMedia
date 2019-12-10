package com.karim.myapplication.Model;

import java.util.List;

public class PhotoGraph {
    String name,price;
    List<TypesItems>items;

    public PhotoGraph() {
    }
    public PhotoGraph(String name, String price, List<TypesItems> items) {
        this.name = name;
        this.price = price;
        this.items = items;
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
