#!/bin/bash
#                        autoBuild . s h
# written by yj, @sj 08/08/2013
#including process: automake, configure, make, make install 

Str_True=true

if [ $# -eq 1 ] ; then
	Is_Debug_Version=$1
else
	Is_Debug_Version=${Str_True}
fi

scriptPath=`pwd`/${0}
Current_Path=${scriptPath%/*}
Execute_Path=${Current_Path}/../../../gloox
Prefix_Dir=/home/yj/Work_Folder/Kunlun/gloox/Install_Dir/arm_depend_no_version_end_openssl_swig_debug
Openssl_Dir=/home/yj/Work_Folder/Kunlun/openssl/Install_Dir/openssl-1.0.1e_version_not_end
Toolchain_Dir=/home/yj/Work_DevelopeEnv/android8-toolchain/bin

echo "ECHO:start autobuild, including automake, configure, make, make install processes"

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
		echo "Success:make install, build process finish"
		return 0
	else
		echo "ERROR:make install, exit"
 		exit -1
	fi
}

if [ -d ${Execute_Path} ] ; then
	cd ${Execute_Path}
	automakeAndConfigureAndBuild
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






