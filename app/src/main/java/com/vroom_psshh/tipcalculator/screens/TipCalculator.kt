package com.vroom_psshh.tipcalculator.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vroom_psshh.tipcalculator.R
import java.text.NumberFormat


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TipCalculatorPreview() {
    BillAndTipAmount()
}

//Just to calculate tip amount
private fun calculateTip(amount: Double, tipPercent: Double, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount
    if (roundUp){
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}


@Composable
fun BillAndTipAmount(
    modifier: Modifier = Modifier
) {
    //States
    var bill by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }


    val amount = bill.toDoubleOrNull() ?: 0.0
    val tipAmount = tipPercent.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipAmount, roundUp)

    Box(modifier = modifier) {
        Column(
            modifier = modifier.fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Calculate Tips",
                fontSize = 17.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            EditTextField(
                bill, tipPercent,
                onValueChange = { value, fieldType ->
                    when (fieldType) {
                        FieldType.BILL -> bill = value
                        FieldType.TIP -> tipPercent = value
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            RoundTheTipRow(
                roundUp = roundUp, onRoundUpChanged = {
                    roundUp = it
                }
            )

            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "Tip Amount: $tip",
                textAlign = TextAlign.Left,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                softWrap = true
            )
        }
    }
}

@Composable
fun EditTextField(
    value: String,
    tipPercentage: String,
    onValueChange: (String, FieldType) -> Unit,
    keyboardOptions: KeyboardOptions
) {
    TextField(
        value = value,
        onValueChange = { newBillValue ->
            onValueChange(newBillValue, FieldType.BILL)
        },
        label = { Text("Enter Bill Amount", fontSize = 16.sp) },
        singleLine = true,
        leadingIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.currency),
                    contentDescription = null
                )
            }
        },
        keyboardOptions = keyboardOptions
    )

    TextField(
        value = tipPercentage, onValueChange = { newTipValue ->
            onValueChange(newTipValue, FieldType.TIP)
        },
        label = { Text(text = "Tip Percentage", fontSize = 16.sp) },
        modifier = Modifier.padding(top = 16.dp),
        singleLine = true,
        leadingIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.percent),
                    contentDescription = null
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun RoundTheTipRow(
    modifier: Modifier = Modifier,
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 38.dp, start = 52.dp, end = 52.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Round up tip?",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Switch(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
        )
    }
}

enum class FieldType {
    BILL, TIP
}