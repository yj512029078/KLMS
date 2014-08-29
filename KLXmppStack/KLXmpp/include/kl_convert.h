/** 示例代码3-1 File: convert.h 
*  实现UTF-8、UTF-16、ANSI（Windows简体中文系统下即GBK）互转 
*/
#ifndef KL_CONVERT_H__
#define KL_CONVERT_H__

#include <sstream>

#if ( defined _WIN32 ) && !defined( __SYMBIAN32__ )
#include <windows.h>

wchar_t* utf82utf16( const char* sz_utf8_ );

char* utf162utf8( const wchar_t* sz_utf16_ );

wchar_t* ansi2utf16( const char* sz_ansi_ );

char* utf162ansi( const wchar_t* sz_utf16_ );

char* ansi2utf8( const char* sz_ansi_ );

char* utf82ansi( const char* sz_utf8_ );

#endif

std::string int2string( int i_ );

std::string double2string( double d_ );

#endif // KL_CONVERT_H__