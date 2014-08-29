/* kl_std_string.i 自定义 std::string 数据类型映射 */

// std::string
%typemap(ctype, out="void *") std::string "wchar_t *"
%typemap(imtype, inattributes="[MarshalAs(UnmanagedType.LPWStr)]") std::string "string"  
%typemap(cstype) std::string "string"

%typemap(csdirectorin) std::string "$iminput"
%typemap(csdirectorout) std::string "$cscall"

%typemap(in, canthrow=1) std::string
%{ 
	/* %typemap(in, canthrow=1) std::string  C# --> C++ */
	if (!$input) {
	SWIG_CSharpSetPendingExceptionArgument(SWIG_CSharpArgumentNullException, "null string", 0);
	return $null;
	}
	$1.assign( utf162utf8($input) ); 
	/* %typemap(in, canthrow=1) std::string  C# --> C++ */
%} 

%typemap(out) std::string 
%{ 
	/* %typemap(out) std::string  C# --> C++ */
	wchar_t* temp_$1 = 0;
	temp_$1 = utf82utf16( $1.c_str() );
	$result = SWIG_csharp_wstring_callback( temp_$1 );
	free( temp_$1 );
	/* %typemap(out) std::string  C# --> C++ */
%}

%typemap(csin) std::string "$csinput"

%typemap(csout, excode=SWIGEXCODE) std::string 
{
	/* %typemap(csout, excode=SWIGEXCODE) std::string  */
	string ret = $imcall;$excode
	return ret;
	/* %typemap(csout, excode=SWIGEXCODE) std::string  */
}

%typemap(directorout, canthrow=1, warning=SWIGWARN_TYPEMAP_THREAD_UNSAFE_MSG) std::string
%{ 
	/* %typemap(directorout, canthrow=1, warning=SWIGWARN_TYPEMAP_THREAD_UNSAFE_MSG) std::string */
	if (!$input) 
	{
		SWIG_CSharpSetPendingExceptionArgument(SWIG_CSharpArgumentNullException, "null string", 0);
		return $null;
	}
	/* possible thread/reentrant code problem */
	static std::wstring $1_str;
	$1_str = $input;
	$result = &$1_str;
	/* %typemap(directorout, canthrow=1, warning=SWIGWARN_TYPEMAP_THREAD_UNSAFE_MSG) std::string */
%}

%typemap(directorin) std::string 
%{ 
    /* %typemap(directorin) std::string C++ --> C# */
    wchar_t* temp_$1 = 0;
	temp_$1 = utf82utf16( $1.c_str() );
    $input = SWIG_csharp_wstring_callback( temp_$1 ); 
    free( temp_$1 );
    /* %typemap(directorin) std::string C++ --> C# */
%}

%typemap(csvarin, excode=SWIGEXCODE2) std::string 
%{
    set 
    {
      $imcall;$excode
    } 
%}

%typemap(csvarout, excode=SWIGEXCODE2) std::string 
%{
    get 
    {
      string ret = $imcall;$excode
      return ret;
    } 
%}

%typemap(throws, canthrow=1) std::string
%{ 
	/* %typemap(throws, canthrow=1) std::string */
	std::string message($1.begin(), $1.end());
	SWIG_CSharpSetPendingException(SWIG_CSharpApplicationException, message.c_str());
	return $null; 
	/* %typemap(throws, canthrow=1) std::string */
%}


// const std::string &
%typemap(ctype, out="void *") const std::string & "wchar_t *"
%typemap(imtype, inattributes="[MarshalAs(UnmanagedType.LPWStr)]") const std::string & "string"  
%typemap(cstype) const std::string & "string"

%typemap(csdirectorin) const std::string & "$iminput"
%typemap(csdirectorout) const std::string & "$cscall"

%typemap(in, canthrow=1) const std::string &
%{
	/* %typemap(in, canthrow=1) const std::string &  C# --> C++ */ 
	if ( !$input ) 
	{	
		SWIG_CSharpSetPendingExceptionArgument( SWIG_CSharpArgumentNullException, "null string", 0 );
		return $null;
	}
	std::wstring $1_str($input);
	$1 = new std::string( utf162utf8( $1_str.c_str() ) );
	/* %typemap(in, canthrow=1) const std::string &  C# --> C++ */
%}

%typemap(out) const std::string & 
%{ 
   /* %typemap(out) const std::string &   C++ --> C# */
   wchar_t* temp_$1 = 0;
   temp_$1 = utf82utf16 ( $1->c_str() );
   $result = SWIG_csharp_wstring_callback( temp_$1 ); 
   free( temp_$1 );
   /* %typemap(out) const std::string &   C++ --> C# */
%}

%typemap(csin) const std::string & "$csinput"

%typemap(csout, excode=SWIGEXCODE) const std::string & 
{
	/* %typemap(csout, excode=SWIGEXCODE) const std::string &  */
	string ret = $imcall;$excode
	return ret;
	/* %typemap(csout, excode=SWIGEXCODE) const std::string &  */
}

%typemap(directorout, canthrow=1, warning=SWIGWARN_TYPEMAP_THREAD_UNSAFE_MSG) const std::string &
%{ 
	/* %typemap(directorout, canthrow=1, warning=SWIGWARN_TYPEMAP_THREAD_UNSAFE_MSG) const std::string & */
	if (!$input) 
	{
		SWIG_CSharpSetPendingExceptionArgument(SWIG_CSharpArgumentNullException, "null string", 0);
		return $null;
	}
	/* possible thread/reentrant code problem */
	static std::wstring $1_str;
	$1_str = $input;
	$result = &$1_str;
	/* %typemap(directorout, canthrow=1, warning=SWIGWARN_TYPEMAP_THREAD_UNSAFE_MSG) const std::string & */
%}

%typemap(directorin) const std::string & 
%{ 
	/* %typemap(directorin) const std::string & */
	wchar_t* temp_$1 = 0;
	temp_$1 = utf82utf16( $1.c_str() );
	$input = SWIG_csharp_wstring_callback( temp_$1 ); // invoke System.Runtime.InteropServices.Marshal.PtrToStringUni impl C++ str --> C# str
	free( temp_$1 );
	/* %typemap(directorin) const std::string & */
%}

%typemap(csvarin, excode=SWIGEXCODE2) const std::string & 
%{
    set 
    {
      $imcall;$excode
    } 
%}
    
%typemap(csvarout, excode=SWIGEXCODE2) const std::string & 
%{
    get 
    {
      string ret = $imcall;$excode
      return ret;
    } 
%}

%typemap(throws, canthrow=1) const std::string &
%{ 
	/* %typemap(throws, canthrow=1) const std::string & */
	std::string message($1.begin(), $1.end());
	SWIG_CSharpSetPendingException(SWIG_CSharpApplicationException, message.c_str());
	return $null;
	/* %typemap(throws, canthrow=1) const std::string & */ 
%}

%{

#include <windows.h>

wchar_t* utf82utf16( const char* sz_utf8_ )
{
	int len = -1;
	wchar_t* ret_utf16 = NULL;
	len = MultiByteToWideChar( CP_UTF8, 0, sz_utf8_, -1, NULL, 0 );
	ret_utf16 = (wchar_t*) malloc ( (len+1) * sizeof (wchar_t) );
	memset( ret_utf16, '\0', ( (len+1) * sizeof (wchar_t) ) );
	MultiByteToWideChar( CP_UTF8, 0, sz_utf8_, -1, (LPWSTR) ret_utf16, len);
	return ret_utf16;
}

char* utf162utf8( const wchar_t* sz_utf16_ )
{
	int len = -1;
	char* ret_utf8 = NULL;
	len = WideCharToMultiByte( CP_UTF8, 0, sz_utf16_, -1, NULL, 0, NULL, NULL );
	ret_utf8 = (char*) malloc ( (len+1) * sizeof(char) );
	memset(ret_utf8, '\0', ( (len+1) * sizeof(char) ) );
	WideCharToMultiByte( CP_UTF8, 0, sz_utf16_, -1, ret_utf8, len, NULL, NULL );
	return ret_utf8;
}

%}