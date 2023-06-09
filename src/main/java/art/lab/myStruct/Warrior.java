package art.lab.myStruct;


import art.lab.task.AutoInjectable;

public class Warrior {
    @AutoInjectable
    private Weapon weapon;
    @AutoInjectable
    private Shield shield;

    public String fight() {
        return weapon.shot() + "\n" + shield.protect();
    }
}
