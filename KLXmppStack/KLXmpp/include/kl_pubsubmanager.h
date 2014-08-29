#ifndef KL_PUBSUBMANAGER_H__
#define KL_PUBSUBMANAGER_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/pubsubmanager.h"
#include "../../gloox/src/pubsubresulthandler.h"

namespace kl
{
	class KLXMPP_API PubSubManager : public gloox::PubSub::Manager
	{
	public:
		PubSubManager( gloox::ClientBase* parent_, gloox::PubSub::ResultHandler* rh_ ) 
			: m_rh( rh_ ),
			  gloox::PubSub::Manager( parent_ ) {}

		virtual ~PubSubManager() {}

		virtual void handleIqID( const gloox::IQ& iq_, int context_ )
		{
			const gloox::JID& service = iq_.from();
			const std::string& id = iq_.id();

			switch( iq_.subtype() )
			{
			case gloox::IQ::Result:
				{
					const gloox::Error* error = iq_.error();
					switch( context_ )
					{
					case 14: // PublishItem
						{
							const gloox::StanzaExtension* se = iq_.findExtension( gloox::StanzaExtensionType::ExtPubSub );
							if ( !se && m_rh )
							{
								m_rh->handleItemPublication( id, service, "", gloox::PubSub::ItemList(), error );
							}
							break;
						}
					case 15: // DeleteItem
						{
							const gloox::StanzaExtension* se = iq_.findExtension( gloox::StanzaExtensionType::ExtPubSub );
							if ( !se && m_rh )
							{
								m_rh->handleItemDeletion( id, service, "", gloox::PubSub::ItemList(), error );
							}
							break;
						}
					default:
						break;
					}
				}
			}
			// 调用父类函数，保证原有逻辑依然会执行
			gloox::PubSub::Manager::handleIqID( iq_, context_ );
		}

	private:
		gloox::PubSub::ResultHandler* m_rh;
	};
}

#endif // KL_PUBSUBMANAGER_H__