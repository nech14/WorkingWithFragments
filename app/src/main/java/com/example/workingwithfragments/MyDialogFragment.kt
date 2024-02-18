package com.example.workingwithfragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment


class MyDialogFragment : DialogFragment() {
    var onPositiveClickListener: DialogInterface.OnClickListener? = null
    var onNegativeClickListener: DialogInterface.OnClickListener? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Эй, войн!")
            .setMessage("Время пришло, чтоб выбрать как отобразить погоду!")
            .setPositiveButton("Кратко, у меня нет времени!"){ _, which ->
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.frameLayout, ShortWeatherFragment())
                    .commitAllowingStateLoss()
                onPositiveClickListener?.onClick(dialog, which)
                dismiss()
            }
            .setNegativeButton("Время есть!"){ _, which ->
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.frameLayout, DetailedWeatherFragment())
                    .commitAllowingStateLoss()
                onNegativeClickListener?.onClick(dialog, which)
                dismiss()
            }
            .create()
            .apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
    }

}