#!/bin/bash
#                        autoBuild . s h
# written by yj, @sj 08/08/2013
#including process: automake, configure, make, make install, copy
#NOTE: make distclean, autogen.sh is not included, if needed, include it

Str_True=true

if [ $# -eq 1 ] ; then
	Is_Debug_Version=$1
else
	Is_Debug_Version=${Str_True}
fi

scriptPath=`pwd`/${0}
Current_Path=${scriptPath%/*}
Execute_Path=${Current_Path}/../gloox
Prefix_Dir=/home/yj/Work_Folder/Kunlun/gloox/Install_Dir/arm_depend_no_version_end_openssl_swig_debug
Openssl_Dir=/home/yj/Work_Folder/Kunlun/openssl/Install_Dir/openssl-1.0.1e_version_not_end
Toolchain_Dir=/home/yj/Work_DevelopeEnv/android8-toolchain/bin
#if swig relative dir is changed, it will also should be changed
#to make this shell independent, so SwigWrap_Dir is assigned here
SwigWrap_Dir=${Execute_Path}/../KLCppWrap/interface/java

echo "ECHO:start autobuild, including automake, configure, make, make install, copy processes"

if [ ! -d ${Toolchain_Dir} ] ; then
	echo "ERROR:Toolchain_Dir" ${Toolchain_Dir} "not exit, exit"
 	exit -1
fi

export PATH=${Toolchain_Dir}:$PATH
export CC=arm-linux-androideabi-gcc

if [ ! -d ${Prefix_Dir} ] ; then
	mkdir ${Prefix_Dir}
	if [ $? -ne 0 ] ; then
		echo "ERROR:Prefix_Dir, exit"
 		exit -1
	fi
fi

if [ ! -d ${Openssl_Dir} ] ; then
	echo "ERROR:Openssl_Dir" ${Openssl_Dir} "not exit, exit"
 	exit -1
fi

function automakeAndConfigureAndBuild()
{
	#echo "ECHO:start make distclean"
	#make distclean
	echo "ECHO:start automake"
	automake
	if [ $? -eq 0 ] ; then
		echo "Success:Automake, start configure"
		configure ${Is_Debug_Version}
		if [ $? -eq 0 ] ; then
			echo "Success:Configure, start modify makefile"
			modifyMakefileConfigAndBuild
			return $?
		else
			echo "ERROR:Configure, exit"
 			exit -1
		fi
	else
		echo "ERROR:automake, exit"
 		exit -1
	fi
}

function configure()
{
	local returnValue
	if [ "$1" = "${Str_True}" ] ;then  
		 ./configure --host=arm-linux-androideabi --enable-debug \
		--prefix=${Prefix_Dir} --with-examples=no  --with-tests=no --with-openssl=${Openssl_Dir}
		returnValue=$?
    else  
    	 	./configure --host=arm-linux-androideabi \
		--prefix=${Prefix_Dir} --with-examples=no  --with-tests=no --with-openssl=${Openssl_Dir}
		returnValue=$?
    fi  

	return ${returnValue}
}

function modifyMakefileConfigAndBuild()
{
	pwd
	sed -i 's/^CXXFLAGS =.*/CXXFLAGS = -g -O2 -std=gnu++0x -fno-strict-aliasing/' Makefile
	sed -i 's/^CXXFLAGS =.*/CXXFLAGS = -g -O2 -std=gnu++0x -fno-strict-aliasing/' src/Makefile

	if [ $? -eq 0 ] ; then
		echo "Success:modifyMakefileConfig, start make"
		makeAndInstall
		return $?
	else
		echo "ERROR:modifyMakefileConfig, exit"
 		exit -1
	fi
}

function makeAndInstall()
{
	make clean
	make
	if [ $? -eq 0 ] ; then
		echo "Success:make, start make install"
		makeInstall
		return 0
	else
		echo "ERROR:make, exit"
 		exit -1
	fi
}

function makeInstall()
{
	make install
	if [ $? -eq 0 ] ; then
		echo "Success:make install, start copy files"
		return 0
	else
		echo "ERROR:make install, exit"
 		exit -1
	fi
}

function judgeCopyFileResult()
{
	local file=$1
	local result=$2
	
	if [ $result -eq 0 ] ; then
		echo "Success:copy file ${file}"
		return 0
	else
		echo "ERROR:copy file ${file} failed"
		exit -1
	fi
}

function copyNeededFiles()
{	
	local destiSolibDir=${Current_Path}/../../KunLunAndroid/KunlunDroid/libs/armeabi
	local destiXmppWrapFilesDir=${Current_Path}/../../KunLunAndroid/KunlunDroid/src/org/xmpp/myWRAP
	
	local opensslLibCryptoName=libcrypto.so.1.0.0.so
	local opensslLibSslName=libssl.so.1.0.0.so
	local glooxLibName=libgloox.so

	local opensslLibCryptoPath=${Openssl_Dir}/lib/${opensslLibCryptoName}
	local opensslLibSslPath=${Openssl_Dir}/lib/${opensslLibSslName}
	local glooxLibPath=${Prefix_Dir}/lib/${glooxLibName}
	
	if [ ! -f ${opensslLibCryptoPath} ] ; then
		echo "ERROR:opensslLibCrypto not found, exit"
		exit -1
	fi
	
	if [ ! -f ${opensslLibSslPath} ] ; then
		echo "ERROR:opensslLibSsl not found, exit"
		exit -1
	fi
	
	if [ ! -f ${glooxLibPath} ] ; then
		echo "ERROR:glooxLib not found, exit"
		exit -1
	fi
	
	if [ -d ${destiSolibDir} ] ; then
		rm -f ${destiSolibDir}/opensslLibCryptoName
		if [ $? -eq 0 ] ; then
			cp -f ${opensslLibCryptoPath} ${destiSolibDir}
			judgeCopyFileResult ${opensslLibCryptoPath} $?
		else
			echo "ERROR:delete file ${destiSolibDir}/opensslLibCryptoName, exit"
			exit -1
		fi
		
		rm -f ${destiSolibDir}/opensslLibSslName
		if [ $? -eq 0 ] ; then
			cp -f ${opensslLibSslPath} ${destiSolibDir}
			judgeCopyFileResult ${opensslLibSslPath} $?
		else
			echo "ERROR:delete file ${destiSolibDir}/opensslLibSslName, exit"
			exit -1
		fi
		
		rm -f ${destiSolibDir}/glooxLibName
		if [ $? -eq 0 ] ; then
			cp -f ${glooxLibPath} ${destiSolibDir}
			judgeCopyFileResult ${glooxLibPath} $?
		else
			echo "ERROR:delete file ${destiSolibDir}/glooxLibName, exit"
			exit -1
		fi
	else
		echo "WARNING:Dir ${destiSolibDir} not exit, skip"
	fi
	
	if [ -d ${destiXmppWrapFilesDir} ] ; then
		rm -f ${destiXmppWrapFilesDir}/*.java
		if [ $? -eq 0 ] ; then
			cp -f ${SwigWrap_Dir}/*.java ${destiXmppWrapFilesDir}
			#for *.java, if any matches, then it passes the real items, otherwise *.java string
			judgeCopyFileResult *.java $?
		else
			echo "ERROR:delete files ${destiXmppWrapFilesDir} \*.java failed"
			exit -1
		fi
	else
		echo "WARNING:Dir ${destiXmppWrapFilesDir} not exit, skip"
	fi
	
	if [ $? -eq 0 ] ; then
		echo "Success:copy files proc, build process finish"
		return 0
	else
		echo "ERROR:copy files proc failed"
 		exit -1
	fi
}

if [ -d ${Execute_Path} ] ; then
	cd ${Execute_Path}
	automakeAndConfigureAndBuild
	copyNeededFiles
else
	echo "ERROR:Execute_Path" ${Execute_Path} "not exit, exit"
 	exit -1
fi

#not used yet, just save it for furture refrence
function modifyMakefileAm()
{
	path=$1  
	cd $path
	for filename in `ls`
	do
    		echo $filename 
	done

	#合并所有续行，非常有用
	sed -n -e '/^[[:space:]]*libgloox_la_SOURCES[[:space:]]*=/ {:a /.*[[:space:]]*\\[[:space:]]*$/ {N;b a;}; p}' Makefile.am | sed -e 's/[[:space:]]*libgloox_la_SOURCES[[:space:]]*=//' -e 's/\\//g' | xargs > array.txt

	#get info and line number;  line number be the first character
	grep  -n  "OUTPUT"   test

	#show the content of desired number of line
	a=7
	awk "NR==$a" test
	awk NR==$a test
	sed -n '1{p;q;}' test

	#get lines of the file, line will be counted even if it is Entered
	awk '{print NR}' test | tail -n1

	sed -e 's/ //g' -e '/^$/d' 
	
	echo $mm | grep " \\\\sfsdf"

	#insert new line after the fourth line
	sed -i "4a\\$vari" test
}






