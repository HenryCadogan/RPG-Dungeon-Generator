package grammar.grammarItems

interface GrammarItemGenerator {
    fun terminal():GrammarItem
    fun nonTerminal():GrammarItem
}