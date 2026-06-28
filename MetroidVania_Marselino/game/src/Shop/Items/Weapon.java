package Shop.Items;


public abstract class Weapon extends Item {

    protected int damage;
    protected float attackSpeed;
    protected float range;

    public Weapon(String name, String description, int value,
                  int damage, float attackSpeed, float range) {

        super(name, description, value);

        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public float getRange() {
        return range;
    }

    @Override
    public void use() {
        System.out.println(name + " equipped.");
    }

    public abstract void attack();
}
