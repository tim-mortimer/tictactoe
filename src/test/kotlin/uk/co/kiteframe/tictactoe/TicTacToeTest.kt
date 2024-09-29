package uk.co.kiteframe.tictactoe

import arrow.core.Either
import arrow.core.getOrElse
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import uk.co.kiteframe.tictactoe.Game.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
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
        val result1 = game1.makeMove(Player.NAUGHTS, position(0, 0))
        assertEquals(GameStatus.IN_PROGRESS, result1.map(Game::status).getOrNull())

        val game2 = Game()
        val result2 = game2.makeMove(Player.CROSSES, position(0, 0))
        assertEquals(GameStatus.IN_PROGRESS, result2.map(Game::status).getOrNull())
    }

    @ParameterizedTest
    @EnumSource
    fun `the players must take it in turns to make their moves`(player: Player) {
        Game().makeMove(player, position(0, 0))
            .andThen()
            .makeMove(player, position(1, 0))
            .failsWith(GameError.RepeatedTurnError(player))
    }

    @Test
    fun `a turn cannot use an already placed position`() {
        Game().makeMove(Player.NAUGHTS, position(0, 0))
            .andThen()
            .makeMove(Player.CROSSES, position(0, 0))
            .failsWith(GameError.AlreadyPlayedPositionError(Player.CROSSES, position(0, 0)))
    }

    @TestFactory
    fun `players cannot make moves on the outside edge of the 3 x 3 grid`(): MutableList<DynamicTest> {
        val testCases: MutableList<DynamicTest> = mutableListOf()

        for (x in -1..3) {
            testCases += DynamicTest.dynamicTest("Position($x, -1)") {
                assertNull(Position(x, -1))
            }
            testCases += DynamicTest.dynamicTest("Position($x, 3)") {
                assertNull(Position(x, 3))
            }
        }

        for (y in 0..2) {
            testCases += DynamicTest.dynamicTest("Position(-1, $y)") {
                assertNull(Position(-1, y))
            }
            testCases += DynamicTest.dynamicTest("Position(3, $y)") {
                assertNull(Position(3, y))
            }
        }

        return testCases
    }

    @Test
    fun `players can not make moves well outside of the 3 x 3 grid`() {
        assertNull(Position(5, 10))
        assertNull(Position(-3, -6))
        assertNull(Position(-6, 9))
        assertNull(Position(9, -100))
    }

    private fun position(x: Int, y: Int): Position = Position(x, y)!!

    fun Either<GameError, Game>.andThen(): Game {
        return when {
            this.isLeft() -> throw IllegalStateException("Can't proceed after a failed move")
            else -> this.getOrElse { throw IllegalStateException("Game cannot be null") }
        }
    }

    fun Either<GameError, Game>.failsWith(error: GameError) {
        assertTrue(this.isLeft())
        assertEquals(error, this.leftOrNull())
    }
}