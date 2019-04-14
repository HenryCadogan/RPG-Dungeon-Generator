package grammar.grammarItems.rooms

import grammar.Constraints
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.enemies.Enemy
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.random.Random
import kotlin.random.nextInt

open class DungeonRoom(
        terminal: Boolean = false
) : GrammarItem(terminal) {
    var size = RoomSize(
            height = Random.nextInt((40..Constraints.rooms.maxRoomSize)),
            width = Random.nextInt((40..Constraints.rooms.maxRoomSize))
    )
    var roomEnemies = listOf<Enemy>()
    var roomObjects = listOf<GrammarItem>()
    internal var trapped: Boolean = false
    open lateinit var description: String
    lateinit var id: String
    lateinit var position: MapPosition
    var leftChiLd:DungeonRoom? = null
    var rightChiLd:DungeonRoom? = null
    val center get()= MapPosition(
            x = position.x + size.width/2,
            y = position.y + size.height/2
    )
}

data class RoomSize(
        val width: Int,
        val height: Int
)

data class MapPosition(
        val x: Int,
        val y: Int
)

fun MapPosition.distanceTo(position: MapPosition):Double{
    return hypot((this.x - position.x).toDouble(),(this.y - position.y).toDouble())
}

fun DungeonRoom.distanceTo(otherRoom: DungeonRoom): Double {
    val thisCentre = MapPosition(this.position.x+this.size.width/2,
            this.position.y+this.size.height/2)
    val otherCentre = MapPosition(otherRoom.position.x+otherRoom.size.width/2,
            otherRoom.position.y+otherRoom.size.height/2)
    return thisCentre.distanceTo(otherCentre)
}

fun DungeonRoom.getNearestRoom(rooms: List<DungeonRoom> ): DungeonRoom{
    if (rooms.size == 1) return rooms[0]
    lateinit var nearest:DungeonRoom
    var nearestDistance = Float.POSITIVE_INFINITY.toDouble()
    val filteredRooms = rooms.filter{it.id != this.id}
    filteredRooms.forEach { dungeonRoom ->
        val distance = dungeonRoom.distanceTo(this)
        if (distance < nearestDistance) {
            nearest = dungeonRoom
            nearestDistance = distance
        }
    }
    return nearest
}

fun MapPosition.angleTo(other:MapPosition):Double{
    return atan2((this.y - other.y).toDouble(), (this.x - other.x).toDouble())
}





