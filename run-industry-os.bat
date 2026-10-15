@echo off
title INDUSTRY-OS Enterprise Platform
echo =========================================================================
echo   INDUSTRY-OS Enterprise Management Platform
echo   Starting Localhost Server on Port 8080...
echo =========================================================================
echo.

set JAVA_CMD=java
if exist "C:\Program Files\Java\latest\jdk-27\bin\java.exe" (
    set "JAVA_CMD=C:\Program Files\Java\latest\jdk-27\bin\java.exe"
)

echo Opening browser at http://localhost:8080/ ...
timeout /t 2 >nul
start "" "http://localhost:8080/"

cd /d "%~dp0industry-os"
"%JAVA_CMD%" -jar target\industry-os-1.0.0.jar

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Could not start Spring Boot server. Launching standalone frontend directly...
    start "" "%~dp0industry-os-app.html"
)
pause

