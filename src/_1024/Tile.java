package _1024;

public class Tile {

    public int value;
    public int dx, dy;

    public static void main() {
        Tile tile = new Tile();
        tile.value = 0;
        tile.dx = 0;
        tile.dy = 0;
    }

    public void x2() {
        this.value*=2;
    }

}
