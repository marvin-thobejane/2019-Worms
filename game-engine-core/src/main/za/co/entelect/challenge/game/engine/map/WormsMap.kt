package za.co.entelect.challenge.game.engine.map;

import za.co.entelect.challenge.game.engine.entities.GameConfig
import za.co.entelect.challenge.game.engine.player.Worm
import za.co.entelect.challenge.game.engine.player.WormsPlayer
import za.co.entelect.challenge.game.engine.processor.GameError
import kotlin.jvm.Transient

class WormsMap(val players: List<WormsPlayer>,
               val rows: Int,
               val columns: Int,
               cells: List<MapCell>,
               @Transient
               val config: GameConfig = GameConfig()) {

    val cells: List<MapCell>
    val errorList = mutableListOf<GameError>()

    private val xRange = 0 until columns
    private val yRange = 0 until rows

    val livingPlayers: List<WormsPlayer>
        get() = players.filter { !it.dead }

    val winningPlayer: WormsPlayer?
        get() {
            if (livingPlayers.size > 1) {
                throw IllegalStateException("More than one living player")
            }

            return if (livingPlayers.isEmpty()) {
                null
            } else {
                livingPlayers[0]
            }
        }

    var currentRound: Int = 0

    init {
        if (cells.size < rows * columns) {
            throw IllegalStateException("Not enough cells to fill the map")
        } else {
            this.cells = cells.sortedWith(MapCell.comparator)
        }

        players.forEach { player -> player.worms.forEach { placeWorm(it) } }
    }

    operator fun contains(target: Point): Boolean {
        return target.x in xRange && target.y in yRange
    }

    operator fun get(target: Point): MapCell {
        return this[target.x, target.y]
    }

    operator fun get(x: Int, y: Int): MapCell {
        if (x !in xRange) {
            throw IndexOutOfBoundsException("x=$x out of range $xRange")
        }

        if (y !in yRange) {
            throw IndexOutOfBoundsException("y=$y out of range $yRange")
        }

        return cells[y * columns + x]
    }

    private fun placeWorm(worm: Worm) {
        this[worm.position].occupier = worm
    }
}