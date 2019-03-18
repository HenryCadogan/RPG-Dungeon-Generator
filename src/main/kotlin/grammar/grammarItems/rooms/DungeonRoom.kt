package grammar.grammarItems.rooms

import grammar.grammarItems.GrammarItem

open class DungeonRoom(
        terminal: Boolean = false
) : GrammarItem(terminal) {
    lateinit var size: RoomSize
    var roomEnemies = listOf<GrammarItem>()
    var trapped: Boolean = false
    var roomObjects = listOf<GrammarItem>()
    open lateinit var description: String

    fun isTrapped() = trapped
}

data class RoomSize(
        val x: Int,
        val y: Int
)

data class MapPosition(
        val x: Int,
        val y: Int
)






