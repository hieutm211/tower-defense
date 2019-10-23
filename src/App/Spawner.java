package App;

public class Spawner extends Road {

    public Spawner(Position position) {
        super(position);
        this.type = TYPE_SPAWNER;
    }

    public Spawner(double x, double y) {
        super(x, y);
        this.type = TYPE_SPAWNER;
    }
}
