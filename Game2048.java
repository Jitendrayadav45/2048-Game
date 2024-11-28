import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Game2048 extends JFrame {
    private static final int SIZE = 4;
    private int[][] board;
    private int score;
    private boolean gameOver;
    private Random random;
    private JLabel scoreLabel;
    private JPanel gamePanel;

    public Game2048() {
        setTitle("2048 Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setResizable(false);

        board = new int[SIZE][SIZE];
        random = new Random();
        score = 0;
        gameOver = false;

        // Initialize UI components
        initializeUI();
        
        // Add key listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    boolean moved = false;
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            moved = moveLeft();
                            break;
                        case KeyEvent.VK_RIGHT:
                            moved = moveRight();
                            break;
                        case KeyEvent.VK_UP:
                            moved = moveUp();
                            break;
                        case KeyEvent.VK_DOWN:
                            moved = moveDown();
                            break;
                    }
                    if (moved) {
                        addNewTile();
                        updateUI();
                        checkGameOver();
                    }
                }
            }
        });

        // Start new game
        startNewGame();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Score panel
        JPanel scorePanel = new JPanel();
        scoreLabel = new JLabel("Score: 0");
        scorePanel.add(scoreLabel);
        add(scorePanel, BorderLayout.NORTH);

        // Game panel
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(400, 400));
        add(gamePanel, BorderLayout.CENTER);
    }

    private void drawBoard(Graphics g) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int value = board[row][col];
                int x = col * 100;
                int y = row * 100;
                
                // Draw tile
                g.setColor(getTileColor(value));
                g.fillRect(x, y, 95, 95);
                
                // Draw value
                if (value != 0) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 24));
                    String text = String.valueOf(value);
                    FontMetrics fm = g.getFontMetrics();
                    int textX = x + (95 - fm.stringWidth(text)) / 2;
                    int textY = y + (95 + fm.getAscent() - fm.getDescent()) / 2;
                    g.drawString(text, textX, textY);
                }
            }
        }
    }

    private Color getTileColor(int value) {
        switch (value) {
            case 0: return Color.LIGHT_GRAY;
            case 2: return new Color(238, 228, 218);
            case 4: return new Color(237, 224, 200);
            case 8: return new Color(242, 177, 121);
            case 16: return new Color(245, 149, 99);
            case 32: return new Color(246, 124, 95);
            case 64: return new Color(246, 94, 59);
            case 128: return new Color(237, 207, 114);
            case 256: return new Color(237, 204, 97);
            case 512: return new Color(237, 200, 80);
            case 1024: return new Color(237, 197, 63);
            case 2048: return new Color(237, 194, 46);
            default: return new Color(205, 193, 180);
        }
    }

    private void startNewGame() {
        // Clear board
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 0;
            }
        }
        score = 0;
        gameOver = false;
        
        // Add initial tiles
        addNewTile();
        addNewTile();
        updateUI();
    }

    private void addNewTile() {
        int value = random.nextInt(10) < 9 ? 2 : 4;
        int row, col;
        do {
            row = random.nextInt(SIZE);
            col = random.nextInt(SIZE);
        } while (board[row][col] != 0);
        board[row][col] = value;
    }

    private boolean moveLeft() {
        boolean moved = false;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 1; col < SIZE; col++) {
                if (board[row][col] != 0) {
                    int newCol = col;
                    while (newCol > 0 && board[row][newCol - 1] == 0) {
                        board[row][newCol - 1] = board[row][newCol];
                        board[row][newCol] = 0;
                        newCol--;
                        moved = true;
                    }
                    if (newCol > 0 && board[row][newCol - 1] == board[row][newCol]) {
                        board[row][newCol - 1] *= 2;
                        score += board[row][newCol - 1];
                        board[row][newCol] = 0;
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    private boolean moveRight() {
        boolean moved = false;
        for (int row = 0; row < SIZE; row++) {
            for (int col = SIZE - 2; col >= 0; col--) {
                if (board[row][col] != 0) {
                    int newCol = col;
                    while (newCol < SIZE - 1 && board[row][newCol + 1] == 0) {
                        board[row][newCol + 1] = board[row][newCol];
                        board[row][newCol] = 0;
                        newCol++;
                        moved = true;
                    }
                    if (newCol < SIZE - 1 && board[row][newCol + 1] == board[row][newCol]) {
                        board[row][newCol + 1] *= 2;
                        score += board[row][newCol + 1];
                        board[row][newCol] = 0;
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    private boolean moveUp() {
        boolean moved = false;
        for (int col = 0; col < SIZE; col++) {
            for (int row = 1; row < SIZE; row++) {
                if (board[row][col] != 0) {
                    int newRow = row;
                    while (newRow > 0 && board[newRow - 1][col] == 0) {
                        board[newRow - 1][col] = board[newRow][col];
                        board[newRow][col] = 0;
                        newRow--;
                        moved = true;
                    }
                    if (newRow > 0 && board[newRow - 1][col] == board[newRow][col]) {
                        board[newRow - 1][col] *= 2;
                        score += board[newRow - 1][col];
                        board[newRow][col] = 0;
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    private boolean moveDown() {
        boolean moved = false;
        for (int col = 0; col < SIZE; col++) {
            for (int row = SIZE - 2; row >= 0; row--) {
                if (board[row][col] != 0) {
                    int newRow = row;
                    while (newRow < SIZE - 1 && board[newRow + 1][col] == 0) {
                        board[newRow + 1][col] = board[newRow][col];
                        board[newRow][col] = 0;
                        newRow++;
                        moved = true;
                    }
                    if (newRow < SIZE - 1 && board[newRow + 1][col] == board[newRow][col]) {
                        board[newRow + 1][col] *= 2;
                        score += board[newRow + 1][col];
                        board[newRow][col] = 0;
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    private void checkGameOver() {
        gameOver = true;
        
        // Check for empty cells
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    gameOver = false;
                    return;
                }
            }
        }
        
        // Check for possible merges
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                if (board[i][j] == board[i][j + 1] || board[j][i] == board[j + 1][i]) {
                    gameOver = false;
                    return;
                }
            }
        }
        
        if (gameOver) {
            JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + score);
        }
    }

    private void updateUI() {
        scoreLabel.setText("Score: " + score);
        gamePanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game2048());
    }
}