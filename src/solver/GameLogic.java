package solver;

/* Notes
 * - updatedNodes does not check if movement is valid & naively updates, since the validators do the checking instead
 * - all methods may not be working properly
 * - mapData cloned; probably not needed since its constant
 */

public class GameLogic {

    DeadlockDetector deadlockDetector = new DeadlockDetector();

    public boolean isValidPlayerMovement(char move, MapData map, Node node){

        //System.out.print("\nChecking move " +"'"+ move +"'" + " : "); // DEBUG
    
        // REVERSED X & Y
        Position playerClone = new Position(node.getPlayer().getY(), node.getPlayer().getX());
        playerClone.updatePosition(move);
    
        // gets object infront of the player
        char[][] mapData = map.getMapData();
        char[][] itemsData = node.getItemsData(map);
        char mapObject = mapData[playerClone.getX()][playerClone.getY()]; 
        char itemObject = itemsData[playerClone.getX()][playerClone.getY()];

        if(mapObject == '#'){
            //System.out.print("wall detected. return false"); // DEBUG
            return false;
        } // if crate, also check if crate's movement is valid
        else if(itemObject == '$'){
            if(!isValidCrateMovement(move, playerClone, mapData , itemsData)) {
                //System.out.print("crate found, its movement not valid. return false"); // DEBUG
                return false;
            }
            /*
            else if(deadlockDetector.isDeadlocked(node, this)){
                System.out.print("crate found, its movement creates deadlock. return false"); // DEBUG
                return false;
            }*/
        }

        //System.out.print("Move valid!");
        return true;

    }

    private boolean isValidCrateMovement(char move, Position crate, char[][] mapData, char[][] itemsData){

        // REVERSED X AND Y
        Position detector = new Position(crate.getY(), crate.getX());

        detector.updatePosition(move);

        char mapObject = mapData[detector.getX()][detector.getY()];
        char itemObject = itemsData[detector.getX()][detector.getY()];

        // Crate movement is not valid if blocked by a wall or another crate
        if(mapObject == '#' || itemObject == '$'){
            return false;
        }
    
        //System.out.print("valid crate movement. "); // DEBUG

        return true; 

    }
    
    // updates map and item data based on moveInput then constructs a valid successor node 
    public Node updatedNode(char move, Node parentNode, MapData map) {

        // clone itemData & mapData
        char[][] clonedItemData = parentNode.getItemsData(map);

        // get player's position // REVERSED x and y
        Position currentPosition = new Position(parentNode.getPlayer().getY(), parentNode.getPlayer().getX());
        Position movedPosition = new Position(parentNode.getPlayer().getY(), parentNode.getPlayer().getX());

        // get object infront of player
        movedPosition.updatePosition(move);
        char itemObjectInFront = clonedItemData[movedPosition.getX()][movedPosition.getY()];
    
        // update itemsData
        clonedItemData[currentPosition.getX()][currentPosition.getY()] = ' ';
        clonedItemData[movedPosition.getX()][movedPosition.getY()] = '@'; 

        if (itemObjectInFront == '$') {
            movedPosition.updatePosition(move); // DEBUG
            clonedItemData[movedPosition.getX()][movedPosition.getY()] = '$'; 
        }

        // after updating items data, construct the node
        Node updatedNode = new Node(clonedItemData, map, parentNode.getIdentifier(), parentNode.getGCost(), move, this);

        return updatedNode;

    }

    public int generateIdentifier(char[][] itemsData) {
        StringBuilder flattenedString = new StringBuilder();
        for (char[] row : itemsData) {
           flattenedString.append(row);
        }
        String hash = flattenedString.toString();
        return hash.hashCode();
     }

}
