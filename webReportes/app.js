const firebaseConfig = {
    apiKey: "AIzaSyBDcJDLnPNEiKhVuo1_kFYBHMIc6WtEY4Y",
    authDomain: "appmantenimiento-8da92.firebaseapp.com",
    projectId: "appmantenimiento-8da92",
    storageBucket: "appmantenimiento-8da92.firebasestorage.app",
    messagingSenderId: "573098117900",
    appId: "1:573098117900:web:e49675731e0b353b451729",
    measurementId: "G-0QFHY2XLWS"
};

firebase.initializeApp(firebaseConfig);
const auth = firebase.auth();

auth.onAuthStateChanged(user => {
    if (user) {
        window.location.href = "dashboard.html";
    }
});

function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    auth.signInWithEmailAndPassword(email, password)
        .then(userCredential => {
            return userCredential.user.getIdToken(true);
        })
        .then(idToken => {
            console.log("Token JWT:", idToken);
            window.location.href = "dashboard.html";
        })
        .catch(error => {
            console.error("Error completo:", error);
            if (error.code === "auth/invalid-credential") {
                alert("Credenciales inválidas. Verifica email/contraseña.");
            } else {
                alert("Error: " + error.message);
            }
        });
}

function logout() {
    auth.signOut()
        .then(() => {
            document.getElementById("resultado").innerText = "Sesión cerrada correctamente";
            document.getElementById("email").value = "";
            document.getElementById("password").value = "";
        })
        .catch(error => {
            document.getElementById("resultado").innerText = " Error al cerrar sesión: " + error.message;
        });
}
