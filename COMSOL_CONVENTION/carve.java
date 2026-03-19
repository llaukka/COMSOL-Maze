void carve(int[][] maze, int x, int y, java.util.Random random) {
    //0=wall, 1=path
    maze[y][x] = 1; //current cell is path

    int[][] directions = {{0, -2}, {0, 2}, {-2, 0}, {2, 0}};
    shuffleDirections(directions, random);

    for (int i = 0; i < directions.length; i++) {
        int tx = x + directions[i][0];
        int ty = y + directions[i][1];

        if (tx > 0 && tx < maze[0].length - 1 &&
            ty > 0 && ty < maze.length - 1 &&
            maze[ty][tx] == 0) {

            maze[y + directions[i][1] / 2][x + directions[i][0] / 2] = 1;
            carve(maze, tx, ty, random);
        }
    }
}