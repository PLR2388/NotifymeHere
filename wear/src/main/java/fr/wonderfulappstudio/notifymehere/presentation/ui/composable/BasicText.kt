package fr.wonderfulappstudio.notifymehere.presentation.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.wear.compose.material.Text

@Composable
fun BasicText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}