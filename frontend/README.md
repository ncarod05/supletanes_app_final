# supletanes_app
Descripción del Proyecto:
- Este proyecto consta del desarrollo de una aplicación para dispositivos móviles, orientada a la venta de suplementos alimenticios/deportivos. Se irá trabajando durante el resto del semetre, implementando nuevas funcionalidades y pantallas según la planificación y el avance registrado.

Integrantes:
- Francisco Olate
- Nicolas Caro
- Christopher Espinoza

Funcionalidades Implementadas:
- Formulario de Registro
- Formulario de Inicio De Sesión
- Autenticación de usuarios
- Plan alimenticio
- Control de configuraciones basicas

Endpoints:

https://supletanesappfinal-production.up.railway.app/swagger-ui/index.html#
https://supletanesappfinal-production.up.railway.app/

Productos:
- GET /api/productos
- GET /api/productos/{id}
- GET /api/productos/categoria/{categoria}
- POST /api/productos
- PUT /api/productos/{id}
- DELETE /api/productos/{id}

Recordatorios:
- POST /api/v1/recordatorios
- GET /api/v1/recordatorios/usuario/{idUsuario}
- PUT /api/v1/recordatorios/{id}
- DELETE /api/v1/recordatorios/{id}

Food:
- /api/food/{barcode}
- /api/food/api/food/search

Codigo fuente micro servicio:

Frontend:
- frontend/app/src/main/java/com/example/supletanes/data/api/FoodApi.kt
- frontend/app/src/main/java/com/example/supletanes/ui/screens/plan/PlanScreen.kt
- frontend/app/src/main/java/com/example/supletanes/ui/screens/plan/viewmodel/PlanViewModel.kt

Backend:
- backend/src/main/java/cl/supletanes/supletanes_app/foods

Pasos para ejecutar:
- Iniciar app
- Presionar botón de continuar
- Iniciar sesión o continuar como invitado
- Acceder a las diferentes opciones y pantallas disponibles

Ramas:

Nicolas Caro: RamaCaro
Christopher Espinoza: RamaCHR
Francisco Olate: RamaFObackend