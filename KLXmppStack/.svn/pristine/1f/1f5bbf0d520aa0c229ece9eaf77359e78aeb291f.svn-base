#include "../include/kl_imailsmtpinfo.h"

namespace kl
{
	const char* TAG_NAME_SMTPINFO      = "smtpinfo";
	const char* TAG_NAME_MESSAGEID     = "messageid";
	const char* TAG_NAME_TITLE         = "title";
	const char* TAG_NAME_ATTACHMENTNUM = "attachmentnum";

	IMailSMTPInfo::IMailSMTPInfo()
		: m_attachmentNum( 0 )
	{
	}

	IMailSMTPInfo::IMailSMTPInfo( const gloox::Tag* tag_ )
		: m_attachmentNum( 0 )
	{
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_MESSAGEID, m_messageID );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_TITLE, m_title );
		kl::util::TagUtil::checkField( tag_, kl::TAG_NAME_ATTACHMENTNUM, &m_attachmentNum );
	}

	gloox::Tag* IMailSMTPInfo::newTag() const
	{
		gloox::Tag* tag_smtpinfo = new gloox::Tag( kl::TAG_NAME_SMTPINFO );

		kl::util::TagUtil::insertField( tag_smtpinfo, kl::TAG_NAME_MESSAGEID, m_messageID );
		kl::util::TagUtil::insertField( tag_smtpinfo, kl::TAG_NAME_TITLE, m_title );
		kl::util::TagUtil::insertField( tag_smtpinfo, kl::TAG_NAME_ATTACHMENTNUM, int2string( m_attachmentNum ) );

		return tag_smtpinfo;
	}
}