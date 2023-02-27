package br.eng.pedro_mendes.mysubscribers.repository

import br.eng.pedro_mendes.mysubscribers.data.db.entities.SubscriberEntity

interface SubscriberRepository {

    suspend fun insert(name: String, email: String): Long

    suspend fun update(id: Long, name: String, email: String)

    suspend fun delete(id: Long)

    suspend fun clear()

    suspend fun getAll(): List<SubscriberEntity>
}