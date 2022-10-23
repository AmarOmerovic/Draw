package com.amaromerovic.draw

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.amaromerovic.draw.databinding.ActivityMainBinding
import com.amaromerovic.draw.databinding.BrushSizeDialogBinding
import com.amaromerovic.draw.databinding.CustomProgressDialogBinding
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var progressDialog: Dialog? = null

    private val readStoragePerm =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                getImage.launch(intent)
            }
        }

    private val getImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val uri = intent?.data
                binding.imageViewContainer.setImageURI(uri)
            }
        }


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

        binding.undo.setOnClickListener {
            binding.drawingView.removeLastLine()
        }

        binding.gallery.setOnClickListener {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //
            } else {
                readStoragePerm.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        binding.save.setOnClickListener {
            lifecycleScope.launch {
                showCustomProgress()
                saveBitmap(getBitmapFromView(binding.frameLayoutContainer))
            }
        }

    }

    private fun showBrushSizeDialog() {
        val brushSizeDialog = Dialog(this)
        val dialogBinding = BrushSizeDialogBinding.inflate(layoutInflater)
        brushSizeDialog.setContentView(dialogBinding.root)
        brushSizeDialog.setTitle("Brush size")
        brushSizeDialog.show()

        dialogBinding.brushSizeOf1dp.setOnClickListener {
            setSizeAndHideDialog(
                1f,
                brushSizeDialog
            )
        }
        dialogBinding.brushSizeOf5dp.setOnClickListener {
            setSizeAndHideDialog(
                5f,
                brushSizeDialog
            )
        }
        dialogBinding.brushSizeOf10dp.setOnClickListener {
            setSizeAndHideDialog(
                10f,
                brushSizeDialog
            )
        }
        dialogBinding.brushSizeOf15dp.setOnClickListener {
            setSizeAndHideDialog(
                15f,
                brushSizeDialog
            )
        }
        dialogBinding.brushSizeOf20dp.setOnClickListener {
            setSizeAndHideDialog(
                20f,
                brushSizeDialog
            )
        }
        dialogBinding.brushSizeOf25dp.setOnClickListener {
            setSizeAndHideDialog(
                25f,
                brushSizeDialog
            )
        }
        dialogBinding.brushSizeOf30dp.setOnClickListener {
            setSizeAndHideDialog(
                30f,
                brushSizeDialog
            )
        }
        dialogBinding.brushSizeOf35dp.setOnClickListener {
            setSizeAndHideDialog(
                35f,
                brushSizeDialog
            )
        }
        dialogBinding.brushSizeOf40dp.setOnClickListener {
            setSizeAndHideDialog(
                40f,
                brushSizeDialog
            )
        }
        dialogBinding.brushSizeOf45dp.setOnClickListener {
            setSizeAndHideDialog(
                45f,
                brushSizeDialog
            )
        }
        dialogBinding.brushSizeOf50dp.setOnClickListener {
            setSizeAndHideDialog(
                50f,
                brushSizeDialog
            )
        }

    }

    private fun setSizeAndHideDialog(size: Float, dialog: Dialog) {
        binding.drawingView.setBrushSize(size)
        dialog.hide()
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val background = view.background

        if (background != null) {
            background.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }

        view.draw(canvas)
        return returnedBitmap
    }

    private suspend fun saveBitmap(bitmap: Bitmap?): String {
        var result = ""

        withContext(Dispatchers.IO) {
            if (bitmap != null) {
                try {
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, bytes)

                    val file = File(
                        externalCacheDir,
                        "Draw_" + (System.currentTimeMillis() / 1000) + ".jpg"
                    )
                    val fileOutputStream = FileOutputStream(file)


                    fileOutputStream.write(bytes.toByteArray())
                    fileOutputStream.close()

                    result = file.absolutePath

                    runOnUiThread {
                        hideCustomProgress()
                        if (result.isNotEmpty()) {
                            Toast.makeText(
                                this@MainActivity,
                                "File saved successfully: $result!",
                                Toast.LENGTH_LONG
                            ).show()
                            shareImage(
                                FileProvider.getUriForFile(
                                    this@MainActivity,
                                    "com.amaromerovic.draw.fileprovider",
                                    file
                                )
                            )
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Something went wrong saving the image!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: java.lang.Exception) {
                    result = ""
                    e.printStackTrace()
                    runOnUiThread {
                        hideCustomProgress()
                        Toast.makeText(
                            this@MainActivity,
                            "Something went wrong saving the image!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        return result
    }


    private fun showCustomProgress() {
        progressDialog = Dialog(this@MainActivity)
        val dialogBinding = CustomProgressDialogBinding.inflate(layoutInflater)
        progressDialog!!.setContentView(dialogBinding.root)
        progressDialog!!.show()
    }

    private fun hideCustomProgress() {
        progressDialog?.hide()
    }

    private fun shareImage(uri: Uri) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "image/jpeg"
        startActivity(Intent.createChooser(intent, "Share image via "))
    }
}