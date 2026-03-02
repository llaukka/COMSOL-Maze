import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class MazeGeneratorVisual {
    
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("What is the hallway thickness? ");
        double hallwayThickness = scanner.nextDouble();

        System.out.print("what is the maze height? ");
        double mazeWidth = scanner.nextDouble();
        
        System.out.print("what is the maze height? ");
        double mazeHeight = scanner.nextDouble();

        scanner.nextLine();

        System.out.print("what is the seed? ");
        String seed = scanner.nextLine();
        scanner.close();
        
        System.out.println("=== MAZE GENERATOR ===");
        System.out.printf("Hallway: %.2fm, Target: %.1fx%.1fm%n", 
            hallwayThickness, mazeWidth, mazeHeight);
        
        int cellsX = (int) Math.floor(mazeWidth/hallwayThickness);
        int cellsY = (int) Math.floor(mazeHeight/hallwayThickness);
        
        if (cellsX % 2 == 0) cellsX--;
        if (cellsY % 2 == 0) cellsY--;
        
        double actualWidth = cellsX * hallwayThickness;
        double actualHeight = cellsY * hallwayThickness;
        
        System.out.printf("Grid: %d x %d cells%n", cellsX, cellsY);
        System.out.printf("Actual: %.2f x %.2f m%n", actualWidth, actualHeight);
        
        // Generate maze
        int[][] maze = mazeGenerator.createMaze(hallwayThickness, mazeWidth, mazeHeight, seed);
        
        // Launch visual window
        System.out.println("\nOpening visual preview...");
        showVisual(maze, hallwayThickness, actualWidth, actualHeight);
    }
    
    
    static void showVisual(int[][] grid, double cellSize, double totalW, double totalH) {
        JFrame frame = new JFrame("Maze Preview - " + grid[0].length + "x" + grid.length);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                
                int h = grid.length;
                int w = grid[0].length;
                
                // Scale to fit window with padding
                double scaleX = (getWidth() - 40) / (w * cellSize);
                double scaleY = (getHeight() - 60) / (h * cellSize);
                double scale = Math.min(scaleX, scaleY);
                
                int offsetX = (int) ((getWidth() - w * cellSize * scale) / 2);
                int offsetY = (int) ((getHeight() - 20 - h * cellSize * scale) / 2) + 20;
                
                // White background (paths)
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw walls (blue)
                g2d.setColor(new Color(40, 40, 100));
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        if (grid[y][x] == 0) {  // Wall
                            int px = offsetX + (int) (x * cellSize * scale);
                            int py = offsetY + (int) ((h - 1 - y) * cellSize * scale);
                            int sz = (int) (cellSize * scale);
                            g2d.fillRect(px, py, sz + 1, sz + 1);
                        }
                    }
                }
                
                // Draw entrance (green circle)
                g2d.setColor(Color.GREEN);
                int inX = offsetX;
                int inY = offsetY + (int) ((h - 2) * cellSize * scale);
                int dotSize = Math.max(6, (int)(cellSize * scale / 3));
                g2d.fillOval(inX - dotSize/2, inY + (int)(cellSize*scale/2) - dotSize/2, dotSize, dotSize);
                
                // Draw exit (red circle)
                g2d.setColor(Color.RED);
                int outX = offsetX + (int) ((w - 1) * cellSize * scale);
                int outY = offsetY + (int) (cellSize * scale);
                g2d.fillOval(outX - dotSize/2, outY + (int)(cellSize*scale/2) - dotSize/2, dotSize, dotSize);
                
                // Info text
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
                String info = String.format("Grid: %dx%d | Cell: %.2fm | Size: %.2fx%.2fm | Entrance: Green | Exit: Red", 
                    w, h, cellSize, totalW, totalH);
                g2d.drawString(info, 10, 15);
            }
        };
        
        canvas.setPreferredSize(new Dimension(800, 800));
        canvas.setBackground(Color.WHITE);
        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);  // Center on screen
        frame.setVisible(true);
    }
}