package model;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Objects;

import persistence.RistodroidDBSchema;
import persistence.SqLiteDb;

public class Category {

    private int id;
    private String name;
    private byte[] photo;



    public Category(int id, String name, byte[] photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getPhoto() {
        return photo;
    }


    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    public static ArrayList<Category> getCategories(Context context) {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = new SqLiteDb(context).getReadableDatabase();
        String[] projection = {
                RistodroidDBSchema.CategoryTable.Cols.ID,
                RistodroidDBSchema.CategoryTable.Cols.NAME,
                RistodroidDBSchema.CategoryTable.Cols.PHOTO
        };

        Cursor cursor = db.query(RistodroidDBSchema.CategoryTable.NAME, projection, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(RistodroidDBSchema.CategoryTable.Cols.ID));
            String name = cursor.getString(cursor.getColumnIndex(RistodroidDBSchema.CategoryTable.Cols.NAME));
            byte[] photo = cursor.getBlob(cursor.getColumnIndex(RistodroidDBSchema.CategoryTable.Cols.PHOTO));
            categories.add(new Category(id, name, photo));
        }

        cursor.close();
        return categories;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
