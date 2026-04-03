package com.timofeev.words.utils

/**
 * Меняет местами элементы списка по указанным индексам.
 *
 * Функция выполняет обмен значениями элементов списка на позициях [i] и [j].
 * Работает in-place (изменяет исходный список).
 *
 * @param i индекс первого элемента
 * @param j индекс второго элемента
 *
 * @throws IndexOutOfBoundsException если любой из индексов выходит за границы списка
 *
 * @receiver изменяемый список [MutableList], элементы которого будут переставлены
 *
 * @sample
 * val list = mutableListOf("one", "two", "three")
 * list.swap(0, 2)
 * println(list) // ["three", "two", "one"]
 *
 * @sample
 * val list = mutableListOf(1, 2, 3, 4)
 * list.swap(1, 3)
 * println(list) // [1, 4, 3, 2]
 */
fun <T> MutableList<T>.swap(i: Int, j: Int): MutableList<T> {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
    return this
}