package grammar.operators

import kotlin.random.Random

class GrammarOperators(rnd: Random) {
    val oneOf = OneOf(rnd)
    val oneOrMore = OneOrMore(rnd)
}