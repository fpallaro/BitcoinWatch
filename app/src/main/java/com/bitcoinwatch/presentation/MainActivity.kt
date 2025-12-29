package com.bitcoinwatch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.bitcoinwatch.data.BitcoinApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BitcoinWatchApp()
        }
    }
}

@Composable
fun BitcoinWatchApp() {
    var bitcoinPrice by remember { mutableStateOf<Double?>(null) }
    var priceChange24h by remember { mutableStateOf<Double?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var lastUpdate by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Fetch price on launch and every 30 seconds
    LaunchedEffect(Unit) {
        while (true) {
            try {
                val priceData = BitcoinApi.getBitcoinPrice()
                bitcoinPrice = priceData.first
                priceChange24h = priceData.second
                lastUpdate = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(java.util.Date())
                isLoading = false
            } catch (e: Exception) {
                isLoading = false
            }
            delay(30000) // Update every 30 seconds
        }
    }

    MaterialTheme {
        Scaffold(
            timeText = { TimeText() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1A1A1A)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Bitcoin icon/text
                    Text(
                        text = "₿",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF7931A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Price
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            indicatorColor = Color(0xFFF7931A)
                        )
                    } else {
                        bitcoinPrice?.let { price ->
                            val formatter = NumberFormat.getCurrencyInstance(Locale.US)
                            Text(
                                text = formatter.format(price),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        } ?: Text(
                            text = "Error",
                            fontSize = 18.sp,
                            color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 24h change
                    priceChange24h?.let { change ->
                        val changeColor = if (change >= 0) Color(0xFF00C853) else Color(0xFFFF5252)
                        val changeSign = if (change >= 0) "+" else ""
                        Text(
                            text = "$changeSign${String.format("%.2f", change)}%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = changeColor
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Last update
                    if (lastUpdate.isNotEmpty()) {
                        Text(
                            text = "Agg. $lastUpdate",
                            fontSize = 10.sp,
                            color = Color(0xFF808080)
                        )
                    }
                }
            }
        }
    }
}
