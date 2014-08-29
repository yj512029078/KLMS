#include "../include/kl_convert.h"

#if ( defined _WIN32 ) && !defined( __SYMBIAN32__ )
wchar_t* utf82utf16( const char* sz_utf8_ )
{
	int len = -1;
	wchar_t* ret_utf16 = NULL;
	len = MultiByteToWideChar( CP_UTF8, 0, sz_utf8_, -1, NULL, 0 );
	ret_utf16 = (wchar_t*) malloc ( (len+1) * sizeof(wchar_t) );
	memset( ret_utf16, 0, ( (len+1) * sizeof(wchar_t) ) );
	MultiByteToWideChar( CP_UTF8, 0, sz_utf8_, -1, (LPWSTR) ret_utf16, len);
	return ret_utf16;
}

char* utf162utf8( const wchar_t* sz_utf16_ )
{
	int len = -1;
	char* ret_utf8 = NULL;
	len = WideCharToMultiByte( CP_UTF8, 0, sz_utf16_, -1, 
		NULL, 0, NULL, NULL );
	ret_utf8 = (char*) malloc ( (len+1) * sizeof(char) );
	memset( ret_utf8,0, ( (len+1) * sizeof(char) ) );
	WideCharToMultiByte( CP_UTF8, 0, sz_utf16_, -1, ret_utf8, len, NULL, NULL );
	return ret_utf8;
}

wchar_t* ansi2utf16( const char* sz_ansi_ )
{
	int len = -1;
	wchar_t* ret_utf16 = NULL;
	len = MultiByteToWideChar( CP_ACP, 0, sz_ansi_, -1, NULL, 0 );
	ret_utf16 = (wchar_t*) malloc ( (len+1) * sizeof(wchar_t) );
	memset( ret_utf16 ,0, ( (len+1) * sizeof(wchar_t) ) );
	MultiByteToWideChar( CP_ACP/*ANSI*/, 0, sz_ansi_, -1, (LPWSTR)ret_utf16, len );
	return ret_utf16;
}

char* utf162ansi( const wchar_t* sz_utf16_ )
{
	int len = -1;
	char* ret_ansi = NULL;
	len = WideCharToMultiByte( CP_ACP, 0, sz_utf16_, -1, 
		NULL, 0, NULL, NULL );
	ret_ansi = (char*) malloc ( (len+1) * sizeof(char) );
	memset( ret_ansi,0, ( (len+1) * sizeof(char) ) );
	WideCharToMultiByte( CP_ACP, 0, sz_utf16_, -1, ret_ansi, len, NULL, NULL );
	return ret_ansi;
}

char* ansi2utf8( const char* sz_ansi_ )
{
	wchar_t* ret_utf16 = ansi2utf16( sz_ansi_ );
	return utf162utf8( ret_utf16 );
}

char* utf82ansi( const char* sz_utf8_ )
{
	wchar_t* ret_utf16 = utf82utf16( sz_utf8_ );
	return utf162ansi( ret_utf16 );
}
#endif

std::string int2string( int i_ )
{
	std::stringstream ss;
	ss<<i_;
	return ss.str();
}

std::string double2string( double d_ )
{
	std::stringstream ss;
	ss<<d_;
	return ss.str();
}