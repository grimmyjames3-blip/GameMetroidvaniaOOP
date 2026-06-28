package Shop.Items;



public abstract class Armor extends Item {

    protected int defense;

    public Armor(String name, String description, int value, int defense) {
        super(name, description, value);

        this.defense = defense;
    }

    public int getDefense() {
        return defense;
    }

    @Override
    public void use() {
        System.out.println(name + " equipped.");
    }
}
