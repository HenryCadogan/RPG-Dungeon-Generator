package grammar.grammarItems.factories

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

import grammar.grammarItems.GrammarItem
import grammar.grammarItems.GrammarItemFactory
import grammar.grammarItems.rooms.DungeonRoom
import grammar.operators.OneOf
import grammar.operators.oneOf
import kotlin.random.Random
import theme.Theme



open class DungeonRoomFactory(private val theme: Theme) : GrammarItemFactory {

    private val descriptionsFile = this::class.java.getResource("/rooms/roomDescriptions.json")
    private val featuresFile = this::class.java.getResource("/rooms/interestingFeatures/features.json")

    private val mapper = jacksonObjectMapper().enable(
            MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)

    private val descriptions = mapper.readValue(descriptionsFile,Descriptions::class.java)
    private val features = mapper.readValue(featuresFile,RoomFeatures::class.java)


    override fun nonTerminal(): GrammarItem {
        val room = DungeonRoom(false)
        room.description = generateDescription()
        return room
    }

    override fun terminal(): GrammarItem {
        val room = DungeonRoom(true)
        room.description = generateDescription()
        return room
    }

    fun generateDescription():String{
        val sb = StringBuilder()

        val themedDesc = descriptions.themes.find{it.name == theme.name} ?: throw ThemeDescriptionNotFound(theme)
        sb.append(themedDesc.walls.oneOf().description)
                .append(themedDesc.floor.oneOf().description)
                .append(themedDesc.ceiling.oneOf().description)
                .append(features.features.oneOf().description or "")
        return sb.toString()
    }

    fun entranceRoomToDungeon(): DungeonRoom {
        val room = DungeonRoom(terminal = true)
        room.description = "The starting room for this adventure. ${generateDescription()}"
        return room
    }
}

private infix fun String.or(s: String): String {
    return OneOf(Random).oneOf(this,s).first()
}

class ThemeDescriptionNotFound(theme: Theme):RuntimeException("Could not find a descriptions block for ${theme.name}, please add it to the Json files")