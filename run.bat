@echo off
title INDUSTRY-OS Local Server
echo ============================================================
echo   INDUSTRY-OS - Production Server
echo   Starting Spring Boot application on http://localhost:8080/
echo ============================================================
echo.
cd /d "%~dp0industry-os"
if not exist "target\industry-os-1.0.0.jar" (
    echo Error: Jar file not found in target directory.
    echo Please build first using mvn clean package -DskipTests
    pause
    exit /b 1
)

echo Open your browser at: http://localhost:8080/
echo Press CTRL+C to stop the server at any time.
echo.
java -jar "target\industry-os-1.0.0.jar"
pause

