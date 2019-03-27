package display.menu
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import theme.Theme
import tornadofx.*



class GeneratorApp: App(){
    override val primaryView= MainMenu::class

}

class MainMenu:View(){
    private val model = ViewModel()
    private val roomCount = model.bind{SimpleStringProperty()}
    private val dungeonTheme = model.bind{SimpleStringProperty()}
    private val myController: MyController by inject()
    override val root = form {


        fieldset(labelPosition = Orientation.VERTICAL) {
            field("Max Number of rooms") {
                textfield(roomCount)
            }
            field("Theme for the dungeon"){
                combobox(values= myController.themes)
            }
        }
        button("Generate!"){
            enableWhen(model.valid)
            isDefaultButton=true
            useMaxHeight=true
            action{
                runAsyncWithProgress {
                    myController.generateMap(model)
                }
            }
        }
    }
}

class MyController:Controller(){
    val themes = Theme.values().toList()

    fun generateMap(model:ViewModel) {
    }
}

fun main() {
    launch<GeneratorApp>()
}
