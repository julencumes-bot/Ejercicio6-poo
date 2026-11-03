@echo off
setlocal
cd /d "%~dp0"
call compilar.bat
if errorlevel 1 (
    pause
    exit /b 1
)
java -Dfile.encoding=UTF-8 -cp build poo.vista.Principal %*
if errorlevel 1 (
    pause
    exit /b 1
)
exit /b 0
