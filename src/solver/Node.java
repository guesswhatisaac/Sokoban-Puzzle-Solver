package solver;

import java.util.ArrayList;

/* Notes
 * - COLLISION DOES NOT AFFECT ITEM AND MAP DATA!
 * - to improve memory usage and since mapdata is constant, maybe it's best to store only itemData
 * - use hash sets instead of array lists probably.
 * - used hashCode() for generating identifiers; may lead to possible collisions.
 * - Heuristic used is Manhattan distance. Manhattan Distance = | x 1 − x 2 | + | y 1 − y 2 | 
 */

public class Node implements Comparable<Node> {
   
   private int parentIdentifier;
   private int identifier;
   private char actionUsed;
   private int gCost;
   private int hCost;
   private int fCost;

   private int width;
   private int height;
   private char[][] itemsData;
   private char[][] mapData;

   private Position player = new Position(0, 0);
   private ArrayList<Position> crate = new ArrayList<Position>();
   private ArrayList<Position> wall = new ArrayList<Position>();
   private ArrayList<Position> target = new ArrayList<Position>();

   public Node(int width, int height, char[][] itemsData, char[][] mapData, int parentIdentifier,
               int previousGCost, char actionUsed){
      
      // plot itemsData & mapData coordinates 
      // i and j are reversed ie. i is y and j is x, but i fixed it in position class
      for(int i = 0; i < height; i++){
         for(int j = 0; j < width; j++){

            Position position = new Position(j, i);
              
               switch (itemsData[i][j]) {
                  case '@' :
                     player = position; break;
                  case '$' :
                     crate.add(position); break;
               }

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
      this.itemsData = itemsData;
      this.mapData = mapData;
      this.parentIdentifier = parentIdentifier;
      this.identifier = generateNodeIdentifier();
      this.actionUsed = actionUsed;
      this.gCost = previousGCost + 1;
      this.hCost = generateNodeHeuristicValue();
      this.fCost = this.gCost + this.hCost;

   }

   // flattens itemsData array to single string & converts it to int
   private int generateNodeIdentifier(){

      StringBuilder flattenedString = new StringBuilder();
      for(char[] row : itemsData) {
         flattenedString.append(row);
      }

      return flattenedString.hashCode();
  }

   // creates heuristic value of nodes based on manhattan distance formula
   private int generateNodeHeuristicValue(){
      
      int heuristicValue = 0;

      for(int i = 0; i < target.size(); i++){

         int crateX = crate.get(i).getX();
         int crateY = crate.get(i).getY();

         int targetX = target.get(i).getX();
         int targetY = target.get(i).getY();

         heuristicValue += Math.abs(crateX - targetX) + Math.abs(crateY - targetY); 
      
      }

      return heuristicValue;
   }

   // generates all possible successors based on player movement
   public ArrayList<Node> generateSuccessors(GameLogic rules, Node parentNode){

      ArrayList<Node> possibleSuccessors = new ArrayList<Node>();

      // player moves up
      if(rules.isValidPlayerMovement('u', player, mapData, itemsData)){
         Node updatedNode = rules.updatedNode('u', parentNode);
         possibleSuccessors.add(updatedNode);

         System.out.println("UP"); // DEBUG
      } 

      // player moves down 
      if(rules.isValidPlayerMovement('d', player, mapData, itemsData)){
         Node updatedNode = rules.updatedNode('d', parentNode);
         possibleSuccessors.add(updatedNode);
         
         System.out.println("DOWN"); // DEBUG
      } 

      // player moves left
      if(rules.isValidPlayerMovement('l', player, mapData, itemsData)){
         Node updatedNode = rules.updatedNode('l', parentNode);
         possibleSuccessors.add(updatedNode);

         System.out.println("LEFT"); // DEBUG
      }
      
      // player moves right
      if(rules.isValidPlayerMovement('r', player, mapData, itemsData)){
         Node updatedNode = rules.updatedNode('r', parentNode);
         possibleSuccessors.add(updatedNode);
         System.out.println("RIGHT"); // DEBUG
      }

      return possibleSuccessors;
   }

   // if all targets are filled, goal is reached
   public boolean isGoal(){

      int targetsFilled = 0;

      for(int i = 0; i < target.size(); i++){
         for(int j = 0; j < crate.size(); j++){
            if(target.get(i).isEqual(crate.get(j))){
               targetsFilled++;
               break;
            }

         }
      }

      if(targetsFilled == target.size())
         return true;
      
      return false;
   }

   @Override
   // orders the nodes from least (top-priority) to most (lowest-priority) fCost value in the priority queue
   public int compareTo(Node otherNode){
      if(this.fCost < otherNode.getFCost()){
         return -1;
      }
      else if(this.fCost > otherNode.getFCost()){
         return 1;
      }
      else{
         return 0;
      }
   }

   //___ Getter Methods ___//

   public int getFCost(){
      return this.fCost;
   }

   public int getGCost(){
      return this.gCost;
   }

   public int getIdentifier(){
      return this.identifier;
   }

   public char getActionUsed(){
      return this.actionUsed;
   }

   public int getParentIdentifier(){
      return this.parentIdentifier;
   }

   public Position getPlayer(){
      return this.player;
   }
   
   public ArrayList<Position> getCrates(){
      return this.crate;
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

   public char[][] getItemsData(){
      return this.itemsData;
   }

   public char[][] getMapData(){
      return this.mapData;
   }

   public void toStringInfo() {

    System.out.println("Action Used: " + actionUsed);
    System.out.println("Player Position: " + "(" + player.getX() + "," + player.getY() +")");
    
    System.out.println("Crate Positions");
    for(int i = 0; i < crate.size(); i++){
      System.out.println("Crate #" + (i+1) + ": " + "(" + crate.get(i).getX() + "," + crate.get(i).getY()+ ")");
    }

   }

   public void toStringMap(){
      for(int i = 0; i < height; i++){
         for(int j = 0; j < width; j++){
            System.out.print(itemsData[i][j]);
         }
         System.out.println();
      }
   }

}