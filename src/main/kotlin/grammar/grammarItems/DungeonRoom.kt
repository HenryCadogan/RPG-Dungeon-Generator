package grammar.grammarItems

import generator.Monster

class DungeonRoom(
        terminal:Boolean = false
) : GrammarItem(terminal){
    private val position: Boolean = false
    private val enemies: List<Monster> = emptyList()
    private val trapped:Boolean = false
    private val loot:Boolean = false
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