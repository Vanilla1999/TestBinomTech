package com.example.test.presentation.infoFragment

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.test.R
import com.example.test.databinding.FragmentInfoBinding
import com.example.test.presentation.OnBackPressedFrament
import com.example.test.utils.atStartOfDay
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewName.text = args.name
        binding.textViewCalendar.text = getDateTimeDay(args.calendar)
        binding.textViewTime.text = getHours(atStartOfDay(Date(args.time)).time + args.time)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
        // TODO: Use the ViewModel
    }
    override fun onBack(): Boolean {
        return false
    }

}