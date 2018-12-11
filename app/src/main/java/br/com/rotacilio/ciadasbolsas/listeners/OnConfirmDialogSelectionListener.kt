package br.com.rotacilio.ciadasbolsas.listeners

import android.content.DialogInterface

interface OnConfirmDialogSelectionListener {
    fun yes(dialogInterface: DialogInterface, i: Int)
    fun no(dialogInterface: DialogInterface, i: Int)
}