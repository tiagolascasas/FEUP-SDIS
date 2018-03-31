setlocal
cd ../bin
taskkill /f /t /im rmiregistry.exe
start /b rmiregistry
cls