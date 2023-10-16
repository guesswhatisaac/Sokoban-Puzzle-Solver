package solver;

/* Notes
 * - updatedNodes does not check if movement is valid, since the validators do it instead
 */

public class GameLogic {

    // checks player movement based on moveInput
    public boolean isValidPlayerMovement(char move, Position player, char[][] mapData, char[][] itemsData){
    
        Position playerClone = new Position(player.getX(), player.getY());

        playerClone.updatePosition(move);
    
        // gets object infront of the player
        char mapObject = mapData[playerClone.getX()][playerClone.getY()];
        char itemObject = itemsData[playerClone.getX()][playerClone.getY()];

        if(mapObject == '#'){
            return false;
        } // if crate, also check if crate's movement is valid
        else if(itemObject == '$'){
            if (!isValidCrateMovement(move, playerClone, mapData, itemsData)) {
                return false;
            }
        }
    
        return true;

    }
    
    private boolean isValidCrateMovement(char move, Position crate, char[][] mapData, char[][] itemsData){

        Position crateClone = new Position(crate.getX(), crate.getY());
        crateClone.updatePosition(move);

        // gets object infront of the crate
        char mapObject = mapData[crateClone.getX()][crateClone.getY()];
        char itemObject = itemsData[crateClone.getX()][crateClone.getY()];

        // Crate movement is not valid if blocked by a wall or another crate
        if(mapObject == '#' || itemObject == '$')
            return false;
    
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

        // update itemsData
        parentNode.getItemsData()[currentPosition.getX()][currentPosition.getY()] = ' ';
        parentNode.getItemsData()[movedPosition.getX()][movedPosition.getY()] = '@';

        if (itemObjectInFront == '$') {
            movedPosition.updatePosition(move);
            parentNode.getItemsData()[movedPosition.getX()][movedPosition.getY()] = '$';
        }
  
        // after updating items data, construct the node
        Node updatedNode = new Node(parentNode.getWidth(), parentNode.getHeight(), parentNode.getItemsData(), 
                            parentNode.getMapData(), parentNode.getIdentifier(), parentNode.getGCost(), move);

        return updatedNode;

    }



}
