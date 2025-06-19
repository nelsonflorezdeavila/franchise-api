# API de Franquicias

Este proyecto es una API REST reactiva construida con Spring Boot, Spring WebFlux y Spring Data MongoDB.

## Requisitos

* Java 21 (Recomendado: Amazon Corretto 21 o Eclipse Temurin 21)
* Maven 3.8+
* Docker (para ejecutar MongoDB localmente o con Testcontainers)
* IntelliJ IDEA (versión 2022.3 o superior recomendada)

## Configuración en IntelliJ IDEA

### 1. Importar el Proyecto
1. Abre IntelliJ IDEA
2. Selecciona "Open" o "File" > "Open..."
3. Navega hasta el directorio `franchise-api` y selecciónalo
4. Asegúrate de que IntelliJ reconozca el proyecto como Maven

### 2. Configurar el SDK de Java
1. Ve a "File" > "Project Structure" (Ctrl+Alt+Shift+S)
2. En "Project Settings" > "Project"
3. Establece el "Project SDK" a Java 21
4. Asegúrate de que "Project language level" esté configurado a "21 (Preview, records, patterns, switch etc.)"

### 3. Configurar Maven
1. Ve a "File" > "Settings" (Ctrl+Alt+S)
2. Navega a "Build, Execution, Deployment" > "Build Tools" > "Maven"
3. Verifica que la ruta de Maven sea correcta
4. Marca "Always update snapshots"
5. En "Runner" verifica que esté seleccionado "Use project settings"
6. Marca "Delegate IDE build/run actions to Maven"
7. Haz clic en "Apply" y luego en "OK"

### 4. Configurar el Perfil de Spring Boot
1. Ve a "Run" > "Edit Configurations..."
2. Haz clic en el botón "+" y selecciona "Spring Boot"
3. Configura lo siguiente:
   - Name: `Franchise API`
   - Main class: `co.com.nequi.franchise.FranchiseApplication`
   - Active profiles: `dev`
   - Environment variables: `SPRING_PROFILES_ACTIVE=dev`
4. En "Before launch" verifica que esté configurado para construir el proyecto
5. Haz clic en "Apply" y luego en "OK"

## Configuración del Entorno

1. **Clonar el repositorio**:
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd franchise-api
   ```

2. **Configuración de la Base de Datos**:
   La aplicación está configurada para conectarse a MongoDB. Tienes dos opciones:

   ### Opción 1: Usar MongoDB local con Docker (Recomendado)
   ```bash
   docker run -d --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=admin123 mongo:5.0
   ```

   ### Opción 2: Usar Testcontainers (para pruebas)
   Las pruebas de integración usan Testcontainers automáticamente, no se requiere configuración adicional.

## Ejecución en IntelliJ

### Ejecutar la Aplicación
1. Asegúrate de tener configurado el perfil de Spring Boot como se describió anteriormente
2. Haz clic en el botón de ejecución (▶) junto a la clase `FranchiseApplication`
3. O selecciona "Run 'Franchise API'" del menú desplegable de ejecución

### Configuración de Variables de Entorno
Si necesitas configurar variables de entorno específicas:
1. Ve a "Run" > "Edit Configurations..."
2. Selecciona "Franchise API"
3. En "Environment variables", añade:
   ```
   SPRING_DATA_MONGODB_URI=mongodb://admin:admin123@localhost:27017/franchise?authSource=admin
   SPRING_DATA_MONGODB_AUTHENTICATION_DATABASE=admin
   ```

### Depuración
1. Configura puntos de interrupción (breakpoints) donde necesites
2. Haz clic en el botón de depuración (🐞) o presiona Shift+F9
3. Usa las herramientas de depuración de IntelliJ para inspeccionar variables y el flujo de ejecución

## Ejecución con Maven

Alternativamente, puedes usar Maven desde la terminal:

```bash
# Ejecutar la aplicación
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# O compilar y ejecutar
mvn clean install
java -jar target/franchise-api-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

La API estará disponible en `http://localhost:8080`.

## Pruebas

Para ejecutar las pruebas unitarias y de integración:

```bash
mvn test
```

Las pruebas de integración utilizan Testcontainers para levantar una instancia de MongoDB, asegurando un entorno de prueba aislado.

## Documentación de la API

La documentación de la API está disponible a través de Swagger UI:

* **Swagger UI**: `http://localhost:8080/swagger-ui.html`
* **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Acceso a Swagger UI en IntelliJ
1. Ejecuta la aplicación en modo desarrollo
2. Abre el navegador integrado de IntelliJ (Alt+2)
3. Navega a `http://localhost:8080/swagger-ui.html`

### Configuración de Autorización en Swagger
Si la API requiere autenticación:
1. Haz clic en el botón "Authorize" (candado) en la esquina superior derecha
2. Ingresa las credenciales si es necesario
3. Haz clic en "Authorize" para guardar la configuración

## Cobertura de Código

Para generar el informe de cobertura de código con JaCoCo:

```bash
mvn clean verify
```

El informe se generará en `target/site/jacoco/index.html`.

## Estructura del Proyecto

El proyecto sigue una arquitectura limpia, con las siguientes capas principales:

* `application/`
  * `dto/`: Objetos de Transferencia de Datos (DTOs)
  * `service/`: Servicios de aplicación
  * `usecase/`: Casos de uso principales
  * `config/`: Configuraciones específicas de la aplicación

* `domain/`
  * `model/`: Entidades y modelos de dominio
  * `port/`: Puertos (interfaces)
  * `service/`: Servicios de dominio
  * `exception/`: Excepciones del dominio

* `infrastructure/`
  * `config/`: Configuraciones de Spring
  * `persistence/`: Implementaciones de repositorios
  * `web/`: Configuraciones web y filtros
  * `util/`: Utilidades y helpers
  * `aspect/`: Aspectos de la aplicación

* `presentation/`
  * `controller/`: Controladores REST
  * `dto/`: DTOs específicos de la API
  * `exception/`: Manejadores de excepciones
  * `filter/`: Filtros de seguridad y logs

## Solución de Problemas Comunes

### Error: "Java 21 not found"
Asegúrate de tener instalado JDK 21 y de que esté configurado correctamente en IntelliJ.

### Error de Conexión a MongoDB
Verifica que MongoDB esté en ejecución:
```bash
docker ps | grep mongodb
```

### Error: "Port 8080 already in use"
Cambia el puerto en `application-dev.yml` o detén el proceso que está usando el puerto 8080:
```bash
# En Windows
taskkill /F /PID $(netstat -ano | findstr :8080 | findstr LISTENING | %{$_ -replace '\s+', ' '} | %{$_.Split(' ')[1]})
```

### Limpiar Caché de IntelliJ
Si experimentas problemas extraños:
1. Ve a "File" > "Invalidate Caches..."
2. Selecciona "Invalidate and Restart"

## Recursos Adicionales

* [Documentación de Spring Boot](https://spring.io/projects/spring-boot)
* [Guía de IntelliJ IDEA](https://www.jetbrains.com/help/idea/spring-boot.html)
* [Documentación de MongoDB](https://docs.mongodb.com/)

## Solución de Problemas Comunes

### Error: "Java 21 not found"
Asegúrate de tener instalado JDK 21 y de que esté configurado correctamente en IntelliJ.

### Error de Conexión a MongoDB
Verifica que MongoDB esté en ejecución:
```bash
docker ps | grep mongodb
```

### Error: "Port 8080 already in use"
Cambia el puerto en `application-dev.yml` o detén el proceso que está usando el puerto 8080:
```bash
# En Windows
taskkill /F /PID $(netstat -ano | findstr :8080 | findstr LISTENING | %{$_ -replace '\s+', ' '} | %{$_.Split(' ')[1]})
```

### Limpiar Caché de IntelliJ
Si experimentas problemas extraños:
1. Ve a "File" > "Invalidate Caches..."
2. Selecciona "Invalidate and Restart"

## Recursos Adicionales

* [Documentación de Spring Boot](https://spring.io/projects/spring-boot)
* [Guía de IntelliJ IDEA](https://www.jetbrains.com/help/idea/spring-boot.html)
* [Documentación de MongoDB](https://docs.mongodb.com/)
