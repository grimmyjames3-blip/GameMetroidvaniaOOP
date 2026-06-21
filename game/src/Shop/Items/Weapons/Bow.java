package Shop.Items.Weapons;



// ini contoh nama item nanti diganti sm nama itemnya 

import Shop.Items.Weapon;

public class Bow extends Weapon {

    public Bow() {
        super(
                "Wooden Bow",
                "Simple ranged weapon.",
                150,
                12,
                0.8f,
                10f
        );
    }

    @Override
    public void attack() {
        System.out.println("Shoot Arrow!");
    }
}
