package grammar.grammarItems.rooms

import grammar.grammarItems.GrammarItem
import grammar.grammarItems.GrammarItemFactory

class TrappedRoom(terminal:Boolean = true): DungeonRoom(terminal){
    override var description = "A trapped room"
}


class TrappedRoomFactory: GrammarItemFactory {

    override fun nonTerminal(): TrappedRoom {
        val room = TrappedRoom(false)
        room.trapped = true
        return room
    }
    override fun terminal(): GrammarItem {
        val room = TrappedRoom(true)
        room.trapped = true
        return room
    }
}
