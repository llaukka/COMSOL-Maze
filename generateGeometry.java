public class generateGeometry {
    public static void build(Model model, int[][] maze, double cellSize) {

        String compTag = "comp1";
        String geomTag = "geom1";

        if (!model.component().contains(compTag)) {
            model.component().create(compTag, true);
        }

        if (!model.component(compTag).geom().contains(geomTag)) {
            model.component(compTag).geom().create(geomTag, 2); //creates geometry in 2D
        }

        GeomSequence geom = model.component(compTag).geom(geomTag);
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
}