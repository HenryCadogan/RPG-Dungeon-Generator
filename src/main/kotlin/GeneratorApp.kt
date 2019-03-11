
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.input.MouseEvent

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

        combobox(values= listOf(""))
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
                    myController.generateMap()
                }
            }
        }
    }

}

class MyController:Controller(){

    fun generateMap(){

    }


}

fun main() {
    launch<GeneratorApp>()
}
