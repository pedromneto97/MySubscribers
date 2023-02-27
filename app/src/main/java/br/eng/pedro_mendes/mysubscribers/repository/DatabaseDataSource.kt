package br.eng.pedro_mendes.mysubscribers.repository

import br.eng.pedro_mendes.mysubscribers.data.db.dao.SubscriberDAO
import br.eng.pedro_mendes.mysubscribers.data.db.entities.SubscriberEntity

class DatabaseDataSource(private val subscriberDAO: SubscriberDAO) : SubscriberRepository {
    override suspend fun insert(name: String, email: String): Long {
        return subscriberDAO.insert(
            SubscriberEntity(name = name, email = email)
        )
    }

    override suspend fun update(id: Long, name: String, email: String) {
        return subscriberDAO.update(
            SubscriberEntity(
                id = id,
                name = name,
                email = email
            )
        )
    }

    override suspend fun delete(id: Long) {
        return subscriberDAO.delete(id)
    }

    override suspend fun clear() {
        return subscriberDAO.clear()
    }

    override suspend fun getAll(): List<SubscriberEntity> {
        return subscriberDAO.getAll()
    }

}