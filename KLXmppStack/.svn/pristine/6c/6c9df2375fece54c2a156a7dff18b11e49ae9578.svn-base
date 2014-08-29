#include "../include/kl_vcardorg.h"

namespace kl
{
	VCardOrg::VCardOrg()
	{
		m_org = new gloox::VCard::Org();
	}

	VCardOrg::VCardOrg( const gloox::VCard::Org& org_ )
	{
		m_org = new gloox::VCard::Org();

		m_org->name = org_.name;
		m_org->units = org_.units;
	}

	VCardOrg::~VCardOrg()
	{
		delete m_org;
	}

	void VCardOrg::setName( const std::string& name_ )
	{
		m_org->name = name_;
	}

	const std::string& VCardOrg::name() const
	{
		return m_org->name;
	}

	void VCardOrg::setUnits( const std::list<std::string>& units_ )
	{
		m_org->units = units_;
	}

	const std::list<std::string>& VCardOrg::units() const
	{
		return m_org->units;
	}
}