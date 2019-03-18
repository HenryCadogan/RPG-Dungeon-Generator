package grammar.grammarItems.enemies

import generator.Bestiary
import generator.Monster
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.GrammarItemFactory
import grammar.operators.OneOf
import kotlin.random.Random

open class Enemy(terminal:Boolean = false): GrammarItem(terminal){
    lateinit var data:Monster
}

class EnemyPlaceholder:GrammarItem(false)

class EnemyFactory(private val theme:Theme):GrammarItemFactory{
    private val bestiary = Bestiary()

    override fun terminal(): Enemy {
        return getThemedEnemy(true)
    }

    override fun nonTerminal(): Enemy {
        return getThemedEnemy(false)
    }

    private fun getThemedEnemy(terminal:Boolean): Enemy {
        val enemies =  when(theme){
            Theme.AQUATIC -> bestiary.aquatic
            Theme.FORESTRY -> bestiary.forestry
            Theme.UNDEAD -> bestiary.undead
            Theme.RANDOM -> bestiary.random
            Theme.DRAGON -> bestiary.dragon
            Theme.HUMANOID -> bestiary.humanoid
        }.toList()
        val enemy = Enemy(terminal)
        enemy.data = OneOf(Random).oneOf(enemies).first()
        return enemy
    }

}

enum class Theme{
    AQUATIC,
    FORESTRY,
    UNDEAD,
    RANDOM,
    DRAGON,
    HUMANOID
}