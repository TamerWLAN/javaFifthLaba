package art.lab.task;

import art.lab.myStruct.Warrior;

import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {

    @org.junit.jupiter.api.Test
    void setFileNameTest() {
        Injector injector = new Injector();

        Exception ex = assertThrows(RuntimeException.class, ()->{
            injector.inject(new Warrior());
        });

        assertEquals("File path must be set", ex.getMessage());
    }

    @org.junit.jupiter.api.Test
    void injectTest() {

        Exception ex = assertThrows(NullPointerException.class, ()->{
            (new Warrior()).fight();
        });

        Injector injector = new Injector();
        injector.setFileName("files/myProperies/lightEquipmentKit.properties");
        Warrior warrior = injector.inject(new Warrior());

        assertEquals("Bang\nLow level protection", warrior.fight());
    }

    @org.junit.jupiter.api.Test
    void injectAnotherTest() {
        Injector injector = new Injector();
        injector.setFileName("files/myProperies/heavyEquipmentKit.properties");
        Warrior warrior = injector.inject(new Warrior());

        assertEquals("Bang-bang-bang\nHigh level protection", warrior.fight());
    }
}