package com.example.calculatorapp

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.calculatorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var result = 0f
    private var operator = ' '
    private var num1 = ""
    private var num2 = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button0.setOnClickListener { displayNums('0') }
        binding.button1.setOnClickListener { displayNums('1') }
        binding.button2.setOnClickListener { displayNums('2') }
        binding.button3.setOnClickListener { displayNums('3') }
        binding.button4.setOnClickListener { displayNums('4') }
        binding.button5.setOnClickListener { displayNums('5') }
        binding.button6.setOnClickListener { displayNums('6') }
        binding.button7.setOnClickListener { displayNums('7') }
        binding.button8.setOnClickListener { displayNums('8') }
        binding.button9.setOnClickListener { displayNums('9') }
        binding.plusBtn.setOnClickListener { handleOperator('+') }
        binding.minusBtn.setOnClickListener { handleOperator('-') }
        binding.mulBtn.setOnClickListener { handleOperator('*') }
        binding.divisionBtn.setOnClickListener { handleOperator('/') }
        binding.pointBtn.setOnClickListener {
            if (operator == ' ' && !num1.contains(".")){displayNums('.')}
            if (operator == ' ' && !num2.contains(".")) {displayNums('.')}
        }
        binding.minusPlusBtn.setOnClickListener {
            // to allow user to use negative numbers
            if (operator == ' '){
                num1 = if(num1.startsWith("-")){
                    num1.substring(1, num1.length)
                } else {
                    "-$num1"
                }
                binding.displayTV.text = num1
            } else {
                num2 = if (num2.startsWith("-")) {
                    num2.substring(1, num2.length)
                } else {
                    "-$num2"
                }
                val text = num1 + operator + num2
                binding.displayTV.text = text
            }
        }

        binding.equalBtn.setOnClickListener { calculate() }
        binding.delBtn.setOnClickListener { deleteLast() }
        binding.clearBtn.setOnClickListener { clearAll() }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

//        val savedOutput = result
//        val savedOperator = operator    // Note to myself: I can do this and use variables in the statements below
//        val savedNum1 = num1
//        val savedNum2 = num2

        outState.putFloat("savedResult", result)
        outState.putChar("savedOperator", operator)
        outState.putString("savedNum1", num1)
        outState.putString("savedNum2", num2)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        result = savedInstanceState.getFloat("savedResult", 0f)
        operator = savedInstanceState.getChar("savedOperator", ' ')
        num1 = savedInstanceState.getString("savedNum1", "")
        num2 = savedInstanceState.getString("savedNum2", "")

        if(operator == ' ') {   // To display the "saved instance state" in the TextView (displayTV)
            binding.displayTV.text = num1
        } else {
            val text = num1 + operator + num2
            binding.displayTV.text = text
        }
    }

    // Note to myself: it didn't work well. Work on this later
    // To adjust the layout in different orientation
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.displayTV.setPadding(0,0,24,0)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.displayTV.setPadding(0,24,24,0)
        }
    }


    private fun displayNums(num: Char) {
        if (operator == ' ') {
            num1 += num
            binding.displayTV.text = num1
        } else {
            num2 += num
            val text = num1 + operator + num2
            binding.displayTV.text = text
        }
    }

    private fun handleOperator(op: Char) {
        operator = op
        val text = num1 + operator
        binding.displayTV.text = text
    }

    private fun calculate() {
        when (operator) {
            '+' -> result = num1.toFloat() + num2.toFloat()
            '-' -> result = num1.toFloat() - num2.toFloat()
            '*' -> result = num1.toFloat() * num2.toFloat()
            '/' -> if (num2.toFloat() != 0f) {  // So the user can't divide by 0
                result = num1.toFloat() / num2.toFloat()
            } else {
                Toast.makeText(this, "You can't divide by 0", Toast.LENGTH_LONG).show()
            }
        }
        num1 = result.toString()  // so the user can operate on the result of the previous calculation
        num2 = ""
        binding.displayTV.text = result.toString()
    }

    private fun deleteLast() {
        if (operator == ' ') {
            if (num1.isNotEmpty()) {
                num1 = num1.substring(0, num1.length - 1)
                if (num1.isEmpty()) {
                    binding.displayTV.text = "0"
                } else {
                    binding.displayTV.text = num1
                }
            } else {
                if (num2.isNotEmpty()) {
                    num2 = num2.substring(0, num2.length - 1)
                    val text = num1 + operator + num2
                    binding.displayTV.text = text
                } else {
                    operator = ' '
                    binding.displayTV.text = num1
                }
            }
        }
    }

    private fun clearAll() {
        result = 0f
        operator = ' '
        num1 = ""
        num2 = ""
        binding.displayTV.text = "0"
    }

}