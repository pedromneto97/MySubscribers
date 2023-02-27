package br.eng.pedro_mendes.mysubscribers.ui.subscriber

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.eng.pedro_mendes.mysubscribers.R
import br.eng.pedro_mendes.mysubscribers.data.db.AppDatabase
import br.eng.pedro_mendes.mysubscribers.data.db.dao.SubscriberDAO
import br.eng.pedro_mendes.mysubscribers.databinding.FragmentSubscriberBinding
import br.eng.pedro_mendes.mysubscribers.repository.DatabaseDataSource
import br.eng.pedro_mendes.mysubscribers.repository.SubscriberRepository
import br.eng.pedro_mendes.mysubscribers.ui.subscriber.view_model.SubscriberViewModel
import com.google.android.material.snackbar.Snackbar


class SubscriberFragment : Fragment() {
    private var _binding: FragmentSubscriberBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubscriberViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val subscriberDAO: SubscriberDAO =
                    AppDatabase.getInstance(requireContext()).subscriberDAO
                val repository: SubscriberRepository = DatabaseDataSource(subscriberDAO)

                return SubscriberViewModel(repository) as T
            }
        }
    }

    private val args: SubscriberFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSubscriberBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.subscriber?.let {
            binding.apply {
                editTextName.setText(it.name)
                editTextEmail.setText(it.email)
                buttonSave.text = getString(R.string.update)
                buttonDelete.visibility = View.VISIBLE
            }
        }

        observeEvents()
        setListeners()
    }


    private fun setListeners() {
        binding.apply {
            buttonSave.setOnClickListener {
                val name = editTextName.text.toString()
                val email = editTextEmail.text.toString()

                viewModel.upsertSubscriber(name, email, args.subscriber?.id ?: 0)
            }
            buttonDelete.setOnClickListener {
                viewModel.removeSubscriber(args.subscriber?.id ?: 0)
            }
        }
    }

    private fun observeEvents() {
        viewModel.subscriberStateEventData.observe(viewLifecycleOwner) {
            when (it) {
                SubscriberViewModel.SubscriberState.Inserted,
                SubscriberViewModel.SubscriberState.Deleted,
                SubscriberViewModel.SubscriberState.Updated,
                -> {
                    clearFields()
                    hideKeyboard()
                    requireView().requestFocus()
                    findNavController().popBackStack()
                }
            }
        }
        viewModel.messageEventData.observe(viewLifecycleOwner) {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun hideKeyboard() {
        val activity = requireActivity()
        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        activity.currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

    }

    private fun clearFields() {
        binding.apply {
            editTextName.text?.clear()
            editTextEmail.text?.clear()
        }
    }
}