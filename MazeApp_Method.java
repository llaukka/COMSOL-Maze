import java.util.Arrays;
import java.util.Random;
//import com.comsol.model.*

//COMSOL App Method: Maze Generator
// Requires:
//main() is linked to a button to generate maze
//setupPhysics() is linked to a button to generate physics
// - form1
// - numericinput1 (hallway thickness)
// - numericinput2 (maze width)
// - numericinput3 (maze height)
// - stringinput1  (seed)
// - component tag: comp1
// - geometry tag: geom1



//builds geometry only
void main() {

    double hallwayThickness = app.form("form1").formObject("numericinput1").getDouble("value");
    if (hallwayThickness <= 0) {
        error("inputs must be greater than 0 and have some numeric value");
    }

    double mazeWidth = app.form("form1").formObject("numericinput2").getDouble("value");
    double mazeHeight = app.form("form1").formObject("numericinput3").getDouble("value");
    if (mazeHeight <= 0 || mazeWidth <= 0) {
        error("inputs must be greater than 0 and have some numeric value");
    }

    String seed = app.form("form1").formObject("stringinput1").getString("value");
    if (seed == null || seed.isEmpty()) {
        seed = String.valueOf(System.currentTimeMillis());
    }

    int[][] maze = mazeGenerator.createMaze(hallwayThickness, mazeWidth, mazeHeight, seed);

    generateGeometry.build(model, maze, hallwayThickness);

}

//builds physical system
/* Finish later
void setupPhysics() {
    generatePhysics.setup(model);
}
*/

//Helper Class Maze Generator
class mazeGenerator {

    static int[][] createMaze(double hallwayThickness, double mazeWidth, double mazeHeight, String seedString) {

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

//Helper class for generate geometry to cleanly take references
class Config {
    static final String COMP = "comp1";
    static final String GEOM = "geom1";
}

//Helper class to create geometry from maze
//takes a double array of 0 and 1s, 1=path 0=walls.
class generateGeometry {
    static void build(Model model, int[][] maze, double cellSize) {

        ensureComponent(model);
        ensureGeometry(model);

        GeomSequence geom = model.component(Config.COMP).geom(Config.GEOM);
        geom.feature().clear();

        int featureCount = 0;

        for (int row=0; row < maze.length; row++) {
            for (int col=0; col < maze[row].length; col++) {
                if (maze[row][col] == 1) {
                    double x = col * cellSize;
                    double y = (maze.length - 1 - row) * cellSize;

                    String recTag = "r" + featureCount++;
                    geom.create(recTag, "Rectangle");

                    geom.feature(recTag).set("base", "corner");
                    geom.feature(recTag).set("pos", new double[]{x, y});
                    geom.feature(recTag).set("size", new double[]{cellSize, cellSize});
                }
            }
        }

        if (featureCount > 0) {
            geom.create("union1", "Union");

            String[] selection = new String[featureCount];

            for (int i = 0; i < featureCount; i++) {
                selection[i]="r" + i;
            }
        geom.feature("union1").selection("input").set(selection);

        geom.feature("union1").set("intbnd", false);
            
        }
        geom.run();
    }
    private static void ensureComponent(Model model) {
        if (!model.component().contains(Config.COMP)) {
            model.component().create(Config.COMP, true);
        }
    }
    private static void ensureGeometry(Model model) {
        if (!model.component(Config.COMP).geom().contains(Config.GEOM)) {
            model.component(Config.COMP).geom().create(Config.GEOM, 2); //creates geometry in 2D
        }
    }
}

//Helper class to generate the physical system in which the maze lives.
/*Finish later
class generatePhysics {
    static void setup(Model model) {
        
        ensureComponent(model);
        ensurePhysics(model);
        ensureMaterial(model);
        ensureStudy(model);
    }

    private static void ensureComponent(Model model) {
        if (!model.component().contains(Config.COMP)) {
            model.component().create(Config.COMP, true);
        }
    }
    private static void ensurePhysics(Model model) {
        if (!model.component(Config.COMP).physics().contains("acpr")) {
            model.component(Config.COMP).physics().create("acpr", "PressureAcoustics", Config.GEOM);
        }
    }

    private static void ensureMaterial(Model model) {
        if (!model.component(Config.COMP).material().contains("mat1")) {
            model.component(Config.COMP).material().create("mat1");

            model.component(Config.COMP).material("mat1").propertyGroup("def").set("density", "1.225");

            model.component(Config.COMP).material("mat1").propertyGroup("def").set("soundspeed", "343");

            model.component(Config.COMP).material("mat1").label("Air");
        }
    }

    private static void ensureStudy(Model model) {
        if (!model.study().contains("std1")) {
            model.study().create("std1");

            model.study("std1").create("freq", "Frequency");

            model.study("std1").feature("freq").set("plist", "range(100,100,2000)");
        }
    }
}
*/

