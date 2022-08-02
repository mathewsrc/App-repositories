package br.com.dio.app.repositories.ui.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import br.com.dio.app.repositories.core.createDialog
import br.com.dio.app.repositories.core.createProgressDialog
import br.com.dio.app.repositories.databinding.FragmentHomeBinding
import br.com.dio.app.repositories.presentation.HomeViewModel
import br.com.dio.app.repositories.presentation.MainViewModel
import br.com.dio.app.repositories.ui.RepoListAdapter
import br.com.dio.app.repositories.ui.ui.adapters.HeaderAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search")

class HomeFragment : Fragment() {

    private var search: Flow<String?>? = null

    private lateinit var adapter: RepoListAdapter
    private lateinit var headerAdapter: HeaderAdapter
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val dialog by lazy { context?.createProgressDialog() }
    private val viewModel by viewModel<HomeViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        search = try {
            requireContext().dataStore.data.map { preferences ->
                preferences[SEARCH_KEY]
            }
        } catch (e: Exception) {
            Log.e("HomeFragment", "Was not possible to get back the value from Datastore", e)
            null
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                search?.collectLatest { key ->
                    if (key != null) {
                        viewModel.getRepoList(key)
                    }
                }
            }
        }

        headerAdapter = HeaderAdapter {
            viewModel.sortBy(HomeViewModel.SortByStar.DESCENDING)
        }

        viewModel.repos.observe(viewLifecycleOwner) {

            when (it) {
                HomeViewModel.State.Loading -> dialog?.show()
                is HomeViewModel.State.Error -> {
                    requireContext().createDialog {
                        setMessage(it.error.message)
                    }.show()
                    dialog?.dismiss()
                }
                is HomeViewModel.State.Success -> {
                    dialog?.dismiss()
                    adapter = RepoListAdapter(viewModel::save)
                    binding.rvRepos.adapter = ConcatAdapter(headerAdapter, adapter)
                    adapter.submitList(it.list)
                }
            }
        }
        mainViewModel.query.observe(viewLifecycleOwner) {
            it?.let { query ->
                viewModel.getRepoList(query)
                mainViewModel.clearQuery()
                // Save query in Datastore
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        requireContext().dataStore.edit { settings ->
                            settings[SEARCH_KEY] = query
                        }
                    } catch (e: Exception) {
                        Log.e("HomeFragment", "Was not possible to save query in Datastore", e)
                    }
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val SEARCH_KEY = stringPreferencesKey("latest_search_key")
    }
}