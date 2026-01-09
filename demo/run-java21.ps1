# PowerShell script to run the JavaFX application with Java 21
$env:JAVA_HOME="C:\Install\jdk-21.0.2"

Write-Host "Using Java 21 from: $env:JAVA_HOME" -ForegroundColor Green
Write-Host "Java Version:" -ForegroundColor Yellow
& "$env:JAVA_HOME\bin\java.exe" -version

Write-Host "`nBuilding and running JavaFX application..." -ForegroundColor Green
mvn clean javafx:run