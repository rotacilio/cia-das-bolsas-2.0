package br.com.rotacilio.ciadasbolsas.listeners

interface OnCompleteRequestListener<T> {
    fun success(t: T)
    fun fail(message: String?)
}