package solver;

import java.util.ArrayList;

public class MapData {

    private char[][] mapData;
    private ArrayList<Position> wall = new ArrayList<>();
    private ArrayList<Position> target = new ArrayList<>();
    private int targetCount = 0;
    private int width;
    private int height;

 
    // reads mapData[][] and converts elements to Positions 
    public MapData(int width, int height, char[][] mapData) {
        this.width = width;
        this.height = height;
        this.mapData = new char[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Position position = new Position(j, i); // i and j reversed due to nested loop logic
                char mapChar = mapData[i][j];
                this.mapData[i][j] = mapChar;

                if (mapChar == '#') {
                    wall.add(position);
                } else if (mapChar == '.') {
                    target.add(position);
                    targetCount++;
                }
            }
        }
    }

    public char[][] getMapData() {
        return this.mapData;
    }

    public ArrayList<Position> getWalls() {
        return new ArrayList<>(wall);
    }

    public ArrayList<Position> getTargets() {
        return new ArrayList<>(target);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getTargetCount(){
        return this.targetCount;
    }

}

