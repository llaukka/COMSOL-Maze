
import java.util.*;

public class mazeSolver {

    // Returns the solution path as a list of [x, y] coordinates,
    // or an empty list if no solution exists.
    public static List<int[]> solve(int[][] maze) {
        int height = maze.length;
        int width  = maze[0].length;

        // Entrance and exit as defined in mazeGenerator
        int[] start = {0, 1};           // [x, y] → maze[1][0]
        int[] end   = {width - 1, height - 2}; // [x, y] → maze[height-2][width-1]

        // BFS
        Queue<int[]> queue    = new LinkedList<>();
        boolean[][] visited   = new boolean[height][width];
        int[][][] cameFrom    = new int[height][width][];  // tracks the previous cell

        queue.add(start);
        visited[start[1]][start[0]] = true;

        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // up, down, left, right

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0];
            int cy = current[1];

            // Reached the exit
            if (cx == end[0] && cy == end[1]) {
                return reconstructPath(cameFrom, start, end);
            }

            for (int[] d : directions) {
                int nx = cx + d[0];
                int ny = cy + d[1];

                if (nx >= 0 && nx < width &&
                    ny >= 0 && ny < height &&
                    !visited[ny][nx] &&
                    maze[ny][nx] == 1) {

                    visited[ny][nx] = true;
                    cameFrom[ny][nx] = current;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private static List<int[]> reconstructPath(int[][][] cameFrom, int[] start, int[] end) {
        LinkedList<int[]> path = new LinkedList<>();
        int[] current = end;

        while (current != null) {
            path.addFirst(current);
            if (current[0] == start[0] && current[1] == start[1]) break;
            current = cameFrom[current[1]][current[0]];
        }

        return path;
    }
}