int[][] generateMaze(int width, int height, java.util.Random random) {
    // 0 = wall, 1 = path
    int[][] maze = new int[height][width];

    // Fill with walls manually (no Arrays.fill)
    for (int r = 0; r < height; r++) {
        for (int c = 0; c < width; c++) {
            maze[r][c] = 0;
        }
    }

    carve(maze, 1, 1, random);

    maze[1][0] = 1;                        // Entrance
    maze[height - 2][width - 1] = 1;       // Exit

    return maze;
}