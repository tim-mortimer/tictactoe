package uk.co.kiteframe.tictactoe

data class Position private constructor(val x: Int, val y: Int) {
    companion object {
        operator fun invoke(x: Int, y: Int): Position? {
            return if (x < 0 || y < 0 || x > 2 || y > 2) {
                null
            } else {
                Position(x, y)
            }
        }
    }
}