package ru.freedominc.sfedu.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.freedominc.sfedu.R

class FavoriteDialog(private val isFavorite: Boolean, private val name: String, private val onYes: ()->Unit): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val messageId = if(isFavorite) R.string.now_is_favorite else R.string.now_is_not_favorite
            val positiveTextId = if(isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites
            builder.setMessage(getString(messageId, name))
                .setPositiveButton(getString(positiveTextId)) { _, _ ->
                    onYes()
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}