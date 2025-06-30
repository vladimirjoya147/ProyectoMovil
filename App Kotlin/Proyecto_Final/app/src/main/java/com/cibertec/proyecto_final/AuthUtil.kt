package com.cibertec.proyecto_final

import com.google.firebase.auth.FirebaseAuth

object AuthUtil {
    fun withFirebaseToken(
        onSuccess: (token: String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.getIdToken(true)
                .addOnSuccessListener { result ->
                    val token = result.token
                    if (!token.isNullOrEmpty()) {
                        RetrofitClient.SessionManager.firebaseToken = token
                        onSuccess(token)
                    } else {
                        onError(Exception("Token vacío"))
                    }
                }
                .addOnFailureListener {
                    onError(it)
                }
        } else {
            onError(Exception("Usuario no autenticado"))
        }
    }
}
