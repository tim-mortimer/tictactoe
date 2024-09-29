package uk.co.kiteframe.tictactoe

import uk.co.kiteframe.tictactoe.Game.GameStatus
import kotlin.test.Test
import kotlin.test.assertEquals

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

    @Test
    fun `the players must take it in turns to make their moves`() {
        val game = Game()
        val result = game.makeMove(Player.NAUGHTS, Position(0, 0))
        val finalResult = result.getOrNull()!!.makeMove(Player.NAUGHTS, Position(1, 0))
        assertEquals(Game.RepeatedTurnError(Player.NAUGHTS), finalResult.leftOrNull())
    }
}