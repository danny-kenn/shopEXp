package com.kiamba.myfirebasemvvm.model

import android.provider.ContactsContract.CommonDataKinds.Phone
import java.net.URL


class User {

    var name: String = ""
    var email: String = ""
    var pass: String = ""
    var confirmpass: String = ""
    var userid: String = ""
    var phone: String? = null  // Make phone mutable so it can be updated
    var profilePictureUrl: String? = null  // Make profilePictureUrl mutable

    // Constructor to initialize all fields, including phone and profile picture
    constructor(name: String, email: String, pass: String, confirmpass: String, userid: String, phone: String?, profilePictureUrl: String?) {
        this.name = name
        this.email = email
        this.pass = pass
        this.confirmpass = confirmpass
        this.userid = userid
        this.phone = phone
        this.profilePictureUrl = profilePictureUrl
    }

    constructor()
}
