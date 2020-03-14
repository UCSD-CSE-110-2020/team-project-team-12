package cse110.ucsd.team12wwr.recycler;

public class Item {
    private String name;

    public Item() {
        this.name = "";
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
