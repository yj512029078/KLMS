#!/bin/bash
#                        generateWrap . s h
# written by yj, @sj 08/08/2013

Pkg_Name=org.xmpp.myWRAP
Wrap_Cpp_File_Name=klcppwrap_wrap.cpp
#If invoke shell script in the current Dir, s{0} will be the filename of the script
scriptPath=`pwd`"/${0}"
shellDir=${scriptPath%/*}
OUT_DIR=${shellDir}/../KLCppWrap/interface/java

echo "Java(Google Dalvik)..."
echo "Google Android special tasks"

rm -f ${OUT_DIR}/*.java
if [ $? -ne 0 ] ; then
	#statement below is ok
	echo "ERROR: rm *.java failed"
	exit -1
fi
rm -f ${OUT_DIR}/*.h
rm -f ${OUT_DIR}/*.cpp

swig -c++ -java -package ${Pkg_Name} -outdir ${OUT_DIR} -o ${OUT_DIR}/${Wrap_Cpp_File_Name} ${OUT_DIR}/java.i

if [ $? -eq 0 ] ; then
	echo "Success:Swig, start sed"
	sed -i 's/dynamic_cast/static_cast/g' ${OUT_DIR}/${Wrap_Cpp_File_Name}
	sed -i 's/const const/const/g' ${OUT_DIR}/${Wrap_Cpp_File_Name}
	echo "wrap code generating process finished"
else
	echo "ERROR:Swig, exit"
 	exit -1
fi


