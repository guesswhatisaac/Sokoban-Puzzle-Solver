package solver;

/* Notes
 * - updatedNodes does not check if movement is valid & naively updates, since the validators do the checking instead
 * - all methods may not be working properly
 */

public class GameLogic {

    // checks player movement based on moveInput
    public boolean isValidPlayerMovement(char move, Position player, char[][] mapData, char[][] itemsData){

        // DEBUG
        System.out.println("Checking move " +"'"+ move +"'");
    
        Position playerClone = new Position(player.getX(), player.getY());
        playerClone.updatePosition(move);
    
        // gets object infront of the player
        char mapObject = mapData[playerClone.getX()][playerClone.getY()];
        char itemObject = itemsData[playerClone.getX()][playerClone.getY()];

        if(mapObject == '#'){
            System.out.println("wall detected. return false"); // DEBUG
            return false;
        } // if crate, also check if crate's movement is valid
        else if(itemObject == '$'){
            if (!isValidCrateMovement(move, playerClone, mapData, itemsData)) {
                System.out.println("crate found, its movement not valid. return false"); // DEBUG
                return false;
            }
        }
    
        return true;

    }
    
    private boolean isValidCrateMovement(char move, Position crate, char[][] mapData, char[][] itemsData){

        Position detector = new Position(crate.getX(), crate.getY());

        /*
        //DEBUG
        System.out.print("Crate detected position: ");
        crate.toStringInfo();
        //
        
        detector.updatePosition(move);
        
        //DEBUG
        System.out.print("detector position: ");
        detector.toStringInfo();
        //
        
        // gets object infront of the crate
        char prevmapObject = mapData[crate.getX()][crate.getY()]; // DEBUG
        char previtemObject = itemsData[crate.getX()][crate.getY()]; // DEBUG


        */

        detector.updatePosition(move);

        char mapObject = mapData[detector.getX()][detector.getY()]; 
        char itemObject = itemsData[detector.getX()][detector.getY()];

        // Crate movement is not valid if blocked by a wall or another crate
        if(mapObject == '#' || itemObject == '$'){
            return false;
        }
    
        return true; 

    }
    
    // updates map and item data based on moveInput then constructs a valid successor node 
    public Node updatedNode(char move, Node parentNode) {

        // get player's position
        Position currentPosition = new Position(parentNode.getPlayer().getX(), parentNode.getPlayer().getY());
        Position movedPosition = new Position(parentNode.getPlayer().getX(), parentNode.getPlayer().getY());

        // get object infront of player
        movedPosition.updatePosition(move);
        char itemObjectInFront = parentNode.getItemsData()[movedPosition.getX()][movedPosition.getY()];
    
        // DEBUG
        System.out.print("Parent Node Pos: ");
        parentNode.getPlayer().toStringInfo();
        System.out.println("Move: " + move);
        System.out.print("CurrentPos: ");
        currentPosition.toStringInfo();
        
        //
        //DEBUG
        System.out.print("MovedPos: ");
        movedPosition.toStringInfo();
        //

        // update itemsData
        parentNode.getItemsData()[currentPosition.getX()][currentPosition.getY()] = ' ';
        parentNode.getItemsData()[movedPosition.getX()][movedPosition.getY()] = '@';

        if (itemObjectInFront == '$') {
            // movedPosition.updatePosition(move); // DEBUG
            parentNode.getItemsData()[movedPosition.getX()][movedPosition.getY()] = '$';
        }
  
        // after updating items data, construct the node
        Node updatedNode = new Node(parentNode.getWidth(), parentNode.getHeight(), parentNode.getItemsData(), 
                            parentNode.getMapData(), parentNode.getIdentifier(), parentNode.getGCost(), move);

        return updatedNode;

    }



}
