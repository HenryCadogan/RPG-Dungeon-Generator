package grammar.grammarItems.treasure.money

import grammar.grammarItems.GrammarItem
import grammar.operators.OneOf
import kotlin.random.Random


data class Money(
        val platinum: Int,
        val gold: Int,
        val silver: Int,
        val copper: Int
): GrammarItem(true) {
    companion object {
        private val r = Random
        private val oneOf = OneOf(r)
        fun commonEnemy() = Money(
                platinum = 0,
                gold = pickFrom(1..2),
                silver = pickFrom(1..50),
                copper = pickFrom(1..100)
        )

        fun lowValue() = Money(
                platinum = 0,
                gold = pickFrom(1..3),
                silver = pickFrom(1..50),
                copper = pickFrom(1..100)
        )

        fun moderateValue() = Money(
                platinum = 0,
                gold = pickFrom(3..10),
                silver = pickFrom(1..100),
                copper = pickFrom(1..100)
        )

        fun highValue() = Money(
                platinum = 0,
                gold = pickFrom(10..15),
                silver = pickFrom(1..100),
                copper = pickFrom(1..100)
        )

        fun motherload() = Money(
                platinum = pickFrom(1..3),
                gold = pickFrom(20..40),
                silver = pickFrom(1..100),
                copper = pickFrom(1..100)
        )

        private fun pickFrom(intRange: IntRange) = oneOf.oneOf(intRange.toList()).first()
        private fun pickFrom(intList:List<Int>) = oneOf.oneOf(intList).first()
    }

    fun Money.add(other:Money):Money{
        return Money(
                platinum = this.platinum + other.platinum,
                gold = this.gold + other.gold,
                silver = this.silver + other.silver,
                copper = this.copper + other.copper
        )
    }
}
