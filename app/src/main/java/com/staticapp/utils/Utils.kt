package com.staticapp.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.*


//fun addFragmentToFragmentTranisition(fragment: Fragment, parentFragmentManager: FragmentManager){
//
////    var notificationFragment= NotificationFragment()
//    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
//    transaction.add(R.id.frame_layout, fragment).addToBackStack("add")
//    transaction.commit()
//}

private fun queryName(context: Context, uri: Uri): String? {
    var cursor: Cursor? = null
    return try {
        var name: String? = null
        if (uri.scheme == "content") {
            cursor = context.contentResolver.query(uri, null, null, null, null)
            assert(cursor != null)
            val nameIndex = cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            name = cursor.getString(nameIndex)
        }
        if (name == null) {
            name = uri.path
            val cut = name!!.lastIndexOf('/')
            if (cut != -1) {
                name = name.substring(cut + 1)
            }
        }
        name
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        ""
    } finally {
        cursor?.close()
    }
}


@Throws(IOException::class)
fun getFile(context: Context, uri: Uri?): File? {
    return try {
        val destinationFilename = File(
            context.filesDir.path + File.separatorChar + uri?.let {
                queryName(
                    context,
                    it
                )
            }
        )
        try {
            context.contentResolver.openInputStream(uri!!).use { ins ->
                ins?.let {
                    createFileFromStream(
                        it,
                        destinationFilename
                    )
                }
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
        destinationFilename
    } catch (e: java.lang.Exception) {
        null
    }
}
fun getMimeType(url: String): String? {
    var type: String? = null
    val extension = url.substring(url.lastIndexOf(".") + 1)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}
fun createFileFromStream(ins: InputStream, destination: File?) {
    try {
        FileOutputStream(destination).use { os ->
            val buffer = ByteArray(4096)
            var length: Int
            while (ins.read(buffer).also { length = it } > 0) {
                os.write(buffer, 0, length)
            }
            os.flush()
        }
    } catch (ex: java.lang.Exception) {
        Log.e("Save File", ex.message!!)
        ex.printStackTrace()
    }
}
