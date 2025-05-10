# Requisitos del Sistema

### Microservicios

1. **Student Service**:
    - [x] Registrar estudiantes.
    - [x] Editar información de estudiantes.
    - [x] Eliminar estudiante.
    - [x] Obtener información de estudiante.
    - [x] Listar estudiantes.
    - [x] Ver qué profesor tiene asignado el estudiante.
    - [x] Ver todas las tareas del estudiante.(Se ve en el microservice task)

2. **Course Service**:
    - [x] Crear cursos.
    - [x] Editar cursos.
    - [x] Obtener información del curso.
    - [x] Ver lista de cursos.
    - [x] Eliminar curso.
    - [x] Ver qué alumnos están inscritos en un curso.
    - [x] Asignar profesores a cursos.
    - [1] Ver las tareas asociadas a un curso.(Se realiza en el microservicios task)
    - [x] Ver qué profesor tiene asignado un curso.

3. **Teacher Service**:
    - [x] Registrar profesores.
    - [x] Editar información de profesores.
    - [x] Eliminar profesor.
    - [x] Listar profesores.
    - [x] Obtener información de profesor.
    - [x] Obtener los cursos asignados a un profesor.

4. **Task Service**:
    - **Profesor**:
        - [x] Crear tareas.
        - [x] Actualizar tareas.
        - [x] Obtener información de tareas.
        - [x] Obtener detalles de tareas.
        - [x] Eliminar tareas.
        - [x] Ver todas las tareas de un curso.
        - [x] Ver todas las tareas asignadas a un estudiante.
        - [ ] Ver tareas por estudiante. (Verificar si el pdf esta bien osea que se vea) listo solo falta ajustarlo al controller
        - [x] Calificar tareas de estudiantes.

    - **Estudiante**:
        - [x] Obtener información de tareas asignadas.
        - [x] Ver detalles de una tarea asignada.
        - [x] Ver todas las tareas asignadas al estudiante.
        - [x] Subir tareas completadas.
        - [x]Que pueda ver que tarea subio



!!Configurar validaciones de si se entrego tarde la tareas, ajustar fecha y agregar la parte de calificacion final 
## Requisitos Técnicos

- **Spring Boot**.
- **Angular**: Versión 18 o superior.
- **Base de datos**: Uso de bases de datos independientes, PostgreSQL y MySQL (según el microservicio).
- **Autenticación y Autorización**: JWT para asegurar acceso a los servicios.
- **Microservicios**: Uso de API RESTful con comunicación entre microservicios.

## Arquitectura

- **Microservicios**: El sistema está basado en microservicios, cada uno responsable de una entidad específica:
    - `Student Service`
    - `Course Service`
    - `Teacher Service`
    - `Task Service`

- **Bases de datos**: Cada microservicio tiene su propia base de datos independiente, utilizando PostgreSQL o MySQL según corresponda:
    - `Student Service` usa PostgreSQL.
    - `Course Service` usa MySQL.
    - `Teacher Service` usa PostgreSQL.
    - `Task Service` usa MySQL.

- **Comunicación entre microservicios**: Los microservicios se comunican entre sí utilizando **OpenFeign**, lo que permite realizar llamadas HTTP (REST API) de manera eficiente entre los servicios sin necesidad de configuraciones complejas de cliente HTTP.

## Requisitos de Implementación

1. Cada microservicio debe tener su propia base de datos o esquema, de modo que se garantice la independencia de los servicios y la integridad de los datos.
2. La autenticación debe ser gestionada por un sistema centralizado utilizando JWT.
3. Los servicios deben ser independientes y escalables, pudiendo ser desplegados y mantenidos de manera separada.

## Seguridad

- **Autenticación**: El sistema debe requerir autenticación de usuarios mediante JWT.
- **Autorización**: Los roles de los usuarios (estudiante, profesor) deben ser controlados para permitir solo acciones permitidas según el rol.

## Consideraciones Adicionales

- El sistema debe ser escalable y permitir la adición de más microservicios si es necesario.
- Debe manejar adecuadamente los errores y las respuestas de API.
