#!/bin/bash
#                        configureAm . s h
# written by yj, @sj  09/08/2013

declare -a sourceArray
declare -a headerArray

function modifyAmSources()
{
	#要注意的是，主要pwd返回的当前路径不是shell脚本所在路径，而是执行shell脚本的路径
	#${0}返回执行路径和脚本路径的相对路径
	local scriptPath=`pwd`/${0}
	local wrapDir=${scriptPath%/*}
	cd $wrapDir

	for file in `ls`
	do
		if [ -f "$file" ];then
			suffix=${file##*.}
			if [[ "$suffix" = "cpp" ]] || [[ "$suffix" = "cxx" ]] ; then
				size=${#sourceArray[*]}
				sourceArray[size]="../../KLCppWrap/interface/java/"$file
				echo ${sourceArray[size]} > /dev/null
			fi
		fi
	done
	
	#目前是按照这个相对目录走的，如果相对目录变化，需要改变
	klXmppDir=../../../KLXmpp/src
	cd $klXmppDir
	for file in `ls`
	do
		if [ -f "$file" ];then
			suffix=${file##*.}
			if [[ "$suffix" = "cpp" ]] || [[ "$suffix" = "cxx" ]] ; then
				size=${#sourceArray[*]}
				sourceArray[size]="../../KLXmpp/src/"$file
				echo ${sourceArray[size]} > /dev/null
			fi
		fi
	done
	
	cd $wrapDir

	declare -i size=${#sourceArray[*]}
	echo size=$size > /dev/null
	if [ $size -eq 0 ] ; then
		echo "ERROR:can not find file info to add to am, exit"
		exit -1
	fi
	
	#目前是按照这个相对目录走的，如果相对目录变化，需要改变
	local fileName=../../../gloox/src/Makefile.am
	deleteNonGlooxItem $fileName
	
	#统计Makefile.am总行数
   	local lineCount=`awk '{print NR}' ${fileName} | tail -n1`
	local startLineInfo=`grep  -n  "libgloox_la_SOURCES"   ${fileName}`
	#获取含有libgloox_la_SOURCES的所在行号
	local startLineNumber=${startLineInfo%%:*}
	echo startLineNumber=$startLineNumber > /dev/null
	
	idx=$startLineNumber
	while [ $idx -lt $lineCount ]
	do
		#获得第idx行文本内容
		content=`awk NR==$idx ${fileName}`
		#判断当前这行是否是空行，但是目前还有一个问题，虽暂时不影响，如果有空格组成的空行，不能区别出来
		grep -n '^$' ${fileName} | grep $idx
		
		#找到空行，直接退出循环
		if [ $? -eq 0 ] ; then
			echo idx=$idx > /dev/null
			echo "break" > /dev/null
			break
		fi

     	let idx=$idx+1
		echo idx=$idx > /dev/null
	done

	let idx=${idx}-1
	content=`awk NR==$idx ${fileName}`
	echo $content | grep " \\\\" > /dev/null
	#判定是否包含空格+反斜杠模式，然后分别处理
	if [ $? -eq 0 ] ; then
		addSourceFileInfoToNewLine ${idx} $fileName
	else
		addSourceBackSlash "$content" $fileName

		if [ $? -eq 0 ] ; then
			echo "Success:addSourceBackSlash finish, start addSourceFileInfoToNewLine"
			addSourceFileInfoToNewLine ${idx} $fileName
		else
			echo "ERROR:addSourceBackSlash fail"
		fi
	fi
}

function addSourceBackSlash()
{
	echo "ECHO:start addSourceBackSlash"

	local lineContent=$1
	local fileName=$2
	#得到的结果是两个反斜杠的，因为反斜杠要转义
	local newLineContent=$lineContent" \\\\"
	echo lineContent=$lineContent > /dev/null
	echo newLineContent=$newLineContent > /dev/null
	
	#替换的是只有一个反斜杠的，因为反斜杠的转义
	sed -i "s/$lineContent/$newLineContent/g" $fileName
	#notice the difference above and the below, 下面这种单引号的无法传递可变替换的参数
	#sed -i 's/$lineContent/$newLineContent/g' $fileName
	return $?
}

function addSourceFileInfoToNewLine()
{
	local lineNumber=$1
	local fileName=$2
	local count=0
	
	#只有最后一行不需要加反斜杠，其它都需要加反斜杠
	for data in ${sourceArray[@]}  
	do  
		if [ $count -eq 0 ] ; then
			sed -i "${lineNumber}a\\$data" $fileName

			if [ $? -ne 0 ] ; then
				echo "ERROR: add ${data} to $fileName"
				exit -1
			else
				echo "ECHO:add ${data}   to file  $fileName end"
				let count=1
			fi
		else
			data=$data" \\\\"
			sed -i "${lineNumber}a\\$data" $fileName

			if [ $? -ne 0 ] ; then
				echo "ERROR: add ${data} to $fileName"
				exit -1
			else
				echo "ECHO:add ${data}   to file  $fileName end"
			fi
		fi
	done  

	echo "ECHO:addSourceFileInfoToNewLine finish"
}

function modifyAmHeaders()
{
	local scriptPath=`pwd`/${0}
	local wrapDir=${scriptPath%/*}
	cd $wrapDir

	for file in `ls`
	do
		if [ -f "$file" ];then
			suffix=${file##*.}
			if [[ "$suffix" = "h" ]] || [[ "$suffix" = "hh" ]] ; then
				size=${#headerArray[*]}
				headerArray[size]="../../KLCppWrap/interface/java/"$file
				echo ${headerArray[size]} > /dev/null
			fi
		fi
	done
	
	klXmppDir=../../../KLXmpp/include
	cd $klXmppDir
	for file in `ls`
	do
		if [ -f "$file" ];then
			suffix=${file##*.}
			if [[ "$suffix" = "h" ]] || [[ "$suffix" = "hh" ]] ; then
				size=${#headerArray[*]}
				headerArray[size]="../../KLXmpp/include/"$file
				echo ${headerArray[size]} > /dev/null
			fi
		fi
	done
	
	cd $wrapDir

	declare -i size=${#headerArray[*]}
	echo size=$size > /dev/null
	if [ $size -eq 0 ] ; then
		echo "ERROR:can not find file info to add to am, exit"
		exit -1
	fi
	
	local fileName=../../../gloox/src/Makefile.am
   	local lineCount=`awk '{print NR}' ${fileName} | tail -n1`
	local startLineInfo=`grep  -n  "libglooxinclude_HEADERS"   ${fileName}`
	local startLineNumber=${startLineInfo%%:*}
	echo startLineNumber=$startLineNumber > /dev/null
	
	idx=$startLineNumber
	while [ $idx -lt $lineCount ]
	do
		content=`awk NR==$idx ${fileName}`
		grep -n '^$' ${fileName} | grep $idx
		
		if [ $? -eq 0 ] ; then
			echo idx=$idx > /dev/null
			echo "break" > /dev/null
			break
		fi

     	let idx=$idx+1
		echo idx=$idx > /dev/null
	done

	let idx=${idx}-1
	content=`awk NR==$idx ${fileName}`
	echo $content | grep " \\\\" > /dev/null
	if [ $? -eq 0 ] ; then
		addHeaderFileInfoToNewLine ${idx} $fileName
	else
		#注意，由于是 connectiontlsserver.h compressiondefault.h 形式，因此必须加引号传递过来，否则函数形参那边只能接收到一个参数connectiontlsserver.h
		addHeaderBackSlash "$content" $fileName

		if [ $? -eq 0 ] ; then
			echo "Success:addHeaderBackSlash finish, start addHeaderFileInfoToNewLine"
			addHeaderFileInfoToNewLine ${idx} $fileName
		else
			echo "ERROR:addHeaderBackSlash fail"
		fi
	fi
}

function addHeaderBackSlash()
{
	echo "ECHO:start addHeaderBackSlash"

	local lineContent=$1
	local fileName=$2
	local newLineContent=$lineContent" \\\\"
	echo lineContent=$lineContent > /dev/null
	echo newLineContent=$newLineContent > /dev/null
	
	sed -i "s/$lineContent/$newLineContent/g" $fileName
	return $?
}

function addHeaderFileInfoToNewLine()
{
	local lineNumber=$1
	local fileName=$2
	local count=0
	
	for data in ${headerArray[@]}  
	do  
		if [ $count -eq 0 ] ; then
			sed -i "${lineNumber}a\\$data" $fileName

			if [ $? -ne 0 ] ; then
				echo "ERROR: add ${data} to $fileName"
				exit -1
			else
				echo "ECHO:add ${data}   to file  $fileName end"
				let count=1
			fi
		else
			data=$data" \\\\"
			sed -i "${lineNumber}a\\$data" $fileName

			if [ $? -ne 0 ] ; then
				echo "ERROR: add ${data} to $fileName"
				exit -1
			else
				echo "ECHO:add ${data}   to file  $fileName end"
			fi
		fi
	done  

	echo "ECHO:addHeaderFileInfoToNewLine finish"
}

function deleteNonGlooxItem()
{
	local fileName=$1
	#将Makefile.am所有含有KLXmpp的行删除
	sed -i '/KLXmpp/d' ${fileName}
	#将Makefile.am所有含有KLCppWrap的行删除
	sed -i '/KLCppWrap/d' ${fileName}
}

#For the resason of delete am config item, it should be the first fun to invoke
modifyAmSources
modifyAmHeaders




