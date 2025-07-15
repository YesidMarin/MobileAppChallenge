# Mobile App Challenge - Mercado Libre 🛒

Este proyecto está desarrollado utilizando tecnologías modernas de Android, siguiendo buenas prácticas de arquitectura, diseño de UI/UX, testing y manejo eficiente de estado.

## Requisitos del sistema

Antes de compilar o ejecutar el proyecto, asegúrate de contar con lo siguiente:

| Requisito        | Versión mínima recomendada |
|------------------|----------------------------|
| **Java**         | 17                         |
| **Gradle**       | `8.11.1`                   |
| **API mínima**   | 26 (Android 8)             |

## Funcionalidad principal

La aplicación permite a los usuarios realizar búsquedas de productos en Mercado Libre con filtros avanzados, y visualizar los resultados en un grid con carga progresiva.

- Búsqueda básica y avanzada
- Visualización de resultados paginados
- Detalle de producto
- UI responsiva y fluida con Jetpack Compose

## Tech Stack

| Área                  | Tecnología                       |
|-----------------------|----------------------------------|
| UI                    | Jetpack Compose                  |
| Arquitectura          | MVVM / Data Flow                 |
| Patron                | Repository                       |
| DI                    | Hilt                             |
| Navegación            | Jetpack Navigation Compose       |
| Testing               | JUnit, Hilt Testing, Compose Test |
| HTTP Client           | Retrofit                         |
| Imagenes              | Coil                             |
| Persistencia temporal | rememberSaveable                 |


## Arquitectura

La app sigue una arquitectura **limpia y desacoplada** basada en MVVM:

- `ViewModel`: expone el estado UI como `StateFlow`.
- `Repository`: se comunica con la API de Mercado Libre.
- `UI`: 100% declarativa y reactiva con Compose.
- `Unidirectional Data Flow`: los datos fluyen UI ➜ ViewModel ➜ Repository ➜ DataSource y regresan como `StateFlow`, evitando estados inconsistentes.

Manejo de estado centralizado, navegación desacoplada mediante eventos (`navigationEvent`), y componentes reutilizables como `DropdownMenu`, `SearchField`, `ProductCard`.

## Configuración `API_KEY`

La clave de API se define como constante de compilación en
`app/build.gradle.kts`:

```kotlin
buildConfigField(
    "String",
    "API_KEY",
    "\"YOUR_API_KEY\""
)
```
Sustituye YOUR_API_KEY por la clave proporcionada por Mercado Libre, ejectua un `clean` y `recompila` el proyecto.


## Pruebas

- ✅ Pruebas unitarias de lógica de navegación y ViewModels.
- ✅ Pruebas UI con `AndroidComposeTestRule`.
- ✅ Test de casos de borde como campo vacío, navegación, errores de API.
- ✅ Cobertura de la pantalla `SearchScreen`.

---

## Imagenes

<img width="270" height="600" alt="Screenshot_20250715_005135" src="https://github.com/user-attachments/assets/a9096bdd-5c81-4be0-aab9-03230d7a2e86" />
<img width="270" height="600" alt="Screenshot_20250715_005121" src="https://github.com/user-attachments/assets/c74ad604-d733-489b-9b9a-04f82ef22517" />
<img width="270" height="600" alt="Screenshot_20250715_005017" src="https://github.com/user-attachments/assets/1f83c6e1-74e0-4cd7-ba15-7feb043fd980" />

## Video

https://github.com/user-attachments/assets/c868b45b-2a9b-4f01-a75b-a270a53389d1

## Test
<img width="483" height="70" alt="Screenshot 2025-07-15 at 01 13 28" src="https://github.com/user-attachments/assets/c21be435-7d64-4ccc-9c94-0c61130b23b2" />
<img width="588" height="397" alt="Screenshot 2025-07-15 at 01 10 13" src="https://github.com/user-attachments/assets/5739d954-3580-4e08-b72a-0447516d2711" />
