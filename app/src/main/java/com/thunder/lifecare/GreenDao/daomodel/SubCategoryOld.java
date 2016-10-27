package com.thunder.lifecare.GreenDao.daomodel;

import com.thunder.lifecare.R;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryOld {

    private final int imageId;
    private final String title;
    private final String year;
    private final String location;

    private SubCategoryOld(int imageId, String title, String year, String location) {
        this.imageId = imageId;
        this.title = title;
        this.year = year;
        this.location = location;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getLocation() {
        return location;
    }

    public static List<SubCategoryOld> getAllPaintings() {
        String[] titles = {"ABC", "mcs"};
        String[] years = {"10", "8"};
        String[] locations = {"Indore", "Indore"};
        int[] images = {R.mipmap.ic_launcher};

        int size = titles.length;
        List<SubCategoryOld> subCategories = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            final int imageId = images[0];
            SubCategoryOld sub = new SubCategoryOld(imageId, titles[i], years[i], locations[i]);
            subCategories.add(sub);
        }

        return subCategories;
    }

}