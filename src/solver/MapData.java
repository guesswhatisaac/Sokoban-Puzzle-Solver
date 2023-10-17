package solver;

import java.util.ArrayList;

public class MapData {

   private char[][] mapData;
   private ArrayList<Position> wall = new ArrayList<Position>();
   private ArrayList<Position> target = new ArrayList<Position>();
   private int width;
   private int height;

    public MapData(int width, int height, char[][] mapData){

      // plot mapData coordinates
      for(int i = 0; i < height; i++){
         for(int j = 0; j < width; j++){

            Position position = new Position(j, i); // i and j reversed due to nested loop logic
              
               switch (mapData[i][j]) {
                  case '#' :
                     wall.add(position); break;
                  case '.' :
                     target.add(position); break;
               }
         }
      }

      this.width = width;
      this.height = height;
      this.mapData = mapData;


    }

    public char[][] getMapData() {
        char[][] cloneMapData = new char[height][width];
        for (int i = 0; i < mapData.length; i++) {
            cloneMapData[i] = mapData[i].clone();
        }
        return cloneMapData;
    }

    public ArrayList<Position> getWalls(){
        return this.wall;
     }
  
     public ArrayList<Position> getTargets(){
        return this.target;
     }

     public int getHeight(){
        return this.height;
     }
  
     public int getWidth(){
        return this.width;
     }
  
  
}


