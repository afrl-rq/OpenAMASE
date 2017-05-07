set here=%CD%
cd ..\..
java -classpath ./dist/*;./lib/*; amase.cmasi.converter.ScenarioConverter
cd %here%