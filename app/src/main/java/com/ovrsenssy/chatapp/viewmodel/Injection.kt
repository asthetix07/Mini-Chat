package com.ovrsenssy.chatapp.viewmodel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ovrsenssy.chatapp.data.UserRepository

object Injection {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun instance() = firebaseFirestore
    fun userInstance() = UserRepository(firebaseAuth, firebaseFirestore)
}
