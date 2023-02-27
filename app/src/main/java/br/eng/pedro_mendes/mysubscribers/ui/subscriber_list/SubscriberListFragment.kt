package br.eng.pedro_mendes.mysubscribers.ui.subscriber_list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.eng.pedro_mendes.mysubscribers.R
import br.eng.pedro_mendes.mysubscribers.data.db.AppDatabase
import br.eng.pedro_mendes.mysubscribers.data.db.dao.SubscriberDAO
import br.eng.pedro_mendes.mysubscribers.databinding.FragmentSubscriberListBinding
import br.eng.pedro_mendes.mysubscribers.repository.DatabaseDataSource
import br.eng.pedro_mendes.mysubscribers.repository.SubscriberRepository
import br.eng.pedro_mendes.mysubscribers.ui.subscriber_list.adapter.SubscriberListAdapter
import br.eng.pedro_mendes.mysubscribers.ui.subscriber_list.view_model.SubscriberListViewModel

class SubscriberListFragment : Fragment(R.layout.fragment_subscriber_list) {
    private var _binding: FragmentSubscriberListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubscriberListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val subscriberDAO: SubscriberDAO = AppDatabase.getInstance(
                    requireContext()
                ).subscriberDAO
                val repository: SubscriberRepository = DatabaseDataSource(subscriberDAO)

                return SubscriberListViewModel(repository) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSubscriberListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelEvents()
        configureListeners()
    }

    private fun configureListeners() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_subscriberListFragment_to_subscriberFragment)
        }
    }

    private fun observeViewModelEvents() {
        viewModel.allSubscribersEvent.observe(viewLifecycleOwner) {
            Log.d("Teste", it.size.toString())
            setHasOptionsMenu(it.isNotEmpty())

            val subscriberListAdapter = SubscriberListAdapter(it) { subscriber ->
                val directions = SubscriberListFragmentDirections
                    .actionSubscriberListFragmentToSubscriberFragment(subscriber)

                findNavController().navigate(directions)
            }

            with(binding.recyclerViewSubscribers) {
                setHasFixedSize(true)
                adapter = subscriberListAdapter
            }
        }

        viewModel.deleteAllSubscribersEvent.observe(viewLifecycleOwner) {
            viewModel.getSubscribers()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSubscribers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.subscriber_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.delete_subscribers) {
            viewModel.deleteAllSubscribers()
            true
        } else super.onOptionsItemSelected(item)
    }
}