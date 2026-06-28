package Shop.Items.Weapons;

// ini contoh nama item nanti diganti sm nama itemnya 

import Shop.Items.Weapon;

public class Gun extends Weapon {

    protected int ammo;

    public Gun() {
        super(
                "Pistol",
                "Basic firearm.",
                300,
                20,
                0.5f,
                12f
        );

        ammo = 12;
    }

    @Override
    public void attack() {

        if(ammo > 0){
            ammo--;
            System.out.println("Bang!");
        }else{
            System.out.println("Out of ammo!");
        }

    }

    public int getAmmo() {
        return ammo;
    }
}
