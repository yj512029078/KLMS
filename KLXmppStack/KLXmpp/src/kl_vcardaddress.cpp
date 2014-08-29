#include "../include/kl_vcardaddress.h"

namespace kl
{
	VCardAddress::VCardAddress()
	{
		m_address = new gloox::VCard::Address();
	}

	VCardAddress::VCardAddress( const gloox::VCard::Address& address_ )
	{
		m_address = new gloox::VCard::Address();

		m_address->pobox = address_.pobox;
		m_address->extadd = address_.extadd;
		m_address->street = address_.street;
		m_address->locality = address_.locality;
		m_address->region = address_.region;
		m_address->pcode = address_.pcode;
		m_address->ctry = address_.ctry;

		m_address->home = address_.home;
		m_address->work = address_.work;
		m_address->parcel = address_.parcel;
		m_address->pref = address_.pref;
		m_address->dom = address_.dom;
		m_address->intl = address_.intl;
	}

	VCardAddress::~VCardAddress()
	{
		delete m_address;
	}

	void VCardAddress::setPobox( const std::string& pobox_ )
	{
		m_address->pobox = pobox_;
	}

	const std::string& VCardAddress::pobox() const
	{
		return m_address->pobox;
	}

	void VCardAddress::setExtadd( const std::string& extadd_ )
	{
		m_address->extadd = extadd_;
	}

	const std::string& VCardAddress::extadd() const
	{
		return m_address->extadd;
	}

	void VCardAddress::setStreet( const std::string& street_ )
	{
		m_address->street = street_;
	}

	const std::string& VCardAddress::street() const
	{
		return m_address->street;
	}

	void VCardAddress::setLocality( const std::string& locality_ )
	{
		m_address->locality = locality_;
	}

	const std::string& VCardAddress::locality() const
	{
		return m_address->locality;
	}

	void VCardAddress::setRegion( const std::string& region_ )
	{
		m_address->region = region_;
	}

	const std::string& VCardAddress::region() const
	{
		return m_address->region;
	}

	void VCardAddress::setPcode( const std::string& pcode_ )
	{
		m_address->pcode = pcode_;
	}

	const std::string& VCardAddress::pcode() const
	{
		return m_address->pcode;
	}

	void VCardAddress::setCtry( const std::string& ctry_ )
	{
		m_address->ctry = ctry_;
	}

	const std::string& VCardAddress::ctry() const
	{
		return m_address->ctry;
	}

	bool VCardAddress::isHome() const
	{
		return m_address->home;
	}

	bool VCardAddress::isWork() const
	{
		return m_address->work;
	}

	bool VCardAddress::isPostal() const
	{
		return m_address->postal;
	}

	bool VCardAddress::isParcel() const
	{
		return m_address->parcel;
	}

	bool VCardAddress::isPref() const
	{
		return m_address->pref;
	}

	bool VCardAddress::isDom() const
	{
		return m_address->dom;
	}

	bool VCardAddress::isIntl() const
	{
		return m_address->intl;
	}

	void VCardAddress::setType( int type_ )
	{
		m_address->home = ( ( type_ & gloox::VCard::AddressType::AddrTypeHome ) == gloox::VCard::AddressType::AddrTypeHome );
		m_address->work = ( ( type_ & gloox::VCard::AddressType::AddrTypeWork ) == gloox::VCard::AddressType::AddrTypeWork );
		m_address->parcel = ( ( type_ & gloox::VCard::AddressType::AddrTypeParcel ) == gloox::VCard::AddressType::AddrTypeParcel );
		m_address->pref = ( ( type_ & gloox::VCard::AddressType::AddrTypePref ) == gloox::VCard::AddressType::AddrTypePref );
		m_address->dom = ( ( type_ & gloox::VCard::AddressType::AddrTypeDom ) == gloox::VCard::AddressType::AddrTypeDom );
		m_address->intl = ( ( type_ & gloox::VCard::AddressType::AddrTypeIntl ) == gloox::VCard::AddressType::AddrTypeIntl );
	}
}