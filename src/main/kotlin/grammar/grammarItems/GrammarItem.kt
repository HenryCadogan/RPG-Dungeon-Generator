package grammar.grammarItems


abstract class GrammarItem(
        val terminal: Boolean

)

infix fun GrammarItem.and(item: GrammarItem): List<GrammarItem> {
    return listOf(this,item)
}

infix fun List<GrammarItem>.and(item:GrammarItem): List<GrammarItem> {
    val items = this as MutableList
    items.add(item)
    return items
}

infix fun GrammarItem.and(items:List<GrammarItem>): List<GrammarItem> {
    val itemList = mutableListOf(this)
    itemList.addAll(items)
    return itemList
}
