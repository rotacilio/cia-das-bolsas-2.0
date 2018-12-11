package br.com.rotacilio.ciadasbolsas.commons

import android.content.Context
import android.support.v7.app.AlertDialog
import br.com.rotacilio.ciadasbolsas.R
import br.com.rotacilio.ciadasbolsas.listeners.OnConfirmDialogSelectionListener

class Utils {

    companion object {
        fun showConfirmDialog(context: Context, listener: OnConfirmDialogSelectionListener) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.remove_confirm_dialog_title)
            builder.setMessage(R.string.remove_confirm_dialog_message)
            builder.setPositiveButton(R.string.yes) { dialogInterface, i ->
                listener.yes(dialogInterface, i)
            }
            builder.setNegativeButton(R.string.no) { dialogInterface, i ->
                listener.no(dialogInterface, i)
            }
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
    }
}