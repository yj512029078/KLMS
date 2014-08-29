#include "../include/kl_xmppstack.h"
#include "../include/kl_xmppcallback.h"
#include "../include/kl_discoinfo.h"
#include "../include/kl_service.h"
#include "../include/kl_entity.h"
#include "../include/kl_mucroom.h"
#include "../include/kl_mucroomconfig.h"
#include "../include/kl_mucroominfo.h"
#include "../include/kl_bytestreamdata.h"
#include "../include/kl_xmlparser.h"
#include "../include/kl_filemetadata.h"
#include "../include/kl_directmucinvitation.h"
#include "../include/kl_microblog.h"
#include "../include/kl_pubsubmanager.h"
#include "../include/kl_avatarmetadata.h"
#include "../include/kl_xmppentity.h"
#include "../include/kl_addressbookinfo.h"
#include "../include/kl_imailsmtpinfo.h"
#include "../include/kl_backgroundinfo.h"
#include "../include/kl_geoloc.h"
#include "../include/kl_receipt.h"

#include "../../gloox/src/util.h"
#include "../../gloox/src/sha.h"
#include "../../gloox/src/tlsbase.h"
#include "../../gloox/src/connectionbase.h"
#include "../../gloox/src/client.h"
#include "../../gloox/src/registration.h"
#include "../../gloox/src/rostermanager.h"
#include "../../gloox/src/presence.h"
#include "../../gloox/src/message.h"
#include "../../gloox/src/attention.h"
#include "../../gloox/src/xhtmlim.h"
#include "../../gloox/src/capabilities.h"
#include "../../gloox/src/pubsub.h"
#include "../../gloox/src/pubsubitem.h"
#include "../../gloox/src/pubsubevent.h"
#include "../../gloox/src/pubsubmanager.h"
#include "../../gloox/src/mucroom.h"
#include "../../gloox/src/instantmucroom.h"
#include "../../gloox/src/amp.h"
#include "../../gloox/src/socks5bytestreamserver.h"

#include <sstream>

namespace kl
{
	XmppStack::XmppStack( const gloox::JID& jid_, const std::string& password_, const std::string& host_, int port_ )
		: m_client( 0 ),
		  m_registration( 0 ),
		  m_pubSubManager( 0 ),
		  m_sIProfileFT( 0 ),
		  m_sOCKS5BytestreamServer( 0 ),
		  m_isCheckMicrobolgNodeConfig( false ),
		  m_isCheckAvatarDataNodeConfig( false ),
		  gloox::MUCInvitationHandler( 0 )
	{
		m_client = new gloox::Client( jid_, password_, port_ );
		
		if ( host_ != "" && host_ != jid_.server() )
		{
			m_client->setServer( host_ );
		}

		init();
	}

	XmppStack::~XmppStack()
	{
		gloox::util::clearMap( m_discoInfoMap );
		gloox::util::clearMap( m_publishAvatarDataTrackMap );
		gloox::util::clearMap( m_mUCRoomMap );
		gloox::util::clearMap( m_createMUCRoomTrackMap );
		delete m_sOCKS5BytestreamServer;
		delete m_sIProfileFT;
		delete m_pubSubManager;
		delete m_registration;
		delete m_client;
	}

	void XmppStack::init()
	{
		m_registration = new gloox::Registration( m_client, this->jid().bareJID() );
		m_pubSubManager = new kl::PubSubManager( m_client, this );
		m_sIProfileFT = new gloox::SIProfileFT( m_client, this );
		m_sOCKS5BytestreamServer = new gloox::SOCKS5BytestreamServer( m_client->logInstance(), 9999 );
		// 启用流压缩
		m_client->setCompression( true );
		// 设置处理订阅请求的方式为异步，否则需要立即选择批准/拒绝
		m_client->rosterManager()->registerRosterListener( this, false );
		// 注册观察者
		m_client->logInstance().registerLogHandler( gloox::LogLevelDebug, gloox::LogAreaAll, this );
		m_client->registerConnectionListener( this );
		m_client->registerPresenceHandler( this );
		m_client->registerMessageHandler( this );
		m_client->registerMUCInvitationHandler( this );
		m_registration->registerRegistrationHandler( this );
		// 添加可以解析的节扩展
		m_client->registerStanzaExtension( new gloox::Attention() );
		m_client->registerStanzaExtension( new gloox::XHtmlIM() );
		m_client->registerStanzaExtension( new kl::Receipt( gloox::Receipt::ReceiptType::Request ) );
		m_client->registerStanzaExtension( new kl::Receipt( gloox::Receipt::ReceiptType::Received ) );
		m_client->registerStanzaExtension( new gloox::MUCRoom::MUCUser() );
		m_client->registerStanzaExtension( new gloox::PubSub::Event( gloox::XMLNS_NICKNAME, gloox::PubSub::EventType::EventItems ) );
		m_client->registerStanzaExtension( new gloox::PubSub::Event( XMLNS_MICROBLOG, gloox::PubSub::EventType::EventItems ) );
		m_client->registerStanzaExtension( new DirectMucInvitation() );
		// 添加实体能力
		m_client->disco()->addFeature( gloox::XMLNS_NICKNAME + "+notify" );
		m_client->disco()->addFeature( XMLNS_AVATAR_METADATA + "+notify" );
		m_client->disco()->addFeature( XMLNS_MICROBLOG + "+notify" );
		m_client->disco()->addFeature( XMLNS_NEEKLE_PERSONALINFO + "+notify" );
		m_client->disco()->addFeature( XMLNS_NEEKLE_ADDRESSBOOK + "+notify" );
		m_client->disco()->addFeature( XMLNS_X_CONFERENCE );
		m_client->disco()->addFeature( gloox::XMLNS_BYTESTREAMS ); // 支持S5B
		// 客户端充当s5b服务器
		if( m_sOCKS5BytestreamServer->listen() != gloox::ConnNoError )
		{
			return;
		}
		m_sIProfileFT->registerSOCKS5BytestreamServer( m_sOCKS5BytestreamServer );
		m_sIProfileFT->addStreamHost( m_client->jid(), "127.0.0.1"/* should get local ip */, 9999 );
		m_sIProfileFT->addStreamHost( gloox::JID( "proxy." + this->jid().server() ), m_client->server(), 7777 );
	}

	const gloox::JID& XmppStack::jid() const
	{
		return m_client->jid();
	}

	const std::string& XmppStack::password() const
	{
		return m_client->password();
	}

	const std::string& XmppStack::host() const
	{
		return m_client->server();
	}

	int XmppStack::port() const
	{
		return m_client->port();
	}

	void XmppStack::handleLog( gloox::LogLevel level_, gloox::LogArea area_, const std::string& message_ )
	{
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onLog, level_, area_, message_ );
	}

	void XmppStack::onConnect()
	{
		// 告知登录成功
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onLoginSuccess );
		// 判断是否检测并修正微博节点配置
		if ( m_isCheckMicrobolgNodeConfig && m_pubSubManager )
		{
			m_pubSubManager->getNodeConfig( this->jid().bareJID(), XMLNS_MICROBLOG, this );
		}
		// 判断是否检测并修正头像数据节点配置
		if ( m_isCheckAvatarDataNodeConfig && m_pubSubManager )
		{
			m_pubSubManager->getNodeConfig( this->jid().bareJID(), XMLNS_AVATAR_DATA, this );
		}
		// 查询服务器实体能力
		//if ( m_client && m_client->disco() )
		//{
		//	m_client->disco()->getDiscoInfo( ( const gloox::JID& ) gloox::JID( m_client->jid().server() ), "", this, 0 );
		//}
	}

	void XmppStack::onDisconnect( gloox::ConnectionError e_ )
	{
		if ( gloox::ConnectionError::ConnUserDisconnected == e_ || gloox::ConnectionError::ConnStreamClosed == e_ )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onLogoutSuccess );
		}
		else if ( gloox::ConnectionError::ConnAuthenticationFailed == e_ )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onAuthFailed );
		}
		else
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onTcpConnFailed, e_ );
		}
	}

	void XmppStack::onResourceBind( const std::string& resource_ )
	{
	}

	void XmppStack::onResourceBindError( const gloox::Error* error_ )
	{
	}

	void XmppStack::onSessionCreateError( const gloox::Error* error_ )
	{
	}

	bool XmppStack::onTLSConnect( const gloox::CertInfo& info_ )
	{
		return true;
	}

	void XmppStack::onStreamEvent( gloox::StreamEvent event_ )
	{
		if ( event_ == gloox::StreamEvent::StreamEventConnecting )
		{
		}
	    else if ( event_ == gloox::StreamEvent::StreamEventEncryption )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onNegotiatingEncryption );
		}
		else if ( event_ == gloox::StreamEvent::StreamEventCompression )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onNegotiatingCompression );
		}
		else if ( event_ == gloox::StreamEvent::StreamEventAuthentication )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onAuthenticating );
		}
		else if ( event_ == gloox::StreamEvent::StreamEventResourceBinding )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onBindingResource );
		}
		else if ( event_ == gloox::StreamEvent::StreamEventSessionCreation )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onCreatingSession );
		}
		else if ( event_ == gloox::StreamEvent::StreamEventRoster )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onLoadingRoster );
		}
		else if ( event_ == gloox::StreamEvent::StreamEventFinished )
		{
		}
	}

	void XmppStack::handleItemAdded( const gloox::JID& jid_ )
	{
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( item )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRosterItemAdded, item );
		}
	}

	void XmppStack::handleItemUpdated( const gloox::JID& jid_ )
	{
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( item )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRosterItemUpdated, item );
		}
	}

	void XmppStack::handleItemRemoved( const gloox::JID& jid_ )
	{
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( item )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRosterItemRemoved, item );
		}
	}

	void XmppStack::handleItemSubscribed( const gloox::JID& jid_ )
	{
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( item )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRosterItemSubscribed, item );
		}
	}

	void XmppStack::handleItemUnsubscribed( const gloox::JID& jid_ )
	{
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( item )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRosterItemUnsubscribed, item );
		}
	}

	void XmppStack::handleRoster( const gloox::Roster& roster_ )
	{
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvRoster, (std::map<std::string, gloox::RosterItem*>&) roster_ );
	}

	void XmppStack::handleRosterPresence( const gloox::RosterItem& item_, 
		                                  const std::string& resource_, 
		                                  gloox::Presence::PresenceType presence_, 
										  const std::string& msg_ )
	{
		XmppCallbackList::const_iterator it = m_xmppCallbacks.begin();
		for ( ; it != m_xmppCallbacks.end(); it++ )
		{
			(*it)->onRecvRosterPresence( item_, resource_, presence_, msg_ );
		}
	}

	void XmppStack::handleSelfPresence( const gloox::RosterItem& item_, 
		                                const std::string& resource_, 
	                                    gloox::Presence::PresenceType presence_, 
										const std::string& msg_ )
	{
	}

	bool XmppStack::handleSubscriptionRequest( const gloox::JID& jid_, const std::string& msg_ )
	{
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvSubscriptionRequest, jid_, msg_ );
		return false;
	}

	bool XmppStack::handleUnsubscriptionRequest( const gloox::JID& jid_, const std::string& msg_ )
	{
		return true;
	}

	void XmppStack::handleNonrosterPresence( const gloox::Presence& presence_ )
	{
	}

	void XmppStack::handleRosterError( const gloox::IQ& iq_ )
	{
	}

	void XmppStack::handlePresence( const gloox::Presence& presence_ )
	{
		if ( presence_.from().resource() != "" )
		{
			const std::string jid = presence_.from().full();
			const gloox::Capabilities* caps = presence_.capabilities();
			if ( caps )
			{
				const std::string ver = caps->ver();
				if ( m_verificationMap.find( jid ) == m_verificationMap.end() )
				{
					m_verificationMap.insert( std::make_pair( jid, ver ) );
				}
				else if( m_verificationMap[jid] != ver )
				{
					m_verificationMap[jid] = ver;
				}

				if ( m_discoInfoMap.find( ver ) == m_discoInfoMap.end() )
				{
					m_client->disco()->getDiscoInfo( presence_.from(), caps->node() + "#" + ver, this, 0, "" );
				}
			}
			else
			{
				m_verificationMap.insert( std::make_pair( jid, "" ) );
			}
		}

		// 填充m_entityMap
		kl::Entity* entity = m_entityMap[presence_.from().full()];
		if ( !entity )
		{
			entity = new kl::Entity();
			entity->jid = presence_.from().full();
			if ( presence_.capabilities() )
			{
				entity->ver = presence_.capabilities()->ver();
			}
			m_entityMap[presence_.from().full()] = entity;
		}
	}

	void XmppStack::handleMessage( const gloox::Message& msg_, gloox::MessageSession* session_ )
	{
		switch ( msg_.subtype() )
		{
		case gloox::Message::Normal:
			{
				// 解析即时邮件消息
				if ( "imail" == msg_.subject() )
				{
					std::string xml = msg_.body();
					XmlParser xmlParser;
					xmlParser.feed( xml );
					const gloox::Tag* tag_imail = xmlParser.tag();
					
					IMailSMTPInfo iMailSMTPInfo( tag_imail );
					bool isRequestReceipt = false;
					if ( msg_.findExtension( gloox::StanzaExtensionType::ExtReceipt ) )
					{
						isRequestReceipt = true;
					}
					std::list<XmppCallback*>::const_iterator it = m_xmppCallbacks.begin();
					for ( ; it != m_xmppCallbacks.end(); it++ ) 
					{
						(*it)->onRecvIMailMessage( msg_.id(), msg_.from(), iMailSMTPInfo, isRequestReceipt );
					}
				}
				// 解析地理位置消息
				else if ( "location" == msg_.subject() )
				{
					std::string xml = msg_.body();
					XmlParser xmlParser;
					xmlParser.feed( xml );
					const gloox::Tag* tag_geoloc = xmlParser.tag();
					
					kl::Geoloc geoloc( tag_geoloc );
					gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvLocationMessage, msg_.id(), msg_.from(), geoloc );
				}
				// 解析回执消息
				else if ( msg_.findExtension( gloox::StanzaExtensionType::ExtReceipt ) ) 
				{
					gloox::Tag* tag_receipt = msg_.tag()->findChild( "received" );
					std::string receiptId = "";
					if ( tag_receipt )
					{
						receiptId = tag_receipt->findAttribute( "id" );
					}
					gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvReceiptMessage, msg_.id(), msg_.from(), receiptId );
				}
				// 解析MUC直接邀请消息
				else if ( msg_.findExtension( kl::StanzaExtensionType::ExtDirectMUCInvitation ) )
				{
					gloox::Tag* tag = msg_.findExtension( kl::StanzaExtensionType::ExtDirectMUCInvitation )->tag();
					const std::string str = tag->findAttribute( "jid" );
					gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvMUCRoomDirectInvitation, gloox::JID( "jid" ), msg_.from(), "" );
				}
			}
			break;
		case gloox::Message::Headline:
			{
				// 解析震屏消息
				if ( msg_.findExtension( gloox::StanzaExtensionType::ExtAttention ) )
				{
					gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvAttentionMessage, msg_.from() );
				}
				// 解析发布订阅消息事件
				const gloox::PubSub::Event* pubsub_event = msg_.findExtension<gloox::PubSub::Event>( gloox::StanzaExtensionType::ExtPubSubEvent );
				if ( pubsub_event )
				{
					const std::string& pubsub_event_node = pubsub_event->node();
					
					// 解析微博消息事件
					if ( XMLNS_MICROBLOG == pubsub_event_node )
					{
						if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
						{
							Microblog microblog( pubsub_event );
							gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvMicroblog, ( const Microblog& ) microblog );
						}
						else if ( gloox::PubSub::EventType::EventItemsRetract == pubsub_event->type() )
						{
							gloox::PubSub::Event::ItemOperationList::const_iterator it = pubsub_event->items().begin();

							for ( ; it != pubsub_event->items().end(); it++ )
							{
								gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onMicroblogDeleted, (*it)->item );
							}
						}
					} 
					// end 解析微博消息事件
					
					// 解析昵称消息事件
					else if ( gloox::XMLNS_NICKNAME == pubsub_event_node )
					{
						std::string nickname = "";
						if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
						{
							gloox::PubSub::Event::ItemOperationList items = pubsub_event->items();
							if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
							{
								gloox::PubSub::Event::ItemOperationList::const_iterator it = items.begin();
								for ( ; it != items.end(); it++ )
								{
									// <item>
									const gloox::Tag* tag_item = (*it)->payload;
									if ( tag_item )
									{
										// <nick>
										gloox::Tag* tag_nick = tag_item->findChild( "nick" );
										if ( tag_nick && gloox::XMLNS_NICKNAME == tag_nick->xmlns() )
										{
											nickname = tag_nick->cdata();
											gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvNickname, ( const gloox::JID& ) msg_.from().bareJID(), nickname );
										}
									}

								} // end for
							
							} // end if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() ) 

						} // end if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
						
					} 
					// end 解析昵称消息事件
					
					// 解析头像元数据消息事件
					else if ( XMLNS_AVATAR_METADATA == pubsub_event_node )
					{
						int bytes = 0;
						int height = 0;
						int width = 0;
						std::string id = "";
						std::string url = "";
						std::string type = "";

						if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
						{
							// <items>
							gloox::PubSub::Event::ItemOperationList items = pubsub_event->items();
							if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
							{
								gloox::PubSub::Event::ItemOperationList::const_iterator it = items.begin();
								for ( ; it != items.end(); it++ )
								{
									// <item>
									const gloox::Tag* tag_item = (*it)->payload;
									if ( tag_item )
									{
										// <metadata xmlns='urn:xmpp:avatar:metadata'>
										gloox::Tag* tag_metadata = tag_item->findChild( "metadata" );
										if ( tag_metadata && XMLNS_AVATAR_METADATA == tag_metadata->xmlns() )
										{
											// <info>
											gloox::Tag* tag_info = tag_metadata->findChild( "info" );
											if ( tag_info )
											{
												id = tag_info->findAttribute( "id" );
												type = tag_info->findAttribute( "type" );
												url = tag_info->findAttribute( "url" );
												const std::string& str_bytes = tag_info->findAttribute( "bytes" );
												if ( str_bytes != "" )
												{
													bytes = atoi( str_bytes.c_str() );
												}
												const std::string& str_height = tag_info->findAttribute( "height" );
												if ( str_height != "" )
												{
													height = atoi( str_height.c_str() );
												}
												const std::string& str_width = tag_info->findAttribute( "width" );
												if ( str_width != "" )
												{
													width = atoi( str_width.c_str() );
												}

												// 
												AvatarMetadata md( id, bytes, type );
												md.setHeight( height );
												md.setWidth( width );
												md.setUrl( url );

												gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvAvatarMetadata, ( const gloox::JID& ) msg_.from().bareJID(), md );

											} // end if ( tag_info )

										} // end if ( tag_metadata && XMLNS_AVATAR_METADATA == tag_nick->xmlns() )

									} // end if ( tag_item )

								} // end for

							} // end if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )

						} // end if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )

					} 
					// end 解析头像元数据消息事件
					
					// 解析头像数据消息事件
					else if ( XMLNS_AVATAR_DATA == pubsub_event_node )
					{
						std::string id = "";
						std::string base64 = "";

						if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
						{
							// <items>
							gloox::PubSub::Event::ItemOperationList items = pubsub_event->items();
							if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
							{
								gloox::PubSub::Event::ItemOperationList::const_iterator it = items.begin();
								for ( ; it != items.end(); it++ )
								{
									// <item>
									const gloox::Tag* tag_item = (*it)->payload;
									if ( tag_item )
									{
										// 头像ID
										id = tag_item->findAttribute( "id" );

										// <data xmlns='urn:xmpp:avatar:data'>
										gloox::Tag* tag_data = tag_item->findChild( "data" );
										if ( tag_data && XMLNS_AVATAR_DATA == tag_data->xmlns() )
										{
											base64 = tag_data->cdata();
											gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvAvatarData, ( const gloox::JID& ) msg_.from().bareJID(), id, base64 );

										} // end if ( tag_data && XMLNS_AVATAR_DATA == tag_data->xmlns() )

									} // end if ( tag_item )

								} // end for

							} // end if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )

						} // end if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )

					} 
					// end 解析头像数据消息事件
					
					// 解析背景已修改消息事件
					else if ( kl::XMLNS_NEEKLE_BACKGROUND == pubsub_event_node )
					{
						if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
						{
							// <items>
							gloox::PubSub::Event::ItemOperationList items = pubsub_event->items();
							if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
							{
								gloox::PubSub::Event::ItemOperationList::const_iterator it = items.begin();
								for ( ; it != items.end(); it++ )
								{
									// <item>
									const gloox::Tag* tag_item = (*it)->payload;
									if ( tag_item )
									{
										kl::BackgroundInfo backgroundInfo( tag_item );
										gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvBackgroundChangedNotification, ( const gloox::JID& ) msg_.from().bareJID(), backgroundInfo );
									}
								}
							}
						}
					} 
					// end 解析背景已修改消息事件
					
					// 解析个人资料已修改消息事件
					else if ( XMLNS_NEEKLE_PERSONALINFO == pubsub_event_node )
					{				
						gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvPersonalInfoChangedNotification, ( const gloox::JID& ) msg_.from().bareJID() );
					}
					// end 解析个人资料已修改消息事件

					// 解析通讯录已修改消息事件
					else if ( XMLNS_NEEKLE_ADDRESSBOOK == pubsub_event_node )
					{
						std::string id = "";
						std::string name = "";
						bool isAvailable = false;

						if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
						{
							// <items>
							gloox::PubSub::Event::ItemOperationList items = pubsub_event->items();
							if ( gloox::PubSub::EventType::EventItems == pubsub_event->type() )
							{
								gloox::PubSub::Event::ItemOperationList::const_iterator it = items.begin();
								for ( ; it != items.end(); it++ )
								{
									// <item>
									const gloox::Tag* tag_item = (*it)->payload;
									if ( tag_item )
									{
										// <addressbook xmlns="http://neekle.com/xmpp/protocol/addressbook">
										gloox::Tag* tag_addressbook = tag_item->findChild( "addressbook" );
										if ( tag_addressbook && XMLNS_NEEKLE_ADDRESSBOOK == tag_addressbook->xmlns() )
										{
											// <id>a-122123</id>
											gloox::Tag* tag_id = tag_addressbook->findChild( "id" );
											if ( tag_id )
											{
												id = tag_id->cdata();
											}
											// <name>大客户</name>
											gloox::Tag* tag_name = tag_addressbook->findChild( "name" );
											if ( tag_name )
											{
												name = tag_name->cdata();
											}
											// <available>true</available>
											gloox::Tag* tag_available = tag_addressbook->findChild( "available" );
											if ( tag_available )
											{
												std::string str_available = tag_available->cdata();
												if ( str_available == "true" )
												{
													isAvailable = true;
												}
											}
										} // end if ( tag_addressbook && XMLNS_NEEKLE_ADDRESSBOOK == tag_addressbook->xmlns() )
									} // end if ( tag_item )
									
									AddressBookInfo addressBookInfo;
									addressBookInfo.id = id;
									addressBookInfo.name = name;
									addressBookInfo.isAvailable = isAvailable;

									gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvAddressBookChangedNotification, ( const gloox::JID& ) msg_.from().bareJID(), addressBookInfo );

								} // for ( ; it != items.end(); it++ )
							}
						}
					} 
					// end 解析通讯录已修改消息事件
				}
			}
			break;
		case gloox::Message::Chat:
			{
				// 解析聊天消息
				std::string xhtml = "";
				const gloox::StanzaExtension* se = msg_.findExtension( gloox::StanzaExtensionType::ExtXHtmlIM );
				if ( se ) 
				{
					gloox::Tag* tag = se->tag();
					if ( tag )
					{
						xhtml = tag->xml();
					}
					delete tag;
					tag = 0;
				}
				bool isRequestReceipt = false;
				if ( msg_.findExtension( gloox::StanzaExtensionType::ExtReceipt ) )
				{
					isRequestReceipt = true;
				}
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvChatMessage, msg_, xhtml, isRequestReceipt );
			}
			break;
		default:
			break;
		}
	}

	void XmppStack::handleRegistrationFields( const gloox::JID& from_, int fields_, std::string instructions_ )
	{
	}

	void XmppStack::handleAlreadyRegistered( const gloox::JID& from_ )
	{
	}

	void XmppStack::handleRegistrationResult( const gloox::JID& from_, gloox::RegistrationResult regResult_ )
	{
	}

	void XmppStack::handleDataForm( const gloox::JID& from_, const gloox::DataForm& form_ )
	{
		// 解析注册信息：用户名、注册名、email
		std::string username = "";
		std::string name = "";
		std::string email = "";
		// <username>
		gloox::DataFormField* field_username =  form_.field( "username" );
		if ( field_username )
		{
			username = field_username->value();
		}
		// <name>
		gloox::DataFormField* field_name =  form_.field( "name" );
		if ( field_name )
		{
			name = field_name->value();
		}
		// <email>
		gloox::DataFormField* field_email =  form_.field( "email" );
		if ( field_email )
		{
			email = field_email->value();
		}

		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRetrieveRegistrationInfo, username, name, email );
	}

	void XmppStack::handleOOB( const gloox::JID& from_, const gloox::OOB& oob_ )
	{
	}

	void XmppStack::handleDiscoInfo( const gloox::JID& from_, const gloox::Disco::Info& info_, int context_ )
	{
		if ( !m_client )
		{
			return;
		}
		// 查询到服务器实体能力
		//if ( from_.full() == m_client->jid().server() )
		//{
		//	XmppEntity* xe = new XmppEntity( from_ );
		//	xe->setDiscoInfo( ( gloox::Disco::Info* ) &info_ );
		//	m_xmppEntityTrackMap[from_.full()] = xe;
		//}

		if ( m_verificationMap.find( from_.full() ) !=  m_verificationMap.end() )
		{
			const std::string node = info_.node();
			std::string::size_type index = node.find("#");
			const std::string ver_code = node.substr( index + 1 );
			m_discoInfoMap.insert( std::make_pair( ver_code, new DiscoInfo( info_ ) ) );
		}

		// 填充m_serviceMap
		//std::map<std::string, kl::Service*>::const_iterator it = m_serviceMap.begin();
		//for( ; it != m_serviceMap.end(); it++ )
		//{
		//	if ( from_.bare() == (*it).first )
		//	{
		//		const gloox::Disco::IdentityList identities = info_.identities();
		//		gloox::Disco::IdentityList::const_iterator it2 = info_.identities().begin();
		//		for( ; it2 != info_.identities().end(); it2++ )
		//		{
		//			// 避免插入重复元素
		//			bool isRepeat = false;
		//			std::list<kl::disco::Identity*>::const_iterator it3 = (*it).second->identities.begin();
		//			for ( ; it3 != (*it).second->identities.end(); it3++ )
		//			{
		//				if ( (*it3)->category == (*it2)->category() 
		//					&& (*it3)->name == (*it2)->name() 
		//					&& (*it3)->type == (*it2)->type() )
		//				{
		//					isRepeat = true;
		//					break;
		//				}
		//			}
		//			if ( !isRepeat ) 
		//			{
		//				kl::disco::Identity* identity = new kl::disco::Identity();
		//				identity->category = (*it2)->category();
		//				identity->name = (*it2)->name();
		//				identity->type = (*it2)->type();
		//				(*it).second->identities.push_back( identity );
		//			}
		//		}
		//		
		//		(*it).second->features = info_.features();
		//	}
		//}
		// 填充m_entityMap
		std::map<std::string, kl::Entity*>::const_iterator it = m_entityMap.begin();
		for ( ; it != m_entityMap.end(); it++ )
		{
			kl::Entity* entity = findEntity( (*it).second, from_.full() );
			if ( entity )
			{
				const gloox::Disco::IdentityList identities = info_.identities();
				gloox::Disco::IdentityList::const_iterator it_idlist = info_.identities().begin();
				for( ; it_idlist != info_.identities().end(); it_idlist++ )
				{
					// 避免插入重复元素
					bool isRepeat = false;
					std::list<kl::disco::Identity*>::const_iterator it_discoidlist = entity->identities.begin();
					for ( ; it_discoidlist != entity->identities.end(); it_discoidlist++ )
					{
						if ( (*it_discoidlist)->category == (*it_idlist)->category() 
							&& (*it_discoidlist)->name == (*it_idlist)->name() 
							&& (*it_discoidlist)->type == (*it_idlist)->type() )
						{
							isRepeat = true;
							break;
						}
					}
					if ( !isRepeat ) 
					{
						kl::disco::Identity* identity = new kl::disco::Identity();
						identity->category = (*it_idlist)->category();
						identity->name = (*it_idlist)->name();
						identity->type = (*it_idlist)->type();
						entity->identities.push_back( identity );
					}
				}

				entity->features = info_.features();

			} // end of if ( entity )
		}

		//kl::Entity* entity = m_entityMap[from_.full()];
		//if ( entity )
		//{
		//	const gloox::Disco::IdentityList identities = info_.identities();
		//	gloox::Disco::IdentityList::const_iterator it = info_.identities().begin();
		//	for( ; it != info_.identities().end(); it++ )
		//	{
		//		// 避免插入重复元素
		//		bool isRepeat = false;
		//		std::list<kl::disco::Identity*>::const_iterator it2 = entity->identities.begin();
		//		for ( ; it2 != entity->identities.end(); it2++ )
		//		{
		//			if ( (*it2)->category == (*it)->category() 
		//				&& (*it2)->name == (*it)->name() 
		//				&& (*it2)->type == (*it)->type() )
		//			{
		//				isRepeat = true;
		//				break;
		//			}
		//		}
		//		if ( !isRepeat ) 
		//		{
		//			kl::disco::Identity* identity = new kl::disco::Identity();
		//			identity->category = (*it)->category();
		//			identity->name = (*it)->name();
		//			identity->type = (*it)->type();
		//			entity->identities.push_back( identity );
		//		}
		//	}

		//	entity->features = info_.features();
		//}
	}

	void XmppStack::handleDiscoItems( const gloox::JID& from_, const gloox::Disco::Items& items_, int context_ )
	{
		// TODO：缓存到内存，供应用层查询
		//if ( "" == from_.username() && this->jid().server() == from_.server() && "" == from_.resource() )
		//{
		//	// 确认是对服务器根节点做disco#items查询，without node
		//	const gloox::Disco::ItemList& disco_items = items_.items();
		//	gloox::Disco::ItemList::const_iterator it = disco_items.begin();
		//	for ( ; it != disco_items.end(); it++ )
		//	{
		//		kl::Service* service = new kl::Service();
		//		service->jid = (*it)->jid().bare();
		//		service->name = (*it)->name();
		//		m_serviceMap.insert( std::make_pair( (*it)->jid().bare(), service ) );
		//		// 做disco#info查询详细信息
		//		this->discoInfo( (*it)->jid() );
		//	}
		//}

		// TODO：填充m_entityMap
		kl::Entity* entity = 0;
		std::map<std::string, kl::Entity*>::const_iterator it = m_entityMap.begin();
		for ( ; it != m_entityMap.end(); it++ )
		{
			entity = findEntity( (*it).second, from_.full() );
			if ( entity )
			{
				break;
			}
		}

		if ( !entity )
		{
			// TODO: 插入新的实体
			entity = new kl::Entity();
			entity->jid = from_.full();
			m_entityMap[from_.full()] = entity;
			this->discoInfo( from_ );
		}

		// TODO: root实体已经在映射表中，如果子实体已经存在，更新；否则创建新的。
		const gloox::Disco::ItemList& disco_items = items_.items();
		gloox::Disco::ItemList::const_iterator it2 = disco_items.begin();
		for ( ; it2 != disco_items.end(); it2++ )
		{
			kl::Entity* child = entity->children[(*it2)->jid().full()];
			if ( !child )
			{
				child = new kl::Entity();
				entity->children[(*it2)->jid().full()] = child;
			}
			child->jid = (*it2)->jid().full();
			child->name = (*it2)->name();
			// 做disco#info查询详细信息
			this->discoInfo( (*it2)->jid() );
		}
	}

	void XmppStack::handleDiscoError( const gloox::JID& from, const gloox::Error* error_, int context_ )
	{
	}

	void XmppStack::handleMUCParticipantPresence( gloox::MUCRoom* room_, const gloox::MUCRoomParticipant participant_, const gloox::Presence& presence_ )
	{
		// 填充m_entityMap
		kl::Entity* muc_service = 0;
		std::map<std::string, kl::Entity*>::const_iterator it1 = m_entityMap.begin();
		for ( ; it1 != m_entityMap.end(); it1++ )
		{
			muc_service = findEntity( (*it1).second, presence_.from().server() );
		}
		
		if ( muc_service )
		{
			kl::Entity* entity = muc_service->children[presence_.from().full()];
			if ( !entity )
			{
				entity = new kl::Entity();
				entity->jid = presence_.from().full();
				muc_service->children[presence_.from().full()] = entity;
			}
		}

		// 告知
		gloox::JID jid( room_->name() + "@" + room_->service() + "/" + room_->nick() );
		gloox::JID participant_jid( "" );
		if ( participant_.jid != 0 )
		{
			participant_jid = (*participant_.jid);
		}
		XmppCallbackList::const_iterator it = m_xmppCallbacks.begin();
		for ( ; it != m_xmppCallbacks.end(); it++ )
		{
			(*it)->onRecvMUCRoomPresence( ( const gloox::JID& )jid.bareJID(), 
				                          (*participant_.nick).resource(), 
										  participant_.newNick, 
										  participant_jid, 
										  presence_, 
										  participant_.affiliation, 
										  participant_.role );
		}
	}

	void XmppStack::handleMUCMessage( gloox::MUCRoom* room_, const gloox::Message& msg_, bool priv_ )
	{
		gloox::JID jid( room_->name() + "@" + room_->service() + "/" + room_->nick() );
		std::string xhtml = "";
		const gloox::StanzaExtension* se = msg_.findExtension( gloox::StanzaExtensionType::ExtXHtmlIM );
		if ( se ) 
		{
			// 带富文本
			gloox::Tag* tag = se->tag();
			if ( tag )
			{
				xhtml = tag->xml();
			}
		}
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvMUCRoomMessage, ( const gloox::JID& ) jid.bareJID(), msg_, xhtml );	
	}

	bool XmppStack::handleMUCRoomCreation( gloox::MUCRoom* room_ )
	{
		gloox::JID jid( room_->name() + "@" + room_->service() + "/" + room_->nick() );

		std::map<std::string, gloox::MUCRoom*>::const_iterator it1 = m_mUCRoomMap.find( jid.bare() );
		std::map<std::string, gloox::DataForm*>::iterator it2 = m_createMUCRoomTrackMap.find( jid.bare() );
		
		if ( it1 != m_mUCRoomMap.end() && it2 != m_createMUCRoomTrackMap.end() )
		{
			// 告知房间创建成功
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onCreateMUCRoomSuccess, ( const gloox::JID& ) jid.bareJID() );
			// 提交房间配置
			room_->setRoomConfig( (*it2).second ); // ptr will be deleted
			m_createMUCRoomTrackMap.erase( it2 );
		}
		// 返回false保持房间锁定直到手动配置完成。
		return false;
	}

	void XmppStack::handleMUCSubject( gloox::MUCRoom* room_, const std::string& nick_, const std::string& subject_ )
	{
		gloox::JID jid( room_->name() + "@" + room_->service() + "/" + room_->nick() );
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onMUCRoomSubjectChanged, ( const gloox::JID& ) jid.bareJID(), nick_, subject_ );
	}

	void XmppStack::handleMUCInviteDecline( gloox::MUCRoom* room_, const gloox::JID& invitee_, const std::string& reason_ )
	{
		gloox::JID jid( room_->name() + "@" + room_->service() + "/" + room_->nick() );
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onMUCRoomInvitationRejected, ( const gloox::JID& ) jid.bareJID(), invitee_, reason_ );
	}

	void XmppStack::handleMUCError( gloox::MUCRoom* room_, gloox::StanzaError error_ )
	{
		gloox::JID jid( room_->name() + "@" + room_->service() + "/" + room_->nick() );
		CreateMUCRoomError error = CreateMUCRoomError::Unknown;
		switch ( error_ )
		{
		case gloox::StanzaError::StanzaErrorNotAllowed:
			error = CreateMUCRoomError::NotAllowed;
		case gloox::StanzaError::StanzaErrorRemoteServerNotFound:
			error = CreateMUCRoomError::RemoteServerNotFound;
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onCreateMUCRoomFailed, ( const gloox::JID& ) jid, error );
			break;
		case gloox::StanzaError::StanzaErrorConflict:
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onChangeMUCNicknameConflict, ( const gloox::JID& ) jid );
			break;
		default:
			break;
		}
	}

	void XmppStack::handleMUCInfo( gloox::MUCRoom* room_, int features_, const std::string& name_, const gloox::DataForm* infoForm_ )
	{
		if ( !room_ )
		{
			return;
		}

		gloox::JID jid( room_->name() + "@" + room_->service() + "/" + room_->nick() );
		int flag_isPersistent = features_ & gloox::FlagPersistent; // 是否是持久房间 persistent vs temporary
		int flag_isMembersOnly = features_ & gloox::FlagMembersOnly; // 是否是仅成员房间 membersonly vs open 
		int flag_isNonAnonymous = features_ & gloox::FlagNonAnonymous; // 是否是非匿名房间 non-anonymous vs semi-anonymous
		int flag_isModerated = features_ & gloox::FlagModerated; // 是否是主持人房间 moderated vs unmoderated
		int flag_isPasswordProtected = features_ & gloox::FlagPasswordProtected; // 是否是密码保护房间 password-protected vs unsecured
		int flag_isPublic = features_ & gloox::FlagPublic; // 是否是公共房间 public vs hidden
		
		std::string roomdesc = "";
		std::string roomsubject = "";
		int occupants = 0;
		std::string creationdate = "";

		if ( infoForm_ )
		{
			gloox::DataFormField* field_roomdesc =  infoForm_->field( "muc#roominfo_description" );
			roomdesc =  field_roomdesc ? field_roomdesc->value() : "";

			gloox::DataFormField* field_roomsubject =  infoForm_->field( "muc#roominfo_subject" );
			roomsubject =  field_roomsubject ? field_roomsubject->value() : "";

			gloox::DataFormField* field_occupants =  infoForm_->field( "muc#roominfo_occupants" );
			occupants = field_occupants ? atoi( field_occupants->value().c_str() ) : 0;

			gloox::DataFormField* field_creationdate =  infoForm_->field( "x-muc#roominfo_creationdate" );
			creationdate =  field_creationdate ? field_creationdate->value() : "";
		}
		
		struct MUCRoomInfo info;
		info.roomname = name_;
		info.roomdesc = roomdesc;
		info.roomsubject = roomsubject;
		info.occupants = occupants;
		info.creationdate = creationdate;
		info.isPersistent = flag_isPersistent == 0 ? false : true;
		info.isMembersOnly = flag_isMembersOnly == 0 ? false : true;
		info.isNonAnonymous = flag_isNonAnonymous == 0 ? false : true;
		info.isModerated = flag_isModerated == 0 ? false : true;
		info.isPasswordProtected = flag_isPasswordProtected == 0 ? false : true;
		info.isPublic = flag_isPublic == 0 ? false : true;

		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRetrieveMUCRoomInfo, ( const gloox::JID& ) jid.bare(), info );
	}

	void XmppStack::handleMUCItems( gloox::MUCRoom* room_, const gloox::Disco::ItemList& items_ )
	{
	}

	void XmppStack::handleMUCConfigList( gloox::MUCRoom* room_, const gloox::MUCListItemList& items_, gloox::MUCOperation operation_ )
	{
		gloox::JID jid( room_->name() + "@" + room_->service() + "/" + room_->nick() );
		gloox::StringList sl;
		gloox::MUCListItemList::const_iterator it = items_.begin();
		for ( ; it != items_.end(); it++ )
		{
			sl.push_back( (*it).jid().bare() );
		}

		switch( operation_ )
		{
		case gloox::MUCOperation::RequestMemberList:
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRetrieveMUCRoomMemberList, ( const gloox::JID& ) jid.bareJID(), sl );
			break;
		case gloox::MUCOperation::RequestOwnerList:
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRetrieveMUCRoomOwnerList, ( const gloox::JID& ) jid.bareJID(), sl );
			break;
		default:
			break;
		}
	}

	void XmppStack::handleMUCConfigForm( gloox::MUCRoom* room_, const gloox::DataForm& form_ )
	{
		gloox::JID jid( room_->name() + "@" + room_->service() + "/" + room_->nick() );
		MUCRoomConfig config;
		// 房间名称
		gloox::DataFormField* field_roomname = form_.field( "muc#roomconfig_roomname" );
		if ( field_roomname )
		{
			config.roomname = field_roomname->value();
		}
		// 房间简介
		gloox::DataFormField* field_roomdesc = form_.field( "muc#roomconfig_roomdesc" );
		if ( field_roomdesc )
		{
			config.roomdesc = field_roomdesc->value();
		}
		// 是否记录日志
		gloox::DataFormField* field_enablelogging = form_.field( "muc#roomconfig_enablelogging" );
		if ( field_enablelogging )
		{
			config.enableLogging = ( field_enablelogging->value() == "1" ) ? true : false;
		}
		// 是否允许住客修改房间主题
		gloox::DataFormField* field_changesubject = form_.field( "muc#roomconfig_changesubject" );
		if ( field_changesubject )
		{
			config.enableChangeSubject = ( field_changesubject->value() == "1" ) ? true : false;
		}
		// 是否是公共房间
		gloox::DataFormField* field_publicroom = form_.field( "muc#roomconfig_publicroom" );
		if ( field_publicroom )
		{
			config.isPublic = ( "1" == field_publicroom->value() ) ? true : false;
		}
		// 是否是持久房间
		gloox::DataFormField* field_persistentroom = form_.field( "muc#roomconfig_persistentroom" );
		if ( field_persistentroom )
		{
			config.isPersistent = ( "1" == field_persistentroom->value() ) ? true : false;
		}
		// 是否是主持人房间
		gloox::DataFormField* field_moderatedroom = form_.field( "muc#roomconfig_moderatedroom" );
		if ( field_moderatedroom )
		{
			config.isModerated = ( "1" == field_moderatedroom->value() ) ? true : false;
		}
		// 是否是仅成员房间
		gloox::DataFormField* field_membersonly = form_.field( "muc#roomconfig_membersonly" );
		if ( field_membersonly )
		{
			config.isMembersOnly = ( "1" == field_membersonly->value() ) ? true : false;
		}
		// 是否是密码保护房间
		gloox::DataFormField* field_passwordprotectedroom = form_.field( "muc#roomconfig_passwordprotectedroom" );
		if ( field_passwordprotectedroom )
		{
			config.isPasswordProtected = ( "1" == field_passwordprotectedroom->value() ) ? true : false;
		}
		// 房间密码
		gloox::DataFormField* field_roomsecret = form_.field( "muc#roomconfig_roomsecret" );
		if (field_roomsecret )
		{
			config.roompassword = field_roomsecret->value();
		}
		// 告知获取到群聊房间配置
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRetrieveMUCRoomConfig, ( const gloox::JID& ) jid, config );
	}

	void XmppStack::handleMUCConfigResult( gloox::MUCRoom* room_, bool success_, gloox::MUCOperation operation_ )
	{
		gloox::JID jid( room_->name() + "@" + room_->service() + "/" + room_->nick() );
		// 配置MUC房间的响应结果
		if ( gloox::MUCOperation::SendRoomConfig == operation_ )
		{
			if ( success_ )
			{
				// 告知群聊房间配置成功
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onConfigMUCRoomSuccess, ( const gloox::JID& ) jid );
			}
			else
			{
				// 告知群聊房间配置失败
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onConfigMUCRoomFailed, ( const gloox::JID& ) jid );
			}
		}
		else if ( gloox::MUCOperation::DestroyRoom == operation_ )
		{
			if ( success_ )
			{
				// 告知群聊房间销毁成功
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onDestroyMUCRoomSuccess, ( const gloox::JID& ) jid );
			}
			else
			{
				// 告知群聊房间销毁失败
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onDestroyMUCRoomFailed, ( const gloox::JID& ) jid );
			}
		}
		else if ( gloox::MUCOperation::StoreOwnerList == operation_ )
		{
			if ( success_ )
			{
				// 告知修改群聊房间OwnerList成功
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onModifyMUCRoomOwnerListSuccess, ( const gloox::JID& ) jid );
			}
			else
			{
				// 告知修改群聊房间OwnerList失败
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onModifyMUCRoomOwnerListFailed, ( const gloox::JID& ) jid );
			}
		}
	}

	void XmppStack::handleMUCRequest( gloox::MUCRoom* room_, const gloox::DataForm& form_ )
	{
	}

	void XmppStack::handleMUCInvitation( const gloox::JID& room_, 
		                                 const gloox::JID& from_, 
										 const std::string& reason_, 
										 const std::string& body_, 
										 const std::string& password_,
										 bool cont_, 
										 const std::string& thread_ )
	{
		// 告知接收到群聊房间间接邀请
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvMUCRoomMediatedInvitation, ( const gloox::JID& ) room_.bareJID(), from_, reason_ );
	}

	void XmppStack::handleItem( const gloox::JID& service_, const std::string& node_, const gloox::Tag* entry_ )
	{
	}

	void XmppStack::handleItems( const std::string& id_, 
		                         const gloox::JID& service_, 
								 const std::string& node_, 
								 const gloox::PubSub::ItemList& itemList_, 
								 const gloox::Error* error_ )
	{
		// 接收到头像数据
		if ( node_ == XMLNS_AVATAR_DATA )
		{
			if ( error_ )
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onLoadAvatarFailed, ( const gloox::JID& ) service_.bareJID(), id_ );
				return;
			}
			gloox::PubSub::ItemList::const_iterator it = itemList_.begin();
			for ( ; it != itemList_.end(); it++ )
			{
				// <data>
				const gloox::Tag* tag_data = (*it)->payload();
				if ( tag_data )
				{
					const std::string base64 = tag_data->cdata();
					gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onLoadAvatarSuccess, ( const gloox::JID& ) service_.bareJID(), id_, base64 );
				}
			}
		}
	}

	void XmppStack::handleItemPublication( const std::string& id_, 
		                                   const gloox::JID& service_, 
										   const std::string& node_, 
										   const gloox::PubSub::ItemList& itemList_,
										   const gloox::Error* error_ )
	{
		std::list<std::string>::iterator it_npic = std::find( m_notifyPersonalInfoChangedTrackList.begin(), m_notifyPersonalInfoChangedTrackList.end(), id_ );
		std::list<std::string>::iterator it_nabc = std::find( m_notifyAddressBookChangedTrackList.begin(), m_notifyAddressBookChangedTrackList.end(), id_ );

		// 发布微博的结果
		if ( m_publishMicroblogTrackMap[id_] != "" )
		{
			if ( !error_ )
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onPublishMicroblogSuccess, m_publishMicroblogTrackMap[id_] );
			}
			else
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onPublishMicroblogFailed, m_publishMicroblogTrackMap[id_] );
			}
			m_publishMicroblogTrackMap.erase( id_ );
		}
		// 发布昵称的结果
		else if ( m_publishNicknameTrackMap[id_] != "" )
		{
			if ( !error_ )
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onPublishNicknameSuccess, m_publishNicknameTrackMap[id_] );
			}
			else
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onPublishNicknameFailed, m_publishNicknameTrackMap[id_] );
			}
			m_publishNicknameTrackMap.erase( id_ );
		}
		// 发布头像数据的结果
		else if ( m_publishAvatarDataTrackMap[id_] )
		{
			if ( !error_ )
			{
				// TODO：发布头像元数据
				if ( m_pubSubManager )
				{
					gloox::PubSub::ItemList list;
					// <item>
					gloox::Tag* tag_item = new gloox::Tag( "item" );
					tag_item->addAttribute( "id", m_publishAvatarDataTrackMap[id_]->id() );
					// <metadata>
					gloox::Tag* tag_metadata = new gloox::Tag( "metadata");
					tag_metadata->setXmlns( XMLNS_AVATAR_METADATA );
					// <info>
					gloox::Tag* tag_info = new gloox::Tag( "info" );
					tag_info->addAttribute( "id", m_publishAvatarDataTrackMap[id_]->id() );
					tag_info->addAttribute( "bytes", gloox::util::int2string( m_publishAvatarDataTrackMap[id_]->bytes() ) );
					tag_info->addAttribute( "type", m_publishAvatarDataTrackMap[id_]->type() );
					
					if ( m_publishAvatarDataTrackMap[id_]->height() > 0 )
					{
						tag_info->addAttribute( "height", m_publishAvatarDataTrackMap[id_]->height() );
					}
					if ( m_publishAvatarDataTrackMap[id_]->width() > 0 )
					{
						tag_info->addAttribute( "width", m_publishAvatarDataTrackMap[id_]->width() );
					}
					tag_metadata->addChild( tag_info );
					tag_item->addChild( tag_metadata );

					gloox::PubSub::Item* item = new gloox::PubSub::Item( tag_item );
					list.push_back( item );
					
					// 发出请求报文
					std::string uid = m_pubSubManager->publishItem( gloox::JID(), XMLNS_AVATAR_METADATA, list, 0, this );

					m_publishAvatarMetadataTrackMap[uid] = m_publishAvatarDataTrackMap[id_]->id();

					delete tag_item;
					tag_item = 0;
				}
			}
			else
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onPublishAvatarFailed, m_publishAvatarDataTrackMap[id_]->id() );
			}
			m_publishAvatarDataTrackMap.erase( id_ );
		}
		// 发布头像元数据的结果
		else if ( m_publishAvatarMetadataTrackMap[id_] != "" )
		{
			if ( !error_ )
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onPublishAvatarSuccess, m_publishAvatarMetadataTrackMap[id_] );
			}
			else
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onPublishAvatarFailed, m_publishAvatarMetadataTrackMap[id_] );
			}
			m_publishAvatarMetadataTrackMap.erase( id_ );
		}
		// 通知个人信息已修改的结果
		else if ( it_npic != m_notifyPersonalInfoChangedTrackList.end() )
		{
			if ( !error_ )
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onNotifyPersonalInfoChangedSuccess, id_ );
			}
			else
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onNotifyPersonalInfoChangedFailed, id_ );
			}
			m_notifyPersonalInfoChangedTrackList.erase( it_npic );
		}
		// 通知通讯录已修改的结果
		else if ( it_nabc != m_notifyAddressBookChangedTrackList.end() )
		{
			if ( !error_ )
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onNotifyAddressBookChangedSuccess, id_ );
			}
			else
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onNotifyAddressBookChangedFailed, id_ );
			}
			m_notifyAddressBookChangedTrackList.erase( it_nabc );
		}
	}

	void XmppStack::handleItemDeletion( const std::string& id_, 
		                                const gloox::JID& service_, 
										const std::string& node_, 
										const gloox::PubSub::ItemList& itemList_, 
										const gloox::Error* error_ )
	{
		// 删除微博的结果
		if ( m_deleteMicroblogTrackMap[id_] != "" )
		{
			if ( !error_ )
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onDeleteMicroblogSuccess, m_deleteMicroblogTrackMap[id_] );
			}
			else
			{
				gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onDeleteMicroblogFailed, m_deleteMicroblogTrackMap[id_] );
			}
			m_deleteMicroblogTrackMap.erase( id_ );
		}
	}

    void XmppStack::handleSubscriptionResult( const std::string& id_,
		                                      const gloox::JID& service_,
											  const std::string& node_,
											  const std::string& sid_,
											  const gloox::JID& jid_,
											  const gloox::PubSub::SubscriptionType subType_,
											  const gloox::Error* error_ )
	{
	}

	void XmppStack::handleUnsubscriptionResult( const std::string& id_, const gloox::JID& service_, const gloox::Error* error_ )
	{
	}

    void XmppStack::handleSubscriptionOptions( const std::string& id_,
		                                       const gloox::JID& service_,
											   const gloox::JID& jid_,
											   const std::string& node_,
											   const gloox::DataForm* options_,
											   const gloox::Error* error_ )
	{
	}

    void XmppStack::handleSubscriptionOptionsResult( const std::string& id_,
		                                             const gloox::JID& service_,
													 const gloox::JID& jid_,
													 const std::string& node_,
													 const gloox::Error* error_ )
	{
	}

    void XmppStack::handleSubscribers( const std::string& id_,
		                              const gloox::JID& service_,
									  const std::string& node_,
									  const gloox::PubSub::SubscriberList* list_,
									  const gloox::Error* error_ )
	{
	}

    void XmppStack::handleSubscribersResult( const std::string& id_,
		                                     const gloox::JID& service_,
											 const std::string& node_,
											 const gloox::PubSub::SubscriberList* list_,
											 const gloox::Error* error_ )
	{
	}

    void XmppStack::handleAffiliates( const std::string& id_,
		                              const gloox::JID& service_,
									  const std::string& node_,
									  const gloox::PubSub::AffiliateList* list_,
									  const gloox::Error* error_ )
	{
	}

    void XmppStack::handleAffiliatesResult( const std::string& id_,
		                                    const gloox::JID& service_,
											const std::string& node_,
											const gloox::PubSub::AffiliateList* list_,
											const gloox::Error* error_ )
	{
	}

    void XmppStack::handleNodeConfig( const std::string& id_,
		                              const gloox::JID& service_,
									  const std::string& node_,
									  const gloox::DataForm* config_,
									  const gloox::Error* error_ )
	{
		if ( error_ )
		{
			// 节点不存在
			if ( service_ == this->jid().bareJID() 
				&& ( XMLNS_MICROBLOG == node_ || XMLNS_AVATAR_DATA == node_ )
				&& gloox::StanzaError::StanzaErrorItemNotFound == error_->error()
				&& m_pubSubManager )
			{
				gloox::DataForm* config = new gloox::DataForm( gloox::FormType::TypeSubmit, "" );
				config->addField( gloox::DataFormField::FieldType::TypeBoolean, "pubsub#persist_items", "1", "" );
				if ( XMLNS_AVATAR_DATA == node_ )
				{
					config->addField( gloox::DataFormField::FieldType::TypeTextSingle, "pubsub#max_items", "1", "" );
				}
				m_pubSubManager->createNode( service_, node_, config, this );
			}

			return;
		}

		if ( service_ == this->jid().bareJID() 
			&& ( XMLNS_MICROBLOG == node_ || XMLNS_AVATAR_DATA == node_ ) )
		{
			// 是否更改重新配置
			bool changeConfig = false;
			// 是否被服务器持久化保存
			gloox::DataFormField* field_persist_items = config_->field( "pubsub#persist_items" );
			if ( field_persist_items && field_persist_items->value() != "1" )
			{
				changeConfig = true;
			}
			// 
			if ( XMLNS_AVATAR_DATA == node_ )
			{
				gloox::DataFormField* field_max_items = config_->field( "pubsub#max_items" );
				if ( field_max_items && field_max_items->value() != "1" )
				{
					changeConfig = true;
				}
			}

			if ( changeConfig && m_pubSubManager )
			{
				gloox::DataForm* config = new gloox::DataForm( gloox::FormType::TypeSubmit, "" );
				config->addField( gloox::DataFormField::FieldType::TypeBoolean, "pubsub#persist_items", "1", "" );
				if ( XMLNS_AVATAR_DATA == node_ )
				{
					config->addField( gloox::DataFormField::FieldType::TypeTextSingle, "pubsub#max_items", "1", "" );
				}
				m_pubSubManager->setNodeConfig( service_, node_, config, this );
			}
		}
	}

    void XmppStack::handleNodeConfigResult( const std::string& id_,
		                                    const gloox::JID& service_,
											const std::string& node_,
											const gloox::Error* error_ )
	{
	}

    void XmppStack::handleNodeCreation( const std::string& id_,
		                                const gloox::JID& service_,
										const std::string& node_,
										const gloox::Error* error_ )
	{
	}

    void XmppStack::handleNodeDeletion( const std::string& id_,
		                                const gloox::JID& service_,
										const std::string& node_,
										const gloox::Error* error_ )
	{
	}

    void XmppStack::handleNodePurge( const std::string& id_,
		                             const gloox::JID& service_,
									 const std::string& node_,
									 const gloox::Error* error_ )
	{
	}

    void XmppStack::handleSubscriptions( const std::string& id_,
		                                 const gloox::JID& service_,
										 const gloox::PubSub::SubscriptionMap& subMap_,
										 const gloox::Error* error_)
	{
	}

    void XmppStack::handleAffiliations( const std::string& id_,
		                                const gloox::JID& service_,
										const gloox::PubSub::AffiliationMap& affMap_,
										const gloox::Error* error_ )
	{
	}

    void XmppStack::handleDefaultNodeConfig( const std::string& id_,
		                                     const gloox::JID& service_,
											 const gloox::DataForm* config_,
											 const gloox::Error* error_ )
	{
	}

	void XmppStack::handleFTRequest( const gloox::JID& from_, 
		                             const gloox::JID& to_, 
									 const std::string& sid_,
									 const std::string& name_, 
									 long size_, 
									 const std::string& hash_,
									 const std::string& date_, 
									 const std::string& mimetype_,
									 const std::string& desc_, 
									 int stypes_ )
	{
		bool supportS5b = ( ( stypes_ & gloox::SIProfileFT::StreamType::FTTypeS5B ) == 0 ) ? false : true;
		bool supportIbb = ( ( stypes_ & gloox::SIProfileFT::StreamType::FTTypeIBB ) == 0 ) ? false : true;
		bool supportOob = ( ( stypes_ & gloox::SIProfileFT::StreamType::FTTypeOOB ) == 0 ) ? false : true;
		
		FileMetadata file;
		file.name = name_;
		file.size = size_;
		file.hash = hash_;
		file.date = date_;
		file.mimetype = mimetype_;
		file.desc = desc_;
		
		XmppCallbackList::const_iterator it = m_xmppCallbacks.begin();
		for ( ; it != m_xmppCallbacks.end(); it++ )
		{
			(*it)->onFtRequest( from_, sid_, file, supportS5b, supportIbb, supportOob );
		}
	}
	
	void XmppStack::handleFTRequestError( const gloox::IQ& iq_, const std::string& sid_ )
	{
	}

	void XmppStack::handleFTBytestream( gloox::Bytestream* bs_ )
	{
		// m_sIProfileFT创建了bs_，何时释放bs_？
		bs_->registerBytestreamDataHandler( this );

		m_bytestreamTrackMap[bs_->sid()] = bs_;

		XmppCallbackList::const_iterator it = m_xmppCallbacks.begin();
		for ( ; it != m_xmppCallbacks.end(); it++ )
		{
			(*it)->onFtBytestreamCreated( bs_->sid(), bs_->type(), bs_->initiator(), bs_->target() );
		}
	}
		
	const std::string XmppStack::handleOOBRequestResult( const gloox::JID& from_, const gloox::JID& to_, const std::string& sid_ )
	{
		return "";
	}

	void XmppStack::handleBytestreamData( gloox::Bytestream* bs_, const std::string& data_ )
	{
		BytestreamData bsdata( data_ );
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onRecvFtBytestreamData, bs_->sid(), ( const BytestreamData& ) bsdata );
	}
		
	void XmppStack::handleBytestreamError( gloox::Bytestream* bs_, const gloox::IQ& iq_ )
	{
		if ( bs_->isOpen() )
		{
			bs_->close();
		}
	}
		
	void XmppStack::handleBytestreamOpen( gloox::Bytestream* bs_ )
	{
		if ( bs_->initiator() == this->jid() )
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onFtOutgoingBytestreamOpened, bs_->sid() );
		}
		else
		{
			gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onFtIncomingBytestreamOpened, bs_->sid() );
		}
	}
		
	void XmppStack::handleBytestreamClose( gloox::Bytestream* bs_ )
	{
		gloox::util::ForEach( m_xmppCallbacks, &XmppCallback::onFtBytestreamClosed, bs_->sid() );

		gloox::Bytestream* bs = m_bytestreamTrackMap[bs_->sid()];
		if ( bs )
		{
			m_bytestreamTrackMap.erase( bs_->sid() );
		}
	}

	bool XmppStack::registerXmppCallback( XmppCallback* xmppCallback_ )
	{
		if ( !xmppCallback_ )
		{
			return false;
		}
		XmppCallbackList::const_iterator it = m_xmppCallbacks.begin();
		for ( ; it != m_xmppCallbacks.end(); it++ )
		{
			if ( (*it) == xmppCallback_ )
			{
				return false;
			}
		}
		m_xmppCallbacks.push_back( xmppCallback_ );
		return true;
	}

	void XmppStack::removeXmppCallback( XmppCallback* xmppCallback_ )
	{
		if ( xmppCallback_ )
		{
			m_xmppCallbacks.remove( xmppCallback_ );
		}
	}

	void XmppStack::login()
	{
		if ( !m_client->jid() || "" == m_client->jid().username() || "" == m_client->jid().server() )
		{
			gloox::util::ForEach( this->m_xmppCallbacks, &XmppCallback::onInvaildJID );
		}
		else if ( "" == m_client->password() )
		{
			gloox::util::ForEach( this->m_xmppCallbacks, &XmppCallback::onInvaildPassword );
		}
		// 尝试TCP连接，非阻塞模型
		else if ( gloox::ConnectionState::StateDisconnected == m_client->state() && m_client->connect( false ) )
		{
			gloox::util::ForEach( this->m_xmppCallbacks, &XmppCallback::onTcpConnSuccess );

			gloox::ConnectionError ce = gloox::ConnNoError;
			gloox::ConnectionError se = gloox::ConnNoError;
			
			while ( ce == gloox::ConnNoError )
			{
				// 从SOCKET接收数据
				ce = m_client->recv();

				if ( gloox::ConnStreamClosed == ce )
				{
					gloox::util::ForEach( this->m_xmppCallbacks, &XmppCallback::onTcpConnFailed, ce );
				}
				// 
				if ( m_sOCKS5BytestreamServer )
				{
					se = m_sOCKS5BytestreamServer->recv( 1 );
					if ( se != gloox::ConnNoError )
					{
						delete m_sOCKS5BytestreamServer;
						m_sOCKS5BytestreamServer = 0;
					}
				}
			}
		}
	}
    
    bool XmppStack::isLogined()
    {
        if ( m_client && gloox::ConnectionState::StateConnected == m_client->state() )
        {
            return true;
        }
        return false;
    }

	void XmppStack::logout()
	{
		m_client->disconnect();
	}

	void XmppStack::ping()
	{
		// <iq>
		gloox::Tag* tag_iq = new gloox::Tag( "iq" );
		tag_iq->addAttribute( "from", this->jid().full() );
		tag_iq->addAttribute( "to", this->jid().server() );
		tag_iq->addAttribute( "type", "get" );
		// <ping>
		gloox::Tag* tag_ping = new gloox::Tag( "ping" );
		tag_ping->setXmlns( gloox::XMLNS_XMPP_PING );

		tag_iq->addChild( tag_ping );

		m_client->send( tag_iq );

		delete tag_iq;
	}

	void XmppStack::discoItems( const gloox::JID& to_, const std::string& node_ )
	{
		if ( !m_client )
		{
			return;
		}

		m_client->disco()->getDiscoItems( to_, node_, 0, 0, "" );
	}

	void XmppStack::discoInfo( const gloox::JID& to_, const std::string& node_ )
	{
		if ( !m_client )
		{
			return;
		}
		
		m_client->disco()->getDiscoInfo( to_, node_, 0, 0, "" );
	}

	bool XmppStack::publishNickname( const std::string& nickname_ )
	{
		if ( !this->isLogined() || !m_pubSubManager )
		{
			return false;
		}

		gloox::PubSub::ItemList list;
		// <item>
		gloox::Tag* tag_item = new gloox::Tag( "item" );
		// <nick>
		gloox::Tag* tag_nick = new gloox::Tag( "nick");
		tag_nick->setXmlns( gloox::XMLNS_NICKNAME );
		tag_nick->setCData( nickname_ );
		
		tag_item->addChild( tag_nick );

		gloox::PubSub::Item* item = new gloox::PubSub::Item( tag_item );
		list.push_back( item );

		const std::string uid = m_pubSubManager->publishItem( gloox::JID(), gloox::XMLNS_NICKNAME, list, 0, this );

		m_publishNicknameTrackMap[uid] = nickname_;

		delete tag_item;
		tag_item = 0;

		return true;
	}

	const std::string XmppStack::publishAvatar( const std::string& id_,
		                                        const std::string& base64_, 
		                                        int bytes_, 
												const std::string& type_, 
												int height_, 
												int width_,
												const std::string& url_ )
	{
		if ( !m_pubSubManager )
		{
			return "";
		}

		std::string id = id_;

		gloox::PubSub::ItemList list;
		// <item>
		gloox::Tag* tag_item = new gloox::Tag( "item" );
		// 计算item的ID
		if ( id == "" )
		{
			gloox::SHA sha;
			sha.feed( base64_ );
			id = sha.hex();
			tag_item->addAttribute( "id", id );
		}
		// <data>
		gloox::Tag* tag_data = new gloox::Tag( "data" );
		tag_data->setXmlns( XMLNS_AVATAR_DATA );
		tag_data->setCData( base64_ );
		
		tag_item->addChild( tag_data );

		gloox::PubSub::Item* item = new gloox::PubSub::Item( tag_item );
		list.push_back( item );

		std::string uid = m_pubSubManager->publishItem( gloox::JID(), XMLNS_AVATAR_DATA, list, 0, this );
		// 创建头像元数据对象，并缓存
		AvatarMetadata* metadata = new AvatarMetadata( id, bytes_, type_ );
		metadata->setHeight( height_ );
		metadata->setWidth( width_ );
		metadata->setUrl( url_ );

		m_publishAvatarDataTrackMap[uid] = metadata;

		delete tag_item;
		tag_item = 0;

		return id;
	}

	void XmppStack::loadAvatar( const gloox::JID& to_, const std::string& id_ )
	{
		if ( !m_pubSubManager )
		{
			return;
		}
		
		gloox::PubSub::ItemList list;
		// <item>
		gloox::Tag* tag_item = new gloox::Tag( "item" );
		tag_item->addAttribute( "id", id_ );

		gloox::PubSub::Item* item = new gloox::PubSub::Item( tag_item );
		list.push_back( item );

		const std::string uid = m_pubSubManager->requestItems( to_, XMLNS_AVATAR_DATA, "", list, this );

		delete tag_item;
		tag_item = 0;
	}

	const std::string XmppStack::notifyPersonalInfoChanged()
	{
		if ( !m_pubSubManager )
		{
			return "";
		}
	
		gloox::PubSub::ItemList list;
		gloox::Tag* tag_item = new gloox::Tag( "item" );
		gloox::Tag* tag_personalinfo = new gloox::Tag( "personalinfo" );
		tag_item->addChild( tag_personalinfo );
		
		gloox::PubSub::Item* item = new gloox::PubSub::Item( tag_item );
		list.push_back( item );

		const std::string uid = m_pubSubManager->publishItem( gloox::JID(), XMLNS_NEEKLE_PERSONALINFO, list, 0, this );

		m_notifyPersonalInfoChangedTrackList.push_back( uid );

		delete tag_item;
		tag_item = 0;

		return uid;
	}

	const std::string XmppStack::notifyAddressBookChanged()
	{
		if ( !m_pubSubManager )
		{
			return "";
		}
	
		gloox::PubSub::ItemList list;
		gloox::Tag* tag_item = new gloox::Tag( "item" );
		gloox::Tag* tag_addressbook = new gloox::Tag( "addressbook" );
		tag_item->addChild( tag_addressbook );
		
		gloox::PubSub::Item* item = new gloox::PubSub::Item( tag_item );
		list.push_back( item );

		const std::string uid = m_pubSubManager->publishItem( gloox::JID(), XMLNS_NEEKLE_ADDRESSBOOK, list, 0, this );

		m_notifyAddressBookChangedTrackList.push_back( uid );

		delete tag_item;
		tag_item = 0;

		return uid;
	}

	void XmppStack::sendXml( const std::string& xml_ )
	{
		if ( !m_client || ! m_client->connectionImpl() || m_client->connectionImpl()->state() != gloox::StateConnected )
		{
			return;
		}

		if ( m_client->encryptionImpl() )
		{
			m_client->encryptionImpl()->encrypt( xml_ );
		}
		else
		{
			m_client->connectionImpl()->send( xml_ );
		}

		m_client->logInstance().dbg( gloox::LogAreaXmlOutgoing, xml_ );
	}

	bool XmppStack::requestRegistrationFields()
	{
		if( !this->isLogined() )
		{
			return false;
		}

		m_registration->fetchRegistrationFields();
		return true;
	}

	bool XmppStack::sendChatMessage( const std::string& id_, 
		                             const gloox::JID& to_, 
									 const std::string& body_,
									 const std::string& xhtml_,
									 const std::string& subject_, 
									 const std::string& thread_,
									 bool amp_,
									 bool receipts_ )
	{

		if( !this->isLogined() )
		{
			return false;
		}

		gloox::Message msg( gloox::Message::Chat, to_, body_, subject_, thread_ );
		msg.setID( id_ );
		// 设置富文本
		if ( "" != xhtml_ )
		{
			XmlParser p;
			p.feed( xhtml_ );
			if ( p.tag() )
			{
				const gloox::XHtmlIM* xhtmlim = new gloox::XHtmlIM( p.tag() );
				if ( xhtmlim->xhtml() )
				{
					msg.addExtension( xhtmlim ); // msg负责xhtmlim的销毁
				}
			}
		}
		if ( amp_ )
		{
			gloox::AMP* amp = new gloox::AMP();
			gloox::AMP::Rule* rule1 = new gloox::AMP::Rule( "deliver", "notify", "direct" );
			gloox::AMP::Rule* rule2 = new gloox::AMP::Rule( "deliver", "notify", "stored" );
			amp->addRule( rule1 );
			amp->addRule( rule2 );
			msg.addExtension( amp ); // msg负责amp的销毁
		}
		if ( receipts_ )
		{
			msg.addExtension( new gloox::Receipt( gloox::Receipt::ReceiptType::Request ) );
		}
		m_client->send( msg );
		return true;
	}

	bool XmppStack::sendIMailMessage( const std::string& id_, 
		                              const gloox::JID& to_, 
									  const IMailSMTPInfo& iMailSMTPInfo_, 
									  bool receipts_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() )
		{
			return false;
		}
		gloox::Tag* tag_smtpinfo = iMailSMTPInfo_.newTag();
		gloox::Message msg( gloox::Message::MessageType::Normal, to_, tag_smtpinfo->xml(), "imail" );
		msg.setID( id_ );
		if ( receipts_ )
		{
			msg.addExtension( new gloox::Receipt( gloox::Receipt::ReceiptType::Request ) );
		}
		m_client->send( msg );
		delete tag_smtpinfo;
		return true;
	}

	bool XmppStack::sendLocationMessage( const std::string& id_, const gloox::JID& to_, const Geoloc& geoloc_ )
	{
		if ( !this->isLogined() )
		{
			return false;
		}
		gloox::Tag* tag_geoloc = geoloc_.newTag();
		gloox::Message msg( gloox::Message::MessageType::Normal, to_, tag_geoloc->xml(), "location" );
		msg.setID( id_ );
		m_client->send( msg );
		delete tag_geoloc;
		return true;
	}

	bool XmppStack::sendReceiptMessage( const std::string& id_, const gloox::JID& to_, const std::string& receiptId_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() )
		{
			return false;
		}
		gloox::Message msg( gloox::Message::Normal, to_ );
		msg.setID( id_ );
		kl::Receipt* receipt = new kl::Receipt( gloox::Receipt::ReceiptType::Received, receiptId_ );
		msg.addExtension( receipt );
		m_client->send( msg );
		return true;
	}

	bool XmppStack::sendAttentionMessage( const gloox::JID& to_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() )
		{
			return false;
		}
		gloox::Message attention( gloox::Message::Headline, to_ );
		attention.addExtension( new gloox::Attention() );
		m_client->send( attention );
		return true;
	}

	bool XmppStack::sendPresence( gloox::Presence::PresenceType type_, const std::string& status_, int priority_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() )
		{
			return false;
		}
		gloox::Presence pres( type_, gloox::JID(), status_, priority_ );
		m_client->send( pres );
		return true;
	}

	bool XmppStack::getRoster()
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() )
		{
			return false;
		}
		m_client->rosterManager()->fill();
		return true;
	}

	bool XmppStack::addRosterItem( const gloox::JID& jid_, 
		                           const std::string& name_, 
								   const gloox::StringList& groups_, 
								   bool subscribe_, 
								   const std::string& msg_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() )
		{
			return false;
		}
		if ( subscribe_ )
		{
			// 添加到花名册 + 订阅对方状态
			m_client->rosterManager()->subscribe( jid_, name_, groups_, msg_ );
		}
		else
		{
			// 只添加到花名册
			m_client->rosterManager()->add( jid_, name_, groups_ );
		}
		return true;
	}

	bool XmppStack::moveRosterItemToGroup( const gloox::JID& jid_, const std::string& group_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() || !m_client->rosterManager() )
		{
			return false;
		}
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( !item )
		{
			return false;
		}
		gloox::StringList groups;
		groups.push_back( group_ );
		m_client->rosterManager()->add( jid_, item->name(), groups );
		return true;
	}

	bool XmppStack::moveRosterItemToGroups( const gloox::JID& jid_, const gloox::StringList& groups_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() || !m_client->rosterManager() )
		{
			return false;
		}
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( !item )
		{
			return false;
		}
		m_client->rosterManager()->add( jid_, item->name(), groups_ );
		return true;
	}

	bool XmppStack::copyRosterItemToGroup( const gloox::JID& jid_, const std::string& group_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() || !m_client->rosterManager() )
		{
			return false;
		}
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( !item )
		{
			return false;
		}
		gloox::StringList groups = item->groups();
		gloox::StringList::const_iterator it = std::find( groups.begin(), groups.end(), group_ );
		if ( it == groups.end() )
		{
			groups.push_back( group_ );
			m_client->rosterManager()->add( jid_, item->name(), groups );
		}
		return true;
	}

	bool XmppStack::copyRosterItemToGroups( const gloox::JID& jid_, const gloox::StringList& groups_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() || !m_client->rosterManager() )
		{
			return false;
		}
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( !item )
		{
			return false;
		}
		gloox::StringList groups = item->groups();
		std::list<std::string>::const_iterator it_groups_ = groups_.begin();
		for ( ; it_groups_ != groups_.end(); it_groups_++ )
		{
			std::list<std::string>::iterator it_same = std::find( groups.begin(), groups.end(), (*it_groups_) );
			if ( it_same == groups.end() )
			{
				groups.push_back( (*it_groups_) );
			}
		}
		m_client->rosterManager()->add( jid_, item->name(), groups );
		return true;
	}

	bool XmppStack::removeRosterItemFromGroup( const gloox::JID& jid_, const std::string& group_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() || !m_client->rosterManager() )
		{
			return false;
		}
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( !item )
		{
			return false;
		}
		gloox::StringList groups = item->groups();
		gloox::StringList::const_iterator it = std::find( groups.begin(), groups.end(), group_ );
		if ( it != groups.end() )
		{
			groups.remove( group_ );
			m_client->rosterManager()->add( jid_, item->name(), groups );
		}
		return true;
	}

	bool XmppStack::removeRosterItemFromGroups( const gloox::JID& jid_, const gloox::StringList& groups_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() || !m_client->rosterManager() )
		{
			return false;
		}
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( !item )
		{
			return false;
		}
		gloox::StringList groups = item->groups();
		std::list<std::string>::const_iterator it_groups_ = groups_.begin();
		for ( ; it_groups_ != groups_.end(); it_groups_++ )
		{
			std::list<std::string>::iterator it_same = std::find( groups.begin(), groups.end(), (*it_groups_) );
			if ( it_same != groups.end() )
			{
				groups.remove( (*it_groups_) );
			}
		}
		m_client->rosterManager()->add( jid_, item->name(), groups );
		return true;
	}

	bool XmppStack::removeRosterItemFromAllGroups( const gloox::JID& jid_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() || !m_client->rosterManager() )
		{
			return false;
		}
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( !item )
		{
			return false;
		}
		if ( item->groups().size() > 0 )
		{
			m_client->rosterManager()->add( jid_, item->name(), gloox::StringList() );
		}
		return true;
	}

	bool XmppStack::changeRosterItemName( const gloox::JID& jid_, const std::string& name_ )
	{
		if ( !m_client || gloox::ConnectionState::StateConnected != m_client->state() || !m_client->rosterManager() )
		{
			return false;
		}
		gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
		if ( !item )
		{
			return false;
		}
		if ( name_ != item->name() )
		{
			m_client->rosterManager()->add( jid_, name_, item->groups() );
		}
		return true;
	}

	bool XmppStack::updateRosterItem( const gloox::JID& jid_, const std::string& name_, const gloox::StringList& groups_ )
	{
		if ( m_client && m_client->rosterManager() && gloox::ConnectionState::StateConnected == m_client->state() )
		{
			gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
			if ( item )
			{
				m_client->rosterManager()->add( jid_, name_, groups_ );
				return true;
			}
		}
		return false;
	}

	bool XmppStack::deleteRosterItem( const gloox::JID& jid_ )
	{
		if ( m_client && gloox::ConnectionState::StateConnected == m_client->state() )
		{
			m_client->rosterManager()->remove( jid_ );
			return true;
		}
		return false;
	}

	bool XmppStack::subscribeRosterItem( const gloox::JID& jid_, const std::string& msg_ )
	{
		if ( m_client && gloox::ConnectionState::StateConnected == m_client->state() )
		{
			gloox::RosterItem* item = m_client->rosterManager()->getRosterItem( jid_ );
			if ( item )
			{
				gloox::Subscription subs( gloox::Subscription::Subscribe, jid_.bareJID(), msg_ );
				m_client->send( subs );
				return true;
			}
		}
		return false;
	}

	// 接收订阅请求
	bool XmppStack::approveSubscription( const gloox::JID& jid_, bool autoSubscribeRemote_ )
	{
		if ( m_client && m_client->rosterManager() && gloox::ConnectionState::StateConnected == m_client->state() )
		{
			m_client->rosterManager()->ackSubscriptionRequest( jid_, true );
			// 自动订阅对方
			if ( autoSubscribeRemote_ )
			{
				gloox::Subscription subs( gloox::Subscription::Subscribe, jid_, "" );
				m_client->send( subs );
			}
			return true;
		}
		return false;
	}

	// 拒绝订阅请求
	bool XmppStack::denySubscription( const gloox::JID& jid_, bool autoDeleteRosterItem )
	{
		if ( m_client && m_client->rosterManager() && gloox::ConnectionState::StateConnected == m_client->state() )
		{
			m_client->rosterManager()->ackSubscriptionRequest( jid_, false );
			// 自动删除花名册项
			if ( autoDeleteRosterItem )
			{
				m_client->rosterManager()->remove( jid_ );
			}
		}
		return false;
	}

	void XmppStack::createPubSubNode( const gloox::JID& service_, const std::string& node_ )
	{
		if ( !m_pubSubManager )
		{
			return;
		}
		gloox::DataForm* config = new gloox::DataForm( gloox::FormType::TypeSubmit, "" );
		config->addField( gloox::DataFormField::FieldType::TypeNone, "pubsub#publish_model", "open", "" );
		m_pubSubManager->createNode( service_, node_, 0, this );
	}

	const std::string XmppStack::publishUserTune( const std::string& title_ )
	{
		if ( !m_pubSubManager )
		{
			return "";
		}
		gloox::PubSub::ItemList list;
		gloox::Tag* tag_item = new gloox::Tag( "item" );
		// 生成动态item的ID
		gloox::SHA sha;
		sha.feed( this->jid().full() + title_ );
		std::string item_id = sha.hex();
		tag_item->addAttribute( "id", item_id );
		// <tune>
		gloox::Tag* tag_tune = new gloox::Tag( "tune" );
		tag_tune->setXmlns( "http://jabber.org/protocol/tune" );
		// <title>
		gloox::Tag* tag_title = new gloox::Tag( "title" );
		tag_title->setCData( title_ );
		
		tag_tune->addChild( tag_title );
		tag_item->addChild( tag_tune );

		gloox::PubSub::Item* item = new gloox::PubSub::Item( tag_item );
		list.push_back( item );
		// <publish-options>
		gloox::DataForm* options = new gloox::DataForm( gloox::FormType::TypeSubmit, "" );
		options->addField( gloox::DataFormField::FieldType::TypeListSingle, "pubsub#access_model", "roster" );
		gloox::DataFormField* field_roster_groups_allowed = new gloox::DataFormField( gloox::DataFormField::FieldType::TypeListMulti );
		field_roster_groups_allowed->setName( "pubsub#roster_groups_allowed" );
		field_roster_groups_allowed->addOption( "", "x" );
		field_roster_groups_allowed->addValue( "x" );
		options->addField( field_roster_groups_allowed );
		
		const std::string uid = m_pubSubManager->publishItem( gloox::JID(""), "http://jabber.org/protocol/tune", list, 0, this );
		return uid;
	}

	void XmppStack::configUserTuneNode()
	{
		if ( !m_pubSubManager )
		{
			return;
		}

		gloox::DataForm* config = new gloox::DataForm( gloox::FormType::TypeSubmit, "" );
		config->addField( gloox::DataFormField::FieldType::TypeListSingle, "pubsub#access_model", "roster", "" );
		gloox::DataFormField* field_roster_groups_allowed = new gloox::DataFormField( gloox::DataFormField::FieldType::TypeListMulti );
		field_roster_groups_allowed->setName( "pubsub#roster_groups_allowed" );
		field_roster_groups_allowed->addOption( "", "a" );//->addValue( "123" );
		//field_roster_groups_allowed->addOption( "", "b" );
		field_roster_groups_allowed->addValue( "a" );
		config->addField( field_roster_groups_allowed );
		//config->addField( gloox::DataFormField::FieldType::TypeListMulti, "pubsub#access_model", "roster", "" );

		m_pubSubManager->setNodeConfig( gloox::JID(this->jid().bare()), "http://jabber.org/protocol/tune", config, this );
	}

	void XmppStack::getUserTuneNodeConfig()
	{
		if ( !m_pubSubManager )
		{
			return;
		}

		m_pubSubManager->getNodeConfig( gloox::JID(this->jid().bare()), "http://jabber.org/protocol/tune", this );
	}

	void XmppStack::deleteUserTuneNode()
	{
		if ( !m_pubSubManager )
		{
			return;
		}
		m_pubSubManager->deleteNode( gloox::JID(this->jid().bare()), "http://jabber.org/protocol/tune", this );
	}

	const std::string XmppStack::publish( const Microblog& microblog_ )
	{
		if ( !m_pubSubManager )
		{
			return "";
		}

		gloox::PubSub::ItemList list;
		// <item>
		gloox::Tag* tag_item = new gloox::Tag( "item" );
		// set id
		std::string item_id = microblog_.id();
		if ( item_id == "" )
		{
			gloox::SHA sha;
			sha.feed( this->jid().full() + microblog_.content() + microblog_.published() );
			item_id = sha.hex();
		}
		tag_item->addAttribute( "id", item_id );
		// <entry>
		gloox::Tag* tag_entry = new gloox::Tag( "entry" );
		tag_entry->setXmlns( XMLNS_ATOM );
		// <author>
		gloox::Tag* tag_author = new gloox::Tag( "author" );
		tag_author->setCData( this->jid().bare() );
		// <content>
		gloox::Tag* tag_content = new gloox::Tag( "content" );
		if ( Microblog::Type::Text == microblog_.type() )
		{
			tag_content->addAttribute( "type", "text" );
		}
		else if ( Microblog::Type::Xhtml == microblog_.type() )
		{
			tag_content->addAttribute( "type", "xhtml" );
		}
		tag_content->setCData( microblog_.content() );
		// <published>
		gloox::Tag* tag_published = new gloox::Tag( "published" );
		tag_published->setCData( microblog_.published() );
		// <geoloc>
		gloox::Tag* tag_geoloc = new gloox::Tag( "geoloc" );
		tag_geoloc->setXmlns( XMLNS_GEOLOC );
		// <locality>
		gloox::Tag* tag_locality = new gloox::Tag( "locality" );
		tag_locality->setCData( microblog_.geoloc() );
		tag_geoloc->addChild( tag_locality );
		// <device>
		gloox::Tag* tag_device = new gloox::Tag( "device" );
		tag_device->setCData( microblog_.device() );
		// <link>
		gloox::Tag* tag_link = 0;
		if ( microblog_.commentLink() != "" )
		{
			tag_link = new gloox::Tag( "link" );
			tag_link->addAttribute( "rel", "replies" );
			tag_link->addAttribute( "title", "comments" );
			tag_link->addAttribute( "href", microblog_.commentLink() );
		}
		//
		tag_entry->addChild( tag_author );
		tag_entry->addChild( tag_content );
		tag_entry->addChild( tag_published );
		tag_entry->addChild( tag_geoloc );
		tag_entry->addChild( tag_device );
		tag_entry->addChild( tag_link );

		tag_item->addChild( tag_entry );
		
		gloox::PubSub::Item* item = new gloox::PubSub::Item( tag_item );
		list.push_back( item );

		const std::string uid = m_pubSubManager->publishItem( gloox::JID(""), XMLNS_MICROBLOG, list, 0, this );

		m_publishMicroblogTrackMap[uid] = item_id;

		delete tag_item;
		tag_item = 0;

		return uid;
	}

	const std::string XmppStack::deleteMicroblog( const std::string& id_ )
	{
		if ( !m_pubSubManager )
		{
			return "";
		}

		gloox::PubSub::ItemList list;
		gloox::Tag* tag_item = new gloox::Tag( "item" );
		tag_item->addAttribute( "id", id_ );
		gloox::PubSub::Item* item = new gloox::PubSub::Item( tag_item );
		list.push_back( item );

		const std::string uid = m_pubSubManager->deleteItem( this->jid().bareJID(), XMLNS_MICROBLOG, list, true, this );

		m_deleteMicroblogTrackMap[uid] = id_;

		return uid;
	}

	void XmppStack::deleteSelfMicroblogNode()
	{
		if ( !m_pubSubManager )
		{
			return;
		}

		m_pubSubManager->deleteNode( this->jid().bareJID(), XMLNS_MICROBLOG, this );
	}

	void XmppStack::requestMicroblogs( const gloox::JID& to_ )
	{
		if ( !m_pubSubManager )
		{
			return;
		}

		m_pubSubManager->requestItems( to_, kl::XMLNS_MICROBLOG, "", gloox::PubSub::ItemList(), this );
	}
	
	void XmppStack::createMUCRoom( const gloox::JID& room_, const MUCRoomConfig& config_ )
	{
		gloox::JID roomJID = room_;
		// 如果没有设置群内昵称，将用户名作为群内昵称
		if ( "" == roomJID.resource() )
		{
			roomJID.setResource( this->jid().username() );
		}
		// MUCRoomConfig --> dataform
		gloox::DataForm* df = new gloox::DataForm( gloox::FormType::TypeSubmit );
		// 设置房间名称
		df->addField( gloox::DataFormField::FieldType::TypeTextSingle, "muc#roomconfig_roomname", config_.roomname, "Natural-Language Room Name" );
		// 设置房间简介
		df->addField( gloox::DataFormField::FieldType::TypeTextSingle, "muc#roomconfig_roomdesc", config_.roomdesc, "Short Description of Room" );
		// 是否记录日志
		df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_enablelogging", config_.enableLogging ? "1" : "0", "Enable Public Logging" );
		// 设置是否允许住客修改主题
		df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_changesubject", config_.enableChangeSubject ? "1" : "0", "Allow Occupants to Change Subject" );
		// 设置是否为公共房间
		df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_publicroom", config_.isPublic ? "1" : "0", "Make Room Publicly Searchable" );
		// 设置是否为保留房间
		df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_persistentroom", config_.isPersistent ? "1" : "0", "Make Room Persistent" );
		// 设置是否为主持人房间
		df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_moderatedroom", config_.isModerated ? "1" : "0", "Make Room Moderated" );
		// 设置是否为白名单房间
		df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_membersonly", config_.isMembersOnly ? "1" : "0", "Make Room Members Only" );
		// 设置是否为密码保护房间
		df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_passwordprotectedroom", config_.isPasswordProtected ? "1" : "0", "Password Required for Entry" );
		// 设置密码
		df->addField( gloox::DataFormField::FieldType::TypeTextPrivate, "muc#roomconfig_roomsecret", config_.roompassword, "Password" );
		// 创建房间
		gloox::MUCRoom* room = new kl::MUCRoom( m_client, roomJID, this, this );
		room->join();
		
		m_mUCRoomMap[roomJID.bare()] = room;
		m_createMUCRoomTrackMap[room_.bare()] = df;
	}

	void XmppStack::destroyMUCRoom( const gloox::JID& room_ )
	{
		std::map<std::string, gloox::MUCRoom*>::const_iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->destroy();
		}
	}

	void XmppStack::modifyMUCRoomMemberList( const gloox::JID& room_, const gloox::StringList& members_ )
	{
		std::map<std::string, gloox::MUCRoom*>::const_iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			gloox::MUCListItemList list;
			gloox::StringList::const_iterator sl_it = members_.begin();
			for ( ; sl_it != members_.end(); sl_it++ )
			{
				gloox::JID jid( (*sl_it) );
				gloox::MUCListItem item( jid );
				list.push_back( item );
			}
			(*it).second->storeList( list, gloox::MUCOperation::StoreMemberList );
		}
	}

	void XmppStack::requestMUCRoomMemberList( const gloox::JID& room_ )
	{
		std::map<std::string, gloox::MUCRoom*>::const_iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->requestList( gloox::MUCOperation::RequestMemberList );
		}
	}

	void XmppStack::modifyMUCRoomOwnerList( const gloox::JID& room_, const gloox::StringList& owners_ )
	{
		std::map<std::string, gloox::MUCRoom*>::const_iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			gloox::MUCListItemList list;
			gloox::StringList::const_iterator sl_it = owners_.begin();
			for ( ; sl_it != owners_.end(); sl_it++ )
			{
				gloox::JID jid( (*sl_it) );
				gloox::MUCListItem item( jid );
				list.push_back( item );
			}
			(*it).second->storeList( list, gloox::MUCOperation::StoreOwnerList );
		}
	}

	void XmppStack::requestMUCRoomOwnerList( const gloox::JID& room_ )
	{
		std::map<std::string, gloox::MUCRoom*>::const_iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->requestList( gloox::MUCOperation::RequestOwnerList );
		}
	}

	void XmppStack::enterMUCRoom( const gloox::JID& room_, 
		                          gloox::Presence::PresenceType presType_,
								  const std::string& presStatus_,
								  int presPriority_,
								  const std::string& password_,
								  bool requestHistory_ )
	{
		gloox::MUCRoom* room = 0;
		std::map<std::string, gloox::MUCRoom*>::const_iterator it = m_mUCRoomMap.find( room_.bare() );
		// 房间已在集合中
		if ( it != m_mUCRoomMap.end() && (*it).second )
		{
			room = (*it).second;
		}
		// 房间不在集合中，需要创建
		else
		{
			// 确保设置了房间内昵称
			gloox::JID roomJID = room_;
			if ( roomJID.resource() == "" )
			{
				roomJID.setResource( this->jid().username() );
			}
			room = new gloox::MUCRoom( m_client, roomJID, this, this );
			m_mUCRoomMap[roomJID.bare()] = room;
		}
		// 设置房间密码，如果需要
		if ( password_ != "" )
		{
			room->setPassword( password_ );
		}
		if ( !requestHistory_ )
		{
			room->setRequestHistory( 0, gloox::MUCRoom::HistoryRequestType::HistoryMaxStanzas );
		}
		room->join();
	}

	void XmppStack::configMUCRoom( const gloox::JID& room_, const MUCRoomConfig& config_ )
	{
		//std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		//if ( it != m_mUCRoomMap.end() )
		//{
		//	gloox::DataForm* df = new gloox::DataForm( gloox::FormType::TypeSubmit );
		//	// 设置房间名称
		//	df->addField( gloox::DataFormField::FieldType::TypeTextSingle, "muc#roomconfig_roomname", config_.roomname, "Natural-Language Room Name" );
		//	// 设置房间简介
		//	df->addField( gloox::DataFormField::FieldType::TypeTextSingle, "muc#roomconfig_roomdesc", config_.roomdesc, "Short Description of Room" );
		//	// Enable Public Logging?
		//	df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_enablelogging", config_.enableLogging ? "1" : "0", "Enable Public Logging" );
		//	// 设置是否允许住客修改主题
		//	df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_changesubject", config_.enableChangeSubject ? "1" : "0", "Allow Occupants to Change Subject" );
		//	// 设置是否允许住客发出邀请
		//	df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_allowinvites", config_.enableInvite ? "1" : "0", "Allow Occupants to Invite Others" );
		//	// 设置谁被允许发送私信
		//	/*if ( config_.allowPrivateMessage == MUCRoomConfig::WhoCanSendPrivateMessages::Anyone )
		//	{
		//		df->addField( gloox::DataFormField::FieldType::TypeListSingle, "muc#roomconfig_allowpm", "anyone", "Who Can Send Private Messages" );
		//	}
		//	else if ( config_.allowPrivateMessage == MUCRoomConfig::WhoCanSendPrivateMessages::Moderators )
		//	{
		//		df->addField( gloox::DataFormField::FieldType::TypeListSingle, "muc#roomconfig_allowpm", "moderators", "Who Can Send Private Messages" );
		//	}
		//	else if ( config_.allowPrivateMessage == MUCRoomConfig::WhoCanSendPrivateMessages::Participants )
		//	{
		//		df->addField( gloox::DataFormField::FieldType::TypeListSingle, "muc#roomconfig_allowpm", "participants", "Who Can Send Private Messages" );
		//	}
		//	else if ( config_.allowPrivateMessage == MUCRoomConfig::WhoCanSendPrivateMessages::None )
		//	{
		//		df->addField( gloox::DataFormField::FieldType::TypeListSingle, "muc#roomconfig_allowpm", "none", "Who Can Send Private Messages" );
		//	}*/
		//	// 设置住客人数上限
		//	if ( config_.maxOccupants > 0 )
		//	{
		//		df->addField( gloox::DataFormField::FieldType::TypeListSingle, "muc#roomconfig_maxusers", gloox::util::int2string( config_.maxOccupants ), "Maximum Number of Occupants" );
		//	}
		//	else
		//	{
		//		df->addField( gloox::DataFormField::FieldType::TypeListSingle, "muc#roomconfig_maxusers", "0", "Maximum Number of Occupants" );
		//	}
		//	// Roles for which Presence is Broadcasted
		//	gloox::DataFormField* filed_presencebroadcast = new gloox::DataFormField( gloox::DataFormField::FieldType::TypeListMulti );
		//	filed_presencebroadcast->setName( "muc#roomconfig_presencebroadcast" );
		//	filed_presencebroadcast->setLabel( "Roles for which Presence is Broadcasted" );
		//	if ( config_.presencebroadcast == gloox::MUCRoomRole::RoleVisitor )
		//	{
		//		filed_presencebroadcast->addValue( "visitor" );
		//		filed_presencebroadcast->addValue( "participant" );
		//		filed_presencebroadcast->addValue( "moderator" );
		//	}
		//	else if ( config_.presencebroadcast == gloox::MUCRoomRole::RoleParticipant )
		//	{
		//		filed_presencebroadcast->addValue( "participant" );
		//		filed_presencebroadcast->addValue( "moderator" );
		//	}
		//	else if ( config_.presencebroadcast == gloox::MUCRoomRole::RoleModerator )
		//	{
		//		filed_presencebroadcast->addValue( "moderator" );
		//	}
		//	df->addField( filed_presencebroadcast );
		//	// Roles and Affiliations that May Retrieve Member List
		//	gloox::DataFormField* filed_getmemberlist = new gloox::DataFormField( gloox::DataFormField::FieldType::TypeListMulti );
		//	filed_getmemberlist->setName( "muc#roomconfig_getmemberlist" );
		//	filed_getmemberlist->setLabel( "Roles and Affiliations that May Retrieve Member List" );
		//	if ( config_.getMemberList == gloox::MUCRoomRole::RoleVisitor )
		//	{
		//		filed_getmemberlist->addValue( "visitor" );
		//	}
		//	else if ( config_.getMemberList == gloox::MUCRoomRole::RoleParticipant )
		//	{
		//		filed_getmemberlist->addValue( "visitor" );
		//		filed_getmemberlist->addValue( "participant" );
		//	}
		//	else if ( config_.getMemberList == gloox::MUCRoomRole::RoleModerator )
		//	{
		//		filed_getmemberlist->addValue( "visitor" );
		//		filed_getmemberlist->addValue( "participant" );
		//		filed_getmemberlist->addValue( "moderator" );
		//	}
		//	df->addField( filed_getmemberlist );
		//	// 设置是否为公共房间
		//	df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_publicroom", config_.isPublic ? "1" : "0", "Make Room Publicly Searchable" );
		//	// 设置是否为保留房间
		//	df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_persistentroom", config_.isPersistent ? "1" : "0", "Make Room Persistent" );
		//	// 设置是否为主持人房间
		//	df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_moderatedroom", config_.isModerated ? "1" : "0", "Make Room Moderated" );
		//	// 设置是否为白名单房间
		//	df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_membersonly", config_.isMembersOnly ? "1" : "0", "Make Room Members Only" );
		//	// 设置是否为密码保护房间
		//	df->addField( gloox::DataFormField::FieldType::TypeBoolean, "muc#roomconfig_passwordprotectedroom", config_.isPasswordProtected ? "1" : "0", "Password Required for Entry" );
		//	// 设置密码
		//	df->addField( gloox::DataFormField::FieldType::TypeTextPrivate, "muc#roomconfig_roomsecret", config_.roompassword, "Password" );
		//	// 设置是否为匿名房间
		//	df->addField( gloox::DataFormField::FieldType::TypeListSingle, "muc#roomconfig_whois", config_.isAnonymous ? "moderators" : "anyone", "Who May Discover Real JIDs" );
		//	// 设置返回历史记录上限
		//	df->addField( gloox::DataFormField::FieldType::TypeListSingle, "muc#maxhistoryfetch", gloox::util::int2string( config_.maxHistory ), "Maximum Number of History Messages Returned by Room" );
		//	// 设置房间管理员
		//	gloox::DataFormField* filed_roomadmins = new gloox::DataFormField( gloox::DataFormField::FieldType::TypeJidMulti );
		//	filed_roomadmins->setName( "muc#roomconfig_roomadmins" );
		//	filed_roomadmins->setLabel( "Room Admins" );
		//	filed_roomadmins->setValues( config_.roomadmins );
		//	df->addField( filed_roomadmins );
		//	//// 设置房间所用者
		//	gloox::DataFormField* filed_roomowners = new gloox::DataFormField( gloox::DataFormField::FieldType::TypeJidMulti );
		//	filed_roomowners->setName( "muc#roomconfig_roomowners" );
		//	filed_roomowners->setLabel( "Room Owners" ); 
		//	std::list<std::string> roomowners = config_.roomowners;
		//	if ( 0 == roomowners.size() )
		//	{
		//		roomowners.push_back( this->jid().bare() );
		//	}
		//	filed_roomowners->setValues( roomowners );
		//	df->addField( filed_roomowners );

		//	(*it).second->setRoomConfig( df );
		//}
	}

	void XmppStack::queryMUCRoomInfo( const gloox::JID& room_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->getRoomInfo();
		}
	}

	void XmppStack::queryMUCRoomConfig( const gloox::JID& room_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->requestRoomConfig();
		}
	}

	void XmppStack::grantMUCRoomMembership( const gloox::JID& room_, const gloox::JID& user_ )
	{
		if ( m_client )
		{
			gloox::Tag* tag_iq = new gloox::Tag( "iq" );
			tag_iq->addAttribute( "id", m_client->getID() );
			tag_iq->addAttribute( "to", room_.bare() );
			tag_iq->addAttribute( "type", "set" );
			gloox::Tag* tag_query = new gloox::Tag( "query" );
			tag_query->setXmlns( gloox::XMLNS_MUC_ADMIN );
			gloox::Tag* tag_item = new gloox::Tag( "item" );
			tag_item->addAttribute( "affiliation", "member" );
			tag_item->addAttribute( "jid", user_.bare() );
			
			tag_query->addChild( tag_item );
			tag_iq->addChild( tag_query );

			//m_client->send( new gloox::IQ( tag_iq ), this, 14 ); // send() will delete the tag
		}
	}

	void XmppStack::grantMUCRoomMembership( const gloox::JID& room_, const std::string& nickname_, const std::string& reason_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->setAffiliation( nickname_, gloox::MUCRoomAffiliation::AffiliationMember, reason_ );
		}
	}

	void XmppStack::revokeMUCRoomMembership( const gloox::JID& room_, const gloox::JID& user_ )
	{
		if ( m_client )
		{
			gloox::Tag* tag_iq = new gloox::Tag( "iq" );
			tag_iq->addAttribute( "id", m_client->getID() );
			tag_iq->addAttribute( "to", room_.bare() );
			tag_iq->addAttribute( "type", "set" );
			gloox::Tag* tag_query = new gloox::Tag( "query" );
			tag_query->setXmlns( gloox::XMLNS_MUC_ADMIN );
			gloox::Tag* tag_item = new gloox::Tag( "item" );
			tag_item->addAttribute( "affiliation", "none" );
			tag_item->addAttribute( "jid", user_.bare() );
			
			tag_query->addChild( tag_item );
			tag_iq->addChild( tag_query );

			m_client->send( tag_iq ); // send() will delete the tag
		}
	}

	void XmppStack::revokeMUCRoomOwnership( const gloox::JID& room_, const gloox::JID& user_ )
	{
		this->revokeMUCRoomMembership( room_, user_ );
	}

	void XmppStack::inviteIntoMUCRoom( const gloox::JID& room_, 
		                               const gloox::JID& invitee_,
									   MUCRoomInvitationType type_,
									   const std::string& reason_,
									   bool amp_,
									   bool receipts_ )
	{
		if ( !m_client )
		{
			return;
		}

		switch ( type_ )
		{
		case MUCRoomInvitationType::Mediated:
			{

				std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
				if ( it != m_mUCRoomMap.end() )
				{
					gloox::Message msg( gloox::Message::Normal, room_.bare() ); // with <body>
					msg.addExtension( new gloox::MUCRoom::MUCUser( gloox::MUCRoom::MUCUserOperation::OpInviteTo, invitee_.bare(), reason_ ) );
					// amp
					if ( amp_ )
					{
						gloox::AMP* amp = new gloox::AMP();
						gloox::AMP::Rule* rule1 = new gloox::AMP::Rule( "deliver", "notify", "direct" );
						gloox::AMP::Rule* rule2 = new gloox::AMP::Rule( "deliver", "notify", "stored" );
						amp->addRule( rule1 );
						amp->addRule( rule2 );
						msg.addExtension( amp );
					}
					// receipts_
					if ( receipts_ )
					{
						msg.addExtension( new gloox::Receipt( gloox::Receipt::ReceiptType::Request ) );
					}
					m_client->send( msg );
					//(*it).second->invite( invitee_, reason_ );
				}
				break;
			}
		case MUCRoomInvitationType::Direct:
			{
				std::string reason = reason_;
				if ( reason == "" ) 
				{
					reason = "directmucinvitation";
				}
				gloox::Message msg( gloox::Message::MessageType::Normal, invitee_.bareJID(), reason ); // with <body>
				gloox::Tag* tag_x = new gloox::Tag( "x" );
				tag_x->setXmlns( XMLNS_X_CONFERENCE );
				tag_x->addAttribute( "jid", room_.bare() );
				DirectMucInvitation* x = new DirectMucInvitation( tag_x );
				msg.addExtension( x );
				// amp
				if ( amp_ )
				{
					gloox::AMP* amp = new gloox::AMP();
					gloox::AMP::Rule* rule1 = new gloox::AMP::Rule( "deliver", "notify", "direct" );
					gloox::AMP::Rule* rule2 = new gloox::AMP::Rule( "deliver", "notify", "stored" );
					amp->addRule( rule1 );
					amp->addRule( rule2 );
					msg.addExtension( amp );
				}
				// receipts
				if ( receipts_ )
				{
					msg.addExtension( new gloox::Receipt( gloox::Receipt::ReceiptType::Request ) );
				}
				m_client->send( msg );
				break;
			}
		default:
			break;
		}
	}

	void XmppStack::rejectMUCRoomInvitation( const gloox::JID& room_, const gloox::JID& invitor_, const std::string& reason_ )
	{
		if ( m_client )
		{
			// 实现离线拒绝：openfire可以发送带<body>的离线<message>，tigase不行
			gloox::Message msg( gloox::Message::Normal, room_.bare(), "abc" );
			msg.addExtension( new gloox::MUCRoom::MUCUser( gloox::MUCRoom::MUCUserOperation::OpDeclineTo, invitor_.bare(), reason_ ) );
			m_client->send( msg );
		}
	}

	void XmppStack::changeSelfNicknameInMUCRoom( const gloox::JID& room_, const std::string& nickname_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->setNick( nickname_ );
		}
	}

	void XmppStack::exitMUCRoom( const gloox::JID& room_, const std::string& reason_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->leave( reason_ );
		}
	}

	void XmppStack::requestMUCRoomVoice( const gloox::JID& room_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->requestVoice();
		}
	}

	void XmppStack::grantMUCRoomVoice( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->grantVoice( occupantNickname_, reason_ );
		}
	}
	
	void XmppStack::revokeMUCRoomVoice( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->revokeVoice( occupantNickname_, reason_ );
		}
	}

	void XmppStack::changeMUCRoomSubject( const gloox::JID& room_, const std::string& subject_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->setSubject( subject_ );
		}
	}

	void XmppStack::kickOutMUCRoom( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->kick( occupantNickname_, reason_ );
		}
	}

	void XmppStack::banOutMUCRoom( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->ban( occupantNickname_, reason_ );
		}
	}

	void XmppStack::sendMUCRoomMessage( const std::string& id_,
		                                const gloox::JID& room_, 
		                                const std::string& msg_, 
										const std::string& xhtml_ )
	{
		gloox::Message m( gloox::Message::Groupchat, room_.bare(), msg_ );
		if ( xhtml_ != "" )
		{
			XmlParser p;
			p.feed( xhtml_ );
			if ( p.tag() )
			{
				m.addExtension( new gloox::XHtmlIM( p.tag() ) );
			}
		}
		if ( id_ != "" )
		{
			m.setID( id_ );
		}
		m_client->send( m );
	}

	void XmppStack::sendMUCRoomPresence( const gloox::JID& room_, gloox::Presence::PresenceType type_, const std::string& status_ )
	{
		std::map<std::string, gloox::MUCRoom*>::iterator it = m_mUCRoomMap.find( room_.bare() );
		if ( it != m_mUCRoomMap.end() )
		{
			(*it).second->setPresence( type_, status_ );
		}
	}
	
	//void XmppStack::acceptIbbFt( const gloox::JID& initiator_, const std::string& sid_ )
	//{
	//	if ( !m_sIProfileFT ) 
	//	{ 
	//		return; 
	//	}

	//	m_sIProfileFT->acceptFT( initiator_, sid_, gloox::SIProfileFT::StreamType::FTTypeIBB );
	//}

	//void XmppStack::acceptS5bFt( const gloox::JID& initiator_, const std::string& sid_ )
	//{
	//	if ( !m_sIProfileFT ) 
	//	{ 
	//		return; 
	//	}

	//	m_sIProfileFT->acceptFT( initiator_, sid_, gloox::SIProfileFT::StreamType::FTTypeS5B );
	//}

	//void XmppStack::acceptOobFt( const gloox::JID& initiator_, const std::string& sid_ )
	//{
	//	if ( !m_sIProfileFT ) 
	//	{ 
	//		return; 
	//	}

	//	m_sIProfileFT->acceptFT( initiator_, sid_, gloox::SIProfileFT::StreamType::FTTypeOOB );
	//}

	void XmppStack::acceptFt( const gloox::JID& initiator_, const std::string& sid_, gloox::SIProfileFT::StreamType type_ )
	{
		if ( !m_sIProfileFT ) 
		{ 
			return; 
		}

		m_sIProfileFT->acceptFT( initiator_, sid_, type_ );
	}

	void XmppStack::rejectFt( const gloox::JID& initiator_, const std::string& sid_, const std::string& reason_ )
	{
		if ( !m_sIProfileFT ) 
		{ 
			return; 
		}

		m_sIProfileFT->declineFT( initiator_, sid_, gloox::SIManager::SIError::RequestRejected, reason_ );
	}

	int XmppStack::recvS5bFtData( const std::string& sid_ )
	{
		if ( m_bytestreamTrackMap[sid_] && gloox::Bytestream::StreamType::S5B == m_bytestreamTrackMap[sid_]->type() && m_bytestreamTrackMap[sid_]->connect() )
		{
			gloox::ConnectionError ce = gloox::ConnectionError::ConnNoError;
			while( gloox::ConnectionError::ConnNoError == ce )
			{
				ce = m_bytestreamTrackMap[sid_]->recv();
			}
			return 0;
		}
		return -1;
	}

	bool XmppStack::sendFtData( const std::string& sid_, void* data_, long size_ )
	{
		char* buf = new char[size_];
		memset( buf, 0, size_ );
		memcpy( buf, data_, size_ );
		std::string data;
		data.assign( buf, buf + size_ );
		
		bool ret = false;
		if ( m_bytestreamTrackMap[sid_] )
		{
			ret = m_bytestreamTrackMap[sid_]->send( data );
		}

		delete buf;
		buf = 0;

		return ret;
	}

	const std::string XmppStack::requestFt( const gloox::JID& to_, const FileMetadata& file_ )
	{
		if ( m_sIProfileFT )
		{
			return m_sIProfileFT->requestFT( to_, file_.name, file_.size, file_.hash, file_.desc, file_.date, file_.mimetype );
		}
		return "";
	}

	bool XmppStack::closeFtBytestream( const std::string& sid_ )
	{		
		m_bytestreamTrackMap[sid_]->close();

		gloox::Bytestream* bs = m_bytestreamTrackMap[sid_];
		if ( bs )
		{
			m_bytestreamTrackMap.erase( sid_ );
			return true;
		}
		return false;
	}

	bool XmppStack::isBytestreamOpened( const std::string& sid_ )
	{
		bool ret = false;
		gloox::Bytestream* bs = m_bytestreamTrackMap[sid_];
		if ( bs )
		{
			ret = bs->isOpen();
		}
		return ret;
	}

	void XmppStack::setIdentity( const std::string& category_, const std::string& type_, const std::string& name_ )
	{
		if ( m_client )
		{
			m_client->disco()->setIdentity( category_, type_, name_ );
		}
	}

	void XmppStack::addFeature( const std::string& feature_ )
	{
		if ( m_client )
		{
			const gloox::StringList& features = m_client->disco()->features();
			gloox::StringList::const_iterator it = features.begin();
			for ( ; it != features.end(); it++ )
			{
				if ( feature_ == (*it) )
				{
					return;
				}
			}
				
			m_client->disco()->addFeature( feature_ );
		}
	}

	void XmppStack::removeFeature( const std::string& feature_ )
	{
		if ( m_client )
		{
			m_client->disco()->removeFeature( feature_ );
		}
	}

} // end namespace kl