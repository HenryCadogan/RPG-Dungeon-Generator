package grammar.grammarItems.rooms

data class Traps(
        val themes: List<Theme>
) {
    data class Theme(
            val ThemeName: String,
            val trapList: List<Type>
    ) {
        data class Type(
                val description: String
        )
    }
}