int[][] createMaze(double hallwayThickness, double mazeWidth, double mazeHeight, String seedString) {
    
    //Calculating Grid
    int cellsX = (int) Math.floor(mazeWidth / hallwayThickness);
    int cellsY = (int) Math.floor(mazeHeight / hallwayThickness);

    // Maze generation requires odd cell counts
    if (cellsX % 2 == 0) --cellsX;
    if (cellsY % 2 == 0) --cellsY;

    long seed = seedString.hashCode();
    java.util.Random random = new java.util.Random(seed);

    return generateMaze(cellsX, cellsY, random);
}