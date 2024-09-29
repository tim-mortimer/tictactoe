package uk.co.kiteframe.tictactoe

import arrow.core.Either
import arrow.core.left
import arrow.core.right

class Game(private val moves: List<Move> = emptyList(), val winner: Player? = null) {

    fun status(): GameStatus {
        return when {
            moves.isEmpty() -> GameStatus.NOT_STARTED
            winner != null -> GameStatus.WON
            else -> GameStatus.IN_PROGRESS
        }
    }

    fun makeMove(player: Player, position: Position): Either<GameError, Game> = when {
        moves.isNotEmpty() && moves.last().player == player -> GameError.RepeatedTurnError(player).left()
        moves.map(Move::position).contains(position) -> GameError.AlreadyPlayedPositionError(player, position).left()
        isWinningMove(Move(player, position)) -> Game(moves + Move(player, position), player).right()
        else -> Game(moves + Move(player, position)).right()
    }

    private fun isWinningMove(move: Move): Boolean {
        val currentMoves = moves + move
        val player = move.player
        return currentMoves.containsHorizontalWin(player)
    }

    private fun List<Move>.containsHorizontalWin(player: Player): Boolean {
        for (y in 0..2) {
            if (containsAll(
                    listOf(
                        Move(player, Position(0, y)!!),
                        Move(player, Position(1, y)!!),
                        Move(player, Position(2, y)!!)
                    )
                )
            ) {
                return true
            }
        }

        return false
    }

    data class Move(val player: Player, val position: Position)

    enum class GameStatus {
        NOT_STARTED,
        IN_PROGRESS,
        WON
    }

    sealed interface GameError {
        data class RepeatedTurnError(val player: Player) : GameError
        data class AlreadyPlayedPositionError(val player: Player, val position: Position) : GameError
    }
}
