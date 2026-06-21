package Shop.Items.Weapons;

// ini contoh nama item nanti diganti sm nama itemnya 

import Shop.Items.Weapon;

public class Dagger extends Weapon {

    public Dagger() {
        super(
                "Dagger",
                "Fast melee weapon.",
                80,
                8,
                2.0f,
                1.0f
        );
    }

    @Override
    public void attack() {
        System.out.println("Stab!");
    }
}
