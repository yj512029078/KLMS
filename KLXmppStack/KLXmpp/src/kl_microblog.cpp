#include "../include/kl_microblog.h"
#include "../../gloox/src/tag.h"
#include "../../gloox/src/pubsubitem.h"

namespace kl
{
	Microblog::Microblog( const std::string& id_ )
		: m_id( id_ ),
		  m_type( Microblog::Type::Text )
	{
	}

	Microblog::Microblog( const gloox::PubSub::Event* event_ )
		: m_type( Microblog::Type::Text )
	{
		if ( event_ )
		{
			gloox::PubSub::Event::ItemOperationList items = event_->items();
			if ( gloox::PubSub::EventType::EventItems == event_->type() )
			{
				gloox::PubSub::Event::ItemOperationList::const_iterator it = items.begin();
				for ( ; it != items.end(); it++ )
				{
					m_id = (*it)->item;
					// <item>
					const gloox::Tag* tag_item = (*it)->payload;
					if ( tag_item )
					{
						// <entry>
						gloox::Tag* tag_entry = tag_item->findChild( "entry" );
						if ( tag_entry && XMLNS_ATOM == tag_entry->xmlns() )
						{
							// <author>
							gloox::Tag* tag_author = tag_entry->findChild( "author" );
							if ( tag_author )
							{
								m_author = tag_author->cdata();
							}
							// <content>
							gloox::Tag* tag_content =  tag_entry->findChild( "content" );
							if ( tag_content )
							{
								const std::string str_type = tag_content->findAttribute( "type" );
								if ( "xhtml" == str_type )
								{
									m_type = Microblog::Type::Xhtml;
								}
								m_content = tag_content->cdata();
							}
							// <published>
							gloox::Tag* tag_published = tag_entry->findChild( "published" );
							if ( tag_published )
							{
								m_published = tag_published->cdata();
							}
							// <geoloc>
							gloox::Tag* tag_geoloc = tag_entry->findChild( "geoloc" );
							if ( tag_geoloc )
							{
								// <locality>
								gloox::Tag* tag_locality = tag_geoloc->findChild( "locality" );
								if ( tag_locality )
								{
									m_geoloc = tag_locality->cdata();
								}
							}
							// <published>
							gloox::Tag* tag_device = tag_entry->findChild( "device" );
							if ( tag_device )
							{
								m_device = tag_device->cdata();
							}
							// <link>
							gloox::Tag* tag_link = tag_entry->findChild( "link" );
							if ( tag_link && tag_link->hasAttribute( "rel", "replies" ) &&  tag_link->hasAttribute( "title", "comments" ) )
							{
								m_commentLink = tag_link->findAttribute( "href" );
							}

						} // end if ( tag_entry && XMLNS_ATOM == tag_entry->xmlns() )

					} // end if ( tag_item )

				} // end for ( ; it != items.end(); it++ )

			} // end if ( gloox::PubSub::EventType::EventItems == event_->type() )

		} // end if ( event_ )
	}

	Microblog::~Microblog()
	{
	}
}
