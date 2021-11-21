package com.lxlgarnett.colorpicker.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lxlgarnett.colorpicker.databinding.FragmentColorPickerBinding

class ColorPickerDialogFragment private constructor(private var initColor: Int) :
    DialogFragment() {

    companion object {
        private const val MAX_RGB_VALUE: Int = 255

        val TAG: String = ColorPickerDialogFragment::class.java.simpleName

        fun newInstance(initColor: Int = Color.WHITE): ColorPickerDialogFragment {
            return ColorPickerDialogFragment(initColor)
        }
    }

    private var _binding: FragmentColorPickerBinding? = null

    private val binding: FragmentColorPickerBinding
        get() = _binding!!

    private var listener: ColorPickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorPickerBinding.inflate(inflater, container, false)

        initView(initColor)

        binding.redSlider.addOnChangeListener { _, value, _ ->
            binding.redValue = value.toInt()
            updateColorView(binding.redValue, binding.greenValue, binding.blueValue)
        }

        binding.blueSlider.addOnChangeListener { _, value, _ ->
            binding.blueValue = value.toInt()
            updateColorView(binding.redValue, binding.greenValue, binding.blueValue)
        }

        binding.greenSlider.addOnChangeListener { _, value, _ ->
            binding.greenValue = value.toInt()
            updateColorView(binding.redValue, binding.greenValue, binding.blueValue)
        }

        binding.okButton.setOnClickListener {
            val color = Color.rgb(binding.redValue, binding.greenValue, binding.blueValue)
            listener?.onSelected(color)
            this.dismiss()
        }

        binding.cancelButton.setOnClickListener {
            listener?.onCanceled()
            this.dismiss()
        }
        return binding.root
    }

    fun setOnColorPickListener(listener: ColorPickListener) {
        this.listener = listener
    }

    private fun initView(initColor: Int) {
        try {
            binding.redValue = Color.red(initColor)
            binding.greenValue = Color.green(initColor)
            binding.blueValue = Color.blue(initColor)
            binding.colorHexValue = convertColorToHexString(initColor)
        } catch (exception: RuntimeException) {
            listener?.onFailed(exception)
        }
    }

    private fun updateColorView(red: Int, green: Int, blue: Int) {
        val color = Color.rgb(red, green, blue)
        binding.colorView.setBackgroundColor(color)
        binding.colorHexValue = convertColorToHexString(color)

        val textColor = getReverseColor(red, green, blue)
        binding.colorHexTextView.setTextColor(textColor)
    }

    private fun convertColorToHexString(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    private fun getReverseColor(red: Int, green: Int, blue: Int): Int {
        return Color.rgb(MAX_RGB_VALUE - red, MAX_RGB_VALUE - green, MAX_RGB_VALUE - blue)
    }

    interface ColorPickListener {
        fun onSelected(color: Int)
        fun onCanceled()
        fun onFailed(exception: RuntimeException)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}