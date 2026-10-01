package com.example.a03_01_calculatortask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a03_01_calculatortask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var firstOperand: Double? = null
    private var pendingOperation: String? = null
    private var isStartingNewInput: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDigitButtons()
        setupOperationButtons()
        setupControlButtons()
    }

    private fun setupDigitButtons() {
        val digitButtons = listOf(
            binding.btn0 to "0",
            binding.btn1 to "1",
            binding.btn2 to "2",
            binding.btn3 to "3",
            binding.btn4 to "4",
            binding.btn5 to "5",
            binding.btn6 to "6",
            binding.btn7 to "7",
            binding.btn8 to "8",
            binding.btn9 to "9"
        )

        for ((button, digit) in digitButtons) {
            button.setOnClickListener { appendDigit(digit) }
        }

        binding.btnDot.setOnClickListener { appendDot() }
    }

    private fun setupOperationButtons() {
        val operations = listOf(
            binding.btnAdd to "+",
            binding.btnSubtract to "-",
            binding.btnMultiply to "*",
            binding.btnDivide to "/"
        )

        for ((button, op) in operations) {
            button.setOnClickListener { onOperatorPressed(op) }
        }

        binding.btnEquals.setOnClickListener { onEqualsPressed() }

        binding.btnPercent.setOnClickListener {
            val current = binding.tvDisplay.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            val percentValue = current / 100.0
            binding.tvDisplay.text = formatResult(percentValue)
            isStartingNewInput = true
        }
    }

    private fun setupControlButtons() {
        binding.btnClear.setOnClickListener {
            firstOperand = null
            pendingOperation = null
            isStartingNewInput = true
            binding.tvDisplay.text = "0"
        }

        binding.btnBackspace.setOnClickListener {
            if (isStartingNewInput) return@setOnClickListener

            val currentText = binding.tvDisplay.text.toString()
            if (currentText.length > 1) {
                binding.tvDisplay.text = currentText.dropLast(1)
            } else {
                binding.tvDisplay.text = "0"
                isStartingNewInput = true
            }
        }
    }

    private fun appendDigit(digit: String) {
        if (binding.tvDisplay.text.toString() == "Помилка") {
            binding.tvDisplay.text = "0"
        }

        if (isStartingNewInput || binding.tvDisplay.text.toString() == "0") {
            binding.tvDisplay.text = digit
            isStartingNewInput = false
        } else {
            binding.tvDisplay.append(digit)
        }
    }

    private fun appendDot() {
        if (isStartingNewInput) {
            binding.tvDisplay.text = "0."
            isStartingNewInput = false
            return
        }

        val currentText = binding.tvDisplay.text.toString()
        if (!currentText.contains('.')) {
            binding.tvDisplay.append(".")
        }
    }

    private fun onOperatorPressed(op: String) {
        val currentNumber = binding.tvDisplay.text.toString().toDoubleOrNull()

        if (currentNumber == null) {
            binding.tvDisplay.text = "Помилка"
            return
        }

        // Ланцюгові обчислення: якщо операція вже була обрана і юзер ввів наступне число
        if (firstOperand != null && pendingOperation != null && !isStartingNewInput) {
            val intermediate = evaluate(firstOperand!!, currentNumber, pendingOperation!!)
            if (intermediate == null) {
                handleError()
                return
            }
            firstOperand = intermediate
            binding.tvDisplay.text = formatResult(intermediate)
        } else {
            firstOperand = currentNumber
        }

        pendingOperation = op
        isStartingNewInput = true
    }

    private fun onEqualsPressed() {
        val op = pendingOperation ?: return
        val num1 = firstOperand ?: return
        val num2 = binding.tvDisplay.text.toString().toDoubleOrNull() ?: return

        val result = evaluate(num1, num2, op)
        if (result == null) {
            handleError()
            return
        }

        binding.tvDisplay.text = formatResult(result)
        firstOperand = null
        pendingOperation = null
        isStartingNewInput = true
    }

    private fun evaluate(num1: Double, num2: Double, op: String): Double? {
        return when (op) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            "/" -> if (num2 == 0.0) null else num1 / num2
            else -> null
        }
    }

    private fun handleError() {
        binding.tvDisplay.text = "Помилка"
        firstOperand = null
        pendingOperation = null
        isStartingNewInput = true
    }

    private fun formatResult(value: Double): String {
        return if (value % 1.0 == 0.0 && !value.isInfinite() && !value.isNaN()) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }
}