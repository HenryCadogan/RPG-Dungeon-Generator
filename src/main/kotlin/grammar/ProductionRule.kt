package grammar

import kotlin.reflect.KClass

data class ProductionRule(
        val lhs: KClass<out GrammarItem>,
        val rhs: () -> List<GrammarItem>
)