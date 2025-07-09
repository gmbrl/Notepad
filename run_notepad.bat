@echo off
cd /d "%~dp0"

echo Compiling Java files...
javac -cp ".;lib\flatlaf-3.6.jar" NotePadApp.java FontChooserDialog.java FindReplaceDialog.java

if %errorlevel% neq 0 (
    echo ❌ Compile failed. Please check for errors.
    pause
    exit /b
)

echo ✅ Compile successful. Launching app...
java --enable-native-access=ALL-UNNAMED -cp ".;lib\flatlaf-3.6.jar" NotePadApp
pause
