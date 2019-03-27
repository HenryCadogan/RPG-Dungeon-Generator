package grammar.grammarItems.rooms

import grammar.grammarItems.GrammarItem
import grammar.grammarItems.enemies.Enemy
import kotlin.random.Random
import kotlin.random.nextInt

open class DungeonRoom(
        terminal: Boolean = false
) : GrammarItem(terminal) {
    var size = RoomSize(
            height = Random.nextInt((50..80)),
            width = Random.nextInt((50..80))
    )
    var roomEnemies = listOf<Enemy>()
    var roomObjects = listOf<GrammarItem>()
    internal var trapped: Boolean = false
    open lateinit var description: String
    lateinit var identifier: String
}

data class RoomSize(
        val width: Int,
        val height: Int
)

data class MapPosition(
        val x: Int,
        val y: Int
)






