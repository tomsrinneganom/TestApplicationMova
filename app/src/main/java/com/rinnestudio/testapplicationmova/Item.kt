package com.rinnestudio.testapplicationmova

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

 open class Item(
): RealmObject(){
     @PrimaryKey
     var searchStr:String =""
     var url: RealmList<String> = RealmList()
}
