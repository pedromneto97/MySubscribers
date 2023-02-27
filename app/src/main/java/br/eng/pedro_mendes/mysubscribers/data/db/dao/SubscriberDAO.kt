package br.eng.pedro_mendes.mysubscribers.data.db.dao

import androidx.room.*
import br.eng.pedro_mendes.mysubscribers.data.db.entities.SubscriberEntity

@Dao
interface SubscriberDAO {
    @Insert
    suspend fun insert(subscriber: SubscriberEntity): Long

    @Update
    suspend fun update(subscriber: SubscriberEntity)

    @Query("DELETE FROM subscriber WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM subscriber")
    suspend fun clear()

    @Query("SELECT * FROM subscriber")
    suspend fun getAll(): List<SubscriberEntity>
}