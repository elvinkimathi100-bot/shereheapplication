package com.mark.shereheke.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

// ─────────────────────────────────────────────
//  Color palette
// ─────────────────────────────────────────────
private val NavyDeep    = Color(0xFF0F2027)
private val NavyMid     = Color(0xFF16213E)
private val NavyLight   = Color(0xFF0F3460)
private val Gold        = Color(0xFFD4AF37)
private val GoldLight   = Color(0xFFFFF5CC)
private val GoldFaint   = Color(0x30D4AF37)
private val GoldBorder  = Color(0x50D4AF37)
private val White       = Color(0xFFFFFFFF)

// ─────────────────────────────────────────────
//  Pill data
// ─────────────────────────────────────────────
private val eventPills = listOf("Galas", "Concerts", "Retreats", "Dining", "Wellness")

// ─────────────────────────────────────────────
//  SplashScreen composable
// ─────────────────────────────────────────────
@Composable
fun SplashScreen(onSplashFinished: () -> Unit = {}) {

    // Auto-navigate after 3 seconds
    LaunchedEffect(Unit) {
        delay(3_000)
        onSplashFinished()
    }

    // ── Entrance animations ──────────────────
    val contentAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "contentAlpha"
    )
    val contentOffsetY by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "contentOffsetY"
    )

    // ── Pulse / ring animations ──────────────
    val infiniteTransition = rememberInfiniteTransition(label = "splash_infinite")

    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.92f, targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glowScale"
    )
    val ring1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.35f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "ring1Alpha"
    )
    val ring1Scale by infiniteTransition.animateFloat(
        initialValue = 0.88f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "ring1Scale"
    )
    val ring2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.25f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, delayMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "ring2Alpha"
    )
    val ring2Scale by infiniteTransition.animateFloat(
        initialValue = 0.88f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, delayMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "ring2Scale"
    )
    val ring3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.15f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, delayMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "ring3Alpha"
    )
    val ring3Scale by infiniteTransition.animateFloat(
        initialValue = 0.88f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, delayMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "ring3Scale"
    )

    // ── Sparkle animations ───────────────────
    val sparkle1 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, delayMillis = 200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "sp1"
    )
    val sparkle2 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, delayMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "sp2"
    )
    val sparkle3 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, delayMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "sp3"
    )

    // ── Loader shimmer ───────────────────────
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -200f, targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmer"
    )

    // ── Star rotation ────────────────────────
    val starRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "starRot"
    )

    // ── Layout ───────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(NavyDeep, NavyMid, NavyLight),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // ── Canvas layer: glow, rings, sparkles, corner dots, divider lines
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f

            // Radial gold glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GoldFaint, Color.Transparent),
                    center = Offset(cx, cy),
                    radius = 260f * glowScale
                ),
                radius = 260f * glowScale,
                center = Offset(cx, cy)
            )

            // Expanding rings
            drawRing(cx, cy, 170f * ring1Scale, ring1Alpha)
            drawRing(cx, cy, 230f * ring2Scale, ring2Alpha)
            drawRing(cx, cy, 300f * ring3Scale, ring3Alpha)

            // Corner accent dots
            val dotPositions = listOf(
                Offset(52f, 160f), Offset(size.width - 52f, 160f),
                Offset(52f, size.height - 180f), Offset(size.width - 52f, size.height - 180f)
            )
            dotPositions.forEach { pos ->
                drawCircle(color = GoldBorder, radius = 5f, center = pos)
            }

            // Top horizontal accent line
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, GoldBorder, Color.Transparent),
                    startX = 70f, endX = size.width - 70f
                ),
                start = Offset(70f, 155f),
                end = Offset(size.width - 70f, 155f),
                strokeWidth = 1f
            )

            // Bottom horizontal accent line
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, GoldBorder, Color.Transparent),
                    startX = 70f, endX = size.width - 70f
                ),
                start = Offset(70f, size.height - 175f),
                end = Offset(size.width - 70f, size.height - 175f),
                strokeWidth = 1f
            )

            // Sparkles
            drawSparkle(Offset(90f, 240f), sparkle1)
            drawSparkle(Offset(size.width - 90f, 310f), sparkle2)
            drawSparkle(Offset(80f, cy + 60f), sparkle3)
            drawSparkle(Offset(size.width - 80f, cy - 60f), sparkle1)
            drawSparkle(Offset(100f, size.height - 280f), sparkle2)
            drawSparkle(Offset(size.width - 100f, size.height - 300f), sparkle3)
        }

        // ── Foreground content column ─────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = contentAlpha
                    translationY = contentOffsetY
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Star / diamond icon ───────────
            Canvas(
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer { rotationZ = starRotation * 0.08f }
            ) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val r = size.width / 2f

                // Outer diamond outline
                val outerDiamond = Path().apply {
                    moveTo(cx, cy - r * 0.94f)
                    lineTo(cx + r * 0.94f, cy)
                    lineTo(cx, cy + r * 0.94f)
                    lineTo(cx - r * 0.94f, cy)
                    close()
                }
                drawPath(outerDiamond, color = Gold.copy(alpha = 0.4f), style = Stroke(width = 1.5f))

                // Inner diamond outline
                val innerDiamond = Path().apply {
                    moveTo(cx, cy - r * 0.65f)
                    lineTo(cx + r * 0.65f, cy)
                    lineTo(cx, cy + r * 0.65f)
                    lineTo(cx - r * 0.65f, cy)
                    close()
                }
                drawPath(innerDiamond, color = Gold.copy(alpha = 0.25f), style = Stroke(width = 1f))

                // 5-pointed star (centre)
                val starPath = buildStarPath(cx, cy, r * 0.52f, r * 0.22f, 5)
                drawPath(starPath, color = Gold)

                // Cardinal spikes
                val spikeLen = r * 0.14f
                val spikes = listOf(
                    Offset(cx, cy - r) to Offset(cx, cy - r + spikeLen),
                    Offset(cx, cy + r) to Offset(cx, cy + r - spikeLen),
                    Offset(cx - r, cy) to Offset(cx - r + spikeLen, cy),
                    Offset(cx + r, cy) to Offset(cx + r - spikeLen, cy)
                )
                spikes.forEach { (start, end) ->
                    drawLine(Gold.copy(alpha = 0.6f), start, end, strokeWidth = 2f,
                        cap = StrokeCap.Round)
                }
            }

            Spacer(Modifier.height(22.dp))

            // ── App name ─────────────────────
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = White)) { append("SHERE") }
                    withStyle(SpanStyle(color = Gold))  { append("HE") }
                },
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            // ── Tagline ───────────────────────
            Text(
                text = "HOTEL EVENTS & EXPERIENCES",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.5.sp,
                color = Gold.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // ── Gold ornament divider ─────────
            Row(
                modifier = Modifier.width(220.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, GoldBorder)
                            )
                        )
                )
                Spacer(Modifier.width(10.dp))
                // Small rotated square (diamond ornament)
                Canvas(modifier = Modifier.size(10.dp)) {
                    rotate(45f) {
                        drawRect(
                            color = Color.Transparent,
                            topLeft = Offset(2f, 2f),
                            size = androidx.compose.ui.geometry.Size(size.width - 4f, size.height - 4f)
                        )
                        drawRect(
                            color = Gold,
                            topLeft = Offset(2f, 2f),
                            size = androidx.compose.ui.geometry.Size(size.width - 4f, size.height - 4f),
                            style = Stroke(width = 1.5f)
                        )
                    }
                }
                Spacer(Modifier.width(10.dp))
                Box(
                    Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(GoldBorder, Color.Transparent)
                            )
                        )
                )
            }

            Spacer(Modifier.height(22.dp))

            // ── Event category pills ──────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        eventPills.take(3).forEach { label -> EventPill(label) }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        eventPills.drop(3).forEach { label -> EventPill(label) }
                    }
                }
            }

            Spacer(Modifier.height(52.dp))

            // ── Loading bar (shimmer) ─────────
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(2.dp)
                        .background(White.copy(alpha = 0.1f), shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color.Transparent, GoldLight, Gold, GoldLight, Color.Transparent),
                                    startX = shimmerOffset,
                                    endX = shimmerOffset + 200f
                                ),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
                            )
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "LOADING",
                    fontSize = 9.sp,
                    letterSpacing = 3.sp,
                    color = White.copy(alpha = 0.3f)
                )
            }
        }

        // ── Version text (bottom) ─────────────
        Text(
            text = "v1.0.0",
            fontSize = 9.sp,
            letterSpacing = 2.sp,
            color = White.copy(alpha = 0.18f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
        )
    }
}

// ─────────────────────────────────────────────
//  Event pill composable
// ─────────────────────────────────────────────
@Composable
private fun EventPill(label: String) {
    Box(
        modifier = Modifier
            .background(
                color = Gold.copy(alpha = 0.12f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            color = Gold.copy(alpha = 0.9f)
        )
    }
}

// ─────────────────────────────────────────────
//  DrawScope helpers
// ─────────────────────────────────────────────
private fun DrawScope.drawRing(cx: Float, cy: Float, radius: Float, alpha: Float) {
    drawCircle(
        color = Gold.copy(alpha = alpha),
        radius = radius,
        center = Offset(cx, cy),
        style = Stroke(width = 1f)
    )
}

private fun DrawScope.drawSparkle(center: Offset, alpha: Float) {
    drawCircle(
        color = Gold.copy(alpha = alpha),
        radius = 3f * alpha,
        center = center
    )
}

// ─────────────────────────────────────────────
//  Star path builder
// ─────────────────────────────────────────────
private fun buildStarPath(cx: Float, cy: Float, outerR: Float, innerR: Float, points: Int): Path {
    val path = Path()
    val angleStep = Math.PI / points
    for (i in 0 until points * 2) {
        val angle = i * angleStep - Math.PI / 2
        val r = if (i % 2 == 0) outerR else innerR
        val x = cx + (r * Math.cos(angle)).toFloat()
        val y = cy + (r * Math.sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

// ─────────────────────────────────────────────
//  Preview
// ─────────────────────────────────────────────
@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
