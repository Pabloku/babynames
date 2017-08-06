package com.kamisoft.babynames.domain.usecase

import com.kamisoft.babynames.domain.model.BabyName
import com.kamisoft.babynames.domain.model.Gender
import com.kamisoft.babynames.domain.repository.NamesRepository
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class GetNameList(val namesRepository: NamesRepository) {

    fun getNames(gender: Gender): List<BabyName> {
        return loadNames(gender)
    }

    fun getNames(gender: Gender, callback: (List<BabyName>) -> Unit) {
        doAsync {
            val nameList = loadNames(gender)
            uiThread { callback.invoke(nameList) }
        }
    }

    private fun loadNames(gender: Gender): List<BabyName> {
        val nameList = namesRepository.getAllNamesByGender(gender)
        val babyNameList = ArrayList<BabyName>()
        nameList.map { babyNameList.add(BabyName(name = it.name, origin = it.origin, meaning = it.meaning)) }
        return babyNameList
    }

}