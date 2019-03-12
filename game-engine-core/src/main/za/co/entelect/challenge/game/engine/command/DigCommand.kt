package za.co.entelect.challenge.game.engine.command

import za.co.entelect.challenge.game.engine.entities.MoveValidation
import za.co.entelect.challenge.game.engine.map.CellType
import za.co.entelect.challenge.game.engine.map.Point
import za.co.entelect.challenge.game.engine.map.WormsMap
import za.co.entelect.challenge.game.engine.player.Worm

/**
 * Command to dig through a cell
 */
class DigCommand(val target: Point) : WormsCommand {

    override val order: Int = 1

    constructor(x: Int, y: Int) : this(Point(x, y))

    /**
     * For a dig command to be valid:
     * * The target cell must be within range
     * * The target cell must be diggable (see CellType})
     */
    override fun validate(gameMap: WormsMap, worm: Worm): MoveValidation {
        if (target !in gameMap) {
            return MoveValidation.invalidMove("$target out of map bounds")
        }

        val targetCell = gameMap[target]

        if (!targetCell.type.diggable) {
            return MoveValidation.invalidMove("Cell type ${targetCell.type} not diggable")
        }

        if (target.movementDistance(worm.position) > worm.diggingRange) {
            return MoveValidation.invalidMove("Cell $target too far away")
        }

        return MoveValidation.validMove()
    }

    override fun execute(gameMap: WormsMap, worm: Worm) {
        val targetCell = gameMap[target]
        targetCell.type = CellType.AIR
    }

}