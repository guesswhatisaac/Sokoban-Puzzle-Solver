package solver;

/* Notes
 * - updatedNodes does not check if movement is valid & naively updates, since the validators do the checking instead
 */

public class GameLogic {

    private boolean[][] notVisited;
    private DeadlockDetector deadlockDetector = new DeadlockDetector();

    // for simple deadlock detection
    GameLogic(MapData map){
        notVisited = deadlockDetector.simpleDeadlockDetector(this, map);

        for(int i = 0; i < map.getHeight(); i++){
            for(int j = 0; j < map.getWidth(); j++){
                if(notVisited[j][i] == false)
                    System.out.print("f");
                else
                    System.out.print("t");
            }
            System.out.println();
        }


    }

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

        if(notVisited[playerClone.getX()][playerClone.getY()]){
            System.out.println("Deadlock detector says no. return false");
        }
        else if(mapObject == '#'){
            //System.out.print("wall detected. return false"); // DEBUG
            return false;
        } // if crate, also check if crate's movement is valid
        else if(itemObject == '$'){
            System.out.println("checking if its not a valid crate movement\n");
            if(!isValidCrateMovement(move, playerClone, mapData , itemsData)) {
                System.out.print("crate found, its movement not valid. return false"); // DEBUG
                return false;
            }
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
    
        System.out.println("valid crate movement."); // DEBUG

        return true; 

    }

    boolean isValidCrateMovement(int x, int y, char[][] mapData) {

        int cornerDeadlockCount = 0;
    
        // Print the current coordinates and mapObject
        System.out.println("Coordinates (x, y): " + x + ", " + y);
        char mapObject = mapData[x][y];
        System.out.println("Map Object: " + mapObject);

        if(mapObject == '#'){
            System.out.println("Evaluating wall, return false.");
            return false;
        }
        else if(mapObject == '.'){
            System.out.println("Evaluating target, return true");
            return true;
        }
    
        // Create and print detector objects
        Position detectorUp = new Position(y, x);
        detectorUp.updatePosition('u');
        char mapObjectUp = mapData[detectorUp.getX()][detectorUp.getY()];
        System.out.println("Map Object Up: " + mapObjectUp);
    
        Position detectorDown = new Position(y, x);
        detectorDown.updatePosition('d');
        char mapObjectDown = mapData[detectorDown.getX()][detectorDown.getY()];
        System.out.println("Map Object Down: " + mapObjectDown);
    
        Position detectorRight = new Position(y, x);
        detectorRight.updatePosition('r');
        char mapObjectRight = mapData[detectorRight.getX()][detectorRight.getY()];
        System.out.println("Map Object Right: " + mapObjectRight);
    
        Position detectorLeft = new Position(y, x);
        detectorLeft.updatePosition('l');
        char mapObjectLeft = mapData[detectorLeft.getX()][detectorLeft.getY()];
        System.out.println("Map Object Left: " + mapObjectLeft);
    
        // Crate movement is not valid if blocked by a wall or another crate

        if (mapObjectUp == '#') {
            cornerDeadlockCount++;
            System.out.println("Deadlock up");
        }
        if (mapObjectDown == '#') {
            cornerDeadlockCount++;
            System.out.println("Deadlock down");
        }
        if (mapObjectLeft == '#') {
            cornerDeadlockCount++;
            System.out.println("Deadlock left");
        }
        if (mapObjectRight == '#') {
            cornerDeadlockCount++;
            System.out.println("Deadlock right");
        }
    
        if (cornerDeadlockCount >= 2) {
            return false;
        }

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
