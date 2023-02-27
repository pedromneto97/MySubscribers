package br.eng.pedro_mendes.mysubscribers.ui.subscriber_list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.eng.pedro_mendes.mysubscribers.R
import br.eng.pedro_mendes.mysubscribers.data.db.entities.SubscriberEntity

class SubscriberListAdapter(
    private val subscriberList: List<SubscriberEntity>,
    private val onSubscriberClickListener: (subscriber: SubscriberEntity) -> Unit,
) :
    Adapter<SubscriberListAdapter.SubscriberListViewHolder>() {


    inner class SubscriberListViewHolder(
        itemView: View,
        private val onSubscriberClickListener: (subscriber: SubscriberEntity) -> Unit,
    ) : ViewHolder(itemView) {

        fun bind(subscriber: SubscriberEntity) {
            with(itemView) {
                findViewById<TextView>(R.id.textViewName).apply {
                    text = subscriber.name
                }

                findViewById<TextView>(R.id.textViewEmail).apply {
                    text = subscriber.email
                }

                setOnClickListener {
                    onSubscriberClickListener.invoke(subscriber)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.subscriber_item,
            parent,
            false
        )

        return SubscriberListViewHolder(view, onSubscriberClickListener)
    }

    override fun getItemCount(): Int = subscriberList.size

    override fun onBindViewHolder(holder: SubscriberListViewHolder, position: Int) =
        holder.bind(subscriberList[position])
}