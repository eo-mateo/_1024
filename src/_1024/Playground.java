package _1024;
import org.w3c.dom.ls.LSInput;

import java.lang.Math;
import java.util.Scanner;


public class Playground {

    final static int _SIDE = 5;
    Tile[][] tiles;

    public int score;

    public enum Direction {
        LEFT, RIGHT, UP, DOWN, NULL;
    }
    public enum MergeMode {
        MOVE, COUNT, NULL;
    }

    public static void main(String[] args) {

        Playground playground = new Playground();

        playground.tiles = new Tile[_SIDE][_SIDE];

        for(int i=0;i<_SIDE;i++) {
            playground.tiles[i] = new Tile[_SIDE];
            for(int j=0;j<_SIDE;j++) {
                playground.tiles[i][j] = new Tile();
            }
       }

        playground.score=0;

        // Zaczynamy. Losujemy dwa początkowe stany na tablicy
        for(int i=0;i<24;i++) playground.addNewTile();

        playground.print();
        playground.game();
    }

    public void game() {
        Direction direction;
        direction = Direction.NULL;
        boolean state=false;
        while(state==false) {
                char c;
            do {
                System.out.println("Kierunek (r / l / u / d): ");
                Scanner reader = new Scanner(System.in);
                c = reader.next(".").charAt(0);
                if ((c != 'r') && (c != 'l') && (c != 'u') && (c != 'd')) System.out.println("Błąd!");
            } while ((c != 'r') && (c != 'l') && (c != 'u') && (c != 'd'));

            if (c == 'r') direction = Direction.RIGHT;
            if (c == 'l') direction = Direction.LEFT;
            if (c == 'u') direction = Direction.UP;
            if (c == 'd') direction = Direction.DOWN;

            for (int i = 0; i < 4; i++) {
                this.move(direction); // LICZYMY DELTY
                this.animate(); // RUSZAMY SIĘ O DELTY
                this.clearDeltas();
            }

            this.merge(direction, MergeMode.MOVE);

            for (int i = 0; i < 2; i++) {
                this.move(direction); // LICZYMY DELTY
                this.animate(); // RUSZAMY SIĘ O DELTY
                this.clearDeltas();
            }

            if(this.ifGameover()){
                state=true;
                System.out.println("KONIEC!");
            }
            else {
                this.addNewTile();
                System.out.println();
                this.print();
            }
        }
    }

    public void print() {


        String leftAlignFormat = "| %-5d | %-5d | %-5d | %-5d | %-5d |%n";

        System.out.println("SCORE: "+this.score);
        System.out.println("+---------+---------+---------+---------+---------+");

        for(int i=0;i<_SIDE;i++)
        {
            System.out.println("|         |         |         |         |         |");
            for(int j=0;j<_SIDE;j++) {
                if (this.tiles[i][j].value!=0)
                    System.out.format("|   %-5d ", this.tiles[i][j].value);
                else
                    System.out.print("|         ");
            }
            System.out.format("|%n");
            System.out.println("|         |         |         |         |         |");
            System.out.println("+---------+---------+---------+---------+---------+");
        }

    }

    public void animate() {
        for(int i=0;i<_SIDE;i++) {
            for(int j=0;j<_SIDE;j++) {
                if(this.tiles[i][j].dx!=0) {
                    this.tiles[i][j+this.tiles[i][j].dx].value=this.tiles[i][j].value;
                    this.tiles[i][j].value=0;
                }
                if(this.tiles[i][j].dy!=0) {
                    this.tiles[i+this.tiles[i][j].dy][j].value=this.tiles[i][j].value;
                    this.tiles[i][j].value=0;
                }
            }
        }
  //      this.clearDeltas();
    }

    public void countScore(int value) {
        this.score +=value;
    }

    public void clearDeltas() {
        for(int i=0;i<_SIDE;i++) {
            for(int j=0;j<_SIDE;j++) {
                this.tiles[i][j].dx=0;
                this.tiles[i][j].dy=0;
            }
        }
    }

    public boolean ifGameover() {
        System.out.println("intern ifGmeover");
        int empties=0;
        int possibleMergers=0;

        for(int i=0;i<_SIDE;i++) {
            for(int j=0;j<_SIDE;j++) {
                if(this.tiles[i][j].value==0)
                    empties++;
            }
        }
        if(empties>0)
            return false;   // Jeśli znajdziemy jakieś puste pole - gra się toczy.
        else {              // Jeśli wszystkie zajęte, to sprawdzamy, czy jest możliwość ruchu.
            for(int i=0;i<_SIDE;i++) {
                for(int j=0;j<_SIDE;j++) {
                    possibleMergers =
                          this.merge(Direction.LEFT,MergeMode.COUNT)
                        + this.merge(Direction.RIGHT,MergeMode.COUNT)
                        + this.merge(Direction.UP,MergeMode.COUNT)
                        + this.merge(Direction.DOWN,MergeMode.COUNT);
                }
            }
            System.out.println("Possible mergers: "+possibleMergers);
            if(possibleMergers>0)
                return false;
            else
                return true; // Jeśli wszystko zapełnione i nie ma możliwości połączenia.
        }
    }

    public void addNewTile() {

        int randomRow;
        int randomCol;
        do {
            randomRow = (int)(_SIDE*Math.random());
            randomCol = (int)(_SIDE*Math.random());
        } while (this.tiles[randomCol][randomRow].value!=0);

        if(Math.random()>0.92)
            this.tiles[randomCol][randomRow].value=4;
        else
            this.tiles[randomCol][randomRow].value=2;

    }

    public void move(Direction direction) {

        if(direction.equals(Direction.RIGHT)){
            for(int i=0;i<_SIDE;i++) {
                for(int j=0;j<_SIDE-1;j++) { // DLA WSZYSTKICH KAFELKÓW BEZ OSTATNIEJ KOLUMNY NA PRAWO
                    int z=1;
                    if(this.tiles[i][j].value!=0) {
                        while(this.tiles[i][j+z].value==0){
                            this.tiles[i][j].dx+=1;
                            if(j+z<_SIDE-1)
                                z++;
                            else
                                break;
                        }
                    }
                }
            }
        }

        if(direction.equals(Direction.LEFT)){
            for(int i=0;i<_SIDE;i++) {
                for(int j=1;j<_SIDE;j++) { // DLA WSZYSTKICH KAFELKÓW BEZ PIERWSZEJ KOLUMNY NA LEWO
                    int z=1;
                    if(this.tiles[i][j].value!=0) {
                        while(this.tiles[i][j-z].value==0){
                            this.tiles[i][j].dx-=1;
                            if(j-z>0)
                                z++;
                            else
                                break;
                        }
                    }
                }
            }
        }

        if(direction.equals(Direction.UP)){
            for(int i=1;i<_SIDE;i++) {
                for(int j=0;j<_SIDE;j++) { // DLA WSZYSTKICH KAFELKÓW BEZ PIERWSZEGO WIERSZA
                    int z=1;
                    if(this.tiles[i][j].value!=0) {
                        while(this.tiles[i-z][j].value==0){
                            this.tiles[i][j].dy-=1;
                            if(i-z>0)
                                z++;
                            else
                                break;
                        }
                    }
                }
            }
        }

        if(direction.equals(Direction.DOWN)){
            for(int i=0;i<_SIDE-1;i++) {
                for(int j=0;j<_SIDE;j++) { // DLA WSZYSTKICH KAFELKÓW BEZ OSTATNIEGO WIERSZA
                    int z=1;
                    if(this.tiles[i][j].value!=0) {
                        while(this.tiles[i+z][j].value==0){
                            this.tiles[i][j].dy+=1;
                            if(i+z<_SIDE-1)
                                z++;
                            else
                                break;
                        }
                    }
                }
            }
        }


    }

    public int merge(Direction direction, MergeMode mode) {

        int potentialMergersNumber=0;

        if(direction.equals(Direction.RIGHT)){
            for(int i=0;i<_SIDE;i++) {
                for(int j=_SIDE-2;j>0;j--) { // DLA WSZYSTKICH KAFELKÓW BEZ OSTATNIEJ KOLUMNY NA PRAWO (OD KOŃCA DO POCZĄTKU)
                    if((this.tiles[i][j].value!=0)&&(this.tiles[i][j+1].value==this.tiles[i][j].value)) {
                        if(mode.equals(MergeMode.MOVE)) {
                            this.tiles[i][j+1].x2();
                            this.tiles[i][j].value=0;
                            this.countScore(this.tiles[i][j+1].value);
                        } else {
                            potentialMergersNumber+=1;
                        }
                    }
                }
            }
        }

        if(direction.equals(Direction.LEFT)) {
            for (int i = 0; i < _SIDE; i++) {
                for (int j = 1; j < _SIDE; j++) { // DLA WSZYSTKICH KAFELKÓW BEZ PIERWSZEJ KOLUMNY NA LEWO (OD POCZĄTKU DO KOŃCA)
                    if ((this.tiles[i][j].value != 0) && (this.tiles[i][j - 1].value == this.tiles[i][j].value)) {
                        if (mode.equals(MergeMode.MOVE)) {
                            this.tiles[i][j - 1].x2();
                            this.tiles[i][j].value = 0;
                            this.countScore(this.tiles[i][j - 1].value);
                        } else {
                            potentialMergersNumber += 1;
                        }
                    }
                }
            }
        }

        if(direction.equals(Direction.UP)){
            for(int i=1;i<_SIDE;i++) {
                for (int j = 0; j < _SIDE; j++) { // DLA WSZYSTKICH KAFELKÓW BEZ PIERWSZEGO WIERSZA
                    if ((this.tiles[i][j].value != 0) && (this.tiles[i - 1][j].value == this.tiles[i][j].value)) {
                        if (mode.equals(MergeMode.MOVE)) {
                            this.tiles[i - 1][j].x2();
                            this.tiles[i][j].value = 0;
                            this.countScore(this.tiles[i - 1][j].value);
                        } else {
                            potentialMergersNumber += 1;
                        }
                    }
                }
            }
        }

        if(direction.equals(Direction.DOWN)){
            for(int i=_SIDE-2;i>0;i--) {
                for(int j=0;j<_SIDE;j++) { // DLA WSZYSTKICH KAFELKÓW BEZ OSTATNIEGO WIERSZA
                    if((this.tiles[i][j].value!=0)&&(this.tiles[i+1][j].value==this.tiles[i][j].value)) {
                        if (mode.equals(MergeMode.MOVE)) {
                            this.tiles[i + 1][j].x2();
                            this.tiles[i][j].value = 0;
                            this.countScore(this.tiles[i + 1][j].value);
                        } else {
                            potentialMergersNumber += 1;
                        }
                    }
                }
            }
        }
        return potentialMergersNumber;
    }
}