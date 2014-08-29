#include "../include/kl_vcardemail.h"

namespace kl
{
	VCardEmail::VCardEmail()
	{
		m_email = new gloox::VCard::Email();
	}

	VCardEmail::VCardEmail( const gloox::VCard::Email& email_ )
	{
		m_email = new gloox::VCard::Email();
		m_email->userid = email_.userid;
		m_email->home = email_.home;
		m_email->internet = email_.internet;
		m_email->work = email_.work;
		m_email->pref = email_.pref;
		m_email->x400 = email_.x400;
	}

	VCardEmail::~VCardEmail()
	{
		delete m_email;
	}

	void VCardEmail::setUserid( const std::string& userid_ )
	{
		m_email->userid = userid_;
	}

	const std::string& VCardEmail::userid() const
	{
		return m_email->userid;
	}

	bool VCardEmail::isHome() const
	{
		return m_email->home;
	}

	bool VCardEmail::isWork() const
	{
		return m_email->work;
	}

	bool VCardEmail::isInternet() const
	{
		return m_email->internet;
	}

	bool VCardEmail::isPref() const
	{
		return m_email->pref;
	}

	bool VCardEmail::isX400() const
	{
		return m_email->x400;
	}

	void VCardEmail::setType( int type_ )
	{
		m_email->home = ( ( type_ & gloox::VCard::AddressType::AddrTypeHome ) == gloox::VCard::AddressType::AddrTypeHome );
		m_email->work = ( ( type_ & gloox::VCard::AddressType::AddrTypeWork ) == gloox::VCard::AddressType::AddrTypeWork );
		m_email->pref = ( ( type_ & gloox::VCard::AddressType::AddrTypePref ) == gloox::VCard::AddressType::AddrTypePref );
		m_email->x400 = ( ( type_ & gloox::VCard::AddressType::AddrTypeX400 ) == gloox::VCard::AddressType::AddrTypeX400 );
		m_email->internet = ( ( type_ & gloox::VCard::AddressType::AddrTypeInet ) == gloox::VCard::AddressType::AddrTypeInet );
	}
}