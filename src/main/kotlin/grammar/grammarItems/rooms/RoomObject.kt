package grammar.grammarItems.rooms

data class RoomObject (
        val position: MapPosition
)

data class MapPosition(
        val x:Int,
        val y:Int
)
