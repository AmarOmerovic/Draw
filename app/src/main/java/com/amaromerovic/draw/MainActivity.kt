package com.amaromerovic.draw

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amaromerovic.draw.databinding.ActivityMainBinding
import com.amaromerovic.draw.databinding.BrushSizeDialogBinding
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.drawingView.setBrushSize(1f)

        binding.brushSize.setOnClickListener {
            showBrushSizeDialog()
        }

        binding.brushColor.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(this)
                .setTitle("Brush Color")
                .setDefaultColor(R.color.teal_700)
                .setColors(resources.getStringArray(R.array.themeColorHex))
                .setColorShape(ColorShape.SQAURE)
                .setColorListener { color, _ ->
                    binding.drawingView.setBrushColor(color)
                }
                .show()
        }

    }

    private fun showBrushSizeDialog() {
        val brushSizeDialog = Dialog(this)
        val dialogBinding = BrushSizeDialogBinding.inflate(layoutInflater)
        brushSizeDialog.setContentView(dialogBinding.root)
        brushSizeDialog.show()

        dialogBinding.brushSizeOf1dp.setOnClickListener {setSizeAndHideDialog(1f, brushSizeDialog)}
        dialogBinding.brushSizeOf5dp.setOnClickListener {setSizeAndHideDialog(5f, brushSizeDialog)}
        dialogBinding.brushSizeOf10dp.setOnClickListener {setSizeAndHideDialog(10f, brushSizeDialog)}
        dialogBinding.brushSizeOf15dp.setOnClickListener {setSizeAndHideDialog(15f, brushSizeDialog)}
        dialogBinding.brushSizeOf20dp.setOnClickListener {setSizeAndHideDialog(20f, brushSizeDialog)}
        dialogBinding.brushSizeOf25dp.setOnClickListener {setSizeAndHideDialog(25f, brushSizeDialog)}
        dialogBinding.brushSizeOf30dp.setOnClickListener {setSizeAndHideDialog(30f, brushSizeDialog)}
        dialogBinding.brushSizeOf35dp.setOnClickListener {setSizeAndHideDialog(35f, brushSizeDialog)}
        dialogBinding.brushSizeOf40dp.setOnClickListener {setSizeAndHideDialog(40f, brushSizeDialog)}
        dialogBinding.brushSizeOf45dp.setOnClickListener {setSizeAndHideDialog(45f, brushSizeDialog)}
        dialogBinding.brushSizeOf50dp.setOnClickListener {setSizeAndHideDialog(50f, brushSizeDialog)}

    }

    private fun setSizeAndHideDialog(size: Float, dialog: Dialog) {
        binding.drawingView.setBrushSize(size)
        dialog.hide()
    }
}