package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomePage(onNavigate: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text("HomePage")
    }
}