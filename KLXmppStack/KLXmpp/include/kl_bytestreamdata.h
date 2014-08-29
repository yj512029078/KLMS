#ifndef KL_BYTESTREAMDATA_H__
#define KL_BYTESTREAMDATA_H__

#include "../../KLXmpp/include/kl_common.h"
#include <string>

namespace kl
{
	class KLXMPP_API BytestreamData
	{
	public:
		BytestreamData( const std::string& data_ ) 
		{ 
			m_content = data_; 
		}
		virtual ~BytestreamData() {}

	public:
		unsigned copy( void* output_, unsigned maxsize_ )
		{
			unsigned retsize = 0;
			if( output_ )
			{
				retsize = ( m_content.size() > maxsize_ ) ? maxsize_ : m_content.size();
				memcpy( output_, m_content.c_str(), retsize );
			}
			return retsize;
		}

		unsigned length()
		{
			return m_content.size();
		}

	private:
		std::string m_content;
	};
}

#endif // KL_BYTESTREAMDATA_H__