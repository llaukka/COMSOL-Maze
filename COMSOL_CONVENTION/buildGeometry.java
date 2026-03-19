void buildGeometry(int[][] maze, double cellSize) {
    String comp = "comp1";
    String geom = "geom1";

    // Ensure component exists
    if (!model.component().contains(comp)) {
        model.component().create(comp, true);
    }

    // Ensure geometry exists
    if (!model.component(comp).geom().contains(geom)) {
        model.component(comp).geom().create(geom, 2);
    }

    // Clear existing geometry features
    model.component(comp).geom(geom).feature().clear();

    int featureCount = 0;

    for (int row = 0; row < maze.length; row++) {
        for (int col = 0; col < maze[row].length; col++) {
            if (maze[row][col] == 1) {
                double x = col * cellSize;
                double y = (maze.length - 1 - row) * cellSize;

                String tag = "r" + featureCount++;
                model.component(comp).geom(geom).create(tag, "Rectangle");
                model.component(comp).geom(geom).feature(tag).set("base", "corner");
                model.component(comp).geom(geom).feature(tag).set("pos", new double[]{x, y});
                model.component(comp).geom(geom).feature(tag).set("size", new double[]{cellSize, cellSize});
            }
        }
    }

    // Union all path rectangles into one solid
    if (featureCount > 0) {
        String[] selection = new String[featureCount];
        for (int i = 0; i < featureCount; i++) {
            selection[i] = "r" + i;
        }

        model.component(comp).geom(geom).create("union1", "Union");
        model.component(comp).geom(geom).feature("union1").selection("input").set(selection);
        model.component(comp).geom(geom).feature("union1").set("intbnd", false);
    }

    model.component(comp).geom(geom).run();
}