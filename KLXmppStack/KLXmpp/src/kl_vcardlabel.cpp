#include "../include/kl_vcardlabel.h"

namespace kl
{
	VCardLabel::VCardLabel()
	{
		m_label = new gloox::VCard::Label();
	}

	VCardLabel::VCardLabel( const gloox::VCard::Label& label_ )
	{
		m_label = new gloox::VCard::Label();

		m_label->lines = label_.lines;

		m_label->home = label_.home;
		m_label->work = label_.work;
		m_label->postal = label_.postal;
		m_label->parcel = label_.parcel;
		m_label->pref = label_.pref;
		m_label->dom = label_.dom;
		m_label->intl = label_.intl;
	}

	VCardLabel::~VCardLabel()
	{
		delete m_label;
	}

	void VCardLabel::setLines( const std::list<std::string>& lines_ )
	{
		m_label->lines = lines_;
	}

	std::list<std::string>& VCardLabel::lines() const
	{
		return m_label->lines;
	}
	
	bool VCardLabel::isHome() const
	{
		return m_label->home;
	}

	bool VCardLabel::isWork() const
	{
		return m_label->work;
	}

	bool VCardLabel::isPostal() const
	{
		return m_label->postal;
	}

	bool VCardLabel::isParcel() const
	{
		return m_label->parcel;
	}

	bool VCardLabel::isPref() const
	{
		return m_label->pref;
	}

	bool VCardLabel::isDom() const
	{
		return m_label->dom;
	}

	bool VCardLabel::isIntl() const
	{
		return m_label->intl;
	}

	void VCardLabel::setType( int type_ )
	{
		m_label->home = ( ( type_ & gloox::VCard::AddressType::AddrTypeHome ) == gloox::VCard::AddressType::AddrTypeHome );
		m_label->work = ( ( type_ & gloox::VCard::AddressType::AddrTypeWork ) == gloox::VCard::AddressType::AddrTypeWork );
		m_label->postal = ( ( type_ & gloox::VCard::AddressType::AddrTypePostal ) == gloox::VCard::AddressType::AddrTypePostal );
		m_label->parcel = ( ( type_ & gloox::VCard::AddressType::AddrTypeParcel ) == gloox::VCard::AddressType::AddrTypeParcel );
		m_label->pref = ( ( type_ & gloox::VCard::AddressType::AddrTypePref ) == gloox::VCard::AddressType::AddrTypePref );
		m_label->dom = ( ( type_ & gloox::VCard::AddressType::AddrTypeDom ) == gloox::VCard::AddressType::AddrTypeDom );
		m_label->intl = ( ( type_ & gloox::VCard::AddressType::AddrTypeIntl ) == gloox::VCard::AddressType::AddrTypeIntl );
	}
}