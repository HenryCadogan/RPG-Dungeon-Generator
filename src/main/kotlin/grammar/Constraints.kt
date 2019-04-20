package grammar

import grammar.grammarItems.enemies.EnemyFactory
import grammar.grammarItems.factories.DungeonRoomFactory
import grammar.grammarItems.factories.ItemsFactory
import grammar.grammarItems.factories.TrappedRoomFactory
import grammar.grammarItems.treasure.money.MoneyValue
import theme.Theme

object Constraints {
    val rooms = Rooms
    val enemies = Enemies
    var theme: Theme = Theme.RANDOM
    val containers = Containers
    val loot = Loot

    object Rooms {
        var maxRoomCount = 20
        var minRoomCount = 8
        var roomSparsity = 0.5f
        var trappedRoomPercentage = 0.2f
        var maxRoomSize = 100
        var minRoomSize = 50
        var minRoomDistance = 1f
        var connectivityThreshold = 10
    }

    object Enemies {
        var maxEnemiesPerRoom = 7
        var minEnemiesPerRoom = 0
        var enemySparsity = 0.5f
    }

    object Containers{
        var maxContainersPerRoom = 3
        var minContainersPerRoom = 0
    }

    object Loot {
        var maxLootPerRoom = 3
        var minLootPerRoom = 0
        var moneyValuePerPile = MoneyValue.MODERATE
    }
}

object Factories {
    lateinit var enemyFactory: EnemyFactory
    lateinit var itemFactory: ItemsFactory
    lateinit var roomFactory: DungeonRoomFactory
    lateinit var trappedRoomFactory: TrappedRoomFactory
}




