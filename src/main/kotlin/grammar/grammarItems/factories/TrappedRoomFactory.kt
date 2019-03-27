package grammar.grammarItems.factories

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import grammar.Constraints
import grammar.grammarItems.GrammarItem
import theme.Theme
import grammar.grammarItems.rooms.TrappedRoom
import grammar.grammarItems.rooms.Traps
import grammar.operators.oneOf


class TrappedRoomFactory : DungeonRoomFactory(Constraints.theme) {

    private val trapFile = this.javaClass.getResource("/rooms/trappedRoomDescriptions.json")
    private val mapper = jacksonObjectMapper()

    //this must have a non terminal method for the interface but currently there are no plans to make a rule
    //to extend modify a trapped room
    override fun nonTerminal(): TrappedRoom {
        return genTrappedRoom(false)
    }

    override fun terminal(): GrammarItem {
        return genTrappedRoom(true)
    }

    private fun genTrappedRoom(terminal: Boolean): TrappedRoom {
        val room = TrappedRoom(terminal)
        room.trapped = true
        room.description = determineTrapDescription()
        return room
    }

    private fun determineTrapDescription(): String {

        val allTraps = mapper.readValue(trapFile, Traps::class.java)
        val themeTraps = allTraps.themes.find { it.ThemeName == Constraints.theme.name }
                ?: throw ThemedTrapDescriptionNotFound(Constraints.theme)
        val trap = themeTraps.trapList.oneOf()
        val oldDescription = super.generateDescription()
        val sb = StringBuilder()

        sb.appendln(oldDescription)
        sb.appendln("This room is Trapped! ${trap.description}")

        return sb.toString()
    }

}

class ThemedTrapDescriptionNotFound(theme: Theme) :
        RuntimeException("Could not find any trap descriptions for theme ${theme.name}, please add at lease one to the trap file")