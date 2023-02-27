package br.eng.pedro_mendes.mysubscribers.ui.subscriber_list.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.eng.pedro_mendes.mysubscribers.data.db.entities.SubscriberEntity
import br.eng.pedro_mendes.mysubscribers.repository.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberListViewModel(
    private val repository: SubscriberRepository,
) : ViewModel() {
    private val _allSubscribersEvent = MutableLiveData<List<SubscriberEntity>>()
    val allSubscribersEvent: LiveData<List<SubscriberEntity>>
        get() = _allSubscribersEvent

    private val _deleteAllSubscribersEvent = MutableLiveData<Unit>()
    val deleteAllSubscribersEvent: LiveData<Unit>
        get() = _deleteAllSubscribersEvent

    fun getSubscribers() = viewModelScope.launch {
        val subscribers = repository.getAll()
        _allSubscribersEvent.postValue(subscribers)
    }

    fun deleteAllSubscribers() {
        viewModelScope.launch {
            try {
                repository.clear()
                _deleteAllSubscribersEvent.postValue(Unit)
            } catch (ex: Exception) {
                Log.d(TAG, ex.toString())
            }
        }
    }

    companion object {
        private val TAG = SubscriberListViewModel::class.java.simpleName
    }
}