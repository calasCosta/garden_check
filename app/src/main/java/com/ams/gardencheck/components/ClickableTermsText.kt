package com.ams.gardencheck.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun ClickableTermsText(
    onTermsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val annotatedText = buildAnnotatedString {
        // Add the non-clickable part
        append("By continuing, you agree to our ")

        // Add "Terms and" as a clickable link
        pushStringAnnotation(
            tag = "TERMS",
            annotation = "terms"
        )
        withStyle(style = SpanStyle(
            color = Color(0xFF4988C4),
            fontWeight = FontWeight.Normal,
            textDecoration = TextDecoration.Underline
        )
        ) {
            append("Terms and")
        }
        pop()

        append(" ")

        // Add "Privacy Policy" as a clickable link
        pushStringAnnotation(
            tag = "PRIVACY",
            annotation = "privacy"
        )
        withStyle(style = SpanStyle(
            color = Color(0xFF4988C4),
            fontWeight = FontWeight.Normal,
            textDecoration = TextDecoration.Underline
        )) {
            append("Privacy Policy")
        }
        pop()
    }

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    ClickableText(
        text = annotatedText,
        modifier = modifier,
        style = TextStyle(
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        ),
        onClick = { offset ->
            // Check which annotation was clicked
            annotatedText.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                .firstOrNull()?.let {
                    onTermsClicked()
                }

            annotatedText.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                .firstOrNull()?.let {
                    onTermsClicked() // Navigate to same screen or different one
                }
        }
    )
}