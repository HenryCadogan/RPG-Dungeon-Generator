package grammar.grammarItems.factories

data class RoomFeatures(
        val features: List<Feature>
) {
    data class Feature(
            val description: String
    )
}