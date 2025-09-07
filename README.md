# RollerSpeed

Proyecto para la asignatura **Frameworks MVC**, donde se desarrollará una aplicación utilizando el framework **Spring Boot** con **Thymeleaf**, **Spring Data JPA** y **MySQL**.

## Desarrollado por:
Juan Pedro Montoya Vélez.
Luis Alberto Cabezas Sánchez.
Juan Diego Johnson Posada.
Cristian Felipe Silva Castiblanco.


---

## 🚀 Requisitos previos

Antes de empezar, asegúrate de tener instalado:

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)  
- [Maven](https://maven.apache.org/download.cgi)  
- [MySQL](https://dev.mysql.com/downloads/)  
- [Visual Studio Code](https://code.visualstudio.com/) con la extensión **Spring Boot Dashboard**  
- Git  

---

## ⚙️ Configuración del proyecto

El proyecto fue generado con **Spring Initializr** con la siguiente configuración:

- **Project:** Maven  
- **Language:** Java  
- **Spring Boot Version:** 3.3.8  
- **Group:** `com.rollerspeed`  
- **Artifact:** `rollerspeed`  
- **Name:** `rollerspeed`  
- **Packaging:** Jar  
- **Java Version:** 17  

### 📦 Dependencias seleccionadas
- Spring Web  
- Thymeleaf  
- Spring Data JPA  
- MySQL Driver  
- Spring Boot DevTools  

---

## 🛠️ Instalación y ejecución

1. **Clonar el repositorio**  
   ```bash
   git clone https://github.com/tu-usuario/rollerspeed.git
   cd rollerspeed

2. **Configurar la base de datos en MySQL**
    ```sql
    CREATE DATABASE speed;
    CREATE USER 'userspeedroller'@'localhost' IDENTIFIED BY '123456';
    GRANT ALL PRIVILEGES ON speed.* TO 'userspeedroller'@'localhost';
    FLUSH PRIVILEGES;

3. **Configurar el archivo application.properties**
    
    spring.application.name=rollerspeed
    server.port=8080

    spring.datasource.url=jdbc:mysql://localhost:3306/speed
    spring.datasource.username=userspeedroller
    spring.datasource.password=123456
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

4. **Ejecutar el proyecto**
    ```bash
    ./mvnw spring-boot:run

    O desde VSCode con la extensión Spring Boot Dashboard.

## Estructura basica del proyecto:
    ```bash
    rollerspeed/
        ┣ src/
        ┃ ┣ main/
        ┃ ┃ ┣ java/com/rollerspeed   # Código fuente de la aplicación
        ┃ ┃ ┗ resources/             # Recursos (templates (fragments, pages, application.properties))
        ┃ ┗ test/                    # Pruebas unitarias
        ┣ pom.xml                    # Configuración del proyecto y dependencias
        ┗ README.md                  # Documentación#


