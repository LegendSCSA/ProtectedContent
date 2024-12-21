package com.shafie.protectedcontent

import android.net.Uri

object Constants {
    val AUTHORITY = "com.muzaffar.protectedcontent.provider"
    // Access COntent Provider, ke DB t1
    val URL = Uri.parse("content://$AUTHORITY/t1")
    // Access cursor database
    val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.muzaffar.protectedcontent.provider.t1"
    val ID = "_ID"
    val TEXT = "MESSAGE"
    val TEXT_DATA = "Hello content provider!"
}