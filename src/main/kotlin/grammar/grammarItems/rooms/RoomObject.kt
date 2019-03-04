package grammar.grammarItems.rooms

open class RoomObject (
        val position: MapPosition,
        val name:String,
        val description:String
)

data class MapPosition(
        val x:Int,
        val y:Int
)

