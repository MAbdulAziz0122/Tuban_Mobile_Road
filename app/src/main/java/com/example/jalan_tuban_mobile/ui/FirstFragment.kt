package com.example.jalan_tuban_mobile.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jalan_tuban_mobile.Application.RoadApp
import com.example.jalan_tuban_mobile.R
import com.example.jalan_tuban_mobile.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val roadViewModel: RoadViewModel by viewModels {
        RoadViewModelFactory((applicationContext as RoadApp).repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RoadListAdapter { road ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(road)
            findNavController().navigate(action)
        }
        binding.dataRecylerView.adapter = adapter
        binding.dataRecylerView.layoutManager = LinearLayoutManager(context)
        roadViewModel.allRoads.observe(viewLifecycleOwner) { roads ->
            roads.let {
                if (roads.isEmpty()) {
                    binding.emptyTextView.visibility = View.VISIBLE
                    binding.ilustrationImageView.visibility = View.VISIBLE
                } else {
                    binding.emptyTextView.visibility = View.GONE
                    binding.ilustrationImageView.visibility = View.GONE
                }
                adapter.submitList(roads)
            }
        }
        binding.addFAB.setOnClickListener {
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(null)
            findNavController().navigate(action)
        }
    }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }