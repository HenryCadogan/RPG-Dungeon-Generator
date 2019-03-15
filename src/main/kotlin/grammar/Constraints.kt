package grammar

import grammar.grammarItems.GrammarItem
import grammar.grammarItems.enemies.Enemy
import grammar.grammarItems.enemies.Theme
import grammar.grammarItems.treasure.Item

object Constraints {
    val rooms = Rooms
    val enemies = Enemies
    var theme: Theme = Theme.RANDOM

    object Rooms {
        var maxRoomCount = 20
        var roomSparsity = 0.5f
        var trappedRoomPercentage = 20
        var trappedContainerPercentage = 0.3
    }

    object Enemies {
        var maxEnemiesPerRoom = 4
        var enemySparcity = 0.7f
    }
}
object Dungeon{
    var rooms = mutableListOf<GrammarItem>()
    val enemies = mutableListOf<Enemy>()
    val treasure = mutableListOf<Item>()
}