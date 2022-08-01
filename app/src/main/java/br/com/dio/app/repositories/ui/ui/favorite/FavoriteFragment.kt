package br.com.dio.app.repositories.ui.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.dio.app.repositories.core.createDialog
import br.com.dio.app.repositories.core.createProgressDialog
import br.com.dio.app.repositories.databinding.FragmentFavoriteBinding
import br.com.dio.app.repositories.presentation.FavoriteViewModel
import br.com.dio.app.repositories.presentation.HomeViewModel
import br.com.dio.app.repositories.ui.RepoListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dialog by lazy { context?.createProgressDialog() }
    private val adapter by lazy { RepoListAdapter() }
    private val viewModel by viewModel<FavoriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.rvRepos.adapter = adapter

        viewModel.repos.observe(viewLifecycleOwner) {
            when (it) {
                FavoriteViewModel.State.Loading -> dialog?.show()
                is FavoriteViewModel.State.Error -> {
                    requireContext().createDialog {
                        setMessage(it.error.message)
                    }.show()
                    dialog?.dismiss()
                }
                is FavoriteViewModel.State.Success -> {
                    dialog?.dismiss()
                    adapter.submitList(it.list)
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}