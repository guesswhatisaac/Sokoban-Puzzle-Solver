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

        for(int i = 0; i < map.getHeight(); i++){
            for(int j = 0; j < map.getWidth(); j++){
                System.out.print(map.getMapData()[i][j]);
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

        if(mapObject == '#'){
            //System.out.print("wall detected. return false"); // DEBUG
            return false;
        }  // if crate, also check if crate's movement is valid
        else if(itemObject == '$'){
            //System.out.println("checking if its not a valid crate movement\n");
            if(!isValidCrateMovement(move, playerClone, mapData , itemsData)) {
                //System.out.print("crate found, its movement not valid. return false"); // DEBUG
                return false;
            }
        }

        //System.out.print("Move valid!");
        return true;

    }

    private boolean isValidCrateMovement(char move, Position crate, char[][] mapData, char[][] itemsData) {

        // REVERSED X AND Y
        Position detector = new Position(crate.getY(), crate.getX());
    
        detector.updatePosition(move);
    
        char mapObject = mapData[detector.getX()][detector.getY()];
        char itemObject = itemsData[detector.getX()][detector.getY()];
    
        // Crate movement is not valid if blocked by a wall or another crate
        if (mapObject == '#') {
            //System.out.println("Blocked by a wall. Return false.");
            return false;
        } else if (itemObject == '$') {
            //System.out.println("Blocked by another crate. Return false.");
            return false;
        } else if (!notVisited[detector.getY()][detector.getX()]) { // add do not push box into deadlock unless it has target
            //System.out.println("Do not push box to (" + detector.getY() + ", " + detector.getX() + "). Return false.");
            return false;
        }
    
        //sSystem.out.println("Valid crate movement."); // DEBUG
    
        return true;
    }
    
    public boolean isValidCrateMovement(int x, int y, MapData map) {

        int cornerDeadlockCount = 0;
    
        // out of bounds, invalid
        if ((x <= 0) || (y <= 0) || (y >= (map.getHeight()-1)) || ((x >= map.getWidth()-1)))
            return false;

        //System.out.println("Coordinates (x, y): " + x + ", " + y);
        char mapObject = map.getMapData()[y][x];
        //System.out.println("Map Object: " + mapObject);

        if(mapObject == '#')
            return false;
        
           
        else if(mapObject == '.')
            return true;
        
    
        // Create and print detector objects
        Position detectorUp = new Position(x, y);
        detectorUp.updatePosition('u');
        char mapObjectUp = map.getMapData()[detectorUp.getX()][detectorUp.getY()];
        //System.out.println("Map Object Up: " + mapObjectUp);
    
        Position detectorDown = new Position(x, y);
        detectorDown.updatePosition('d');
        char mapObjectDown = map.getMapData()[detectorDown.getX()][detectorDown.getY()];
        //System.out.println("Map Object Down: " + mapObjectDown);
    
        Position detectorRight = new Position(x, y);
        detectorRight.updatePosition('r');
        char mapObjectRight = map.getMapData()[detectorRight.getX()][detectorRight.getY()];
        //System.out.println("Map Object Right: " + mapObjectRight);
    
        Position detectorLeft = new Position(x, y);
        detectorLeft.updatePosition('l');
        char mapObjectLeft = map.getMapData()[detectorLeft.getX()][detectorLeft.getY()];
        //System.out.println("Map Object Left: " + mapObjectLeft);

        
        boolean isTunnel = checkTunnel(x, y, map);
        if (isTunnel) 
            return true;
    
        // Crate movement is not valid if blocked by a wall or another crate

        if (mapObjectUp == '#') {
            cornerDeadlockCount++;
        }

        if (mapObjectDown == '#') {
            cornerDeadlockCount++;
        }

        if (mapObjectLeft == '#') {
            cornerDeadlockCount++;
        }

        if (mapObjectRight == '#') {
            cornerDeadlockCount++;
        }

    
        if (cornerDeadlockCount >= 2) 
            return false;
    

        return true;
    }

    public boolean checkTunnel(int x, int y, MapData map) {

        // does not work
        if ((x <= 0) || (y <= 0) || (y >= (map.getHeight()-1)) || ((x >= map.getWidth()-1))){
            //System.out.println("Evaluating out of bounds. Return false.");
            return false;
        }
        // Create and print detector objects
        Position detectorUp = new Position(x, y);
        detectorUp.updatePosition('u');
        char mapObjectUp = map.getMapData()[detectorUp.getX()][detectorUp.getY()];
        //System.out.println("Map Object Up: " + mapObjectUp);
    
        Position detectorDown = new Position(x, y);
        detectorDown.updatePosition('d');
        char mapObjectDown = map.getMapData()[detectorDown.getX()][detectorDown.getY()];
        //System.out.println("Map Object Down: " + mapObjectDown);
    
        Position detectorRight = new Position(x, y);
        detectorRight.updatePosition('r');
        char mapObjectRight = map.getMapData()[detectorRight.getX()][detectorRight.getY()];
        //System.out.println("Map Object Right: " + mapObjectRight);
    
        Position detectorLeft = new Position(x, y);
        detectorLeft.updatePosition('l');
        char mapObjectLeft = map.getMapData()[detectorLeft.getX()][detectorLeft.getY()];
        //System.out.println("Map Object Left: " + mapObjectLeft);

        // checks tunnels to the left
        if (mapObjectDown == '#' && mapObjectUp == '#' && mapObjectLeft == ' ') {
            System.out.println("Checking for tunnel");
            while (mapObjectLeft != '#') {
                detectorLeft.updatePosition('l');
                System.out.println("moving left");
                mapObjectLeft = map.getMapData()[detectorLeft.getX()][detectorLeft.getY()];
            }   
            Position currentPosition = new Position(detectorLeft.getY() + 1, detectorLeft.getX());

            detectorDown = currentPosition;
            detectorDown.updatePosition('d');
            mapObjectDown = map.getMapData()[detectorDown.getX()][detectorDown.getY()];

            detectorUp = currentPosition;
            detectorUp.updatePosition('u');
            mapObjectUp = map.getMapData()[detectorUp.getX()][detectorUp.getY()];

            System.out.println("coordinates (x, y): " + detectorRight.getX() + ", " + detectorRight.getY() + " right: " + mapObjectRight);
            System.out.println("coordinates (x, y): " + detectorLeft.getX() + ", " + detectorLeft.getY() + " left: " + mapObjectLeft);
            System.out.println("coordinates (x, y): " + detectorDown.getX() + ", " + detectorDown.getY() + " down: " + mapObjectDown);
            System.out.println("coordinates (x, y): " + detectorRight.getX() + ", " + detectorRight.getY() + " up: " + mapObjectUp);

            if (mapObjectDown != '#' || mapObjectUp != '#'){
                System.out.println("tunnel found");
                return true;
            }
        }

        else if (mapObjectDown == '#' && mapObjectUp == '#' && mapObjectRight == ' ') {
            System.out.println("Checking for tunnel");
            while (mapObjectRight != '#') {
                detectorRight.updatePosition('r');
                System.out.println("moving right");
                mapObjectRight = map.getMapData()[detectorRight.getX()][detectorRight.getY()];
            }   
            Position currentPosition = new Position(detectorRight.getY() - 1, detectorRight.getX());

            detectorDown = currentPosition;
            detectorDown.updatePosition('d');
            mapObjectDown = map.getMapData()[detectorDown.getX()][detectorDown.getY()];

            detectorUp = currentPosition;
            detectorUp.updatePosition('u');
            mapObjectUp = map.getMapData()[detectorUp.getX()][detectorUp.getY()];

           
            System.out.println("coordinates (x, y): " + detectorRight.getX() + ", " + detectorRight.getY() + " right: " + mapObjectRight);
            System.out.println("coordinates (x, y): " + detectorLeft.getX() + ", " + detectorLeft.getY() + " left: " + mapObjectLeft);
            System.out.println("coordinates (x, y): " + detectorDown.getX() + ", " + detectorDown.getY() + " down: " + mapObjectDown);
            System.out.println("coordinates (x, y): " + detectorRight.getX() + ", " + detectorRight.getY() + " up: " + mapObjectUp);
        
            
            if (mapObjectDown != '#' || mapObjectUp != '#') {
                System.out.println("tunnel found");
                return true;
            }
        }

        else if (mapObjectLeft == '#' && mapObjectRight == '#' && mapObjectDown == ' ') {
            System.out.println("Checking for tunnel");
            while (mapObjectDown != '#') {
                detectorDown.updatePosition('d');
                System.out.println("moving down");
                mapObjectDown = map.getMapData()[detectorDown.getX()][detectorDown.getY()];
            }   
            Position currentPosition = new Position(detectorDown.getY(), detectorDown.getX() - 1);

            detectorRight = currentPosition;
            detectorRight.updatePosition('r');

            detectorLeft = currentPosition;
            detectorLeft.updatePosition('l');
            
            System.out.println("coordinates (x, y): " + detectorRight.getX() + ", " + detectorRight.getY() + " right: " + mapObjectRight);
            System.out.println("coordinates (x, y): " + detectorLeft.getX() + ", " + detectorLeft.getY() + " left: " + mapObjectLeft);
            System.out.println("coordinates (x, y): " + detectorDown.getX() + ", " + detectorDown.getY() + " down: " + mapObjectDown);
            System.out.println("coordinates (x, y): " + detectorRight.getX() + ", " + detectorRight.getY() + " up: " + mapObjectUp);
            
            
            mapObjectRight = map.getMapData()[detectorRight.getX()][detectorRight.getY()];
            mapObjectLeft = map.getMapData()[detectorLeft.getX()][detectorLeft.getY()];

            if (mapObjectRight != '#' || mapObjectLeft != '#'){
                System.out.println("tunnel found");
                return true;
            }
        }

        else if (mapObjectLeft == '#' && mapObjectRight == '#' && mapObjectUp == ' ') {
            System.out.println("Checking for tunnel");
            while (mapObjectUp != '#') {
                detectorUp.updatePosition('u');
                System.out.println("moving up");
                mapObjectUp = map.getMapData()[detectorUp.getX()][detectorUp.getY()];
            }   
            Position currentPosition = new Position(detectorUp.getY(), detectorUp.getX() + 1);

            detectorRight = currentPosition;
            detectorRight.updatePosition('r');

            if (detectorRight.getX() < map.getWidth())
                mapObjectRight = map.getMapData()[detectorRight.getX()][detectorRight.getY()];

            detectorLeft = currentPosition;
            detectorLeft.updatePosition('l');
            mapObjectLeft = map.getMapData()[detectorLeft.getX()][detectorLeft.getY()];

            System.out.println("coordinates (x, y): " + detectorRight.getX() + ", " + detectorRight.getY() + " right: " + mapObjectRight);
            System.out.println("coordinates (x, y): " + detectorLeft.getX() + ", " + detectorLeft.getY() + " left: " + mapObjectLeft);
            System.out.println("coordinates (x, y): " + detectorDown.getX() + ", " + detectorDown.getY() + " down: " + mapObjectDown);
            System.out.println("coordinates (x, y): " + detectorRight.getX() + ", " + detectorRight.getY() + " up: " + mapObjectUp);

            if (mapObjectRight != '#' || mapObjectLeft != '#'){
                System.out.println("tunnel found");
                return true;
            }
        }

        return false;
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
