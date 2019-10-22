package App;

import java.util.List;

public class GameStage {

    public static class Round {
        private List<EnemyType> enemy;
        private List<Integer> amount;
        private int lastReleased;

//      enemyId && amount has same size
        public EnemyType nextEnemy() {
            return EnemyType.NORMAL_ENEMY;
        }
    }

    enum RoadType {
        MOUNTAIN,
        ROAD
    }

    enum EnemyType {
        TANKER_ENEMY,
        NORMAL_ENEMY,
        SMALLER_ENEMY,
        BOSS_ENEMY
    }

    enum TowerType {
        NORMAL_TOWER,
        MACHINEGUN_TOWER,
        SNIPER_TOWER
    }

    public final int UNIT_WIDTH;
    public final int UNIT_HEIGHT;
    public final int WIDTH;
    public final int HEIGHT;
    public final int INITIAL_GOLD;
    private RoadType[][] mapData;
    private GameImg demoImg;

    private List<Round> roundList;

    public GameStage(String folder) {
        UNIT_WIDTH = 0;
        UNIT_HEIGHT = 0;
        WIDTH = 0;
        HEIGHT = 0;
        INITIAL_GOLD = 0;
    }
}
