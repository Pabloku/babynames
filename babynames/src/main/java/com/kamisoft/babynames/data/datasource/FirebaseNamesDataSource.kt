package com.kamisoft.babynames.data.datasource

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kamisoft.babynames.data.entity.FireBaseBabyName
import com.kamisoft.babynames.logger.Logger


//TODO [Paloga] Proguard config for firebase real time database https://firebase.google.com/docs/database/android/start/
class FirebaseNamesDataSource : NamesDataSource {
    override fun getNamesList(gender: NamesDataSource.Gender): List<FireBaseBabyName> {
        return Tasks.await(getNameListTask(gender))
    }

    fun getNameListTask(gender: NamesDataSource.Gender): Task<List<FireBaseBabyName>> {
        val firebaseBabyNamesDBReference = FirebaseDatabase.getInstance().reference.child(FirebaseDBCommons.Node.BABY_NAMES.toString())
        val firebaseQuery = firebaseBabyNamesDBReference.child(FirebaseDBCommons.Node.valueOf(gender.toString().toUpperCase()).toString())

        val taskCompletionSource = TaskCompletionSource<List<FireBaseBabyName>>()

        firebaseQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                taskCompletionSource.setResult(
                        dataSnapshot?.children?.map {
                            val firebaseBabyName = it.getValue(FireBaseBabyName::class.java)
                            firebaseBabyName.name = it.key
                            return@map firebaseBabyName
                        }
                )
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