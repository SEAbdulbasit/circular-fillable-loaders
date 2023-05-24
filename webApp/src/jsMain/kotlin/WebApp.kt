import androidx.compose.ui.window.Window
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("Circular Fillable Loaders") {
            MainView()
        }
    }
}



