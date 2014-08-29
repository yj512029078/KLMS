#ifndef KL_AVATARMETADATA_H__
#define KL_AVATARMETADATA_H__

#include "../../KLXmpp/include/kl_common.h"

namespace kl
{
	class KLXMPP_API AvatarMetadata
	{
	public:
		AvatarMetadata( const std::string& id_, int bytes_, const std::string& type_ )
			: m_id( id_ ),
			  m_url( "" ),
		      m_bytes( bytes_ ),
			  m_height( -1 ),
			  m_width( -1 ),
		      m_type( type_ )
		{
		}
		virtual ~AvatarMetadata() 
		{
		}
	
	public:
		const std::string id() const { return m_id; }

		const std::string url() const { return m_url; }
		
		int bytes() const { return m_bytes; }
		
		int height() const { return m_height; }
		
		int width() const { return m_width; }
		
		const std::string type() const { return m_type; }
		
		void setUrl( const std::string& url_ ) { m_url = url_; }
		
		void setHeight( int height_ ) { m_height = height_; }

		void setWidth( int width_ ) { m_width = width_; }
	
	private:
		std::string m_id;
		std::string m_type;
		std::string m_url;
		int         m_bytes;
		int         m_height;
		int         m_width;

	}; // end class KLXMPP_API AvatarMetadata
}

#endif // KL_AVATARMETADATA_H__