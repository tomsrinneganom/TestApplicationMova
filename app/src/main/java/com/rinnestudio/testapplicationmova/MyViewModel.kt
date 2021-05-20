package com.rinnestudio.testapplicationmova

import androidx.lifecycle.*
import io.realm.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


class MyViewModel : ViewModel() {

    fun getItem(str: String) = liveData {
        val item = RealmManager().getItem(str)
        if (item == null) {
            val result = getImageUrl(str)
            if (result != null) {
                RealmManager().writeData(result)
            }
            this.emit(result)
        } else {
            this.emit(item)
        }
    }


    private suspend fun getImageUrl(str: String): Item? = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://bing-image-search1.p.rapidapi.com/images/search?q=${str}&count=10")
            .get()
            .addHeader(
                "x-rapidapi-key",
                "47423b8697mshc0d6b39f01b1338p1f686ajsn30710883224d"
            )
            .addHeader("x-rapidapi-host", "bing-image-search1.p.rapidapi.com")
            .build()

        try {
            val response = client.newCall(request).execute()

            val array = JSONObject(response.body()!!.string()).getJSONArray("value")
            val url = mutableListOf<String>()
            while (url.size != array.length()) {
                url.add(array.getJSONObject(url.size).getString("contentUrl"))
            }

             Item().apply {
                this.url = RealmList(*url.toTypedArray())
                searchStr = str
            }
        } catch (e: Exception) {
            null

        }
    }

    fun readData() = liveData {
       this.emit(RealmManager().readData())
    }

}