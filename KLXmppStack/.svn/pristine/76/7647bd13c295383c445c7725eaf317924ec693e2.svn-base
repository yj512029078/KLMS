#ifndef KL_UTIL_H__
#define KL_UTIL_H__

#include "kl_common.h"
#include "../../gloox/src/tag.h"

namespace kl
{
	namespace util
	{
		class KLXMPP_API TagUtil
		{
		public:
			static void insertField( gloox::Tag* tag_, const char* field_, const std::string& var_ );
			static void checkField ( const gloox::Tag* tag_, const char* field_, std::string& var_ );
			static void checkField ( const gloox::Tag* tag_, const char* field_, int* var_ );
			static void checkField ( const gloox::Tag* tag_, const char* field_, double* var_ );
		};
	}
}
#endif // KL_UTIL_H__