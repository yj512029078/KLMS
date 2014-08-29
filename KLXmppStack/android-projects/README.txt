#This is just a simple description of how to build our project of Kunlun
#Author yj, @sj

Build C++ libraries:

The source code is built for Android Platform. We often build the module KLXmppStack on linux-like platform.
To build it successfully, there are several steps you should take to prepare related needed build-environment.

1> 
Ubuntu (12.04 or later)
2>
build-essential tools (make, gcc, g++... ) 
make sure the version of make should be 3.81 or later
3>
autotools (automake autoconf...)
4>
swig 
make sure the version of swig should be 2.0.10 or later
5>
NDK
download ndk VERSION: android-ndk-r7c
extract the build-toolchain of lower version of android8-toolchain

Note:
Swig will depend on PCRE for regular expression, so install it
Swig, Autotools  will depend on python, so install it.
Our build procedure is integrated as shell scripts, when build, 
make sure KLCppWrap/interface/java/autoBuild.sh is modified:
Toolchain_Dir=/home/yj/Work_DevelopeEnv/android8-toolchain/bin should be your own real Toolchain_Dir

After environment listed above prepared, the just go to source dir: KLXmppStack/gloox, execute:
bash buildAndroid.sh


IDE:
Some may be customed to develop in an IDE, so we also provide ways to integrate all codes(C++, java...) into an IDE, we choose Eclipse.
To build it successfully, there are several steps you should take to prepare related needed build-environment.

1>
JDK
2>
Eclipse
3>
CDT plugin for Eclipse
4>
android sdk

Note:
As eclipse depend on jdk, the path of jdk, jre should be configured correctly to the PATH var.
When uplating Android sdk, as some widely-known reason in China, we have to configure network parameters to make it update correctly.


Summarise:
These above is just a simple description of ways to build, detailed information will be provided recently.

