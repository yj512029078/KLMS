#!/bin/bash
#                        allBuild . s h
# written by yj, @sj 08/08/2013
#including process: swig, modify makefile.am, automake, configure, make, make install 

echo "ECHO:start all build"

scriptPath=`pwd`/${0}
# allBuild.sh generateWrap.sh configureAm.sh autoBuild.sh should be in the same Dir
wrapDir=${scriptPath%/*}
cd $wrapDir

if [ -f "generateWrap.sh" ];then
	echo "ECHO:start generateWrap.sh"
	./generateWrap.sh
else
	echo "ERROR:generateWrap.sh not found"
fi

if [ $? -eq 0 ] ; then
	if [ -f "configureAm.sh" ];then
		echo "ECHO:start configureAm.sh"
		./configureAm.sh
	else
		echo "ERROR:configureAm.sh not found"
	fi
fi

if [ $? -eq 0 ] ; then
	if [ -f "autoBuild.sh" ];then
		echo "ECHO:start autoBuild.sh"
		#true means build deubg version, otherwise false
		./autoBuild.sh true
	else
		echo "ERROR:autoBuild.sh not found"
	fi
fi

echo "ECHO:end all build"