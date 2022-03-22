package nl.joozd.joozdcalendarapi.helpers

internal inline fun<reified T> mergeArrays(arrays: Collection<Array<T>>): Array<T> {
    var result: Array<T> = emptyArray()
    arrays.forEach { a ->
        result += a;
    }
    return result
}