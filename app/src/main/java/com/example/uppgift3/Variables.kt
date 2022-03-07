package com.example.uppgift3

// klass med statiska varibler direkt i kotlin
object Variables {
    object SharedPref {
        object image {
            const val key = "include_image"
            const val defualt = true
        }

        object order {
            const val key = "order_key"
            const val  ascending = "ascending"
            const val descending = "descending"
            const val defualt = descending
        }
        object sort {
            const val date = "date"
            const val alpha = "alpa"
            const val size = "size"
            const val key = "sort_key"
            const val defualt = date
        }
        const val Prefname = pacageName

          val listOfSortOptions = listOf(
            sort.date,
            sort.alpha,
            sort.size
        )

        val sortOptionMap = listOfSortOptions.withIndex().map { it.value to it.index }.toMap()

         val listOfOrderOptions = listOf(
            order.ascending,
            order.descending
        )
        val orderOptionMap = listOfOrderOptions.withIndex().map { it.value to it.index  }.toMap()


        val listOfImageOptions = listOf(
           true,
           false
        )
        val imageOptionsMap = listOfImageOptions.withIndex().map { it.value to it.index  }.toMap()

    }
    private const val pacageName ="com.example.uppgift3"
    const val authority = pacageName

}