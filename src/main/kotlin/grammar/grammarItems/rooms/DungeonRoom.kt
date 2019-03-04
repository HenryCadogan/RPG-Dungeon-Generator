package grammar.grammarItems.rooms

import grammar.GrammarItem

open class DungeonRoom(
        terminal:Boolean = false
) : GrammarItem(terminal){
    lateinit var position: MapPosition
    private val enemies= mutableListOf<Monster>()
    private val trapped:Boolean = false
    private val loot:Boolean = false
    private val objects= mutableListOf<RoomObject>()
}


class DungeonRoomFactory{
    fun terminal(): DungeonRoom {
        return createRoom(true)
    }

    fun nonTerminal(): DungeonRoom {
        return createRoom(false)
    }

    private fun createRoom(terminal: Boolean): DungeonRoom {
        return DungeonRoom(
                terminal = terminal
        )
    }
}