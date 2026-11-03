@echo off
setlocal
cd /d "%~dp0"
chcp 65001 >nul
where javac >nul 2>nul
if errorlevel 1 (
    echo Instala un JDK 17 o posterior y agrega su carpeta bin al PATH.
    exit /b 1
)
if not exist build mkdir build
javac -encoding UTF-8 --release 17 -Xlint:all -Werror -d build @fuentes.txt
if errorlevel 1 exit /b 1
echo Compilacion correcta.
exit /b 0
