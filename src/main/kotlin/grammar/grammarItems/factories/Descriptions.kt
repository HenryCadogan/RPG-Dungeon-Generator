package grammar.grammarItems.factories

data class Descriptions(
        val themes: List<Theme>
) {
    data class Theme(
            val ceiling: List<Ceiling>,
            val floor: List<Floor>,
            val name: String,
            val walls: List<Wall>
    ) {
        data class Floor(
                val description: String
        )

        data class Ceiling(
                val description: String
        )

        data class Wall(
                val description: String
        )
    }
}