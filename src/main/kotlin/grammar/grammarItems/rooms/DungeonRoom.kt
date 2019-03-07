package grammar.grammarItems.rooms

import generator.Monster
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.treasure.ItemsFactory
import kotlin.random.Random

open class DungeonRoom(
        terminal:Boolean = false
) : GrammarItem(terminal){
    lateinit var position: MapPosition
    private val roomEnemies= mutableListOf<Monster>()
    private var trapped:Boolean = false
    private var loot:Boolean = false
    private val roomObjects= mutableListOf<RoomObject>()
    private var description : String = "A room"

    fun isTrapped() = trapped

    fun addEnemies(monsters:List<Monster>){
        roomEnemies.addAll(monsters)
    }

    fun addObjects(objects: List<RoomObject>){
        roomObjects.addAll(objects)
    }

    companion object {
        fun trapped():DungeonRoom{
            val room = DungeonRoom(true)
            room.trapped = true
            return room
        }
    }
}


interface GrammarItemFactory{
    fun terminal():GrammarItem
    fun nonTerminal():GrammarItem
}

open class DungeonRoomFactory:GrammarItemFactory{
    override fun terminal(): DungeonRoom {
        return createRoom(true)
    }

    override fun nonTerminal(): DungeonRoom {
        return createRoom(false)
    }

    private fun createRoom(terminal: Boolean): DungeonRoom {
        return DungeonRoom(
                terminal = terminal
        )
    }
}