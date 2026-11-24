
package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.ceil

private val Purple = Color(0xFF7257E8)
private val Background = Color(0xFFF7F6FC)
private val Dark = Color(0xFF252238)

private const val DEFAULT_PEOPLE = 5
private const val DEFAULT_TIP = 16
private const val ROUNDING_STEP = 0.20

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalcApp()
        }
    }
}

@Composable
fun TipCalcApp() {
    var billAmount by remember { mutableStateOf("") }
    var selectedTip by remember { mutableStateOf(DEFAULT_TIP) }
    var customTip by remember { mutableStateOf("") }
    var people by remember { mutableIntStateOf(DEFAULT_PEOPLE) }

    val tipPercentage = customTip.toDoubleOrNull() ?: selectedTip.toDouble()
    val bill = billAmount.toDoubleOrNull() ?: 0.0

    val tipAmount = bill * tipPercentage / 100
    val totalAmount = bill + tipAmount
    val perPerson = totalAmount / people

    val roundedPerPerson = if (perPerson > 0) {
        ceil((perPerson / ROUNDING_STEP) - 0.000000001) * ROUNDING_STEP
    } else {
        0.0
    }

    fun money(amount: Double): String {
        return "$" + String.format(Locale.US, "%.2f", amount)
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Purple,
            background = Background
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "TipCalc",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Dark
                )

                Text(
                    text = "The easiest way to split the bill.",
                    fontSize = 15.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(30.dp))

                // BILL AMOUNT
                Text(
                    text = "Bill amount",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Dark
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = billAmount,
                    onValueChange = { billAmount = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("0.00") },
                    leadingIcon = { Text("$", fontSize = 22.sp) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(25.dp))

                // TIP SELECTION
                Text(
                    text = "Select tip",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Dark
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(10, 15, 20, 25).forEach { percent ->
                        FilterChip(
                            selected = selectedTip == percent && customTip.isEmpty(),
                            onClick = {
                                selectedTip = percent
                                customTip = ""
                            },
                            label = { Text("$percent%") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = customTip,
                    onValueChange = { customTip = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Custom tip percentage") },
                    suffix = { Text("%") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Text(
                    text = "Starting tip: 16%",
                    color = Purple,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(25.dp))

                // EXERCISE 1.1: SPLIT BETWEEN 5 PEOPLE
                Text(
                    text = "Split between",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Dark
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$people ${if (people == 1) "person" else "people"}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = {
                                if (people > 1) people--
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("−", fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = { people++ },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+", fontSize = 22.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // RESULTS CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Purple
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "TOTAL PER PERSON",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = money(roundedPerPerson),
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Rounded up to nearest $0.20",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        HorizontalDivider(
                            color = Color.White.copy(alpha = 0.25f)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        ResultRow("Tip amount", money(tipAmount))

                        Spacer(modifier = Modifier.height(12.dp))

                        ResultRow("Total bill", money(totalAmount))

                        Spacer(modifier = Modifier.height(12.dp))

                        ResultRow("People", people.toString())

                        Spacer(modifier = Modifier.height(12.dp))

                        ResultRow("Each person pays", money(roundedPerPerson))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // EXERCISE 1.3: CLEAR / RESET BUTTON
                OutlinedButton(
                    onClick = {
                        billAmount = ""
                        customTip = ""
                        selectedTip = DEFAULT_TIP
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Clear / Reset",
                        color = Purple,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Personalized for N = 011 | P = 5 | Δ = $0.20 | R = 16%",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 15.sp
        )

        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
