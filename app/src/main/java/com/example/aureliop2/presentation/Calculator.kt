package com.example.aureliop2.presentation

import java.math.BigDecimal

/** Estado inmutable de la calculadora. */
data class CalcState(
    val display: String = "0",
    val acc: Double? = null,
    val op: Char? = null,
    val newEntry: Boolean = true,
)

private const val MAX_DIGITS = 9
private const val ERROR = "Error"

fun CalcState.press(key: String): CalcState {
    if (display == ERROR && key != "C") return this
    return when (key) {
        "C" -> CalcState()
        "⌫" -> backspace()
        "%" -> copy(display = format(display.toDouble() / 100), newEntry = true)
        "." -> dot()
        "+", "−", "×", "÷" -> operator(key[0])
        "=" -> equals()
        else -> digit(key)
    }
}

private fun CalcState.digit(d: String): CalcState {
    if (newEntry || display == "0") return copy(display = d, newEntry = false)
    if (display.count { it.isDigit() } >= MAX_DIGITS) return this
    return copy(display = display + d)
}

private fun CalcState.dot(): CalcState = when {
    newEntry -> copy(display = "0.", newEntry = false)
    '.' in display -> this
    else -> copy(display = "$display.")
}

private fun CalcState.backspace(): CalcState {
    if (newEntry) return this
    val next = display.dropLast(1)
    return copy(display = if (next.isEmpty() || next == "-") "0" else next)
}

private fun CalcState.operator(next: Char): CalcState {
    val current = display.toDouble()
    val pending = op
    val base = acc
    if (pending != null && base != null && !newEntry) {
        val result = apply(base, current, pending) ?: return CalcState(display = ERROR)
        return CalcState(display = format(result), acc = result, op = next, newEntry = true)
    }
    return copy(acc = current, op = next, newEntry = true)
}

private fun CalcState.equals(): CalcState {
    val pending = op
    val base = acc
    if (pending == null || base == null) return this
    val result = apply(base, display.toDouble(), pending) ?: return CalcState(display = ERROR)
    return CalcState(display = format(result))
}

private fun apply(a: Double, b: Double, op: Char): Double? = when (op) {
    '+' -> a + b
    '−' -> a - b
    '×' -> a * b
    '÷' -> if (b == 0.0) null else a / b
    else -> null
}

private fun format(value: Double): String {
    if (value.isNaN() || value.isInfinite()) return ERROR
    val abs = Math.abs(value)
    if (abs != 0.0 && (abs >= 1e9 || abs < 1e-6)) return String.format("%.3e", value)
    val text = BigDecimal(value).setScale(6, java.math.RoundingMode.HALF_UP)
        .stripTrailingZeros().toPlainString()
    return if (text == "-0") "0" else text
}
