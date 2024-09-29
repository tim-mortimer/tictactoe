package uk.co.kiteframe.tictactoe

import arrow.core.Either
import arrow.core.getOrElse
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import uk.co.kiteframe.tictactoe.Game.GameStatus
import uk.co.kiteframe.tictactoe.Game.RepeatedTurnError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TicTacToeTest {
    @Test
    fun `a game with no moves placed is not started`() {
        val game = Game()
        assertEquals(GameStatus.NOT_STARTED, game.status())
    }

    @Test
    fun `any player can make the first move`() {
        val game1 = Game()
        val result1 = game1.makeMove(Player.NAUGHTS, Position(0, 0))
        assertEquals(GameStatus.IN_PROGRESS, result1.map(Game::status).getOrNull())

        val game2 = Game()
        val result2 = game2.makeMove(Player.CROSSES, Position(0, 0))
        assertEquals(GameStatus.IN_PROGRESS, result2.map(Game::status).getOrNull())
    }

    @ParameterizedTest
    @EnumSource
    fun `the players must take it in turns to make their moves`(player: Player) {
        Game().makeMove(player, Position(0, 0))
            .andThen()
            .makeMove(player, Position(1, 0))
            .failsWith(RepeatedTurnError(player))
    }

    fun Either<Game.GameError, Game>.andThen(): Game {
        return when {
            this.isLeft() -> throw IllegalStateException("Can't proceed after a failed move")
            else -> this.getOrElse { throw IllegalStateException("Game cannot be null") }
        }
    }

    fun Either<Game.GameError, Game>.failsWith(error: Game.GameError) {
        assertTrue(this.isLeft())
        assertEquals(error, this.leftOrNull())
    }
}