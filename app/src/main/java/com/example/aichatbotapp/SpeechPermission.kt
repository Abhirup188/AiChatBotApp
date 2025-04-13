import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun VoiceInput(onResult: (String) -> Unit) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()

            Toast.makeText(context, "Received voice input: $spokenText", Toast.LENGTH_LONG).show()

            if (spokenText != null) {
                onResult(spokenText)
            } else {
                Toast.makeText(context, "No speech recognized", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Speech recognition cancelled or failed", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
        }

        Toast.makeText(context, "Launching voice input...", Toast.LENGTH_SHORT).show()
        launcher.launch(intent)
    }
}



