cd ../bin
rm -rf state_peer*
rm -rf backups_peer*
cd files_restored
rm -rf test*
cd ../../scripts
javac -d ../bin @sources.txt
clear