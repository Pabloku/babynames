package com.kamisoft.babynames.data.datasource

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kamisoft.babynames.logger.Logger

//TODO [Paloga] Proguard config for firebase real time database https://firebase.google.com/docs/database/android/start/
class FirebaseNamesDataSource : NamesDataSource {
    override fun getNamesList(gender: NamesDataSource.Gender): List<String> {
        return Tasks.await(getNameListTask(gender))
    }

    fun getNameListTask(gender: NamesDataSource.Gender): Task<List<String>> {
        val firebaseGendersDBReference = FirebaseDatabase.getInstance().reference.child(FirebaseDBCommons.Node.GENDERS.toString())
        val firebaseQuery = firebaseGendersDBReference.child(FirebaseDBCommons.Node.valueOf(gender.toString().toUpperCase()).toString())

        val taskCompletionSource = TaskCompletionSource<List<String>>()

        firebaseQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot?.let {
                    taskCompletionSource.setResult((it.value as HashMap<String, Boolean>).map { (it.key) }.sorted())
                    //I prefer to order the data in Firebase Query, but I was not able to, so I used sorted() list function instead :(
                }

            }

            override fun onCancelled(databaseError: DatabaseError?) {
                //TODO [Paloga] Report it to online error handler?
                databaseError?.toException()?.let {
                    Logger.error(it, "Error on getNameListTask")
                    taskCompletionSource.setException(it)
                }
            }

        })
        return taskCompletionSource.task
    }
}