package solver;

public class SokoBot {

  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {

    Search search = new Search();
    String path = search.executeSearch(width, height, mapData, itemsData);

    return path; 
  }

}
