#ifndef KL_XMPPSTACK_H__
#define KL_XMPPSTACK_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/loghandler.h"
#include "../../gloox/src/connectionlistener.h"
#include "../../gloox/src/registrationhandler.h"
#include "../../gloox/src/rosterlistener.h"
#include "../../gloox/src/presencehandler.h"
#include "../../gloox/src/messagehandler.h"
#include "../../gloox/src/discohandler.h"
#include "../../gloox/src/mucroomhandler.h"
#include "../../gloox/src/mucroomconfighandler.h"
#include "../../gloox/src/mucinvitationhandler.h"
#include "../../gloox/src/pubsubresulthandler.h"
#include "../../gloox/src/siprofilefthandler.h"
#include "../../gloox/src/bytestreamdatahandler.h"
#include "../../gloox/src/siprofileft.h"

namespace gloox
{
	class Client;
	class Registration;
	class SOCKS5BytestreamServer;
}

namespace kl
{
	class XmppEntity;
	class DiscoInfo;
	class XmppCallback;
	class PubSubManager;
	class Microblog;
	class AvatarMetadata;

	struct MUCRoomConfig;
	struct FileMetadata;

	struct Entity;

	class IMailSMTPInfo;
	class Geoloc;

	typedef std::list<kl::XmppCallback*> XmppCallbackList;

	/**
	 * @brief 封装XMPP相关操作。
	 *
	 * C++示例:
     * @code
	   using namespace kl;
	 
	   int main()
	   {
	       gloox::JID jid( "a@b/c" );
	       std::string str_pwd = "000000";
	       std::string str_host = "172.30.3.78";
	       int port = 52220;
	 
	       XmppStack* xs = new XmppStack( jid, str_pwd, str_host, port );
	       MyXmppCallback* xc = new MyXmppCallback(); // MyXmppCallback类继承自XmppCallback类并复写其中的虚函数
	       xs->registerXmppCallback( xc );
	 
	       xs->login(); // 阻塞，如果登录成功，回调xc的onLoginSuccess()函数
	     
	       delete xc;
	       delete xs;
	       return 0;
	   }
	   @endcode
	 *
	 * CSharp示例:
     * @code
	 
	   public class MyXmppCallback : XmppCallback // MyXmppCallback类继承自XmppCallback类并复写其中的虚函数
	   {
	     public XmppStack XmppStack { get; set; }
	  
	     public override void onLog(LogLevel level_, LogArea area_, string log_)
	     {
	       ...
	     }
	     
	     public override ...
	   }
	  
       public class Program 
       {
         static void Main(string[] args)
	     {
	       JID jid = new JID("a@b/c");
	       XmppStack xs = new XmppStack(jid, "000000", "172.30.3.78", 52220);
	       MyXmppCallback mxc = new MyXmppCallback();
	       mxc.XmppStack = xs;
	       xs.registerXmppCallback(mxc);
	       xs.login();
	     }
	   }
	   @endcode
	 * 
	 * Java示例：
	 * @code
	   
	   public class MyXmppCallback extends XmppCallback { // MyXmppCallback类继承自XmppCallback类并复写其中的虚函数
	  
	     @Override
	     public void onLog(LogLevel level_, LogArea area_, String log_) {
	       ...
	     }
	   
	     @Override
	     ...
	  
	   }
	   
	   public class Main {
	   
	     static { // 注意加载顺序不能颠倒
	       System.loadLibrary("libeay32");
	       System.loadLibrary("ssleay32");
	       System.loadLibrary("gloox");
	       System.loadLibrary("KLXmpp");
	       System.loadLibrary("KLCppWrap");
	     }
	  
	     public static void main(String[] args) {
	       JID jid = new JID("a@b/c");
	       XmppStack xs = new XmppStack(jid, "000000", "172.30.3.78", 52220);
	       MyXmppCallback mxc = new MyXmppCallback();
	       xs.registerXmppCallback(mxc);
	       xs.login();
	     }
	   }
	   @endcode
	 *
	 * @author JI Yixuan
	 */
	class KLXMPP_API XmppStack : public gloox::LogHandler,
		                         public gloox::ConnectionListener,
								 public gloox::RosterListener,
								 public gloox::PresenceHandler,
								 public gloox::MessageHandler,
								 public gloox::RegistrationHandler,
								 public gloox::DiscoHandler,
								 public gloox::MUCRoomHandler,
								 public gloox::MUCRoomConfigHandler,
								 public gloox::MUCInvitationHandler,
								 public gloox::PubSub::ResultHandler,
								 public gloox::SIProfileFTHandler,
								 public gloox::BytestreamDataHandler
	{
	public:
		
		/**
		 * 构造函数。
		 * 
		 * @param jid_       XMPP登录帐号，格式：username @ server / resource。
		 * @param password_  XMPP登录密码。
		 * @param host_      XMPP服务器域名/地址，可缺省，默认：取jid_中server部分。
		 * @param port_      XMPP服务端口，可缺省，默认：5222。
		 */
		XmppStack( const gloox::JID& jid_, const std::string& password_, const std::string& host_, int port_ );
		
		/**
         * 虚析构函数。
         */
		virtual ~XmppStack();

#ifndef SWIG // SWIG过滤以下接口
					
		/* override gloox::LogHandler */
		void handleLog( gloox::LogLevel level_, gloox::LogArea area_, const std::string& message_ );
		
		/* override gloox::ConnectionListener */
		void onConnect();

		void onDisconnect( gloox::ConnectionError e_ );

		void onResourceBind( const std::string& resource_ );

		void onResourceBindError( const gloox::Error* error_ );

		void onSessionCreateError( const gloox::Error* error_ );
		
		bool onTLSConnect( const gloox::CertInfo& info_ );

		void onStreamEvent( gloox::StreamEvent event_ );

		/* override gloox::RosterListener */
		void handleItemAdded( const gloox::JID& jid_ );
		
		void handleItemSubscribed( const gloox::JID& jid_ );
		
		void handleItemRemoved( const gloox::JID& jid_ );
		
		void handleItemUpdated( const gloox::JID& jid_ );
		
		void handleItemUnsubscribed( const gloox::JID& jid_ );
		
		void handleRoster( const gloox::Roster& roster_ );
		
		void handleRosterPresence( const gloox::RosterItem& item_, 
			                       const std::string& resource_, 
			                       gloox::Presence::PresenceType presence_, 
								   const std::string& msg_ );
		
		void handleSelfPresence( const gloox::RosterItem& item_, 
			                     const std::string& resource_, 
								 gloox::Presence::PresenceType presence_, 
								 const std::string& msg_ );
		
		bool handleSubscriptionRequest( const gloox::JID& jid_, const std::string& msg_ );
		
		bool handleUnsubscriptionRequest( const gloox::JID& jid_, const std::string& msg_ );
		
		void handleNonrosterPresence( const gloox::Presence& presence_ );
		
		void handleRosterError( const gloox::IQ& iq_ );
		
		/* override gloox::PresenceHandler */
		void handlePresence( const gloox::Presence& presence_ );

		/* override gloox::MessageHandler */
		void handleMessage( const gloox::Message& msg_, gloox::MessageSession* session_ );

		/* override gloox::RegistrationHandler */
		void handleRegistrationFields( const gloox::JID& from_, int fields_, std::string instructions_ );

		void handleAlreadyRegistered( const gloox::JID& from_ );

		void handleRegistrationResult( const gloox::JID& from_, gloox::RegistrationResult regResult_ );

		void handleDataForm( const gloox::JID& from_, const gloox::DataForm& form_ );

		void handleOOB( const gloox::JID& from_, const gloox::OOB& oob_ );
		
		/* override gloox::DiscoHandler */
		void handleDiscoInfo( const gloox::JID& from_, const gloox::Disco::Info& info_, int context_ );
		
		void handleDiscoItems( const gloox::JID& from_, const gloox::Disco::Items& items_, int context_ );
		
		void handleDiscoError( const gloox::JID& from, const gloox::Error* error_, int context_ );
		
		/* override gloox::MUCRoomHandler */
		void handleMUCParticipantPresence( gloox::MUCRoom* room_, const gloox::MUCRoomParticipant participant_, const gloox::Presence& presence_ );
		
		void handleMUCMessage( gloox::MUCRoom* room_, const gloox::Message& msg_, bool priv_ );
		
		bool handleMUCRoomCreation( gloox::MUCRoom* room_ );
		
		void handleMUCSubject( gloox::MUCRoom* room_, const std::string& nick_, const std::string& subject_ );
		
		void handleMUCInviteDecline( gloox::MUCRoom* room_, const gloox::JID& invitee_, const std::string& reason_ );
		
		void handleMUCError( gloox::MUCRoom* room_, gloox::StanzaError error_ );
		
		void handleMUCInfo( gloox::MUCRoom* room_, int features_, const std::string& name_, const gloox::DataForm* infoForm_ );
		
		void handleMUCItems( gloox::MUCRoom* room_, const gloox::Disco::ItemList& items_ );

		/* override gloox::MUCRoomConfigHandler */
		void handleMUCConfigList( gloox::MUCRoom* room_, const gloox::MUCListItemList& items_, gloox::MUCOperation operation_ );

		void handleMUCConfigForm( gloox::MUCRoom* room_, const gloox::DataForm& form_ );

		void handleMUCConfigResult( gloox::MUCRoom* room_, bool success_, gloox::MUCOperation operation_ );

		void handleMUCRequest( gloox::MUCRoom* room_, const gloox::DataForm& form_ );

		/* overrride gloox::MUCInvitationHandler */
		void handleMUCInvitation( const gloox::JID& room_, 
			                      const gloox::JID& from_, 
								  const std::string& reason_, 
								  const std::string& body_, 
								  const std::string& password_,
                                  bool cont_, 
								  const std::string& thread_ );
		
		/* overrride  gloox::PubSub::ResultHandler */
		void handleItem( const gloox::JID& service_, const std::string& node_, const gloox::Tag* entry_ );
		
		void handleItems( const std::string& id_, 
			              const gloox::JID& service_, 
						  const std::string& node_, 
						  const gloox::PubSub::ItemList& itemList_, 
						  const gloox::Error* error_ = 0 );
		
		void handleItemPublication( const std::string& id_, 
			                        const gloox::JID& service_, 
									const std::string& node_, 
									const gloox::PubSub::ItemList& itemList_,
									const gloox::Error* error_ = 0 );
		
		void handleItemDeletion( const std::string& id_, 
			                     const gloox::JID& service_, 
			                     const std::string& node_, 
								 const gloox::PubSub::ItemList& itemList_, 
								 const gloox::Error* error_ = 0 );
        
		void handleSubscriptionResult( const std::string& id_,
			                           const gloox::JID& service_,
									   const std::string& node_,
									   const std::string& sid_,
									   const gloox::JID& jid_,
									   const gloox::PubSub::SubscriptionType subType_,
									   const gloox::Error* error_ = 0 );
		
		void handleUnsubscriptionResult( const std::string& id_, const gloox::JID& service_, const gloox::Error* error_ = 0 );
        
		void handleSubscriptionOptions( const std::string& id_,
                                        const gloox::JID& service_,
                                        const gloox::JID& jid_,
                                        const std::string& node_,
                                        const gloox::DataForm* options_,
										const gloox::Error* error_ = 0 );
        
		void handleSubscriptionOptionsResult( const std::string& id_,
                                              const gloox::JID& service_,
                                              const gloox::JID& jid_,
                                              const std::string& node_,
											  const gloox::Error* error_ = 0 );
        
		void handleSubscribers( const std::string& id_,
                                const gloox::JID& service_,
                                const std::string& node_,
								const gloox::PubSub::SubscriberList* list_,
								const gloox::Error* error_ = 0 );
        
		void handleSubscribersResult( const std::string& id_,
                                      const gloox::JID& service_,
                                      const std::string& node_,
                                      const gloox::PubSub::SubscriberList* list_,
                                      const gloox::Error* error_ = 0 );
        
		void handleAffiliates( const std::string& id_,
                               const gloox::JID& service_,
                               const std::string& node_,
                               const gloox::PubSub::AffiliateList* list_,
                               const gloox::Error* error_ = 0 );
        
		void handleAffiliatesResult( const std::string& id_,
                                     const gloox::JID& service_,
                                     const std::string& node_,
                                     const gloox::PubSub::AffiliateList* list_,
                                     const gloox::Error* error_ = 0 );
        
		void handleNodeConfig( const std::string& id_,
                               const gloox::JID& service_,
                               const std::string& node_,
                               const gloox::DataForm* config_,
                               const gloox::Error* error_ = 0 );
        
		void handleNodeConfigResult( const std::string& id_,
                                     const gloox::JID& service_,
                                     const std::string& node_,
                                     const gloox::Error* error_ = 0 );
        
		void handleNodeCreation( const std::string& id_,
                                 const gloox::JID& service_,
                                 const std::string& node_,
                                 const gloox::Error* error_ = 0 );
        
		void handleNodeDeletion( const std::string& id_,
                                 const gloox::JID& service_,
                                 const std::string& node_,
                                 const gloox::Error* error_ = 0 );
        
		void handleNodePurge( const std::string& id_,
                              const gloox::JID& service_,
                              const std::string& node_,
                              const gloox::Error* error_ = 0 );
        
		void handleSubscriptions( const std::string& id_,
                                  const gloox::JID& service_,
								  const gloox::PubSub::SubscriptionMap& subMap_,
                                  const gloox::Error* error_ = 0);
        
		void handleAffiliations( const std::string& id_,
                                 const gloox::JID& service_,
                                 const gloox::PubSub::AffiliationMap& affMap_,
                                 const gloox::Error* error_ = 0 );
        
		void handleDefaultNodeConfig( const std::string& id_,
                                      const gloox::JID& service_,
                                      const gloox::DataForm* config_,
                                      const gloox::Error* error_ = 0 );

		/* override gloox::SIProfileFTHandler */
		void handleFTRequest( const gloox::JID& from_, 
			                  const gloox::JID& to_, 
							  const std::string& sid_,
							  const std::string& name_, 
							  long size_, 
							  const std::string& hash_,
							  const std::string& date_, 
							  const std::string& mimetype_,
							  const std::string& desc_, 
							  int stypes_ );

		void handleFTRequestError( const gloox::IQ& iq_, const std::string& sid_ );

		void handleFTBytestream( gloox::Bytestream* bs_ );
		
		const std::string handleOOBRequestResult( const gloox::JID& from_, const gloox::JID& to_, const std::string& sid_ );

		/* override gloox::BytestreamDataHandler */
		void handleBytestreamData( gloox::Bytestream* bs_, const std::string& data_ );
		
		void handleBytestreamError( gloox::Bytestream* bs_, const gloox::IQ& iq_ );
		
		void handleBytestreamOpen( gloox::Bytestream* bs_ );
		
		void handleBytestreamClose( gloox::Bytestream* bs_ );

#endif // end #ifndef SWIG	

#pragma region 配置
		
		/**
         * 返回当前设置的XMPP帐号。
		 *
		 * @return 当前XMPP帐号。
         */
		const gloox::JID& jid() const;

		/**
         * 返回当前设置的XMPP密码。
		 *
		 * @return 当前XMPP密码。
		 */
		const std::string& password() const;
		
		/**
         * 返回当前设置的XMPP服务器域名/地址。
		 *
		 * @return 当前XMPP服务器域名/地址。
         */
		const std::string& host() const;
		
		/**
         * 返回当前设置的XMPP服务端口。
		 *
		 * @return 当前XMPP服务端口。
         */
		int port() const;
		
		/**
		 * 注册一个观察者，不允许插入空指针或重复指针。
		 *
		 * @param xmppCallback_ 观察者。
		 * @return 注册成功 返回 @b true, 否则 返回 @b false 。
		 */
		bool registerXmppCallback( XmppCallback* xmppCallback_ );
		
		/**
		 * 移除一个观察者。
		 *
		 * @param xmppCallback_ 观察者。
		 */
		void removeXmppCallback( XmppCallback* xmppCallback_ );

#ifndef SWIG // SWIG过滤以下接口
		/**
		 * 是否在登录成功后检测并修正微博节点配置。（弃用）
		 *
		 * @return 需要检测 返回 @b true, 不检测 返回 @b false.
		 */
		bool isCheckMicrobolgNodeConfig() const { return m_isCheckMicrobolgNodeConfig; }

		/**
		 * 使登录成功后检测并修正微博节点配置。（弃用）
		 */
		void enableCheckMicrobolgNodeConfig() { m_isCheckMicrobolgNodeConfig = true; }

		/**
		 * 使登录成功后不检测微博节点配置。（弃用）
		 */
		void disableCheckMicrobolgNodeConfig() { m_isCheckMicrobolgNodeConfig = false; }

		/**
		 * 是否在登录成功后检测并修正头像数据节点配置。（弃用）
		 *
		 * @return 需要检测 返回 @b true, 不检测 返回 @b false.
		 */
		bool isCheckAvatarDataNodeConfig() const { return m_isCheckAvatarDataNodeConfig; }

		/**
		 * 使登录成功后检测并修正头像数据节点配置。（弃用）
		 */
		void enableCheckAvatarDataNodeConfig() { m_isCheckAvatarDataNodeConfig = true; }

		/**
		 * 使登录成功后不检测头像数据节点配置。（弃用）
		 */
		void disableCheckAvatarDataNodeConfig() { m_isCheckAvatarDataNodeConfig = false; }
#endif // end #ifndef SWIG

#pragma endregion


#pragma region 登录与登出
		
		/**
		 * 登录XMPP服务器。（阻塞模式）
		 */
		void login();
        
        /**
		 * 检测是否已经登录。
		 */
        bool isLogined();
		
		/**
		 * 登出XMPP服务器。
		 */
		void logout();
		
#pragma endregion
		
#pragma region 昵称

		/**
		 * 发布昵称。
		 *
		 * @param nickname_ 新昵称。
		 */
		bool publishNickname( const std::string& nickname_ );

#pragma endregion

#ifndef SWIG // SWIG过滤以下接口
#pragma region 头像（弃用）

		/**
		 * 发布头像。
		 *
		 * @param id_      头像ID，如果ID为空，会自动生成一个。
		 * @param base64_  头像数据（Base64编码格式）。
		 * @param bytes_   头像数据字节数。
		 * @param type_    头像类型，比如image/png。
		 * @param height_  头像高度（像素），可缺省，默认：-1，表示未知。
		 * @param width_   头像宽度（像素），可缺省，默认：-1，表示未知。
		 * @param url_     外部链接。
		 * @return         实际设置的头像ID。
		 */
		const std::string publishAvatar( const std::string& id_,
			                             const std::string& base64_,
			                             int bytes_,
										 const std::string& type_,
										 int height_ = -1,
										 int width_ = -1,
										 const std::string& url_ = "" );
		
		/**
		 * 加载头像。
		 *
		 * @param to_ 对方JID。
		 * @param id_ 头像ID。
		 */
		void loadAvatar( const gloox::JID& to_, const std::string& id_ );
#pragma endregion


#pragma region 通知（弃用）
		
		/**
		 * 通知个人信息已修改。（弃用）
		 *
		 * @return 请求ID。
		 */
		const std::string notifyPersonalInfoChanged();
		
		/**
		 * 通知通讯录已修改。（弃用）
		 *
		 * @return 请求ID。
		 */
		const std::string notifyAddressBookChanged();

#pragma endregion
#endif // end #ifndef SWIG


#pragma region 消息与状态
				
		/** 
		 * 发送双人聊天消息。
		 *
		 * @param id_       XMPP消息ID。
		 * @param to_       对方JID。
		 * @param body_     纯文本内容。
		 * @param xhtml_    富文本（XHtml格式）内容，可缺省。
		 * @param subject_  主题，可缺省。
		 * @param thread_   会话ID，可缺省。
		 * @param amp_      是否需要服务器确认收到，默认：否。
		 * @param receipts_ 是否需要对方确认收到，默认：否。
		 */
		bool sendChatMessage( const std::string& id_,
			                  const gloox::JID& to_, 
			                  const std::string& body_,
							  const std::string& xhtml_ = "",
							  const std::string& subject_ = "", 
							  const std::string& thread_ = "",
							  bool amp_ = false,
							  bool receipts_ = false );

		/** 
		 * 发送即时邮件消息。
		 *
		 * @param id_            XMPP消息ID。
		 * @param to_            对方JID。
		 * @param iMailSMTPInfo_ 邮件信息。
		 * @param receipts_      是否需要对方确认收到，默认：否。
		 */
		bool sendIMailMessage( const std::string& id_, 
			                   const gloox::JID& to_, 
							   const IMailSMTPInfo& iMailSMTPInfo_, 
							   bool receipts_ = false );

		/** 
		 * 发送地理位置消息。
		 *
		 * @param id_         XMPP消息ID。
		 * @param to_         对方JID。
		 * @param geolocInfo_ 地理位置信息。
		 */
		bool sendLocationMessage( const std::string& id_, const gloox::JID& to_, const Geoloc& geoloc );

		/**
		 * 发送回执消息。
		 * @param id_        XMPP消息ID。
		 * @param to_        对方JID。
		 * @param receiptId_ 确认收到的消息ID。
		 */
		bool sendReceiptMessage( const std::string& id_, const gloox::JID& to_, const std::string& receiptId_ ); 
		
		/** 
		 * 发送震屏消息。
		 *
		 * @param to_ 对方JID。
		 */
		bool sendAttentionMessage( const gloox::JID& to_ );
		
		/** 
		 * 修改自己的在线状态。
		 *
		 * @param type_     在线状态类型。
		 * @param status_   在线状态文字说明，可缺省。
		 * @param priority_ 状态优先级，可缺省，默认：0。
		 */
		bool sendPresence( gloox::Presence::PresenceType type_, const std::string& status_ = "", int priority_ = 0 );

#pragma endregion


#pragma region 花名册

		/**
		 * 添加花名册项。
		 * 
		 * @param jid_       对方JID。
		 * @param name_      备注名称。
		 * @param groups_    分组。
		 * @param isHidden_  是否设为隐藏，即属于Hidden分组，默认：否。
		 * @param subscribe_ 是否订阅对方在线状态，默认：是。
		 * @param msg_       附带信息。
		 */
		bool addRosterItem( const gloox::JID& jid_, 
			                const std::string& name_, 
							const gloox::StringList& groups_, 
							bool subscribe_ = true, 
							const std::string& msg_ = "" );

		bool moveRosterItemToGroup( const gloox::JID& jid_, const std::string& group_ );

		bool moveRosterItemToGroups( const gloox::JID& jid_, const gloox::StringList& groups_ );
		
		bool copyRosterItemToGroup( const gloox::JID& jid_, const std::string& group_ );

		bool copyRosterItemToGroups( const gloox::JID& jid_, const gloox::StringList& groups_ );

		bool removeRosterItemFromGroup( const gloox::JID& jid_, const std::string& group_ );

		bool removeRosterItemFromGroups( const gloox::JID& jid_, const gloox::StringList& groups_ );

		bool removeRosterItemFromAllGroups( const gloox::JID& jid_ );

		bool changeRosterItemName( const gloox::JID& jid_, const std::string& name_ );

		/**
		 * 更新花名册项。
		 * 
		 * @param jid_       裸JID。
		 * @param name_      备注名称。
		 * @param groups_    分组。
		 */
		bool updateRosterItem( const gloox::JID& jid_, const std::string& name_, const gloox::StringList& groups_ );

		/**
		 * 删除花名册项。
		 * 
		 * @param jid_       裸JID。
		 */
		bool deleteRosterItem( const gloox::JID& jid_ );
		
		/**
		 * 订阅花名册项（的状态），自动添加花名册项。
		 * 
		 * @param jid_ 裸JID。
		 * @param msg_ 附加信息。
		 */
		bool subscribeRosterItem( const gloox::JID& jid_, const std::string& msg_ = "" );
		
		/**
		 * 批准订阅请求。
		 * 
		 * @param jid_                 裸JID。
		 * @param autoSubscribeRemote_ 是否自动订阅对方（的状态）。
		 */
		bool approveSubscription( const gloox::JID& jid_, bool autoSubscribeRemote_ = true );
		
		/**
		 * 拒绝订阅请求。
		 * 
		 * @param jid_                 裸JID。
		 * @param autoDeleteRosterItem 是否自动删除花名册项。
		 */
		bool denySubscription( const gloox::JID& jid_, bool autoDeleteRosterItem = true );
		
#pragma endregion


#pragma region 微博
		
		/** 
		 * 发布微博。
		 *
		 * @param microblog_ 微博。
		 * @return 请求ID。
		 */
		const std::string publish( const Microblog& microblog_ );

		/** 
		 * 删除微博。
		 *
		 * @param id_ 微博ID。
		 * @return 请求ID。
		 */
		const std::string deleteMicroblog( const std::string& id_ );

#ifndef SWIG // SWIG过滤以下接口
		/** 
		 * 删除自己的微博节点。（弃用）
		 */
		void deleteSelfMicroblogNode();

		/** 
		 * 请求加载所有微博。（弃用）
		 * 
		 * @param to_ 对方JID。
		 */
		void requestMicroblogs( const gloox::JID& to_ );
#endif // end #ifndef SWIG

#pragma endregion


#pragma region 群聊
		/**
		 * 创建群聊房间，房间的JID格式：id @ service / nickname。如果房间创建成功，自动提交配置。
		 *
		 * @param room_   房间JID，如果没有nickname会将登录帐号中的username作为nickname。
		 * @param config_ 房间配置。 
		 */
		void createMUCRoom( const gloox::JID& room_, const MUCRoomConfig& config_ );

		/**
		 * 销毁群聊房间。
		 *
		 * @param room_ 房间JID。
		 */
		void destroyMUCRoom( const gloox::JID& room_ );

		/**
		 * 修改群聊房间的成员列表。
		 *
		 * @param room_     房间JID。
		 * @param members_  成员JID列表。
		 */
		void modifyMUCRoomMemberList( const gloox::JID& room_, const gloox::StringList& members_ );
		
		/**
		 * 请求获取群聊房间成员列表。
		 *
		 * @param room_     房间JID。
		 */
		void requestMUCRoomMemberList( const gloox::JID& room_ );

		/**
		 * 修改群聊房间的所有者列表。
		 *
		 * @param room_    房间JID。
		 * @param owners_  所有者JID列表。
		 */
		void modifyMUCRoomOwnerList( const gloox::JID& room_, const gloox::StringList& owners_ );

		/**
		 * 请求获取群聊房间所有者列表。
		 *
		 * @param room_     房间JID。
		 */
		void requestMUCRoomOwnerList( const gloox::JID& room_ );

		/**
		 * 进入群聊房间。
		 *
		 * @param room_         房间JID。
		 * @param presType_     进入房间后的状态，可缺省，默认：在线。
		 * @param presStatus_   进入房间后的状态说明，可缺省。
		 * @param presPriority_ 进入房间后的状态优先级，可缺省，默认：0。
		 * @param password_     房间密码，可缺省。
		 */
		void enterMUCRoom( const gloox::JID& room_,
						   gloox::Presence::PresenceType presType_ = gloox::Presence::Available,
						   const std::string& presStatus_ = "",
						   int presPriority_ = 0,
						   const std::string& password_ = "",
						   bool requestHistory_ = true );
				
		/**
		 * 配置群聊房间。
		 *
		 * @param room_   房间JID，可以是裸JID。
		 * @param config_ 配置参数。
		 */
		void configMUCRoom( const gloox::JID& room_, const MUCRoomConfig& config_ );

		/**
		 * 查询群聊房间信息。
		 *
		 * @param room_ 房间JID，可以是裸JID。
		 */
		void queryMUCRoomInfo( const gloox::JID& room_ );
		
		/**
		 * 查询群聊房间配置。
		 *
		 * @param room_ 房间JID，可以是裸JID。
		 */
		void queryMUCRoomConfig( const gloox::JID& room_ );
		
		/**
		 * 授予用户（群聊房间的）Member权限。（弃用）
		 *
		 * @param room_ 房间JID。
		 * @param user_ 被授予者JID。
		 */
		void grantMUCRoomMembership( const gloox::JID& room_, const gloox::JID& user_ );
		
		/**
		 * 授予用户（群聊房间的）Member权限。
		 *
		 * @param room_     房间JID。
		 * @param nickname_ 被授予者房间内昵称。
		 * @param reason_   理由，可缺省。
		 */
		void grantMUCRoomMembership( const gloox::JID& room_, const std::string& nickname_, const std::string& reason_ = "" );
		
		/**
		 * 剥夺用户（群聊房间的）Member权限。
		 *
		 * @param room_ 房间JID。
		 * @param user_ 被剥夺者JID。
		 */
		void revokeMUCRoomMembership( const gloox::JID& room_, const gloox::JID& user_ );

		/**
		 * 剥夺用户（群聊房间的）Owner权限。
		 *
		 * @param room_ 房间JID。
		 * @param user_ 被剥夺者JID。
		 */
		void revokeMUCRoomOwnership( const gloox::JID& room_, const gloox::JID& user_ );
		
		/**
		 * 离开群聊房间。
		 *
		 * @param room_   房间JID。
		 * @param reason_ 理由，可缺省。
		 */
		void exitMUCRoom( const gloox::JID& room_, const std::string& reason_ = "" );

		/**
		 * 邀请其他用户进入群聊房间。
		 *
		 * @param room_    房间JID，可以是裸JID。
		 * @param invitee_ 被邀请者JID。
		 * @param invite_
		 * @param reason_  邀请理由，可缺省。
		 */
		void inviteIntoMUCRoom( const gloox::JID& room_, 
			                    const gloox::JID& invitee_, 
								MUCRoomInvitationType type_ = MUCRoomInvitationType::Mediated, 
								const std::string& reason_ = "",
								bool amp_ = false,
								bool receipts_ = false );

		/**
		 * 拒绝加入群聊房间的邀请。
		 *
		 * @param room_    房间JID，可以是裸JID。
		 * @param invitor_ 邀请者JID。
		 * @param reason_  拒绝理由，可缺省。
		 */
		void rejectMUCRoomInvitation( const gloox::JID& room_, const gloox::JID& invitor_, const std::string& reason_ = "" );
		
		/**
		 * 修改（自己在）群聊房间内的昵称。
		 *
		 * @param room_     房间JID。
		 * @param nickname_ （自己的）新昵称。
		 */
		void changeSelfNicknameInMUCRoom( const gloox::JID& room_, const std::string& nickname_ );

		/**
		 * 请求发言。
		 */
		void requestMUCRoomVoice( const gloox::JID& room_ );
		
		/**
		 * 授予住客（群聊房间内的）发言权。
		 *
		 * @param room_             房间JID。
		 * @param occupantNickname_ 住客昵称。
		 * @param reason_           理由，可缺省。
		 */
		void grantMUCRoomVoice( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ = "" );

		/**
		 * 剥夺住客（群聊房间内的）发言权。
		 *
		 * @param room_             房间JID。
		 * @param occupantNickname_ 住客昵称。
		 * @param reason_           理由，可缺省。
		 */
		void revokeMUCRoomVoice( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ = "" );

		/**
		 * 修改群聊房间的主题。
		 *
		 * @param room_    房间JID。
		 * @param subject_ 新主题。
		 */
		void changeMUCRoomSubject( const gloox::JID& room_, const std::string& subject_ );

		/**
		 * 将一名住客踢出群聊房间。
		 *
		 * @param room_             房间JID。
		 * @param occupantNickname_ 被踢住客（房间内的）昵称。
		 * @param reason_           理由，可缺省。
		 */
		void kickOutMUCRoom( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ = "" );

		/**
		 * 将一名用户加入群聊房间的黑名单。
		 *
		 * @param room_             房间JID。
		 * @param occupantNickname_ 被禁止住客（房间内的）昵称。
		 * @param reason_           理由，可缺省。
		 */
		void banOutMUCRoom( const gloox::JID& room_, const std::string& occupantNickname_, const std::string& reason_ = "" );

		/**
		 * 发送群聊消息（到群聊房间）。
		 *
		 * @param room_  房间JID。
		 * @param msg_   消息。
		 * @param xhtml_ xhtml格式文本。
		 */
		void sendMUCRoomMessage( const std::string& id_,
			                     const gloox::JID& room_, 
			                     const std::string& msg_, 
								 const std::string& xhtml_ = "" );

		/**
		 * 发送状态（到群聊房间）。
		 *
		 * @param room_   房间JID。
		 * @param type_   进入房间后的状态，可缺省，默认：在线。
		 * @param status_ 进入房间后的状态说明，可缺省。
		 */
		void sendMUCRoomPresence( const gloox::JID& room_, gloox::Presence::PresenceType type_ = gloox::Presence::Available, const std::string& status_ = "" );

#pragma endregion


#pragma region 文件传输
		/**
		 * 批准文件传输。
		 *
		 * @param initiator_  文件发送方JID。
		 * @param sid_        流ID。
		 * @param type_       流类型：IBB、S5B、OOB。
		 */
		void acceptFt( const gloox::JID& initiator_, const std::string& sid_, gloox::SIProfileFT::StreamType type_ );

		/**
		 * 拒绝文件传输。
		 *
		 * @param initiator_  文件发送方JID。
		 * @param sid_        流ID。
		 * @param reason_     理由。
		 */
		void rejectFt( const gloox::JID& initiator_, const std::string& sid_, const std::string& reason_ = "" ); 

		/**
		 * 接收S5B流数据。（阻塞）
		 *
		 * @param sid_        流ID。
		 */
		int recvS5bFtData( const std::string& sid_ );
		
		/**
		 * 发送文件数据。
		 *
		 * @param sid_  流ID。
		 * @param data_ 数据。
		 * @param size_ 数据大小。
		 */
		bool sendFtData( const std::string& sid_, void* data_, long size_ );
		
		/**
		 * 请求传输文件。
		 *
		 * @param to_   对方JID。
		 * @param file_ 文件元数据。
		 */
		const std::string requestFt( const gloox::JID& to_, const FileMetadata& file_ );
		
		/**
		 * 关闭文件传输字节流。
		 *
		 * @param sid_ 流ID。
		 */
		bool closeFtBytestream( const std::string& sid_ );
		
		/**
		 * 判断字节流是否已经打开。
		 *
		 * @param sid_ 流ID。
		 */
		bool isBytestreamOpened( const std::string& sid_ );
		
#pragma endregion

#ifndef SWIG // SWIG过滤以下接口
#pragma region 调试用
		/**
		 * 直接发送XML报文。
		 */
		void sendXml( const std::string& xml_ );

		/**
		 *  发送心跳包。
		 */
		void ping();

		void discoItems( const gloox::JID& to_, const std::string& node_ = "" );

		void discoInfo( const gloox::JID& to_, const std::string& node_ = "" );

		/**
		 * 请求获取花名册。
		 */
		bool getRoster();

		void createPubSubNode( const gloox::JID& service_, const std::string& node_ );
		
		const std::string publishUserTune( const std::string& title_ );
		
		void configUserTuneNode();
		
		void getUserTuneNodeConfig();
		
		void deleteUserTuneNode();

		/**
		 * 请求获取注册信息。
		 */
		bool requestRegistrationFields();
		
		void setIdentity( const std::string& category_, const std::string& type_, const std::string& name_ );

		void addFeature( const std::string& feature_ );
	
		void removeFeature( const std::string& feature_ );

#pragma endregion
#endif // end #ifndef SWIG

#ifdef KL_XMPPSTACK_TEST
    public:
#else
	protected:
#endif 

		/**
		 * 初始化。
		 */
		void init();

		gloox::Client*                 m_client;
		gloox::Registration*           m_registration;
		gloox::SIProfileFT*            m_sIProfileFT;
		gloox::SOCKS5BytestreamServer* m_sOCKS5BytestreamServer;
		kl::PubSubManager*             m_pubSubManager;
		
		// 观察者集合
		std::list<XmppCallback*> m_xmppCallbacks;

		// XMPP实体集合 <JID，实体>
		std::map<std::string, XmppEntity*> m_xmppEntityTrackMap;
		
		// 发布微博请求集合 <请求ID，微博ID>
		std::map<std::string, std::string> m_publishMicroblogTrackMap;
		
		// 删除微博请求集合 <请求ID，微博ID>
		std::map<std::string, std::string> m_deleteMicroblogTrackMap;
		
		// 字节流对象集合 <SID, 字节流>
		std::map<std::string, gloox::Bytestream*> m_bytestreamTrackMap;

		// 是否在登录成功后获取并修正微博节点配置
		bool m_isCheckMicrobolgNodeConfig;

		// 是否在登录成功后获取并修正头像数据节点配置
		bool m_isCheckAvatarDataNodeConfig;

		// 发布昵称请求集合 <请求ID，昵称>
		std::map<std::string, std::string> m_publishNicknameTrackMap;

		// 发布头像数据的请求集合 <请求ID，头像元数据>
		std::map<std::string, AvatarMetadata*> m_publishAvatarDataTrackMap;

		// 发布头像元数据的请求集合 <请求ID，头像ID>
		std::map<std::string, std::string> m_publishAvatarMetadataTrackMap;

		// 通知个人信息已修改请求集合 <请求ID>
		std::list<std::string> m_notifyPersonalInfoChangedTrackList;
		
		// 通知通讯录已修改请求集合 <请求ID>
		std::list<std::string> m_notifyAddressBookChangedTrackList;

		// 房间集合，<房间JID, 房间>
		std::map<std::string, gloox::MUCRoom*> m_mUCRoomMap;

		// 创建房间请求集合，<房间JID, 房间配置>
		std::map<std::string, gloox::DataForm*> m_createMUCRoomTrackMap;
								
		
		
		
		
		




		/* 实体能力 jid-ver FIX: change a name may be better */
		gloox::StringMap     m_verificationMap;
		/* 实体能力 ver-discoinfo FIX: change a name may be better */
		std::map<std::string, kl::DiscoInfo*> m_discoInfoMap;
		// FIX: change a name may be better
		gloox::StringList    m_pendingMucInvitations;


		// 实体能力映射表，包括服务器、对方客户端
		std::map<std::string, kl::Entity*> m_entityMap;

	}; // end class KLXMPP_API XmppStack

} // end namespace kl

#endif // KL_XMPPSTACK_H__