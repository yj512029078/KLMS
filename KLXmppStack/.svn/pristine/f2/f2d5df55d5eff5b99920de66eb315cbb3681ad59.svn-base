#include "../include/kl_vcardname.h"

namespace kl
{
	VCardName::VCardName()
	{
		m_name = new gloox::VCard::Name();
	}

	VCardName::VCardName( const gloox::VCard::Name& name_ )
	{
		m_name = new gloox::VCard::Name();
		m_name->family = name_.family;
		m_name->given = name_.given;
		m_name->middle = name_.middle;
		m_name->prefix = name_.prefix;
		m_name->suffix = name_.suffix;
	}

	VCardName::~VCardName()
	{
		delete m_name;
	}

	const std::string& VCardName::family() const
	{
		return m_name->family;
	}

	const std::string& VCardName::given() const
	{
		return m_name->given;
	}

	const std::string& VCardName::middle() const
	{
		return m_name->middle;
	}

	const std::string& VCardName::prefix() const
	{
		return m_name->prefix;
	}

	const std::string& VCardName::suffix() const
	{
		return m_name->suffix;
	}

	void VCardName::setFamily( const std::string& family_ )
	{
		m_name->family = family_;
	}

	void VCardName::setGiven( const std::string& given_ )
	{
		m_name->given = given_;
	}

	void VCardName::setMiddle( const std::string& middle_ )
	{
		m_name->middle = middle_;
	}

	void VCardName::setPrefix( const std::string& prefix_ )
	{
		m_name->prefix = prefix_;
	}

	void VCardName::setSuffix( const std::string& suffix_ )
	{
		m_name->suffix = suffix_;
	}
}