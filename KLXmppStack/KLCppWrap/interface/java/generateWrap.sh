#!/bin/bash
#                        generateWrap . s h
# written by yj, @sj 08/08/2013

Pkg_Name=org.xmpp.myWRAP
Wrap_Cpp_File_Name=klcppwrap_wrap.cpp
#If invoke shell script in the current Dir, s{0} will be the filename of the script
scriptPath=`pwd`"/${0}"
OUT_DIR=${scriptPath%/*}

echo "Java(Google Dalvik)..."
echo "Google Android special tasks"
swig -c++ -java -package ${Pkg_Name} -outdir ${OUT_DIR} -o ${Wrap_Cpp_File_Name} java.i

if [ $? -eq 0 ] ; then
	echo "Success:Swig, start sed"
	sed -i 's/dynamic_cast/static_cast/g' ${Wrap_Cpp_File_Name}
	sed -i 's/const const/const/g' ${Wrap_Cpp_File_Name}
	echo "wrap code generating process finished"
else
	echo "ERROR:Swig, exit"
 	exit -1
fi


