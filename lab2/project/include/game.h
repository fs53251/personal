#ifndef GAME_H
#define GAME_H

#include <stdlib.h>

#define BOARD_WIDTH 7
#define BOARD_HEIGHT 6
#define MAX_DEPTH 8

typedef struct {
    int board[BOARD_WIDTH][BOARD_HEIGHT];
    int column_heights[BOARD_WIDTH];
} GameBoard;

void initialize_board(GameBoard* board);
void print_board(GameBoard* board);
int is_final_move(GameBoard* board, int last_column);
double evaluate_board(GameBoard* board, int last_column, int depth, int max_depth, int player);

#endif

