package com.example.tip_checker

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val INITIAL_TIP_PERCENTAGE = 50
private const val TAG = "MainActivity"
//To check if the seekbar have been linked with the SeekBarChangedListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //make reference to the seekbar view by calling the ID and
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "OnProgressChanged $progress")
                tvTipPercentage.text = "$INITIAL_TIP_PERCENTAGE"

                //To make the progressbar to control the tip percentage, we simply link the two
                tvTipPercentage.text = "$progress%"
                //tvTipPercentage is asyned to the progress which is an int and its converted to text
                computeTipAndTotal()
                //This will make the total amount and Tip percentage to change with the seekBar
                updateSavingDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        tvEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s ")
                computeTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }


        })
    }

    private fun updateSavingDescription(savingPercentage: Int) {
        val savingDescription: String
        when (savingPercentage) {
            in 0..9 -> savingDescription = "Too Small"
            in 10..19 -> savingDescription = "Not Bad!"
            in 20..29 -> savingDescription = "Moving Up"
            in 30..50 -> savingDescription = "Great!"
            else -> savingDescription = "Excellent"

        }
        tvSaveDescription.text = savingDescription.toString()
        val color = ArgbEvaluator().evaluate(
            savingPercentage.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.colorWorst),
            ContextCompat.getColor(this, R.color.colorBest)
        ) as Int
        tvSaveDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (tvEdit.text.isEmpty()) {
            tvFinal.text = ""
            tvfinalTotal.text = ""
            return

        }
        //Get the value of the base and tip percentage
        //We get the baseAmount from the tvEdit, we converted to double for the sake of calculation
        val baseAmount = tvEdit.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val amount_to_safe = baseAmount * tipPercent / 100
        //TotalAmount = tipAmount + baseAmount
        val amountLeft = baseAmount - amount_to_safe
        //make the textviews to display the calculated totalAmount and tipAmount by calling their ids
        val yearlySavings = amount_to_safe * 12

        tvfinalTotal.text = amountLeft.toString()
        tvFinal.text = amount_to_safe.toString()
        peryear.text = yearlySavings.toString()


    }
}