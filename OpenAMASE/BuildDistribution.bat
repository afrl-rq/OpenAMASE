:: This calls a java program to build a zip file with the contents of the folder
:: syntax:  MakeDistribution <folder to build> <leading name of file> [list of excluded files/folders]
:: The file will be appended with date/time in UTC
java -classpath ./dist/* MakeDistribution  . OpenAMASE build versions
pause
