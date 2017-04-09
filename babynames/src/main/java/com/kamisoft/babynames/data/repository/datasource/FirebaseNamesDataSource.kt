package com.kamisoft.babynames.data.repository.datasource

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kamisoft.babynames.logger.Logger

//TODO [Paloga] Proguard config for firebase realtime database https://firebase.google.com/docs/database/android/start/
class FirebaseNamesDataSource : NamesDataSource {
    override fun getNamesList(genre: NamesDataSource.Genre): List<String> {
        return Tasks.await(getNameListTask(genre))
    }

    fun getNameListTask(genre: NamesDataSource.Genre): Task<List<String>> {
        val firebaseDBReference = FirebaseDatabase.getInstance().reference
        val firebaseQuery = firebaseDBReference.child(FirebaseDBCommons.Node.valueOf(genre.genreName.toUpperCase()).nodeName)

        val taskCompletionSource = TaskCompletionSource<List<String>>()

        firebaseQuery.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot?.let { taskCompletionSource.setResult(it.children.map { it.value as String }.toList()) }
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