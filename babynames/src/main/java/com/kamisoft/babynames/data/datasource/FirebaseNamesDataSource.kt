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
    override fun getNamesList(genre: NamesDataSource.Genre): List<String> {
        return Tasks.await(getNameListTask(genre))
    }

    fun getNameListTask(genre: NamesDataSource.Genre): Task<List<String>> {
        val firebaseGenresDBReference = FirebaseDatabase.getInstance().reference.child(FirebaseDBCommons.Node.GENRES.toString())
        val firebaseQuery = firebaseGenresDBReference.child(FirebaseDBCommons.Node.valueOf(genre.toString().toUpperCase()).toString())

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