/*
 * Created by Xiaolei Li on 11/21/21, 11:57 PM
 * Copyright (c) 2021 Xiaolei Li.
 * All rights reserved.
 * Last modified 11/21/21, 11:57 PM
 */
package com.lxlgarnett.colorpicker.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lxlgarnett.colorpicker.databinding.FragmentColorPickerBinding

/**
 * Color Picker's Dialog Fragment class.
 *
 * @author Xiaolei Li
 */
class ColorPickerDialogFragment private constructor(private var initColor: Int) :
    DialogFragment() {

    companion object {

        /**
         * Max value of RGB.
         */
        private const val MAX_RGB_VALUE: Int = 255

        /**
         * TAG value of ColorPickerDialogFragment.
         */
        val TAG: String = ColorPickerDialogFragment::class.java.simpleName

        /**
         * Gets a new instance of ColorPickerDialogFragment.
         */
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

    /**
     * Sets on a listener to receive selected color value.
     *
     * @param listener Listener to receive selected color value.
     */
    fun setOnColorPickListener(listener: ColorPickListener) {
        this.listener = listener
    }

    /**
     * Initializes the view.
     *
     * @param initColor The initial color which is selected.
     * @throws RuntimePermission
     */
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

    /**
     * Uploads color view's background color by selected color and set the hex text of the selected color.
     *
     * @param red Selected color's red value.
     * @param green Selected color's green value.
     * @param blue Selected color's blue value.
     */
    private fun updateColorView(red: Int, green: Int, blue: Int) {
        val color = Color.rgb(red, green, blue)
        binding.colorView.setBackgroundColor(color)
        binding.colorHexValue = convertColorToHexString(color)

        val textColor = getReverseColor(red, green, blue)
        binding.colorHexTextView.setTextColor(textColor)
    }

    /**
     * Converts given color's int value to Hex string.
     *
     * @param color The color's int value to convert.
     * @return Converted Hex string of the given color.
     */
    private fun convertColorToHexString(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    /**
     * Gets reverse color by the given color.
     *
     * @param red Color's red value.
     * @param green Color's green value.
     * @param blue Color's blue value.
     * @return The reverse color's int value.
     */
    private fun getReverseColor(red: Int, green: Int, blue: Int): Int {
        return Color.rgb(MAX_RGB_VALUE - red, MAX_RGB_VALUE - green, MAX_RGB_VALUE - blue)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    /**
     * Listener to receive selected color's interface.
     *
     * @author Xiaolei Li
     */
    interface ColorPickListener {

        /**
         * The callback for selected a color succeed.
         *
         * @param color Selected color's int value.
         */
        fun onSelected(color: Int)

        /**
         * The callback for canceled color selecting
         */
        fun onCanceled()

        /**
         * The callback for something going wrong.
         *
         * @param exception The exception occurred.
         */
        fun onFailed(exception: RuntimeException)
    }
}