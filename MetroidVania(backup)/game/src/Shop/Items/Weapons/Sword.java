package Shop.Items.Weapons;

// ini contoh nama item 

import Shop.Items.Weapon;

public class Sword extends Weapon {

    public Sword() {
        super(
                "Iron Sword",
                "A basic sword.",
                100,
                15,
                1.0f,
                1.5f
        );
    }

    @Override
    public void attack() {
        System.out.println("Slash!");
    }
}