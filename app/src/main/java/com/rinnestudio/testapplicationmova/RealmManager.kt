package com.rinnestudio.testapplicationmova

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.kotlin.where
import kotlinx.coroutines.*

const val realmName = "name"

class RealmManager {

    suspend fun writeData(item: Item) {
        withContext(Dispatchers.IO) {
            val realm = createRealmInstance()
            realm.executeTransaction {
                it.insert(item)
            }
        }
    }

    fun readData(): List<Item> {
        val realm = createRealmInstance()
        return realm.where<Item>().findAll()
    }

    fun getItem(str: String): Item? {
        val realm = createRealmInstance()
        return realm.where<Item>().equalTo("searchStr", str).findFirst()
    }

    fun updateUrlListForItem(item: Item, position: Int) {
         val realm = createRealmInstance()
        realm.executeTransaction {
            val oldItem =
                it.where<Item>().equalTo("searchStr", item.searchStr).findFirst()
            if (oldItem != null) {
                oldItem.url = RealmList(item.url[position])
            }
        }
    }


    private fun createRealmInstance() =
        Realm.getInstance(
            RealmConfiguration.Builder().name(realmName).allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true).build()
        )
}