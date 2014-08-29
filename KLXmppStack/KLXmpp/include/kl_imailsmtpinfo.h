#ifndef KL_IMAILSMTPINFO_H__
#define KL_IMAILSMTPINFO_H__

#include "kl_common.h"
#include "kl_convert.h"
#include "kl_util.h"
#include "../../gloox/src/tag.h"

namespace kl
{
	/**
	 * @brief 即时邮件信息实体。
	 *
	 * @author JI Yixuan
	 */
	class KLXMPP_API IMailSMTPInfo
	{
	public:
		IMailSMTPInfo();

#ifndef SWIG // SWIG过滤以下接口
		IMailSMTPInfo( const gloox::Tag* tag_ );
#endif // end #ifndef SWIG

		virtual ~IMailSMTPInfo() {}

		void setMessageID( const std::string& messageID_ ) { m_messageID = messageID_; }

		const std::string& messageID() const { return m_messageID; }

		void setTitle( const std::string& title_ ) { m_title = title_; }

		const std::string& title() const { return m_title; }

		void setAttachmentNum( int attachmentNum_ ) { m_attachmentNum = attachmentNum_; }

		int attachmentNum() { return m_attachmentNum; }

#ifndef SWIG // SWIG过滤以下接口
		gloox::Tag* newTag() const;
#endif // end #ifndef SWIG
		
	private:
		/**
		 * 邮件ID。
		 */
		std::string m_messageID;

		/**
		 * 邮件标题。
		 */
		std::string m_title;
				
		/**
		 * 邮件附件数量。
		 */
		int m_attachmentNum;
	};
}

#endif // KL_IMAILSMTPINFO_H__