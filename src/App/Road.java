package App;

public class Road implements GameTile {
    public static final int TYPE_ROAD = 0;
    public static final int TYPE_SPAWNER = 1;
    public static final int TYPE_TARGET = 2;

    protected int type;
    protected static GameImg image;
    protected Position position;

    public Road(Position position) {
        this.position = position;
        this.type = TYPE_ROAD;
    }

    public Road(double x, double y) {
        this.position = new Position(x, y);
        this.type = TYPE_ROAD;
    }

    public void setPosition(Position position) {}

    public Position getPosition() {
        return null;
    }

    public boolean collision(GameEntity gameEntity) {
        return false;
    }

    public void update() {}

    public GameImg getImage() {
        return image;
    }
}
