package com.example.githubapp.ui.detailuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentFollowerBinding
import com.google.android.material.snackbar.Snackbar

class FollowerFragment : Fragment() {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowers.layoutManager = layoutManager
        val itemDecoration =
            DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        binding.rvFollowers.addItemDecoration(itemDecoration)

        viewModel.apply {
            followerList.observe(viewLifecycleOwner) {
                val adapter = UserFollowAdapter(it)
                binding.rvFollowers.adapter = adapter
            }

            snackBarText.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { snackBarText ->
                    Snackbar.make(
                        binding.root,
                        snackBarText,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.github_black
                            )
                        )
                        .setTextColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.github_white
                            )
                        )
                        .show()
                }
            }
        }
    }
}