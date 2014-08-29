#include "../include/kl_backgroundinfo.h"

namespace kl
{
	const char* TAG_NAME_BACKGROUND = "background";

	BackgroundInfo::BackgroundInfo()
		: m_bytes( 0 ),
		  m_height( 0 ),
		  m_width( 0 )
	{
	}

	BackgroundInfo::BackgroundInfo( const gloox::Tag* tag_ )
		: m_bytes( 0 ),
		  m_height( 0 ),
		  m_width( 0 )
	{
		if ( TAG_NAME_BACKGROUND != tag_->name() || kl::XMLNS_NEEKLE_BACKGROUND != tag_->xmlns() )
		{
			return;
		}
		gloox::Tag* tag_info = tag_->findChild( "info" );
		if ( tag_info )
		{
			m_id = tag_info->findAttribute( "id" );
			m_type = tag_info->findAttribute( "type" );
			m_url = tag_info->findAttribute( "url" );
			m_bytes = atoi( tag_info->findAttribute( "bytes" ).c_str() );
			m_height = atoi( tag_info->findAttribute( "height" ).c_str() );
			m_width = atoi( tag_info->findAttribute( "width" ).c_str() );
		}
	}

	gloox::Tag* BackgroundInfo::newTag() const
	{
		gloox::Tag* tag_background = new gloox::Tag( kl::TAG_NAME_BACKGROUND );
		tag_background->setXmlns( kl::XMLNS_NEEKLE_BACKGROUND );

		gloox::Tag* tag_info = new gloox::Tag( "info" );
		tag_info->addAttribute( "id", m_id );
		tag_info->addAttribute( "type", m_type );
		tag_info->addAttribute( "url", m_url );
		tag_info->addAttribute( "bytes", int2string( m_bytes ) );
		tag_info->addAttribute( "height", int2string( m_height ) );
		tag_info->addAttribute( "width", int2string( m_width ) );

		tag_background->addChild( tag_info );

		return tag_background;
	}
}