package grammar

import grammar.grammarItems.StartItem
import grammar.grammarItems.and
import grammar.grammarItems.enemies.Enemy
import grammar.grammarItems.enemies.EnemyPlaceholder
import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.factories.ItemPlaceholder
import grammar.grammarItems.factories.ItemSize
import grammar.grammarItems.factories.MiscItemPlaceholder
import grammar.grammarItems.factories.TreasurePlaceholder
import grammar.grammarItems.treasure.items.ContainerCategory
import grammar.grammarItems.treasure.items.MiscItemCategory
import grammar.grammarItems.treasure.items.WeaponCategory
import grammar.grammarItems.treasure.money.Money
import grammar.operators.GrammarOperators
import grammar.operators.oneOf
import grammar.operators.oneOrMore
import grammar.operators.or
import kotlin.random.Random

object Rules{
    private val rnd = Random
    private val ops = GrammarOperators(rnd)

    val roomRules = listOf(
            ProductionRule(
                    lhs = DungeonRoom::class,
                    rhs = {
                        ops.oneOf.oneOf(mapOf(
                                Factories.roomFactory.terminal() to 100 - Constraints.rooms.trappedRoomPercentage,
                                Factories.trappedRoomFactory.terminal() to Constraints.rooms.trappedRoomPercentage
                        ))
                    }
            ),
            ProductionRule(
                    lhs = StartItem::class,
                    rhs = {
                        Factories.roomFactory.entranceRoomToDungeon() and ops.oneOrMore.oneOrMore(
                                Factories.roomFactory.nonTerminal(), Constraints.rooms.maxRoomCount, Constraints.rooms.roomSparsity)
                    }
            )
    )

    val itemRules = listOf(
            ProductionRule(
                    lhs = ItemPlaceholder::class,
                    rhs = {
                        Factories.itemFactory.generateContainer(
                                size = ItemSize.values().toList().oneOf(),
                                type = ContainerCategory.CHEST).oneOrMore(3) or
                                Factories.itemFactory.generateWeapon(
                                        size = ItemSize.SMALL,
                                        type = WeaponCategory.WAND
                                ) or
                                Factories.itemFactory.generateLockedContainerAndKey(ItemSize.values().toList().oneOf(),ContainerCategory.values().toList().oneOf()) or
                                emptyList()
                    }
            )
    )

    val enemyRules = listOf(
            ProductionRule(
                    lhs = EnemyPlaceholder::class,
                    rhs = {
                        ops.oneOrMore.oneOrMore(
                                item = (Factories.enemyFactory.terminal() or Factories.enemyFactory.nonTerminal()).first(),
                                limit = Constraints.enemies.maxEnemiesPerRoom,
                                probability = Constraints.enemies.enemySparsity
                        )
                    }
            ),
            ProductionRule(
                    lhs = Enemy::class,
                    rhs = {
                        listOf(Factories.enemyFactory.terminal()) or emptyList()
                    }
            )
    )

    val lootRules = listOf(
            ProductionRule(
                    lhs = TreasurePlaceholder::class,
                    rhs = {
                        ((Money.lowValue() and MiscItemPlaceholder()) or (ops.oneOrMore.oneOrMore(MiscItemPlaceholder(), 3, 70f)) or emptyList())
                    }
            ),
            ProductionRule(
                    lhs = MiscItemPlaceholder::class,
                    rhs = {
                        (Factories.itemFactory.generateMiscItem(MiscItemCategory.values().toList().oneOf()) or emptyList() or Money.moderateValue())
                    }
            )
    )
}