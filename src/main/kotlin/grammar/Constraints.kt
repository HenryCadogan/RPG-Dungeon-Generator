package grammar

import grammar.grammarItems.GrammarItem
import grammar.grammarItems.enemies.Enemy
import grammar.grammarItems.enemies.EnemyFactory
import grammar.grammarItems.enemies.Theme
import grammar.grammarItems.rooms.DungeonRoomFactory
import grammar.grammarItems.rooms.TrappedRoomFactory
import grammar.grammarItems.treasure.Item
import grammar.grammarItems.treasure.ItemsFactory

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

object Dungeon {
    var rooms = mutableListOf<GrammarItem>()
    val enemies = mutableListOf<Enemy>()
    val treasure = mutableListOf<Item>()
}

object Factories {
    lateinit var enemyFactory: EnemyFactory
    lateinit var itemFactory: ItemsFactory
    lateinit var roomFactory: DungeonRoomFactory
    lateinit var trappedRoomFactory: TrappedRoomFactory
}
