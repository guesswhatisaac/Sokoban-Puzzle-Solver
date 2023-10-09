package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Map {
    private int width;
    private int height;
    private char[][] mapData;
    private char[][] itemsData;
    private char[][] level;
    private boolean targetFound = false;

    public Map(int width, int height, char[][] mapData, char[][] itemsData) {
        this.width = width;
        this.height = height;
        this.mapData = mapData;
        this.itemsData = itemsData;
        level = itemsData;
    }

    public int[] getPlayerPosition()
    {
        // gets current player position as an array of integers
        int[] position = new int[2];
        for (int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                if (level[i][j] == '@') {
                    position[0] = i;
                    position[1] = j;
                }

        return position;
    }

    public ArrayList<int[]> getBoxPositions() {
        // gets all box positions as an array of integers
        ArrayList<int[]> boxes = new ArrayList<>();

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (level[i][j] == '$') {
                    int[] box = new int[2];
                    box[0] = i;
                    box[1] = j;
                    boxes.add(box);
                }

        return boxes;
    }

    public ArrayList<int[]> getTargetPositions() {
        ArrayList<int[]> targets = new ArrayList<>();

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (mapData[i][j] == '.') {
                    int[] target = new int[2];
                    target[0] = i;
                    target[1] = j;
                    targets.add(target);
                }

        return targets;
    }

    public int[] updateValid(int x, int y, int x2, int y2) {
        int[] coords = new int[2];
        if (itemsData[y][x] != '$' && mapData[y][x] != '#') {
            coords[0] = x;
            coords[1] = y;
            return coords;
        }
        // if the space has a crate and the spot next to it is empty, move
        else if (itemsData[y][x] == '$' && itemsData[y2][x2] != '$' && mapData[y2][x2] != '#') {
            coords[0] = x;
            coords[1] = y;
            return coords;
        }
        return null;
    }

    public ArrayList<int[]> getPossibleActions(int y, int x) {
        int[] coords;
        ArrayList<int[]> possibleMoves = new ArrayList<>();

        if (x - 2 >= 0) {
            coords = updateValid(x - 1, y, x - 2, y);
            if (coords != null) {
                possibleMoves.add(coords);
            }
        }

        if (x + 2 < width) {
            coords = updateValid(x + 1, y, x + 2, y);
            if (coords != null) {
                possibleMoves.add(coords);
            }
        }

        if (y - 2 >= 0) {
            coords = updateValid(x, y - 1, x, y - 1);
            if (coords != null) {
                possibleMoves.add(coords);
            }
        }

        if (y + 2 < height) {
            coords = updateValid(x, y + 1, x, y + 1);
            if (coords != null) {
                possibleMoves.add(coords);
            }
        }

        return possibleMoves;
    }

    public void checkValid(int x, int y, int x1, int y1, int x2, int y2) {;
        if (level[y1][x1] == ' ' && (mapData[y1][x1] == ' ' || mapData[y1][x1] == '.')) {
            level[y1][x1] = '@';
            level[y][x] = ' ';
        }

        else if (level[y1][x1] == '$') {
            if (level[y2][x2] == ' ' && (mapData[y2][x2] == ' ' || mapData[y2][x2] == '.')) {
                level[y2][x2] = '$';
                level[y1][x1] = '@';
                level[y][x] = ' ';
            }
        }

        else if (level[y1][x1] == '.') {
            level[y1][x1] = '@';
            level[y][x] = ' ';
        }

    }
    public void gameSpace(char direction) {
        int y = getPlayerPosition()[0];
        int x = getPlayerPosition()[1];

        if (direction == 'l')
            checkValid(x, y, x-1, y, x-2, y);
        else if (direction == 'r')
            checkValid(x, y, x+1, y, x+2, y);
        else if (direction == 'u')
            checkValid(x, y, x, y-1, x, y-2);
        else if (direction == 'd')
            checkValid(x, y, x, y+1, x, y+2);


        //System.out.println(Arrays.deepToString(level));

        /*
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                System.out.print(level[i][j]);
            System.out.println();
        } */
    }

    public void loadLevel(int width, int height, char[][] mapData, char[][] itemsData) {
        int x = getPlayerPosition()[0];
        int y = getPlayerPosition()[1];

        //System.out.println();
    }

    public char[][] getLevel() {
        return level;
    }
}
