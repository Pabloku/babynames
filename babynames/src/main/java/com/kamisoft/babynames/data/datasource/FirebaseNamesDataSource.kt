package com.kamisoft.babynames.data.datasource

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.database.*
import com.kamisoft.babynames.data.entity.FireBaseBabyName
import com.kamisoft.babynames.data.entity.FireBaseBabyNameWithFavoriteCounter
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.logger.Logger
import java.text.Collator
import java.util.*


//Proguard config for firebase real time database https://firebase.google.com/docs/database/android/start/
class FirebaseNamesDataSource : NamesDataSource {

    override fun getNamesList(gender: Gender): List<FireBaseBabyName> {
        return Tasks.await(getNameListTask(gender))
    }

    fun getNameListTask(gender: Gender): Task<List<FireBaseBabyName>> {
        val firebaseBabyNamesDBReference = FirebaseDatabase.getInstance().reference.child(FirebaseDBCommons.Node.BABY_NAMES.toString())
        val firebaseQuery = firebaseBabyNamesDBReference.child(FirebaseDBCommons.Node.valueOf(gender.toString().toUpperCase()).toString())

        val taskCompletionSource = TaskCompletionSource<List<FireBaseBabyName>>()

        firebaseQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val list = dataSnapshot?.children?.mapNotNull {
                    val firebaseBabyName = it.getValue(FireBaseBabyName::class.java)
                    firebaseBabyName?.name = it.key
                    return@mapNotNull firebaseBabyName
                }

                // This strategy mean it'll ignore the accents. Maybe better way to do this in kotlin?
                Collections.sort(list) { babyName1, babyName2 ->
                    val collator = Collator.getInstance()
                    collator.strength = Collator.PRIMARY
                    collator.compare(babyName1.name, babyName2.name)
                }

                taskCompletionSource.setResult(list)
            }

            override fun onCancelled(databaseError: DatabaseError?) {
                databaseError?.toException()?.let {
                    FirebaseCrash.report(it)
                    Logger.error(it, "Error on getNameListTask")
                    taskCompletionSource.setException(it)
                }
            }

        })
        return taskCompletionSource.task
    }

    override fun increaseNameLikedCounter(gender: Gender, name: String) {
        updateNameLikedCounter(gender, name, liked = true)
    }

    override fun decreaseNameLikedCounter(gender: Gender, name: String) {
        updateNameLikedCounter(gender, name, liked = false)
    }

    private fun updateNameLikedCounter(gender: Gender, name: String, liked: Boolean) {
        val firebaseBabyNamesDBReference = FirebaseDatabase.getInstance().reference.child(FirebaseDBCommons.Node.BABY_NAMES.toString())
        val firebaseQuery = firebaseBabyNamesDBReference.child(FirebaseDBCommons.Node.valueOf(gender.toString().toUpperCase()).toString())
        val firebaseNameNode = firebaseQuery.child(name)
        firebaseNameNode.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val babyName = mutableData.getValue(FireBaseBabyNameWithFavoriteCounter::class.java)
                if (babyName != null) {
                    if (liked) babyName.favoriteCount++ else babyName.favoriteCount--
                    mutableData.value = babyName
                }
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, committed: Boolean,
                                    dataSnapshot: DataSnapshot?) {
                if (databaseError != null) {
                    Logger.error(databaseError.toException(), "Error in onComplete")
                } else {
                    Logger.debug("onComplete success")
                }
            }
        })
    }
}