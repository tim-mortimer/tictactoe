package uk.co.kiteframe.tictactoe

import arrow.core.Either
import arrow.core.left
import arrow.core.right

class Game(private val moves: List<Move> = emptyList()) {
    fun status(): GameStatus {
        return when {
            moves.isEmpty() -> GameStatus.NOT_STARTED
            else -> GameStatus.IN_PROGRESS
        }
    }

    fun makeMove(player: Player, position: Position): Either<GameError, Game> {
        if (moves.isNotEmpty() && moves.last().player == player) {
            return RepeatedTurnError(player).left()
        }

        return Game(moves + Move(player, position)).right()
    }

    data class Move(val player: Player, val position: Position)

    enum class GameStatus {
        NOT_STARTED,
        IN_PROGRESS
    }

    sealed interface GameError
    data class RepeatedTurnError(val player: Player): GameError
}