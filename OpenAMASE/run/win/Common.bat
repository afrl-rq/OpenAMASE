set here=%CD%
cd ..\..
java -splash:./data/amase_splash.png -classpath ./dist/*;./lib/*;./lib/jogl/*;./lib/GRAL/*;  avtas.app.Application %*
cd %here%
