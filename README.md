# Ejercicio 6: Polimorfismo vía interfaces

CC2008 - Programación Orientada a Objetos, semestre II de 2026. Temario C.

Prototipo Java con **interfaz gráfica Swing y menú de consola**, patrón MVC y una única lista polimórfica. Carga 12 integraciones de tarjetas de crédito, transferencias, billeteras, logística y FEL. Permite listar, buscar la configuración, ordenar por latencia y demostrar cobros, reintegros, rastreo, cotizaciones, XML y auditoría.

Todos los proveedores, estados y tiempos son simulados. No se realizan llamadas de red ni cobros reales. Los XML muestran la capacidad documental y no tienen validez fiscal. Los datos se reinician al cerrar.

## Ejecutar en Windows

1. Extrae el ZIP; no ejecutes los archivos dentro del archivo comprimido.
2. Comprueba en una terminal que `java -version` y `javac -version` indiquen **17 o posterior**. Se requiere un JDK, no solamente un JRE. Si el comando no existe, instala un JDK y agrega su carpeta `bin` al `PATH`.
3. Haz doble clic en **`ejecutar.bat`** para compilar y abrir la interfaz gráfica.
4. Para usar el menú de consola, abre una terminal en esta carpeta y ejecuta:

```bat
ejecutar.bat --consola
```

En PowerShell usa `./ejecutar.bat --consola`. La compilación usa UTF-8; conserva esa codificación al editar.


