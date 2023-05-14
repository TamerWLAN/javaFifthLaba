package art.lab;

import art.lab.exampleStruct.SomeBean;
import art.lab.myStruct.Warrior;
import art.lab.task.Injector;

public class Main {
    public static void main(String[] args) {
        Injector injector = new Injector();
        injector.setFileName("files/exampleProperties/first.properties");
        SomeBean sb = injector.inject(new SomeBean());
        sb.foo();

        injector.setFileName("files/exampleProperties/second.properties");
        sb = injector.inject(new SomeBean());
        sb.foo();



        injector.setFileName("files/myProperies/lightEquipmentKit.properties");
        Warrior warrior = injector.inject(new Warrior());
        System.out.println(warrior.fight());

        injector.setFileName("files/myProperies/heavyEquipmentKit.properties");
        warrior = injector.inject(new Warrior());
        System.out.println(warrior.fight());

    }
}