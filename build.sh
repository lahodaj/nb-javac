set -e
#git clone --depth=1 https://github.com/openjdk/jdk jdk
git clone --depth=1 $HOME/src/jdk/jdk jdk
mkdir src
cp -r jdk/src/java.compiler src
cp -r jdk/src/jdk.compiler src
wget -O jackpot.jar https://search.maven.org/remotecontent?filepath=org/apache/netbeans/modules/jackpot30/tool/11.1/tool-11.1.jar
javac `find jdk/make/langtools/tools/propertiesparser/ -type f -name "*.java"`
java -classpath jdk/make/langtools/tools/ propertiesparser.PropertiesParser -compile src/jdk.compiler/share/classes/com/sun/tools/javac/resources/compiler.properties src/jdk.compiler/share/classes/com/sun/tools/javac/resources/
java -classpath jdk/make/langtools/tools/ propertiesparser.PropertiesParser -compile src/jdk.compiler/share/classes/com/sun/tools/javac/resources/launcher.properties src/jdk.compiler/share/classes/com/sun/tools/javac/resources/
(cd src; patch -p1 -i ../temporary-patches/language-changes)
java -classpath jackpot.jar org.netbeans.modules.jackpot30.cmdline.Main -hint-file make/langtools/netbeans/nb-javac/src/META-INF/upgrade/nbjavac.hint  -sourcepath make/langtools/netbeans/nb-javac/src/:src/jdk.compiler/share/classes/:src/java.compiler/share/classes --apply src/java.compiler/share/classes src/jdk.compiler/share/classes
(cd src; patch -p1 -i ../temporary-patches/manual-workarounds)
(cd src; patch -p1 -i ../temporary-patches/filesystems-run-on-jdk8)

(cd make/langtools/netbeans/nb-javac; ant clean && ant "-Djavac.compilerargs=-bootclasspath $JAVA_HOME8/jre/lib/rt.jar" jar)
