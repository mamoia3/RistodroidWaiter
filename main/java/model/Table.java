package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import persistence.RistodroidDBSchema;
import persistence.SqLiteDb;

public class Table {
    private int id;
    private List<Order> orderList;

    public Table(int id, List<Order> orderList) {
        this.id = id;
        this.orderList = orderList;
    }

    public int getId() {
        return id;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table)) return false;
        Table table = (Table) o;
        return id == table.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static List<Table> getTablesFromDb(Context context) {
        SQLiteDatabase db = new SqLiteDb(context).getReadableDatabase();
        List<Table> tables = new ArrayList<>();

        String[] projection = {
                RistodroidDBSchema.TableTable.Cols.ID
        };

        Cursor cursor = db.query(RistodroidDBSchema.TableTable.NAME, projection, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(RistodroidDBSchema.CategoryTable.Cols.ID));
            tables.add(new Table(id, null));
        }

        cursor.close();
        return tables;
    }
}
