## INSTALL
Window installer: Windows x64 Compressed Archive (đuôi .zip)
https://download.oracle.com/java/17/archive/jdk-17.0.12_windows-x64_bin.zip

Ubuntu (Linux) installer: Linux x64 Compressed Archive (đuôi .tar.gz)
https://download.oracle.com/java/17/archive/jdk-17.0.12_linux-x64_bin.tar.gz

## WIDOW ACTIVATE JAVA17 & SBT
$env:SBT_GLOBAL_BASE="E:\scala-portable\sbt-global"
$env:SBT_BOOT_DIRECTORY="E:\scala-portable\sbt-boot"
$env:REPOSITORIES="E:\scala-portable\ivy2-cache"
$env:IVY_HOME="E:\scala-portable\ivy2-cache"
$env:JAVA_HOME="E:\jdk\jdk17-win"
$env:PATH="$env:JAVA_HOME\bin;E:\scala-portable\sbt\bin;" + $env:PATH
java -version

sbt clean; sbt run;

## UBUNTU ACTIVATE JAVA17 & SBT
export SBT_GLOBAL_BASE="/path/to/scala-portable/sbt-global"
export SBT_BOOT_DIRECTORY="/path/to/scala-portable/sbt-boot"
export REPOSITORIES="/path/to/scala-portable/ivy2-cache"
export IVY_HOME="/path/to/scala-portable/ivy2-cache"
export JAVA_HOME="/path/to/scala-portable/jdk17-linux"
export PATH="$JAVA_HOME/bin:/path/to/scala-portable/sbt/bin:$PATH"
java -version

sbt clean; sbt run;