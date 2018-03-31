setlocal
cd ../bin
del /F state_peer*
for /d %%x in (backups_peer*) do rd /s /q "%%x"
cd files_restored
del /F test*
cd ..
cls