#include <mpi.h>
#include <stdio.h>
#include "game.h"
#include "mpi_utils.h"

int handle_player_turn(GameBoard* game);
int handle_computer_turn(GameBoard* game, int rank, int size);
void player_wins();
void computer_wins();
void invalid_move();
void print_prompt();

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);
    
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    GameBoard game;

    if (rank == 0) {
        initialize_board(&game);
        print_board(&game);

        while (1) {
            // Player's turn
            int tmp = handle_player_turn(&game);
            if (is_final_move(&game, tmp) == 1) {
                player_wins();
                break;
            }
            print_board(&game);

            // Computer's turn
            tmp = handle_computer_turn(&game, rank, size);
            if (is_final_move(&game, tmp) == 2) {
                print_board(&game);
                computer_wins();
                break;
            }
            print_board(&game);
        }
    } else {
        while (1) {
            int col;
            MPI_Recv(&col, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(game.board, BOARD_WIDTH * BOARD_HEIGHT, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(game.column_heights, BOARD_WIDTH, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            game.board[col][game.column_heights[col]] = 2;
            game.column_heights[col]++;

            double value = evaluate_board(&game, col, 0, MAX_DEPTH, 2);

            game.column_heights[col]--;
            game.board[col][game.column_heights[col]] = 0;

            MPI_Send(&value, 1, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);
        }
    }
    
    MPI_Finalize();
    return 0;
}

int handle_player_turn(GameBoard* game) {
    int col;
    print_prompt();
    fflush(stdout);
    scanf("%d", &col);

    while (col < 0 || col >= BOARD_WIDTH || game->column_heights[col] >= BOARD_HEIGHT) {
        invalid_move();
        scanf("%d", &col);
    }

    game->board[col][game->column_heights[col]] = 1;
    game->column_heights[col]++;
    return col;
}

int handle_computer_turn(GameBoard* game, int rank, int size) {
    printf("Computer is thinking...\n");

    int best_move = next_computer_move(game, size);
    game->board[best_move][game->column_heights[best_move]] = 2;
    game->column_heights[best_move]++;

    printf("Computer placed a piece in column %d.\n", best_move);
    
    return best_move;
}

void player_wins() {
    printf("Congratulations! You win!\n");
}

void computer_wins() {
    printf("The computer wins! Better luck next time.\n");
}

void invalid_move() {
    printf("Invalid move. Enter column (0-%d): ", BOARD_WIDTH - 1);
}

void print_prompt() {
    printf("Enter number (0-%d): ", BOARD_WIDTH - 1);
}

