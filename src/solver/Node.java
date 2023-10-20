package solver;

import java.util.ArrayList;

/* Notes
 * - maybe instead of having to read new nodes over and over, i can just store positions 
 */

public class Node implements Comparable<Node> {

   private int parentIdentifier;
   private int identifier;
   private char actionUsed;
   private int gCost;
   private int hCost;
   private int fCost;
   private boolean isGoal;
   private int targetsFilled = 0;
   private char[][] itemsData;
   private Position player;
   private ArrayList<Position> crates;

   public Node(char[][] itemsData, MapData map, int parentIdentifier, int previousGCost, char actionUsed, GameLogic rules) {
      
      initializePositions(itemsData, map);

      this.itemsData = itemsData;
      this.parentIdentifier = parentIdentifier;
      this.identifier = rules.generateIdentifier(itemsData);
      this.actionUsed = actionUsed;
      this.gCost = previousGCost + 1;
      this.hCost = generateNodeHeuristicValue(map);
      this.fCost = this.gCost + this.hCost;
      
      if(this.targetsFilled == map.getTargetCount())
         isGoal = true;
      else 
         isGoal = false;


   }

   private void initializePositions(char[][] itemsData, MapData map) {
      
      crates = new ArrayList<>();
      
      for (int i = 0; i < map.getHeight(); i++) {
         for (int j = 0; j < map.getWidth(); j++) {

               Position position = new Position(j, i); // inversed due to nested loop logic
               char currentChar = itemsData[i][j];
               if (currentChar == '@') {
                  player = position;
               } else if (currentChar == '$') {
                  crates.add(position);

                  // checks if node is goal; all targetPosition filled with crates 
                  for(int k = 0; k < map.getTargets().size(); k++){
                     if(position.isEqual(map.getTargets().get(k)))
                        targetsFilled++;
                  }


               }

         }
      }
      
   }

   private int generateNodeHeuristicValue(MapData map) {
      int heuristicValue = 0;
      
      ArrayList<Position> crates = getCrates();
      ArrayList<Position> targets = map.getTargets();
      Position playerPosition = getPlayer();
  
      for (int i = 0; i < crates.size(); i++) {
         int crateX = crates.get(i).getX();
         int crateY = crates.get(i).getY();

         int targetX = targets.get(i).getX();
         int targetY = targets.get(i).getY();

         // distance from the player to the crate
         int playerToCrateDistance = Math.abs(playerPosition.getX() - crateX) + Math.abs(playerPosition.getY() - crateY);

         // distance from the crate to the target
         int crateToTargetDistance = Math.abs(crateX - targetX) + Math.abs(crateY - targetY);

         // Add both distances to the heuristic value
         heuristicValue += playerToCrateDistance + crateToTargetDistance;
      }
  
      return heuristicValue;
  }
   
   // generates all possible successors from movements allowed
   public ArrayList<Node> generateSuccessors(GameLogic rules, MapData map) {

      ArrayList<Node> possibleSuccessors = new ArrayList<>();
      char[] movements = { 'u', 'd', 'l', 'r' };

      for(char move : movements) {
         if (rules.isValidPlayerMovement(move, map, this) ) {
            Node updatedNode = rules.updatedNode(move, this, map);
            possibleSuccessors.add(updatedNode);
         }
      }
      return possibleSuccessors;
   }

   public boolean isGoal(){
      return this.isGoal;
   }

    @Override
   public int compareTo(Node otherNode) {
        return Integer.compare(this.fCost, otherNode.fCost);
    }

   // clones itemData for creating new nodes
   public char[][] getItemsData(MapData map) {
        char[][] cloneItemsData = new char[map.getHeight()][map.getWidth()];
        for (int i = 0; i < itemsData.length; i++) {
            System.arraycopy(itemsData[i], 0, cloneItemsData[i], 0, map.getWidth());
        }
        return cloneItemsData;
    }

   // for debugging purposes
   public void toStringInfo() {

        System.out.println("Action Used: " + actionUsed);
        System.out.print("Player: " );  player.toStringInfo();
        System.out.print(" | Crates ");

        for (int i = 0; i < crates.size(); i++) {
            System.out.print("#" + (i + 1) + ": "); 
            crates.get(i).toStringInfo();
         }

        System.out.println("\nFCost: " + this.fCost);
        System.out.println("ID: " + getIdentifier());
        System.out.println("PARENT ID: " + parentIdentifier);
        System.out.println();
    }

   // for debugging purposes
   public void toStringMap(MapData map) {
        System.out.println("\n");
        for (int i = 0; i < map.getHeight(); i++) {
            System.out.println(new String(itemsData[i]));
        }
    }

   /* GETTER METHODS */
   public int getFCost() {
        return fCost;
    }

   public int getGCost() {
        return gCost;
    }

   public int getIdentifier() {
        return identifier;
    }

   public char getActionUsed() {
        return actionUsed;
    }

   public int getParentIdentifier() {
        return parentIdentifier;
    }

   public Position getPlayer() {
        return player;
    }

   public ArrayList<Position> getCrates() {
        return crates;
    }

}
