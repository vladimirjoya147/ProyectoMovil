
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
    if (!user) {
        alert("Debes iniciar sesión");
        window.location.href = "index.html";
    } else {
        cargarEquipos();
        cargarMantenimientos();
    }
});

function logout() {
    auth.signOut().then(() => {
        window.location.href = "index.html";
    });
}

function cargarEquipos() {
    auth.currentUser.getIdToken().then(token => {
        return fetch("https://cibertec-servicio-production.up.railway.app/equipo", {
            headers: { "Authorization": `Bearer ${token}` }
        });
    })
    .then(res => res.json())
    .then(data => {
        const tbody = document.querySelector("#tablaEquipos tbody");
        tbody.innerHTML = "";
        data.content.forEach(e => {
            const row = `<tr><td>${e.nombre}</td><td>${e.tipo}</td><td>${e.sku}</td></tr>`;
            tbody.innerHTML += row;
        });
    });
}

function buscarEquipos() {
    const nombre = document.getElementById("buscarNombre").value;
    if (!nombre) return;

    auth.currentUser.getIdToken().then(token => {
        return fetch(`https://cibertec-servicio-production.up.railway.app/equipo/buscar?nombre=${nombre}`, {
            headers: { "Authorization": `Bearer ${token}` }
        });
    })
    .then(res => res.json())
    .then(data => {
        const tbody = document.querySelector("#tablaEquipos tbody");
        tbody.innerHTML = "";
        data.forEach(e => {
            const row = `<tr><td>${e.nombre}</td><td>${e.tipo}</td><td>${e.sku}</td></tr>`;
            tbody.innerHTML += row;
        });
    });
}

function cargarMantenimientos() {
    auth.currentUser.getIdToken().then(token => {
        return fetch("https://cibertec-servicio-production.up.railway.app/ordenes/todas", {
            headers: { "Authorization": `Bearer ${token}` }
        });
    })
    .then(res => res.json())
    .then(data => {
        const tbody = document.querySelector("#tablaMantenimientos tbody");
        tbody.innerHTML = "";
        data.forEach(m => {
            let estadoClase = "";
            if (m.estado === "Recibido") estadoClase = "estado-recibido";
            else if (m.estado === "En Proceso") estadoClase = "estado-proceso";
            else if (m.estado === "Finalizado") estadoClase = "estado-finalizado";

            const row = `
                <tr>
                    <td>${m.nombreEquipo}</td>
                    <td>${m.descripcion}</td>
                    <td>${m.tipo_mantenimiento}</td>
                    <td><span class="estado-circle ${estadoClase}"></span>${m.estado}</td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    });
}

function filtrarMantenimientos(estado) {
    auth.currentUser.getIdToken().then(token => {
        return fetch("https://cibertec-servicio-production.up.railway.app/ordenes/todas", {
            headers: { "Authorization": `Bearer ${token}` }
        });
    })
    .then(res => res.json())
    .then(data => {
        const tbody = document.querySelector("#tablaMantenimientos tbody");
        tbody.innerHTML = "";
        data
            .filter(m => m.estado === estado)
            .forEach(m => {
                let estadoClase = "";
                if (m.estado === "Recibido") estadoClase = "estado-recibido";
                else if (m.estado === "En Proceso") estadoClase = "estado-proceso";
                else if (m.estado === "Finalizado") estadoClase = "estado-finalizado";

                const row = `
                    <tr>
                        <td>${m.nombreEquipo}</td>
                        <td>${m.descripcion}</td>
                        <td>${m.tipo_mantenimiento}</td>
                        <td><span class="estado-circle ${estadoClase}"></span>${m.estado}</td>
                    </tr>
                `;
                tbody.innerHTML += row;
            });
    });
    }
