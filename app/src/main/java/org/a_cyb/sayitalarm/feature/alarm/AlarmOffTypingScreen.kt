package org.a_cyb.sayitalarm.feature.alarm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.a_cyb.sayitalarm.core.designsystem.theme.SayItAlarmTheme

private const val TAG = "AlarmOffTypingScreen"
private val DEV_OFF_TEXT_DUMMY = listOf(
    "Whatever I am gonna smile today.",
    "Instead of worrying about what you cannot control, shift your energy to what you can create.",
    "Nothing can shake my mind.",
    "Keep Going Your hardest times often lead to the greatest moments of your life. Keep going. Tough situations build strong people in the end.",
    "I believe in my abilities",
    "Every challenge I overcome is a success.",
    "I am stronger than my fear"
)

val paragraphIntrinsics: @Composable (TextStyle) -> @Composable (String) -> ParagraphIntrinsics =
    @Composable { textStyle -> { str ->
        ParagraphIntrinsics(
            text = str,
            style = textStyle,
            density = LocalDensity.current,
            fontFamilyResolver = createFontFamilyResolver(LocalContext.current)
        )
    }}

@Composable
fun AlarmOffTypingScreen(
    modifier: Modifier = Modifier,
    offStrings: List<String> = DEV_OFF_TEXT_DUMMY,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    val intrinsics  = paragraphIntrinsics(textStyle)
    // Calculate width of the string in px.
    @Composable
    fun calculateStrWidth(str: String): Float = intrinsics(str).maxIntrinsicWidth

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val maxWidthOfTextField = with(LocalDensity.current) {
            maxWidth.toPx()
        }

        Column {
            offStrings.forEach { offStr ->
                val maxWidthOfStr = calculateStrWidth(offStr)

                // Check if the OffString is short enough to fit in an one line.
                if (maxWidthOfStr > maxWidthOfTextField) {
                    // TODO: Refactoring [A0].
                    val delimiterWidth = calculateStrWidth(str = " ")
                    val words = offStr.split(" ")
                    val wordsSubStr: (Int, Int) -> String = { start, end ->
                        words.subList(start,end).joinToString(" ")
                    }

                    var len = 0f
                    var startIdx = 0

                    words.forEachIndexed { idx, w ->
                        len += calculateStrWidth(w) + delimiterWidth

                        if (len >= maxWidthOfTextField) {
                            TypingTextField(text = wordsSubStr(startIdx, idx))

                            startIdx = idx
                            len = calculateStrWidth(w) + delimiterWidth
                        }
                    }
                    // Display remained string.
                    TypingTextField(text = wordsSubStr(startIdx, words.size))
                } else {
                    TypingTextField(text = offStr)
                }
                Spacer(modifier = modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun TypingTextField(text: String, modifier: Modifier = Modifier) {
    var inputText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    BasicTextField(
        value = inputText,
        onValueChange = { inputText = it },
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge, // Test 16.sp
        maxLines = Int.MAX_VALUE,
        visualTransformation = TypoColorTransformation(text),
        onTextLayout = {
        },
        // interactionSource = remember { MutableInteractionSource() },
//        cursorBrush = SolidColor(Color.Cyan),
        decorationBox = { innerTextField ->
            Column(
                Modifier
                    .background(Color.LightGray, RoundedCornerShape(percent = 0))
                    .wrapContentHeight()
            ) {
                Text(text = text)
                innerTextField()
            }
        }
    )
}

private class TypoColorTransformation(
    val guideText: String,
    val typoColors: TextColorSet = TextColorSet(Pair(Color.Red, Color.DarkGray)),
    val textColors: TextColorSet = TextColorSet(Pair(Color.Black, Color.LightGray)),
): VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            getAnnotatedString(text.toString()),
            OffsetMapping.Identity
        )
    }

    fun getAnnotatedString(text: String): AnnotatedString {
        val builder = AnnotatedString.Builder()

        text.forEachIndexed { index, c ->
            val isTypo = c != guideText.getOrNull(index)

            builder.withStyle(style = SpanStyle(
                background = if(isTypo) typoColors.bg else textColors.bg,
                color = if(isTypo) typoColors.text else textColors.text
            )) {
                append("$c")
            }
        }
        return builder.toAnnotatedString()
    }
}

@Preview
@Composable
fun AlarmOffTypingPreview() {
    SayItAlarmTheme {
        AlarmOffTypingScreen()
    }
}

@JvmInline
value class TextColorSet(private val colors: Pair<Color, Color>) {
    val text: Color
        get() = colors.first

    val bg: Color
        get() = colors.second
}

/*  Simple function form for ParagraphIntrinsics calculator
@Composable
fun calculator(str: String) =
    ParagraphIntrinsics(
        text = "TEST",
        style = MaterialTheme.typography.bodyLarge,  // TODO: Get exact style of user-selected font.
        density = LocalDensity.current,
        fontFamilyResolver = createFontFamilyResolver(LocalContext.current)
)
 */

/*
    val calculateIntrinsics: @Composable (String) -> ParagraphIntrinsics = @Composable {
        ParagraphIntrinsics(
            text = it,
            style = MaterialTheme.typography.bodyLarge,  // TODO: Get exact style of user-selected font.
            density = LocalDensity.current,
            fontFamilyResolver = createFontFamilyResolver(LocalContext.current)
        )
    }
 */

/* Currying version of calculateIntrinsics If I need to use intrinsics more then one time.
val calculateIntrinsics: @Composable (TextStyle) -> @Composable (String) -> ParagraphIntrinsics =
    @Composable { textStyle -> { str ->
        ParagraphIntrinsics(
        text = str,
        style = textStyle,
        density = LocalDensity.current,
        fontFamilyResolver = createFontFamilyResolver(LocalContext.current)
        )
    }
}
 */

/* Display overflowed text Solution1. List
        if (maxWidthOfStr > maxWidthOfTextField) {
            val delim = calculateStrWidth(str = " ")
            val words = offStr.split(" ")
            val wordsSubString: (Int, Int) -> String = { start, end ->
                words.subList(start,end).joinToString(" ")
            }

            var len: Float = 0f
            var startIdx = 0

            words.forEachIndexed { idx, w ->
                len += calculateStrWidth(w) + delim

                if (len >= maxWidthOfTextField) {
                    TypingTextField(text = wordsSubString(startIdx, idx))

                    startIdx = idx
                    len = calculateStrWidth(w) + delim
                }
            }
            // Display remained string.
            TypingTextField(text = wordsSubString(startIdx, words.size))
        } else {
            TypingTextField(text = offStr)
        }
 */