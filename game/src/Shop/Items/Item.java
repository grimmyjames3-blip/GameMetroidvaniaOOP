package Shop.Items;


public abstract class Item {

    protected String name;
    protected String description;
    protected int value;

    public Item(String name, String description, int value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getValue() {
        return value;
    }

    public abstract void use();
}
