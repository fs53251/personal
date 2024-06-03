#include "game.h"
#include "mpi_utils.h"
#include <string.h>
#include <stdio.h>

void initialize_board(GameBoard* board) {
    memset(board->board, 0, sizeof(board->board));
    memset(board->column_heights, 0, sizeof(board->column_heights));
}

void print_board(GameBoard* board) {
    for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
        for (int col = 0; col < BOARD_WIDTH; col++) {
            char c = '-';
            if (board->board[col][row] == 1) {
                c = 'P';
            } else if (board->board[col][row] == 2) {
                c = 'C';
            }
            printf("%c", c);
        }
        printf("\n");
    }
    printf("\n");
}

int is_final_move(GameBoard* board, int last_column) {
    int last_row = board->column_heights[last_column] - 1;
    int player = board->board[last_column][last_row];
    int directions[4][2] = { {1, 0}, {0, 1}, {1, 1}, {1, -1} };

    for (int i = 0; i < 4; i++) {
        int count = 1;

        for (int j = 1; j < 4; j++) {
            int new_col = last_column + directions[i][0] * j;
            int new_row = last_row + directions[i][1] * j;
            if (new_col >= 0 && new_col < BOARD_WIDTH && new_row >= 0 && new_row < BOARD_HEIGHT &&
                board->board[new_col][new_row] == player) {
                count++;
            } else {
                break;
            }
        }

        for (int j = 1; j < 4; j++) {
            int new_col = last_column - directions[i][0] * j;
            int new_row = last_row - directions[i][1] * j;
            if (new_col >= 0 && new_col < BOARD_WIDTH && new_row >= 0 && new_row < BOARD_HEIGHT &&
                board->board[new_col][new_row] == player) {
                count++;
            } else {
                break;
            }
        }

        if (count >= 4) {
            return player;
        }
    }

    return 0;
}

double evaluate_board(GameBoard* board, int last_column, int depth, int max_depth, int player) {
    int winner = is_final_move(board, last_column);
    if (winner == 2) return 1.0;
    if (winner == 1) return -1.0;
    if (depth == max_depth) return 0.0;

    double total = 0.0;
    int next_player = (player == 2) ? 1 : 2;
    int valid_moves = 0;

    for (int col = 0; col < BOARD_WIDTH; col++) {
        if (board->column_heights[col] < BOARD_HEIGHT) {
            board->board[col][board->column_heights[col]] = next_player;
            board->column_heights[col]++;
            total += evaluate_board(board, col, depth + 1, max_depth, next_player);
            board->column_heights[col]--;
            board->board[col][board->column_heights[col]] = 0;
            valid_moves++;
        }
    }

    if (valid_moves == 0) return 0.0;

    return total / valid_moves;
}

