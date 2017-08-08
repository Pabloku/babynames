package com.kamisoft.babynames.tracking

data class TrackerItem(val id: String,
                       val name: String,
                       val category: String) {

    override fun toString(): String = "{id = $id, name = $name, category = $category}"

}
