package com.duyhung.gamecaro;

import java.util.*;

public class CaroAI {

    private static final int MAX_DEPTH = 1;   // Có thể chỉnh xuống 3 nếu lag
    private static final int NEIGHBOR_RANGE = 3;

    public static int[] getBestMove(CaroBoard board, int aiPlayer, int moveCount) {
        int maxDepth = getAdaptiveDepth(moveCount);

        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        List<int[]> candidateMoves = getCandidateMoves(board);

        for (int[] move : candidateMoves) {
            CaroBoard newBoard = board.cloneBoard();
            newBoard.setMove(move[0], move[1], aiPlayer);
            int score = minimax(newBoard, 1, false, aiPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE, maxDepth);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    public static int[] getEasyMove(CaroBoard board) {
        List<int[]> emptyCells = board.getEmptyCells();
        if (emptyCells.isEmpty()) return null;
        Random random = new Random();
        return emptyCells.get(random.nextInt(emptyCells.size()));
    }

    public static int[] getMediumMove(CaroBoard board, int aiPlayer) {
        int opponent = (aiPlayer == CaroBoard.PLAYER_X) ? CaroBoard.PLAYER_O : CaroBoard.PLAYER_X;

        // 1. Thắng ngay nếu có thể
        int[] winMove = findWinningMove(board, aiPlayer);
        if (winMove != null) {
            return winMove;
        }

        // 2. Chặn đối thủ thắng ngay
        int[] blockMove = findWinningMove(board, opponent);
        if (blockMove != null) {
            return blockMove;
        }

        // 3. Tạo hai hướng thắng tiềm năng
        int[] doubleThreatMove = findDoubleThreat(board, aiPlayer);
        if (doubleThreatMove != null) {
            return doubleThreatMove;
        }

        // 4. Chặn hàng 3 hoặc 4 quân mở của đối thủ
        int[] blockOpenLine = findOpenLineBlock(board, opponent);
        if (blockOpenLine != null) {
            return blockOpenLine;
        }

        // 5. Chọn nước đi đầu tiên không phải góc
        List<int[]> emptyCells = board.getEmptyCells();
        List<int[]> nonCornerCells = new ArrayList<>();

        // Lọc các ô không phải góc
        for (int[] cell : emptyCells) {
            int r = cell[0];
            int c = cell[1];
            if (!(r == 0 && c == 0) && !(r == 0 && c == CaroBoard.SIZE - 1) &&
                    !(r == CaroBoard.SIZE - 1 && c == 0) && !(r == CaroBoard.SIZE - 1 && c == CaroBoard.SIZE - 1)) {
                nonCornerCells.add(cell);
            }
        }

        // Nếu có ô không phải góc, chọn ngẫu nhiên từ đó
        if (!nonCornerCells.isEmpty()) {
            Random random = new Random();
            return nonCornerCells.get(random.nextInt(nonCornerCells.size()));
        } else {
            // Nếu không còn lựa chọn nào khác ngoài góc, chọn ô bất kỳ
            Random random = new Random();
            return emptyCells.get(random.nextInt(emptyCells.size()));
        }
    }

    // Tìm nước đi thắng ngay
    private static int[] findWinningMove(CaroBoard board, int player) {
        List<int[]> emptyCells = board.getEmptyCells();
        for (int[] move : emptyCells) {
            CaroBoard tempBoard = board.cloneBoard();
            tempBoard.setMove(move[0], move[1], player);
            if (tempBoard.checkWin(player)) {
                return move;
            }
        }
        return null;
    }

    // Tìm nước đi tạo hai hướng thắng tiềm năng
    private static int[] findDoubleThreat(CaroBoard board, int aiPlayer) {
        List<int[]> emptyCells = board.getEmptyCells();
        for (int[] move : emptyCells) {
            CaroBoard tempBoard = board.cloneBoard();
            tempBoard.setMove(move[0], move[1], aiPlayer);
            int threatCount = 0;
            for (int[] nextMove : tempBoard.getEmptyCells()) {
                CaroBoard nextBoard = tempBoard.cloneBoard();
                nextBoard.setMove(nextMove[0], nextMove[1], aiPlayer);
                if (nextBoard.checkWin(aiPlayer)) {
                    threatCount++;
                }
                if (threatCount >= 2) {
                    return move; // Tạo được ít nhất 2 hướng thắng
                }
            }
        }
        return null;
    }

    // Chặn hàng 3 hoặc 4 quân mở của đối thủ
    private static int[] findOpenLineBlock(CaroBoard board, int opponent) {
        List<int[]> emptyCells = board.getEmptyCells();
        for (int[] move : emptyCells) {
            CaroBoard tempBoard = board.cloneBoard();
            tempBoard.setMove(move[0], move[1], opponent);
            if (countOpenLines(tempBoard, opponent, 4) > 0 || countOpenLines(tempBoard, opponent, 3) > 0) {
                return move; // Chặn nước đi tạo hàng 3 hoặc 4 mở
            }
        }
        return null;
    }

    // Đếm số hàng mở (3 hoặc 4 quân) của một người chơi
    private static int countOpenLines(CaroBoard board, int player, int length) {
        int count = 0;
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}}; // Ngang, dọc, chéo
        int size = board.getSize();
        int[][] grid = board.getBoard();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != 0) continue;
                for (int[] dir : directions) {
                    int consecutive = countConsecutive(board, i, j, dir, player, length);
                    if (consecutive == length) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // Đếm số quân liên tiếp trong một hướng
    private static int countConsecutive(CaroBoard board, int row, int col, int[] dir, int player, int target) {
        int count = 0;
        int size = board.getSize();
        int[][] grid = board.getBoard();

        for (int step = -target; step <= target; step++) {
            int r = row + step * dir[0];
            int c = col + step * dir[1];
            if (r >= 0 && r < size && c >= 0 && c < size && grid[r][c] == player) {
                count++;
            } else if (count >= target) {
                break;
            } else {
                count = 0;
            }
        }
        return count;
    }

    // Tìm nước đi tốt nhất dựa trên đánh giá
    private static int[] findBestMove(CaroBoard board, int aiPlayer, int opponent) {
        List<int[]> emptyCells = board.getEmptyCells();
        int[] bestMove = null;
        int bestScore = -1;

        for (int[] move : emptyCells) {
            CaroBoard tempBoard = board.cloneBoard();
            tempBoard.setMove(move[0], move[1], aiPlayer);
            int score = evaluateBoard(tempBoard, aiPlayer, opponent);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    // Đánh giá bảng dựa trên số quân liên tiếp
    private static int evaluateBoard(CaroBoard board, int aiPlayer, int opponent) {
        int aiScore = 0;
        int opponentScore = 0;
        int size = board.getSize();
        int[][] grid = board.getBoard();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == aiPlayer) {
                    aiScore += evaluatePosition(board, i, j, aiPlayer);
                } else if (grid[i][j] == opponent) {
                    opponentScore += evaluatePosition(board, i, j, opponent);
                }
            }
        }
        return aiScore - opponentScore; // Tối ưu cho máy, giảm lợi thế đối thủ
    }

    // Đánh giá một vị trí dựa trên số quân liên tiếp
    private static int evaluatePosition(CaroBoard board, int row, int col, int player) {
        int score = 0;
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int count = countConsecutive(board, row, col, dir, player, 4);
            if (count >= 4) score += 100; // Hàng 4 rất giá trị
            else if (count == 3) score += 10; // Hàng 3
            else if (count == 2) score += 2; // Hàng 2
        }
        return score;
    }

    private static int getAdaptiveDepth(int moveCount) {
        if (moveCount < 20) return MAX_DEPTH;
        else if (moveCount < 40) return 3;
        else return 2;
    }

    private static int minimax(CaroBoard board, int depth, boolean isMaximizing, int aiPlayer,
                               int alpha, int beta, int maxDepth) {
        int opponent = (aiPlayer == CaroBoard.PLAYER_X) ? CaroBoard.PLAYER_O : CaroBoard.PLAYER_X;

        if (board.checkWin(aiPlayer)) return 1000000 - depth * 1000;
        if (board.checkWin(opponent)) return -1000000 + depth * 1000;
        if (depth >= maxDepth || board.getEmptyCells().isEmpty()) {
            return heuristic(board, aiPlayer, opponent);
        }

        List<int[]> candidateMoves = getCandidateMoves(board);

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : candidateMoves) {
                CaroBoard newBoard = board.cloneBoard();
                newBoard.setMove(move[0], move[1], aiPlayer);
                int eval = minimax(newBoard, depth + 1, false, aiPlayer, alpha, beta, maxDepth);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] move : candidateMoves) {
                CaroBoard newBoard = board.cloneBoard();
                newBoard.setMove(move[0], move[1], opponent);
                int eval = minimax(newBoard, depth + 1, true, aiPlayer, alpha, beta, maxDepth);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    private static List<int[]> getCandidateMoves(CaroBoard board) {
        Set<String> visited = new HashSet<>();
        List<int[]> candidates = new ArrayList<>();

        for (int r = 0; r < CaroBoard.SIZE; r++) {
            for (int c = 0; c < CaroBoard.SIZE; c++) {
                if (board.getCell(r, c) != CaroBoard.EMPTY) {
                    for (int i = Math.max(0, r - NEIGHBOR_RANGE); i <= Math.min(CaroBoard.SIZE - 1, r + NEIGHBOR_RANGE); i++) {
                        for (int j = Math.max(0, c - NEIGHBOR_RANGE); j <= Math.min(CaroBoard.SIZE - 1, c + NEIGHBOR_RANGE); j++) {
                            if (board.getCell(i, j) == CaroBoard.EMPTY) {
                                String key = i + "," + j;
                                if (!visited.contains(key)) {
                                    candidates.add(new int[]{i, j});
                                    visited.add(key);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (candidates.isEmpty()) {
            candidates.add(new int[]{CaroBoard.SIZE / 2, CaroBoard.SIZE / 2});
        }

        return candidates;
    }

    // Hàm heuristic đánh giá trạng thái bàn, ưu tiên chặn và tấn công
    private static int heuristic(CaroBoard board, int aiPlayer, int opponent) {
        int aiScore = evaluateBoard(board, aiPlayer, false);
        int opponentScore = evaluateBoard(board, opponent, true);
        return aiScore - opponentScore * 5; // Nhân 5 để ưu tiên chặn
    }

    private static int evaluateBoard(CaroBoard board, int player, boolean isOpponent) {
        int score = 0;
        int[][] directions = {{0,1}, {1,0}, {1,1}, {1,-1}};

        for (int r = 0; r < CaroBoard.SIZE; r++) {
            for (int c = 0; c < CaroBoard.SIZE; c++) {
                if (board.getCell(r, c) == player) {
                    for (int[] dir : directions) {
                        int count = countSequence(board, r, c, dir[0], dir[1], player);
                        boolean openStart = isOpen(board, r - dir[0], c - dir[1]);
                        boolean openEnd = isOpen(board, r + count * dir[0], c + count * dir[1]);
                        score += getScoreForCount(count, openStart, openEnd, isOpponent);
                    }
                }
            }
        }
        return score;
    }

    // Đếm số quân liên tiếp theo hướng (dx, dy)
    private static int countSequence(CaroBoard board, int r, int c, int dx, int dy, int player) {
        int count = 0;
        int x = r;
        int y = c;
        while (x >= 0 && x < CaroBoard.SIZE && y >= 0 && y < CaroBoard.SIZE && board.getCell(x, y) == player) {
            count++;
            x += dx;
            y += dy;
        }
        return count;
    }

    // Kiểm tra vị trí có mở (EMPTY) hay không (dùng cho open-ended sequence)
    private static boolean isOpen(CaroBoard board, int r, int c) {
        if (r < 0 || r >= CaroBoard.SIZE || c < 0 || c >= CaroBoard.SIZE) return false;
        return board.getCell(r, c) == CaroBoard.EMPTY;
    }

    // Tính điểm theo độ dài chuỗi và trạng thái 2 đầu chuỗi
    private static int getScoreForCount(int count, boolean openStart, boolean openEnd, boolean isOpponent) {
        if (count >= 5) return 1000000;

        int baseScore = 0;

        if (openStart && openEnd) { // chuỗi mở 2 đầu - rất mạnh
            switch (count) {
                case 4: baseScore = 100000; break;
                case 3: baseScore = 10000; break;
                case 2: baseScore = 1000; break;
                case 1: baseScore = 100; break;
            }
        } else if (openStart || openEnd) { // chuỗi mở 1 đầu
            switch (count) {
                case 4: baseScore = 10000; break;
                case 3: baseScore = 1000; break;
                case 2: baseScore = 100; break;
                case 1: baseScore = 10; break;
            }
        } else { // chuỗi đóng (2 đầu bị chặn)
            switch (count) {
                case 4: baseScore = 1000; break;
                case 3: baseScore = 100; break;
                case 2: baseScore = 10; break;
                case 1: baseScore = 1; break;
            }
        }

        return isOpponent ? baseScore * 5 : baseScore;
    }
}