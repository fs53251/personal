#include "mpi_utils.h"
#include <mpi.h>

double calculate_move_value(GameBoard* game, int col) {
    game->board[col][game->column_heights[col]] = 2;
    game->column_heights[col]++;
    double value = evaluate_board(game, col, 0, MAX_DEPTH, 2);
    game->column_heights[col]--;
    game->board[col][game->column_heights[col]] = 0;
    return value;
}

void send_data_to_rank(int rank, int col, GameBoard* game, MPI_Request* requests, int* request_index) {
    MPI_Send(&col, 1, MPI_INT, rank, 0, MPI_COMM_WORLD);
    MPI_Isend(game->board, BOARD_WIDTH * BOARD_HEIGHT, MPI_INT, rank, 0, MPI_COMM_WORLD, &requests[(*request_index)++]);
    MPI_Isend(game->column_heights, BOARD_WIDTH, MPI_INT, rank, 0, MPI_COMM_WORLD, &requests[(*request_index)++]);
}

double receive_data_from_rank(int rank) {
    double value;
    MPI_Recv(&value, 1, MPI_DOUBLE, rank, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    return value;
}

int find_best_move(double* values) {
    double max_value = -1.0;
    int best_move = -1;
    for (int col = 0; col < BOARD_WIDTH; col++) {
        if (values[col] > max_value) {
            max_value = values[col];
            best_move = col;
        }
    }
    return best_move;
}

int next_computer_move(GameBoard* game, int size) {
    double values[BOARD_WIDTH];
    for (int i = 0; i < BOARD_WIDTH; i++) values[i] = -1.0;

    int dest_rank = 1;
    MPI_Request requests[BOARD_WIDTH * 2];
    int request_index = 0;

    for (int col = 0; col < BOARD_WIDTH; col++) {
        if (game->column_heights[col] < BOARD_HEIGHT) {
            if (size > 1 && dest_rank < size) {
                send_data_to_rank(dest_rank, col, game, requests, &request_index);
                dest_rank++;
            } else {
                values[col] = calculate_move_value(game, col);
            }
        }
    }

    if (size > 1) {
        for (int col = 0; col < BOARD_WIDTH; col++) {
            if (game->column_heights[col] < BOARD_HEIGHT && col < dest_rank - 1) {
                double value = receive_data_from_rank(col + 1);
                values[col] = value;
            }
        }
    }

    return find_best_move(values);
}
