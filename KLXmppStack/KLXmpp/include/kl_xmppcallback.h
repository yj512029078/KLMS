#ifndef KL_XMPPCALLBACK_H__
#define KL_XMPPCALLBACK_H__

#include "../../gloox/src/message.h"

namespace kl
{
	struct MUCRoomInfo;
	struct AddressBookInfo;
	class BytestreamData;

	class BackgroundInfo;

	/**
	 * @brief XMPP回调，作为XmppStack的观察者。
	 *
	 * @author JI Yixuan
	 */
	class KLXMPP_API XmppCallback
	{
	public:

		/**
		 * 虚析构函数。
		 */
		virtual ~XmppCallback() {}

		#pragma region 日志
		/** 
		 * 告知记录了一条日志。
		 *
		 * @param level_ 日志等级。
		 * @param area_  日志类别。
		 * @param log_   日志内容。
		 */
		virtual void onLog( gloox::LogLevel level_, gloox::LogArea area_, const std::string& log_ ) {}
		#pragma endregion

		#pragma region 登录与登出
		/**
		 * 告知登录时使用了无效的XMPP帐号(JID)，JID格式：username @ server / resource，username和server部分不能为空。
		 */
		virtual void onInvaildJID() {}
		
		/**
		 * 告知登录时使用了无效的密码，密码不能为空。
		 */
		virtual void onInvaildPassword() {}
		
		/**
		 * 告知TCP连接成功。
		 */
		virtual void onTcpConnSuccess() {}
		
		/**
		 * 告知TCP连接失败。
		 */
		virtual void onTcpConnFailed( gloox::ConnectionError error_ ) {}		
		
		/**
		 * 告知正在协商数据加密策略。
		 */
		virtual void onNegotiatingEncryption() {}
		
		/**
		 * 告知正在协商数据压缩策略。
		 */
		virtual void onNegotiatingCompression() {}		
		
		/**
		 * 告知正在身份验证。
		 */
		virtual void onAuthenticating() {}
		
		/** 
		 * 告知身份验证失败。
		 */
		virtual void onAuthFailed() {}
		
		/** 
		 * 告知正在资源绑定。
		 */
		virtual void onBindingResource() {}
		
		/** 
		 * 告知正在建立会话。
		 *
		 */
		virtual void onCreatingSession() {}
		
		/**
		 * 告知正在加载花名册。
		 */
		virtual void onLoadingRoster() {}
		
		/** 
		 * 告知登录XMPP服务器成功。
		 */
		virtual void onLoginSuccess() {}

		/** 
		 * 告知登出XMPP服务器成功。
		 */
		virtual void onLogoutSuccess() {}
		#pragma endregion
		
		#pragma region 消息与状态
		/**
		 * 告知接收到联系人的状态。
		 * 
		 * @param item_     联系人。
		 * @param resource_ 资源。
		 * @param presence_ 状态类型。
		 * @param msg_      附带信息。
		 */
		virtual void onRecvRosterPresence( const gloox::RosterItem& item_, 
			                               const std::string& resource_, 
										   gloox::Presence::PresenceType type_, 
										   const std::string& msg_ ) {}
				
		/**
		 * 告知接收到一条单人聊天消息。 
		 * 
		 * @param msg_       消息对象。
		 * @param xhtml_     富文本（XHtml格式）。
		 * @param receipts_  是否需要回执。
		 */
		virtual void onRecvChatMessage( const gloox::Message& msg_, const std::string& xhtml_, bool receipts_ ) {}

		/**
		 * 告知接收到震屏。
		 *
		 * @param from_ 对方JID。
		 */
		virtual void onRecvAttentionMessage( const gloox::JID& from_ ) {}

		/**
		 * 告知接收到一条即时邮件消息。 
		 * 
		 * @param id_            XMPP消息ID。
		 * @param from_          发送人JID。
		 * @param iMailSMTPInfo_ 即时邮件信息。
		 * @param receipts_      是否需要回执。
		 */
		virtual void onRecvIMailMessage( const std::string& id_, const gloox::JID& from_, const kl::IMailSMTPInfo& iMailSMTPInfo_, bool receipts_ ) {}

		/**
		 * 告知接收到一条地理位置消息。 
		 * 
		 * @param id_            XMPP消息ID。
		 * @param from_          发送人JID。
		 * @param iMailSMTPInfo_ 即时邮件信息。
		 */
		virtual void onRecvLocationMessage( const std::string& id_, const gloox::JID& from_, const Geoloc& geoloc_ ) {}

		/**
		 * 告知接收到一条回执消息。
		 * 
		 * @param id_        XMPP消息ID。
		 * @param from_      对方JID。
		 * @param receiptId_ 被回执消息ID。
		 */
		virtual void onRecvReceiptMessage( const std::string& id_, const gloox::JID& from_, const std::string& receiptId_ ) {}
		#pragma endregion
		
		#pragma region 花名册
		/** 
		 * 告知获取到花名册。
		 * 参考协议：RFC6121(XMPP-IM) 2.1.4.Roster Result
		 *
		 * @param roster_ 花名册对象。
		 */
		virtual void onRecvRoster( const std::map<std::string, gloox::RosterItem*>& roster_ ) {}
		
		/**
		 * 告知花名册中新添加了一项。
		 * @param item_     JID。
		 * @param nickname_ 昵称。
		 * @param groups_   所在组。
		 */
		virtual void onRosterItemAdded( gloox::RosterItem* item_ ) {}

		/** 
		 * 当花名册中更新了一项。
		 * @param jid_      对方JID。
		 * @param nickname_ 对方昵称（由我设置）。
		 * @param groups_   对方所在联系人组（由我设置）。
		 * @param type_     我对对方的订阅状态。
		 */
		virtual void onRosterItemUpdated( gloox::RosterItem* item_ ) {}
		
		/**
		 * 告知从花名册中删除了一项。
		 * @param item_ 对方JID。
		 */
		virtual void onRosterItemRemoved( gloox::RosterItem* item_ ) {}
		
		/**
		 * 告知已订阅对方状态。
		 * 
		 * @param jid_ 对方JID。
		 */
		virtual void onRosterItemSubscribed( gloox::RosterItem* item_ ) {}
		
		/**
		 * 告知对方拒绝/取消我对他的状态订阅。
		 * 
		 * @param jid_ 对方JID。
		 */
		virtual void onRosterItemUnsubscribed( gloox::RosterItem* item_ ) {}
				
		/** 
		 * 告知接收到状态订阅请求。
		 * 
		 * @param jid_ 请求方JID.
		 * @param msg_ 附加信息.
		 * @return     返回 @b true 接受, @b false 拒绝。
		 */
		virtual void onRecvSubscriptionRequest( const gloox::JID& jid_, const std::string& msg_ ) {}
		#pragma endregion
		
		#pragma region 注册信息
		/** 
		 * 获取到用户的注册信息。
		 *
		 * @param username_ 用户名。
		 * @param name      注册名，比如实名。
		 * @param email_    电子邮箱。
		 */
		virtual void onRetrieveRegistrationInfo( const std::string& username_, const std::string& name_, const std::string& email_ ) {}
		#pragma endregion
		
		#pragma region 昵称
		/**
		 * 告知昵称发布成功。
		 *
		 * @param nickname_ （请求发布的）昵称。
		 */
		virtual void onPublishNicknameSuccess( const std::string& nickname_ ) {}
		
		/**
		 * 告知昵称发布失败。
		 *
		 * @param nickname_ （请求发布的）昵称。
		 */
		virtual void onPublishNicknameFailed( const std::string& nickname_ ) {}
		
		/**
		 * 
		 *
		 * @param from_     通知发布者JID。
		 * @param nickname_ 发布的昵称。
		 */
		virtual void onRecvNickname( const gloox::JID& from_, const std::string& nickname_ ) {}
		#pragma endregion
		
		#pragma region 头像
		
		/**
		 * 告知头像发布成功。
		 *
		 * @param id_ 头像ID。
		 */
		virtual void onPublishAvatarSuccess( const std::string& id_ ) {}
		
		/**
		 * 告知头像发布失败。
		 *
		 * @param id_ 头像ID。
		 */
		virtual void onPublishAvatarFailed( const std::string& id_ ) {}
		
		/**
		 * 告知接收头像元数据。
		 *
		 * @param from_     对方JID。
		 * @param metadata_ 头像元数据。
		 */
		virtual void onRecvAvatarMetadata( const gloox::JID& from_, const AvatarMetadata& metadata_ ) {}
		
		/**
		 * 告知接收到头像数据。
		 *
		 * @param from_   对方JID。
		 * @param id_     头像ID。
		 * @param base64_ 头像数据（Base64格式）。
		 */
		virtual void onRecvAvatarData( const gloox::JID& from_, const std::string& id_, const std::string& base64_ ) {}

		/**
		 * 告知加载头像成功。
		 *
		 * @param from_   对方JID。
		 * @param id_     头像ID。
		 * @param base64_ 头像数据（Base64格式）。
		 */
		virtual void onLoadAvatarSuccess( const gloox::JID& from_, const std::string& id_, const std::string& base64_ ) {}

		/**
		 * 告知加载头像失败。
		 *
		 * @param from_   对方JID。
		 * @param id_     头像ID。
		 */
		virtual void onLoadAvatarFailed( const gloox::JID& from_, const std::string& id_ ) {}

		#pragma endregion
		
		#pragma region 通知
		/**
		 * 告知（自己）通知个人信息已修改成功。
		 * 
		 * @return uid_ 请求ID。
		 */
		virtual void onNotifyPersonalInfoChangedSuccess( const std::string& uid_ ) {}
		
		/**
		 * 告知（自己）通知个人信息已修改失败。
		 *
		 * @param uid_ 请求ID。
		 */
		virtual void onNotifyPersonalInfoChangedFailed( const std::string& uid_ ) {}
		
		/**
		 * 告知接收到（别人的）个人信息已修改通知。
		 *
		 * @param from_ 通知发布者JID。
		 */
		virtual void onRecvPersonalInfoChangedNotification( const gloox::JID& from_ ) {}

		/**
		 * 告知（自己）通知通讯录已修改成功。
		 *
		 * @param uid_ 请求ID。
		 */
		virtual void onNotifyAddressBookChangedSuccess( const std::string& uid_ ) {}

		/**
		 * 告知（自己）通知通讯录已修改失败。
		 *
		 * @param uid_ 请求ID（即<iq id=''>）。
		 */
		virtual void onNotifyAddressBookChangedFailed( const std::string& uid_ ) {}
		
		/**
		 * 告知接收到（别人的）通讯录已修改通知。
		 *
		 * @param from_ 通知发布者JID。
		 */
		virtual void onRecvAddressBookChangedNotification( const gloox::JID& from_, const AddressBookInfo& addressBookInfo_ ) {}

		/**
		 * 告知接收到背景已修改通知。
		 *
		 * @param from_ 通知发布者JID。
		 */
		virtual void onRecvBackgroundChangedNotification( const gloox::JID& from_, const BackgroundInfo& backgroundInfo_ ) {}

		#pragma endregion


		#pragma region 微博
		
		/** 
		 * 告知发布微博成功。
		 * 
		 * @param id_ 微博ID。
		 */
		virtual void onPublishMicroblogSuccess( const std::string& id_ ) {}

		/** 
		 * 告知发布微博失败。
		 * 
		 * @param id_ 微博ID。
		 */
		virtual void onPublishMicroblogFailed( const std::string& id_ ) {}
		
		/** 
		 * 告知删除（自己的）微博成功。
		 * 
		 * @param id_ 微博ID。
		 */
		virtual void onDeleteMicroblogSuccess( const std::string& id_ ) {}

		/** 
		 * 告知删除（自己的）微博失败。
		 * 
		 * @param id_ 微博ID。
		 */
		virtual void onDeleteMicroblogFailed( const std::string& id_ ) {}

		/** 
		 * 告知接收到一条微博。
		 * 
		 * @param microblog_ 微博。
		 */
		virtual void onRecvMicroblog( const Microblog& microblog_ ) {}
		
		/** 
		 * 告知一条微博被（别人）删除。
		 * 
		 * @param id_ 微博ID。
		 */
		virtual void onMicroblogDeleted( const std::string& id_ ) {}
		
		#pragma endregion
		
		#pragma region 群聊
		/**
		 * 告知群聊房间创建成功。
		 * 
		 * @param room_ 房间JID。
		 */
		virtual void onCreateMUCRoomSuccess( const gloox::JID& room_ ) {}
		
		/**
		 * 告知群聊房间创建失败。
		 * 
		 * @param room_  房间JID。
		 * @param error_ 错误类型。
		 */
		virtual void onCreateMUCRoomFailed( const gloox::JID& room_, CreateMUCRoomError error_ ) {}
		
		/**
		 * 告知群聊房间配置成功。
		 * 
		 * @param room_ 房间JID。
		 */
		virtual void onConfigMUCRoomSuccess( const gloox::JID& room_ ) {}
		
		/**
		 * 告知群聊房间配置失败。
		 * 
		 * @param room_ 房间JID。
		 */
		virtual void onConfigMUCRoomFailed( const gloox::JID& room_ ) {}
		
		/**
		 * 告知群聊房间销毁成功。
		 * 
		 * @param room_ 房间JID。
		 */
		virtual void onDestroyMUCRoomSuccess( const gloox::JID& room_ ) {}
		
		/**
		 * 告知群聊房间销毁失败。
		 * 
		 * @param room_ 房间JID。
		 */
		virtual void onDestroyMUCRoomFailed( const gloox::JID& room_ ) {}
		
		/**
		 * 告知接收到群聊状态。
		 * 
		 * @param room_ 房间JID。
		 * @param participantOldNickname_
		 * @param participantNewNickname_
		 * @param participantJid_
		 * @param pres_
		 * @param affi_
		 * @param role_
		 */
		virtual void onRecvMUCRoomPresence( const gloox::JID& room_,
			                                const std::string& participantOldNickname_,
											const std::string& participantNewNickname_,
			                                const gloox::JID& participantJid_,
			                                const gloox::Presence& pres_, 
											gloox::MUCRoomAffiliation affi_, 
											gloox::MUCRoomRole role_ ) {}
		
		/**
		 * 告知接收到群聊消息。
		 * 
		 * @param room_   房间JID。
		 * @param msg_    群聊消息。
		 * @param xhtml_  群聊富文本消息。
		 */
		virtual void onRecvMUCRoomMessage( const gloox::JID& room_, const gloox::Message& msg_, const std::string& xhtml_ ) {}

		/**
		 * 告知修改群聊房间成员列表成功。
		 * 
		 * @param room_ 房间JID。
		 */
		virtual void onModifyMUCRoomMemberListSuccess( const gloox::JID& room_ ) {}
		
		/**
		 * 告知修改群聊房间成员列表失败。
		 * 
		 * @param room_ 房间JID。
		 */
		virtual void onModifyMUCRoomMemberListFailed( const gloox::JID& room_ ) {}
		
		/**
		 * 告知修改群聊房间所有者列表成功。
		 * 
		 * @param room_ 房间JID。
		 */
		virtual void onModifyMUCRoomOwnerListSuccess( const gloox::JID& room_ ) {}
		
		/**
		 * 告知修改群聊房间所有者列表失败。
		 * 
		 * @param room_ 房间JID。
		 */
		virtual void onModifyMUCRoomOwnerListFailed( const gloox::JID& room_ ) {}

		/**
		 * 告知接收到群聊房间（间接）邀请。
		 * 
		 * @param room_    房间JID。
		 * @param invitor_ 邀请者JID。
		 * @param reason_  邀请理由。
		 */
		//virtual void onRecvMUCRoomInvitation( const gloox::JID& room_, const gloox::JID& invitor_, const std::string& reason_ ) {}

		/**
		 * 告知接收到群聊房间（直接）邀请。
		 * 
		 * @param room_    房间JID。
		 * @param invitor_ 邀请者JID。
		 * @param reason_  邀请理由。
		 */
		virtual void onRecvMUCRoomDirectInvitation( const gloox::JID& room_, const gloox::JID& invitor_, const std::string& reason_ ) {}

		/**
		 * 告知接收到群聊房间（间接）邀请。
		 * 
		 * @param room_    房间JID。
		 * @param invitor_ 邀请者JID。
		 * @param reason_  邀请理由。
		 */
		virtual void onRecvMUCRoomMediatedInvitation( const gloox::JID& room_, const gloox::JID& invitor_, const std::string& reason_ ) {}

		/**
		 * 告知获取到群聊房间的成员列表。
		 * 
		 * @param room_    房间JID。
		 * @param members_ 成员列表。
		 */
		virtual void onRetrieveMUCRoomMemberList( const gloox::JID& room_, const gloox::StringList& members_ ) {}
		
		/**
		 * 告知获取到群聊房间的成员列表。
		 * 
		 * @param room_    房间JID。
		 * @param members_ 成员列表。
		 */
		virtual void onRetrieveMUCRoomOwnerList( const gloox::JID& room_, const gloox::StringList& owners_ ) {}
		
		/**
		 * 告知群聊房间主题被修改。
		 * 
		 * @param room_     房间JID。
		 * @param nickname_ 修改人昵称。
		 * @param subject_  修改后的主题。
		 */
		virtual void onMUCRoomSubjectChanged( const gloox::JID& room_, const std::string& nickname_, const std::string& subject_ ) {}

		/**
		 * 告知获取到群聊房间信息。
		 * 
		 * @param room_ 房间JID。
		 * @param info_ 房间信息。
		 */
		virtual void onRetrieveMUCRoomInfo( const gloox::JID& room_, const MUCRoomInfo& info_ ) {}
		
		/**
		 * 告知获取到群聊房间配置。
		 * 
		 * @param room_   房间JID。
		 * @param config_ 房间配置。
		 */
		virtual void onRetrieveMUCRoomConfig( const gloox::JID& room_, const MUCRoomConfig& config_ ) {}

		virtual void onMUCRoomInvitationRejected( const gloox::JID& room_, const gloox::JID& invitee_, const std::string& reason_ ) {}

		//virtual void onRecvHuddleInvitation( const gloox::JID& room_, const gloox::JID& invitor_, const std::string& reason_ ) {}

		//virtual void onRecvInstantMUCRoomInvitation( const gloox::JID& room_, const gloox::JID& invitor_, const std::string& reason_ ) {}

		/**
		 * 告知修改自己群聊房间内昵称发生冲突，别人已经占用该昵称。
		 * 
		 * @param room_    房间JID。
		 */
		virtual void onChangeMUCNicknameConflict( const gloox::JID& room_ ) {}

		#pragma endregion
				
		#pragma region 文件传输

		/**
		 * 告知接收到文件传输请求。
		 *
		 * @param initiator_  发送方JID。
		 * @param sid_        流ID。
		 * @param file_       文件元数据。
		 * @param supportS5b_ 支持S5B方式。
		 * @param supportIbb_ 支持IBB方式。
		 * @param supportOob_ 支持OBB方式。
		 */
		virtual void onFtRequest( const gloox::JID& initiator_, 
			                      const std::string& sid_, 
								  const FileMetadata& file_,
								  bool supportS5b_,
								  bool supportIbb_,
								  bool supportOob_ ) {}

		/**
		 * 告知文件传输字节输入流已创建。
		 *
		 * @param sid_       流ID。
		 * @param type_      流类型。
		 * @param initiator_ 发送方JID。
		 * @param target_    接收方JID。
		 */
		virtual void onFtBytestreamCreated( const std::string& sid_, gloox::Bytestream::StreamType type_, const gloox::JID& initiator_, const gloox::JID& target_ ) {}

		/**
		 * 告知文件传输字节输入流已打开。
		 *
		 * @param sid_ 流ID。
		 */
		virtual void onFtIncomingBytestreamOpened( const std::string& sid_ ) {}
		
		/**
		 * 告知文件传输字节输出流已打开。
		 *
		 * @param sid_ 流ID。
		 */
		virtual void onFtOutgoingBytestreamOpened( const std::string& sid_ ) {}

		/**
		 * 告知接收到文件传输字节流数据。
		 *
		 * @param sid_  流ID。
		 * @param data_ 流数据。
		 */
		virtual void onRecvFtBytestreamData( const std::string& sid_, const BytestreamData& data_ ) {}

		/**
		 * 告知文件传输字节流已关闭。
		 *
		 * @param sid_  流ID。
		 */
		virtual void onFtBytestreamClosed( const std::string& sid_ ) {}

		#pragma endregion

		/* ------------------------------------------------ ?? ------------------------------------------------ */
		virtual void onDiscoItem( const gloox::JID& parent_, 
			                      const gloox::JID& jid_, 
			                      const std::string& node_, 
								  const std::string& name_ ) {}
		/*  ------------------------------------------------ XEP-Muc ------------------------------------------------ */
		/* 查询到一个群(信息) */
		//virtual void onDiscoMucGroup( const gloox::JID& jid_, const std::string& name_ ) {}
		///* 成功创建了一个多人聊天群 */
		//virtual void onMucGroupCreated( const gloox::JID& jid_ ) {}
		///* 获取到一个群成员 */
		////virtual void onRecvMucGroupRosterItem( const std::string& nickname_, gloox::MUCRoomAffiliation affi_ ) {}
		///* 获取到一个普通群成员(信息) */
		//virtual void onRetrieveMucGroupMember( const std::string& group_jid_, const std::string& member_jid_ ) {}
		///* 获取到一个群管理员(信息) */
		//virtual void onRetrieveMucGroupAdmin( const std::string& group_jid_, const std::string& admin_jid_ ) {}
		///* 获取到一个群主(信息) */
		//virtual void onRetrieveMucGroupOwner( const std::string& group_jid_, const std::string& owner_jid_ ) {}
		///* 接收到群/讨论组中成员的状态 */
		//virtual void onRecvMucPresence( const std::string& group_jid_, 
		//	                            const gloox::JID& nickname_, 
		//								const gloox::JID* jid_, 
		//								gloox::MUCRoomAffiliation affi_,
		//								gloox::MUCRoomRole role_,
		//								const gloox::Presence& presence_ ) {}
		///* 接收到群聊消息 */
		//virtual void onRecvMucMessage( const std::string& group_jid_, const gloox::Message& msg_ ) {}
		///* 接收到多人聊天记录消息 */
		//virtual void onRecvMucChatHistory( const gloox::JID& room_jid_, 
		//	                               const gloox::JID& from_, 
		//								   const std::string& msg_, 
		//								   const std::string& stamp_ ) {}
		///* 获取到群信息 */
		//virtual void onRetrieveHuddleInfo( const std::string& jid_,
		//	                               const std::string& name_,
		//	                               const std::string desc_, 
		//                                   const std::string& subject_, 
		//								   int onlinecount_, 
		//								   const std::string& creationdate_ ) {}
		////
		//virtual void onRecvMUCInvitation( const gloox::JID& room_, const gloox::JID& from_ ) {}
		//
		//virtual void onRetrieveHuddleConfig( const std::string& huddle_jid_, kl::HuddleConfig* config_ ) {}

		//virtual void onRetrieveHuddleConfigFailed() {}
		///* 接收到一个多人聊天(临时房间)邀请 */
		//virtual void onRecvInstantRoomInvitation( kl::InstantRoom* room_ ) {}

	};
}

#endif // KL_XMPPCALLBACK_H__