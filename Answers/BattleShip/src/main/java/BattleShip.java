import java.util.Random;
import java.util.Scanner;

/**
 * The BattleShip class manages the gameplay of the Battleship game between two
 * players.
 * It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
     * The main method that runs the game loop.
     * It initializes the grids for both players, places ships randomly, and manages
     * turns.
     * The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;
        String p1g = "";
        String p2g = "";

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid, p1g, 1);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid, p2g, 2);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    /**
     * Initializes the grid by filling it with water ('~').
     * 
     * @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid) {
        // done
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[i][j] = '~';
            }
        }
    }

    /**
     * Places ships randomly on the given grid.
     * This method is called for both players to place their ships on their
     * respective grids.
     * 
     * @param grid The grid where ships need to be placed.
     */
    static void placeShips(char[][] grid) {
        Random intrand = new Random();
        for (int size = 2; size <= 5; size++) {
            boolean horizontal = intrand.nextBoolean();
            int row, col;
            do {
                row = intrand.nextInt(GRID_SIZE);
                col = intrand.nextInt(GRID_SIZE);
            } while (!canPlaceShip(grid, row, col, size, horizontal));

            for (int i = 0; i < size; i++) {
                if (horizontal) {
                    grid[row][col + i] = 'S';
                } else {
                    grid[row + i][col] = 'S';
                }
            }
        }
    }

    /**
     * Checks if a ship can be placed at the specified location on the grid.
     * This includes checking the size of the ship, its direction (horizontal or
     * vertical),
     * and if there's enough space to place it.
     * 
     * @param grid       The grid where the ship is to be placed.
     * @param row        The starting row for the ship.
     * @param col        The starting column for the ship.
     * @param size       The size of the ship.
     * @param horizontal The direction of the ship (horizontal or vertical).
     * @return true if the ship can be placed at the specified location, false
     *         otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > GRID_SIZE)
                return false;
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] != '~')
                    return false;
            }
        } else {
            if (row + size > GRID_SIZE)
                return false;
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] != '~')
                    return false;
            }
        }
        return true;
    }

    /**
     * Manages a player's turn, allowing them to attack the opponent's grid
     * and updates their tracking grid with hits or misses.
     * 
     * @param opponentGrid The opponent's grid to attack.
     * @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid, String give, int player) {
        System.out.println("Enter a cell to fire at (e.g., A5):");
        boolean played = false;
        do {
            String cell = scanner.next().toUpperCase();
            if (isValidInput(cell)) {
                int row = cell.charAt(0) - 'A';
                int col = cell.charAt(1) - '1';
                if (trackingGrid[row][col] == '~') {
                    played = true;
                    if (opponentGrid[row][col] == 'S') {
                        trackingGrid[row][col] = '*';
                        opponentGrid[row][col] = '~';
                        System.out.println("Hit!");
                        if (allShipsSunk(opponentGrid)) {
                            System.out.println("Player " + player + " wins!");
                            return;
                        }
                    } else {
                        trackingGrid[row][col] = '!';
                        System.out.println("Miss!");
                    }
                } else {
                    System.out.println("This cell has already been attacked. Please enter another one!");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid cell (e.g., A5):");
            }
        } while (!played);
    }

    /**
     * Checks if the game is over by verifying if all ships are sunk.
     * 
     * @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        return allShipsSunk(player1Grid) || allShipsSunk(player2Grid);
    }

    /**
     * Checks if all ships have been destroyed on a given grid.
     * 
     * @param grid The grid to check for destroyed ships.
     * @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        // done
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (grid[i][j] == 'S')
                    return false;
            }
        }
        return true;
    }

    /**
     * Validates if the user input is in the correct format (e.g., A5).
     * 
     * @param input The input string to validate.
     * @return true if the input is in the correct format, false otherwise.
     */
    static boolean isValidInput(String input) {
        // done
        char ch0 = input.charAt(0);
        char ch1 = input.charAt(1);

        if (ch0 <= 74 && ch0 >= 65 && ch1 <= 58 && ch1 >= 49) {
            return true;
        }
        return false;
    }

    /**
     * Prints the current state of the player's tracking grid.
     * This method displays the grid, showing hits, misses, and untried locations.
     * 
     * @param grid The tracking grid to print.
     */
    static void printGrid(char[][] grid) {
        System.out.print("   ");
        for (int i = 1; i <= GRID_SIZE; i++) {
            System.out.print((char) (i + 'A' - 1) + " ");
        }
        System.out.println();
        for (int i = 0; i < GRID_SIZE; i++) {
            if (i == GRID_SIZE - 1) {
                System.out.print((i + 1) + " ");
                for (int j = 0; j < GRID_SIZE; j++) {
                    System.out.print(grid[i][j] + " ");
                }
                System.out.println();
            } else {
                System.out.print((i + 1) + "  ");
                for (int j = 0; j < GRID_SIZE; j++) {
                    System.out.print(grid[i][j] + " ");
                }
                System.out.println();

            }
        }
    }
}
