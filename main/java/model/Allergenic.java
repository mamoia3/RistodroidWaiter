package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.waiter.Utility;

public class Allergenic {
    private int id;
    private String name;

    public Allergenic(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Allergenic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Allergenic)) return false;
        Allergenic that = (Allergenic) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public static String getAllergenicsToString (List<Allergenic> allergenics) {
        ArrayList<String> list = new ArrayList<>();
        for(int i=0; i<allergenics.size(); i++) {
            list.add(allergenics.get(i).getName());
        }
        return Utility.listToStringWithDelimiter(list, ", ");
    }
}
