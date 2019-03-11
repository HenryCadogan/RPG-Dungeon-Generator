package grammar.grammarItems.rooms

import generator.Monster
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.GrammarItemFactory
import grammar.grammarItems.treasure.*

open class DungeonRoom(
        terminal: Boolean = false
) : GrammarItem(terminal) {
    lateinit var position: MapPosition
    lateinit var size: RoomSize
    private val roomEnemies = mutableListOf<Monster>()
    var trapped: Boolean = false
    var roomObjects = listOf<GrammarItem>()
    open var description: String = "A room"

    fun isTrapped() = trapped
}

data class RoomSize(
        val x:Int,
        val y:Int
)
data class MapPosition(
        val x:Int,
        val y:Int
)

class DungeonRoomFactory : GrammarItemFactory {

    override fun nonTerminal(): GrammarItem {
        return DungeonRoom(false)
    }

    override fun terminal(): GrammarItem {
        return DungeonRoom(true)
    }

    fun determineRoomPlacement(){

    }

    fun entranceRoomToDungeon():DungeonRoom{
        val room = DungeonRoom(
                terminal = true
        )
        room.description = "The starting room for this adventure"
        return room
    }


}





