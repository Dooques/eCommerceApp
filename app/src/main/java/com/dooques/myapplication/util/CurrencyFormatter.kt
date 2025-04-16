package com.dooques.myapplication.util

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class VisualTransformationForCurrencyFormat(
    private val fixedCursorAtTheEnd: Boolean = true,
    private val numberOfDecimals: Int = 2
): VisualTransformation {

    val symbols: DecimalFormatSymbols = DecimalFormat().decimalFormatSymbols

    override fun filter(text: AnnotatedString): TransformedText {
        val thousandsSeparator = symbols.groupingSeparator
        val decimalSeparator = symbols.decimalSeparator
        val zero = symbols.zeroDigit

        val inputText = text.text

        val intPart = inputText
            .dropLast(numberOfDecimals)
            .reversed()
            .chunked(3)
            .joinToString(thousandsSeparator.toString())
            .reversed()
            .ifEmpty {
                zero.toString()
            }

        var fractionPart = inputText.takeLast(numberOfDecimals).let {
            if (it.length != numberOfDecimals) {
                List(numberOfDecimals - it.length) {
                    zero
                }.joinToString("") + it
            } else {
                it
            }
        }

        val formattedNumber = intPart + decimalSeparator + fractionPart

        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles =  text.paragraphStyles
        )

        val offsetMapping = if (fixedCursorAtTheEnd) {
            FixedCursorOffsetMapping(
                contentLength = inputText.length,
                formattedContentLength = formattedNumber.length
            )
        } else {
            MovableCursorOffsetMapping(
                unmaskedText = text.toString(),
                maskedText = newText.toString(),
                decimalDigits = numberOfDecimals
            )
        }

        return TransformedText(newText, offsetMapping)
    }
}
private class FixedCursorOffsetMapping(
    private val contentLength: Int,
    private val formattedContentLength: Int
): OffsetMapping {
    override fun originalToTransformed(offset: Int) = formattedContentLength

    override fun transformedToOriginal(offset: Int) = contentLength
}

private class MovableCursorOffsetMapping(
    private val unmaskedText: String,
    private val maskedText: String,
    private val decimalDigits: Int
): OffsetMapping {
    override fun originalToTransformed(offset: Int) =
        when {
            unmaskedText.length <= decimalDigits -> {
                val result = maskedText.length - (unmaskedText.length - offset)
                result.coerceIn(0, maskedText.length)
            }
            else -> {
                val result = offset + offsetMaskCount(offset, maskedText)
                result.coerceIn(0, maskedText.length)
            }
        }

    override fun transformedToOriginal(offset: Int) =
        when {
            unmaskedText.isEmpty() -> 0
            unmaskedText.length <= decimalDigits -> {
                val result = offset
                result.coerceIn(0, unmaskedText.length)
            }
            else -> {
                val result = offset - maskedText.take(offset).count { !it.isDigit() }
                result.coerceIn(0, unmaskedText.length)
            }
        }

    private fun offsetMaskCount(offset: Int, maskedText: String): Int {
        var maskOffsetCount = 0
        var dataCount = 0
        for (maskChar in maskedText) {
            if (!maskChar.isDigit()) {
                maskOffsetCount++
            } else if (++dataCount > offset) {
                break
            }
        }
        return maskOffsetCount
    }
}
