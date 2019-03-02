
import javafx.beans.property.SimpleStringProperty

import tornadofx.*


class GeneratorApp: App(){
    override val primaryView= MainMenu::class

}

class MainMenu:View(){
    private val roomCount = SimpleStringProperty()

    private val myController: MyController by inject()
    override val root = form {
        combobox(values= listOf(""))
        fieldset {
            field("Input") {
                textfield(roomCount)
            }
        }
    }

}

class MyController:Controller(){

    fun loadText(): String {
        return "ABC"
    }
}

fun main() {
    launch<GeneratorApp>()
}
