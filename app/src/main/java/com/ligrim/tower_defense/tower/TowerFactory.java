package com.ligrim.tower_defense.tower;

import com.ligrim.tower_defense.base.Position;

public class TowerFactory {
    public TowerFactory() {}
    public Tower getInstance(String towerId) {
        switch(towerId) {
            case "tower_normal": return new NormalTower(new Position(0,0));
            case "tower_sniper": return new SniperTower(new Position(0,0));
            case "tower_machine_gun": return new MachineGunTower(new Position(0,0));
        }
        return null;
    }
}
