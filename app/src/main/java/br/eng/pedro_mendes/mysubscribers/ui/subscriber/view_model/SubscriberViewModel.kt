package br.eng.pedro_mendes.mysubscribers.ui.subscriber.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.eng.pedro_mendes.mysubscribers.R
import br.eng.pedro_mendes.mysubscribers.repository.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel() {
    private val _subscriberStateEventData = MutableLiveData<SubscriberState>()
    val subscriberStateEventData: LiveData<SubscriberState>
        get() = _subscriberStateEventData

    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int>
        get() = _messageEventData

    fun upsertSubscriber(name: String, email: String, id: Long = 0) = viewModelScope.launch {
        if (id > 0) {
            updateSubscriber(id, name, email)
        } else {
            insertSubscriber(name, email)
        }
    }

    fun removeSubscriber(id: Long) = viewModelScope.launch {
        try {
            if (id > 0) {
                repository.delete(id)
                _subscriberStateEventData.postValue(SubscriberState.Deleted)
                _messageEventData.value = R.string.subscriber_deleted_successfully
            }
        } catch (ex: Exception) {
            _messageEventData.value = R.string.subscriber_deletion_failed
            Log.d(TAG, ex.toString())
        }
    }

    private suspend fun updateSubscriber(id: Long, name: String, email: String) {
        try {
            repository.update(id, name, email)
            _subscriberStateEventData.value = SubscriberState.Updated
            _messageEventData.value = R.string.subscriber_updated_successfully
        } catch (ex: Exception) {
            _messageEventData.value = R.string.subscriber_update_error
            Log.d(TAG, ex.toString())
        }
    }

    private suspend fun insertSubscriber(name: String, email: String) {
        try {
            val newId = repository.insert(name, email)
            if (newId > 0) {
                _subscriberStateEventData.value = SubscriberState.Inserted
                _messageEventData.value = R.string.subscriber_created_successfully
            }
        } catch (ex: Exception) {
            _messageEventData.value = R.string.subscriber_creation_failed
            Log.d(TAG, ex.toString())
        }
    }

    enum class SubscriberState {
        Inserted,
        Updated,
        Deleted,
    }

    companion object {
        private val TAG = SubscriberViewModel::class.java.simpleName
    }
}