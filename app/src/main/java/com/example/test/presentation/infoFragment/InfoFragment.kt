package com.example.test.presentation.infoFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.test.R
import com.example.test.databinding.FragmentInfoBinding
import com.example.test.presentation.MainActivity
import com.example.test.presentation.OnBackPressedFrament
import com.example.test.utils.atStartOfDay
import com.example.test.utils.getCroppedBitmap
import com.example.test.utils.getDateTimeDay
import com.example.test.utils.getHours
import java.util.*

class InfoFragment : Fragment(R.layout.fragment_info), OnBackPressedFrament {
    private val binding: FragmentInfoBinding by viewBinding()
    companion object {
        fun newInstance() = InfoFragment()
    }
    val args: InfoFragmentArgs by navArgs()
    private lateinit var viewModel: InfoViewModel
private lateinit var context: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context as MainActivity
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewName.text = args.name
        binding.textViewCalendar.text = getDateTimeDay(args.calendar)
        binding.textViewTime.text = getHours(atStartOfDay(Date(args.time)).time + args.time)
        val img = when (args.img) {
            "1" -> {
                context.getDrawable(R.drawable.svidetel)
            }
            "2" -> {
                context.getDrawable(R.drawable.gendalf)
            }
            "3" -> {
                context.getDrawable(R.drawable.oxl_vs)
            }
            else -> {
                context.getDrawable(R.drawable.svidetel)
            }
        }
        val bitmap =   getCroppedBitmap((img as BitmapDrawable).bitmap,250,context.resources)!!
        binding.imageView.setImageBitmap(bitmap)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
        // TODO: Use the ViewModel
    }
    override fun onBack(): Boolean {
        return true
    }

}