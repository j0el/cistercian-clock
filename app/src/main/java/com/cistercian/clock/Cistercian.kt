package com.cistercian.clock

data class Seg(val x1: Float, val y1: Float, val x2: Float, val y2: Float)

// Unit-square stroke table — same logic as the Rust reference implementation.
// TL=(0,0), TR=(1,0), BR=(1,1), BL=(0,1)
private val TL = 0f to 0f
private val TR = 1f to 0f
private val BR = 1f to 1f
private val BL = 0f to 1f

private fun rawStrokes(digit: Int): List<Pair<Pair<Float, Float>, Pair<Float, Float>>> = when (digit) {
    1 -> listOf(TL to TR)
    2 -> listOf(BL to BR)
    3 -> listOf(TL to BR)
    4 -> listOf(BL to TR)
    5 -> listOf(TL to TR, BL to TR)
    6 -> listOf(TR to BR)
    7 -> listOf(TL to TR, TR to BR)
    8 -> listOf(BL to BR, TR to BR)
    9 -> listOf(TL to TR, BL to BR, TR to BR)
    else -> emptyList()
}

enum class Quadrant { UNITS, TENS, HUNDREDS, THOUSANDS }

/**
 * Maps a unit-square point (u,v) into canvas space for the given quadrant.
 *
 * Layout:
 *   UNITS     = upper-right  (sx=+1, origin=top,    sy=+1 → grows downward)
 *   TENS      = upper-left   (sx=-1, origin=top,    sy=+1)
 *   HUNDREDS  = lower-right  (sx=+1, origin=bottom, sy=-1 → grows upward)
 *   THOUSANDS = lower-left   (sx=-1, origin=bottom, sy=-1)
 *
 * cx, top, bottom: canvas coordinates of stave centre-x, stave top, stave bottom
 * quadW: width of each quadrant arm
 * halfH: height of each quadrant arm
 */
fun mapPoint(q: Quadrant, u: Float, v: Float, cx: Float, top: Float, bottom: Float, quadW: Float, halfH: Float): Pair<Float, Float> {
    val (sx, yOrigin, sy) = when (q) {
        Quadrant.UNITS     ->  Triple( 1f, top,    1f)
        Quadrant.TENS      ->  Triple(-1f, top,    1f)
        Quadrant.HUNDREDS  ->  Triple( 1f, bottom,-1f)
        Quadrant.THOUSANDS ->  Triple(-1f, bottom,-1f)
    }
    return (cx + sx * u * quadW) to (yOrigin + sy * v * halfH)
}

fun cistercianStrokes(digit: Int, q: Quadrant, cx: Float, top: Float, bottom: Float, quadW: Float, halfH: Float): List<Seg> {
    if (digit == 0) return emptyList()
    return rawStrokes(digit).map { (p1, p2) ->
        val (x1, y1) = mapPoint(q, p1.first, p1.second, cx, top, bottom, quadW, halfH)
        val (x2, y2) = mapPoint(q, p2.first, p2.second, cx, top, bottom, quadW, halfH)
        Seg(x1, y1, x2, y2)
    }
}

data class TimeDigits(val units: Int, val tens: Int, val hundreds: Int, val thousands: Int)

/** Decomposes HHMM into four Cistercian digits. */
fun decomposeTime(hour: Int, minute: Int): TimeDigits {
    val n = hour * 100 + minute
    return TimeDigits(
        units     = n % 10,
        tens      = (n / 10) % 10,
        hundreds  = (n / 100) % 10,
        thousands = (n / 1000) % 10,
    )
}

/** Decomposes a date into four Cistercian digits: month in thousands/hundreds, day in tens/units. */
fun decomposeDate(month: Int, day: Int): TimeDigits {
    return TimeDigits(
        units     = day % 10,
        tens      = day / 10,
        hundreds  = month % 10,
        thousands = month / 10,
    )
}
