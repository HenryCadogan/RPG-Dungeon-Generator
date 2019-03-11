package grammar.grammarItems

interface GrammarItemFactory {
    fun terminal():GrammarItem
    fun nonTerminal():GrammarItem
}