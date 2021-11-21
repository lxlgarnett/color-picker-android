package com.lxlgarnett.colorpicker.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.lxlgarnett.colorpicker.databinding.FragmentColorPickerBinding

class ColorPickerDialogFragment private constructor(private var initColor: Int) :
    DialogFragment() {

    companion object {
        private val TAG: String = ColorPickerDialogFragment::class.java.simpleName

        fun newInstance(initColor: Int = Color.WHITE): ColorPickerDialogFragment {
            val instance = ColorPickerDialogFragment(initColor)
            instance.isCancelable = false
            return instance
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

        binding.redValue = Color.red(initColor)
        binding.greenValue = Color.green(initColor)
        binding.blueValue = Color.blue(initColor)
        binding.colorHexValue = String.format("#%06X", 0xFFFFFF and initColor)
        binding.redSlider.addOnChangeListener { _, value, _ ->
            binding.redValue = value.toInt()
            val color = Color.rgb(
                binding.redValue,
                binding.greenValue,
                binding.blueValue
            )
            binding.colorView.setBackgroundColor(color)
            binding.colorHexValue = String.format("#%06X", 0xFFFFFF and color)

            val textColor =
                Color.rgb(255 - binding.redValue, 255 - binding.greenValue, 255 - binding.blueValue)
            binding.colorHexTextView.setTextColor(textColor)
        }

        binding.blueSlider.addOnChangeListener { _, value, _ ->
            binding.blueValue = value.toInt()
            val color = Color.rgb(
                binding.redValue,
                binding.greenValue,
                binding.blueValue
            )
            binding.colorView.setBackgroundColor(color)
            binding.colorHexValue = String.format("#%06X", 0xFFFFFF and color)
            val textColor =
                Color.rgb(255 - binding.redValue, 255 - binding.greenValue, 255 - binding.blueValue)
            binding.colorHexTextView.setTextColor(textColor)
        }

        binding.greenSlider.addOnChangeListener { _, value, _ ->
            binding.greenValue = value.toInt()
            val color = Color.rgb(
                binding.redValue,
                binding.greenValue,
                binding.blueValue
            )
            binding.colorView.setBackgroundColor(color)
            binding.colorHexValue = String.format("#%06X", 0xFFFFFF and color)
            val textColor =
                Color.rgb(255 - binding.redValue, 255 - binding.greenValue, 255 - binding.blueValue)
            binding.colorHexTextView.setTextColor(textColor)
        }
        return binding.root
    }

    fun setOnColorPickListener(listener: ColorPickListener) {
        this.listener = listener
    }

    fun show(childFragmentManager: FragmentManager) {
        show(childFragmentManager, TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface ColorPickListener {
        fun onSelected()
        fun onCanceled()
    }

}