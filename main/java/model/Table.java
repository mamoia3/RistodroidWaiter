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
    private String id;

    public Table(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
            String id = cursor.getString(cursor.getColumnIndex(RistodroidDBSchema.TableTable.Cols.ID));
            tables.add(new Table(id));
        }

        cursor.close();
        return tables;
    }
}
