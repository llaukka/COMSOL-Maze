import java.util.Arrays;
import java.util.Random;


public class mazeGenerator {

    public static int[][] createMaze(double hallwayThickness, double mazeWidth, double mazeHeight, String seedString) {

        //Calculating Grid
        int cellsX = (int) Math.floor(mazeWidth/hallwayThickness);
        int cellsY = (int) Math.floor(mazeHeight/hallwayThickness);
        //for correct maze generation the amount of cells must be odd
        if (cellsX % 2 == 0) --cellsX;
        if (cellsY % 2 == 0) --cellsY;

        long seed = seedString.hashCode();
        Random random = new Random(seed);
        
         return generateMaze(cellsX, cellsY, random);

        
    }

    private static int[][] generateMaze(int width, int height, Random random) {
        //0 = wall
        //1 = path
        int[][] maze = new int[height][width];
        for (int[] row : maze) {
            Arrays.fill(row, 0);
        } //fill every row with walls

        carve(maze,1,1, random);
        maze[1][0] = 1; //Entrance is on location (1, 0)
        maze[height - 2][width - 1] = 1; //Exit is on location (height - 2, width - 1) meaning 
        return maze;
    }

    private static void carve(int[][] maze, int x, int y, Random random) { //carving a path through the maze
        //0 = wall
        //1 = path
        maze[y][x] = 1; //current cell is a path
        int[][] directions = {{0,-2}, {0, 2}, {-2, 0}, {2, 0}}; //valid directions for target cell (up, down, left, right; by 2 cells)
        shuffle(directions, random); //randomizes direction order to choose target cell
        for (int[] d : directions) { //name a direction pair as d
            int targetx = x + d[0]; //modifies the position index of current to the target position index for x
            int targety = y + d[1]; //modifies the position index of current to the target position index for y

            //this is just checking if the target cell location is outside the bounds of the maze
            if (targetx > 0 && targetx < maze[0].length-1 && targety > 0 && targety < maze.length-1 && maze[targety][targetx] == 0) {
                maze[y + d[1]/2][x + d[0]/2] = 1;
                carve(maze, targetx, targety, random);
            }
        }
    }
    private static void shuffle(int[][] a, Random random) {
        for (int i = a.length-1; i > 0; i--) {
            int j = random.nextInt(i+1);
            int[] t = a[i]; a[i] = a[j]; a[j] = t;
        }
    }


}