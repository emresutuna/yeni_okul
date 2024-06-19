import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import navigation.HomeNav
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        HomeNav()
    }
}