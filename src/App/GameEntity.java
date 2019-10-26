package App;

public interface GameEntity {

    void setPosition(Position position);
    Position getPosition();
    boolean collision(GameEntity other);
    void update();
    GameImg getImage();
}
