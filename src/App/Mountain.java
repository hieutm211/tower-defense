package App;

public class Mountain implements GameTile {

    private static GameImg image;
    private Position position;

    public Mountain(Position position) {}

    public Mountain(int x, int y) {}

    public void setPosition(Position position) {}

    public Position getPosition() {
        return null;
    }

    public boolean collision(GameEntity gameEntity) {
        return false;
    }

    public GameImg getImage() {
        return image;
    }

    public void update() {}
}
