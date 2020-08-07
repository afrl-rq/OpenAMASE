here=$PWD;
cd ../..;
java -Xmx2048m -splash:./data/amase_splash.png -classpath ./dist/*:lib/*:./lib/GRAL/* avtas.app.Application $*;
cd "$here";
