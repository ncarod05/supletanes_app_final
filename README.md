Supletanes App - Gestión de Suplementos y Seguimiento Nutricional
Aplicación móvil diseñada para ayudar a los usuarios a llevar un seguimiento de su plan de alimentación y gestionar su inventario de suplementos.

Arquitectura y Tecnologías

Frontend (Android):
  - Lenguaje: Kotlin
  - Arquitectura: MVVM (Model-View-ViewModel)
  - Librerías Clave:
    - Coroutines / Flow: Gestión de concurrencia y flujo de datos asíncrono.
    - Jetpack Compose: LiveData/State, ViewModel, Navigation.
    - Retrofit: Comunicación HTTP con el backend.
    - MockK / Coroutine Test: Pruebas unitarias de ViewModels.

Backend (Servicio API REST):
  - Framework: Spring Boot
  - Lenguaje: Java
  - Despliegue Público: Servicio alojado en Render.com.
  - Base de Datos: PostgreSQL, también gestionada desde el entorno de render.com.

Características Principales

- Autenticación de usuario con feedback visual
- Gestión de la sesión de usuario
- Seguimiento nutricional con registro de comidas mediante cámara
- Recordatorios personalizados con acceso a calendario
- Gestión de Suplementos (CRUD)
- Gestión de perfil con opciones de privacidad y vista de recordatorios
