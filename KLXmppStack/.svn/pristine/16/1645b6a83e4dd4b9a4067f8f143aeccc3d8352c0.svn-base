#include "../include/kl_vcardtelephone.h"

namespace kl
{
	VCardTelephone::VCardTelephone()
	{
		m_telephone = new gloox::VCard::Telephone();
	}

	VCardTelephone::VCardTelephone( const gloox::VCard::Telephone& telephone_ )
	{
		m_telephone = new gloox::VCard::Telephone();

		m_telephone->number = telephone_.number;

		m_telephone->home = telephone_.home;
		m_telephone->work = telephone_.work;
		m_telephone->voice = telephone_.voice;
		m_telephone->fax = telephone_.fax;
		m_telephone->pager = telephone_.pager;
		m_telephone->msg = telephone_.msg;
		m_telephone->video = telephone_.video;
		m_telephone->bbs = telephone_.bbs;
		m_telephone->modem = telephone_.modem;
		m_telephone->isdn = telephone_.isdn;
		m_telephone->pcs = telephone_.pcs;
		m_telephone->pref = telephone_.pref;
	}

	VCardTelephone::~VCardTelephone()
	{
		delete m_telephone;
	}

	void VCardTelephone::setNumber( const std::string& number_ )
	{
		m_telephone->number = number_;
	}

	const std::string& VCardTelephone::number() const
	{
		return m_telephone->number;
	}

	bool VCardTelephone::isHome() const
	{
		return m_telephone->home;
	}

	bool VCardTelephone::isWork() const
	{
		return m_telephone->work;
	}

	bool VCardTelephone::isVoice() const
	{
		return m_telephone->voice;
	}

	bool VCardTelephone::isFax() const
	{
		return m_telephone->fax;
	}

	bool VCardTelephone::isPager() const
	{
		return m_telephone->pager;
	}

	bool VCardTelephone::isMsg() const
	{
		return m_telephone->msg;
	}

	bool VCardTelephone::isCell() const
	{
		return m_telephone->cell;
	}

	bool VCardTelephone::isVideo() const
	{
		return m_telephone->video;
	}

	bool VCardTelephone::isBBS() const
	{
		return m_telephone->bbs;
	}

	bool VCardTelephone::isModem() const
	{
		return m_telephone->modem;
	}

	bool VCardTelephone::isISDN() const
	{
		return m_telephone->isdn;
	}

	bool VCardTelephone::isPCS() const
	{
		return m_telephone->pcs;
	}

	bool VCardTelephone::isPref() const
	{
		return m_telephone->pref;
	}

	void VCardTelephone::setType( int type_ )
	{
		m_telephone->work = ( ( type_ & gloox::VCard::AddressType::AddrTypeWork ) == gloox::VCard::AddressType::AddrTypeWork );
		m_telephone->home = ( ( type_ & gloox::VCard::AddressType::AddrTypeHome ) == gloox::VCard::AddressType::AddrTypeHome );
		m_telephone->voice = (( type_ & gloox::VCard::AddressType::AddrTypeVoice ) == gloox::VCard::AddressType::AddrTypeVoice );
		m_telephone->fax = ( ( type_ & gloox::VCard::AddressType::AddrTypeFax ) == gloox::VCard::AddressType::AddrTypeFax );
		m_telephone->pager = ( ( type_ & gloox::VCard::AddressType::AddrTypePager ) == gloox::VCard::AddressType::AddrTypePager );
		m_telephone->msg = ( ( type_ & gloox::VCard::AddressType::AddrTypeMsg ) == gloox::VCard::AddressType::AddrTypeMsg );
		m_telephone->cell = ( ( type_ & gloox::VCard::AddressType::AddrTypeCell ) == gloox::VCard::AddressType::AddrTypeCell );
		m_telephone->video = ( ( type_ & gloox::VCard::AddressType::AddrTypeVideo ) == gloox::VCard::AddressType::AddrTypeVideo );
		m_telephone->bbs = ( ( type_ & gloox::VCard::AddressType::AddrTypeBbs ) == gloox::VCard::AddressType::AddrTypeBbs );
		m_telephone->modem = ( ( type_ & gloox::VCard::AddressType::AddrTypeModem ) == gloox::VCard::AddressType::AddrTypeModem );
		m_telephone->isdn = ( ( type_ & gloox::VCard::AddressType::AddrTypeIsdn ) == gloox::VCard::AddressType::AddrTypeIsdn );
		m_telephone->pcs = ( ( type_ & gloox::VCard::AddressType::AddrTypePcs ) == gloox::VCard::AddressType::AddrTypePcs );
		m_telephone->pref = ( ( type_ & gloox::VCard::AddressType::AddrTypePref ) == gloox::VCard::AddressType::AddrTypePref );
	}
}