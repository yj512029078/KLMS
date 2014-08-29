#!/bin/bash
#                        allBuild . s h
# written by yj, @sj 08/08/2013
#including process: swig, modify makefile.am, automake, configure, make, make install, copy

echo "ECHO:start all build"

scriptPath=`pwd`/${0}
# allBuild.sh generateWrap.sh configureAm.sh autoBuild.sh should be in the same Dir
shellDir=${scriptPath%/*}
cd $shellDir

if [ -f "generateWrap.sh" ];then
	echo "ECHO:start generateWrap.sh"
	./generateWrap.sh
	if [ $? -ne 0 ] ; then
		exit -1
	else
		echo "ECHO: generateWrap.sh finished"
	fi
else
	echo "ERROR:generateWrap.sh not found"
	exit -1
fi

if [ -f "configureAm.sh" ];then
	echo "ECHO:start configureAm.sh"
	./configureAm.sh
	if [ $? -ne 0 ] ; then
		exit -1
	else
		echo "ECHO: configureAm.sh finished"
	fi
else
	echo "ERROR:configureAm.sh not found"
	exit -1
fi

if [ -f "autoBuild.sh" ];then
	echo "ECHO:start autoBuild.sh"
	#true means build deubg version, otherwise false
	./autoBuild.sh true
	if [ $? -ne 0 ] ; then
		exit -1
	else
		echo "ECHO: autoBuild.sh finished"
		echo "ECHO:end all build"
	fi
else
	echo "ERROR:autoBuild.sh not found"
	exit -1
fi

