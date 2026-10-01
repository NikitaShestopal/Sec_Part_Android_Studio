package com.example.a03_01_calculatortask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val tag = "CalculatorApp"

    private lateinit var etFirstNumber: EditText
    private lateinit var etSecondNumber: EditText
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFirstNumber = findViewById(R.id.etFirstNumber)
        etSecondNumber = findViewById(R.id.etSecondNumber)
        tvResult = findViewById(R.id.tvResult)

        val btnAdd: Button = findViewById(R.id.btnAdd)
        val btnSubtract: Button = findViewById(R.id.btnSubtract)
        val btnMultiply: Button = findViewById(R.id.btnMultiply)
        val btnDivide: Button = findViewById(R.id.btnDivide)
        val btnClear: Button = findViewById(R.id.btnClear)

        btnAdd.setOnClickListener { performOperation('+') }
        btnSubtract.setOnClickListener { performOperation('-') }
        btnMultiply.setOnClickListener { performOperation('*') }
        btnDivide.setOnClickListener { performOperation('/') }

        btnClear.setOnClickListener {
            Log.d(tag, "Очищення полів вводу та результату")
            etFirstNumber.text.clear()
            etSecondNumber.text.clear()
            tvResult.text = "Результат:"
        }
    }

    private fun performOperation(operation: Char) {
        val firstStr = etFirstNumber.text.toString().trim()
        val secondStr = etSecondNumber.text.toString().trim()

        if (firstStr.isEmpty() || secondStr.isEmpty()) {
            val errorText = "Будь ласка, введіть обидва числа"
            Log.w(tag, "Спроба виконати '$operation': одне або обидва поля порожні")
            tvResult.text = errorText
            return
        }

        val num1 = firstStr.toDoubleOrNull()
        val num2 = secondStr.toDoubleOrNull()

        if (num1 == null || num2 == null) {
            val errorText = "Некоректний формат числа"
            Log.w(tag, "Помилка парсингу рядків у Double: '$firstStr', '$secondStr'")
            tvResult.text = errorText
            return
        }

        Log.d(tag, "Операція: $num1 $operation $num2")

        if (operation == '/' && num2 == 0.0) {
            val zeroDivText = "Помилка: ділення на нуль неможливе!"
            Log.w(tag, "Спроба ділення на нуль: $num1 / $num2")
            tvResult.text = zeroDivText
            return
        }

        val result = when (operation) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '*' -> num1 * num2
            '/' -> num1 / num2
            else -> 0.0
        }

        val formattedResult = if (result % 1.0 == 0.0) {
            result.toLong().toString()
        } else {
            result.toString()
        }

        Log.d(tag, "Успішний результат: $formattedResult")
        tvResult.text = "Результат: $formattedResult"
    }
}