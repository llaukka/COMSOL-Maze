void main() {
    double hallwayThickness = app.form("form1").formObject("numericinput1").getDouble("value");
    double mazeWidth        = app.form("form1").formObject("numericinput2").getDouble("value");
    double mazeHeight       = app.form("form1").formObject("numericinput3").getDouble("value");
    String seed             = app.form("form1").formObject("stringinput1").getString("value");

    if (hallwayThickness <= 0 || mazeWidth <= 0 || mazeHeight <= 0) {
        error("All inputs must be greater than 0.");
    }
    if (seed == null || seed.isEmpty()) {
        seed = String.valueOf(System.currentTimeMillis());
    }

    int[][] maze = createMaze(hallwayThickness, mazeWidth, mazeHeight, seed);
    buildGeometry(maze, hallwayThickness);
}