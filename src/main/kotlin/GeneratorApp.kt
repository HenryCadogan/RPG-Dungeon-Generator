
import grammar.Constraints
import grammar.grammarItems.enemies.Theme
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
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

        combobox(values= myController.themes)
        fieldset(labelPosition = Orientation.VERTICAL) {
            field("Max Number of rooms") {
                textfield(roomCount)
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
    val constraints = Constraints
    val themes = Theme.values().toList()



    fun generateMap(model:ViewModel){

    }
}

fun main() {
    launch<GeneratorApp>()
}
