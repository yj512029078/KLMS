#include "../include/kl_vcard.h"
#include "../include/kl_vcardname.h"
#include "../include/kl_vcardphoto.h"
#include "../include/kl_vcardemail.h"
#include "../include/kl_vcardaddress.h"
#include "../include/kl_vcardlabel.h"
#include "../include/kl_vcardtelephone.h"
#include "../include/kl_vcardgeo.h"
#include "../include/kl_vcardorg.h"
#include "../../gloox/src/util.h"
#include "../../gloox/src/parser.h"
#include "../../gloox/src/tag.h"
#include "../../gloox/src/vcard.h"

namespace kl
{
	VCard::VCard() 
		: m_isValid( true ),
		  m_parser( 0 ),
		  m_vcard( 0 ),
		  m_xml( "" ),
		  m_name( 0 ),
		  m_photo( 0 ),
		  m_logo( 0 ),
		  m_geo( 0 ),
		  m_org( 0 )
	{
		m_vcard = new gloox::VCard();
		m_name = new VCardName();
		m_photo = new VCardPhoto();
		m_logo = new VCardPhoto();
		m_geo = new VCardGeo();
		m_org = new VCardOrg();

		updateXml();
	}

	VCard::VCard( const std::string& xml_ ) 
		: m_isValid( false),
		  m_parser( 0 ),
		  m_vcard( 0 ),
		  m_xml( xml_ ),
		  m_name( 0 ),
		  m_photo( 0 ),
		  m_logo( 0 ),
		  m_geo( 0 ),
		  m_org( 0 )
	{
		m_parser = new gloox::Parser( this );
		m_parser->feed( ( std::string& ) xml_ );

		if ( m_vcard )
		{
			m_name = new VCardName( m_vcard->name() );
			m_photo = new VCardPhoto( m_vcard->photo() );
			m_logo = new VCardPhoto( m_vcard->logo() );
			m_geo = new VCardGeo( m_vcard->geo() );
			m_org = new VCardOrg( m_vcard->org() );

			// fill m_emailList
			const gloox::VCard::EmailList& list_email =  m_vcard->emailAddresses();
			gloox::VCard::EmailList::const_iterator it_email = list_email.begin();
			for ( ; it_email != list_email.end(); it_email++ )
			{
				VCardEmail* ve = new VCardEmail( (*it_email) );
				m_emailList.push_back( ve );
			}
			// ~fill m_emailList

			// fill m_addressList
			const gloox::VCard::AddressList& list_address =  m_vcard->addresses();
			gloox::VCard::AddressList::const_iterator it_addresss = list_address.begin();
			for ( ; it_addresss != list_address.end(); it_addresss++ )
			{
				VCardAddress* va = new VCardAddress( (*it_addresss) );
				m_addressList.push_back( va );
			}
			// ~fill m_addressList

			// fill m_labelList
			const gloox::VCard::LabelList& list_label =  m_vcard->labels();
			gloox::VCard::LabelList::const_iterator it_label = list_label.begin();
			for ( ; it_label != list_label.end(); it_label++ )
			{
				VCardLabel* vl = new VCardLabel( (*it_label) );
				m_labelList.push_back( vl );
			}
			// ~fill m_labelList

			// fill m_telephoneList
			const gloox::VCard::TelephoneList& list_telephone =  m_vcard->telephone();
			gloox::VCard::TelephoneList::const_iterator it_telephone = list_telephone.begin();
			for ( ; it_telephone != list_telephone.end(); it_telephone++ )
			{
				VCardTelephone* vt = new VCardTelephone( (*it_telephone) );
				m_telephoneList.push_back( vt );
			}
			// ~fill m_telephoneList
		}
	}
	
	VCard::~VCard()
	{
		delete m_parser;
		delete m_vcard;
		delete m_name;
		delete m_photo;
		delete m_logo;
		delete m_geo;
		delete m_org;

		gloox::util::clearList( m_emailList );
		gloox::util::clearList( m_addressList );
		gloox::util::clearList( m_labelList );
		gloox::util::clearList( m_telephoneList );
	}

	bool VCard::isValid() const
	{
		return m_isValid;
	}

	const std::string& VCard::xml() const
	{
		return m_xml;
	}

	void VCard::setFormattedname( const std::string& name_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setFormattedname( name_ );

		updateXml();
	}

	const std::string& VCard::formattedname() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->formattedname();
	}

	void VCard::setName( const std::string& family_, const std::string& given_ )
	{
		if ( !m_name || !m_vcard )
		{
			return;
		}

		m_name->setFamily( family_ );
		m_name->setGiven( given_ );
		m_vcard->setName( family_, given_ );

		updateXml();
	}

	VCardName* VCard::name() const
	{
		return m_name;
	}

	void VCard::setNickname( const std::string& nickname_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setNickname( nickname_ );

		updateXml();
	}

	const std::string& VCard::nickname() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->nickname();
	}

	void VCard::setUrl( const std::string& url_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setUrl( url_ );

		updateXml();
	}

	const std::string& VCard::url() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->url();
	}

	void VCard::setBday( const std::string& bday_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setBday( bday_ );

		updateXml();
	}

	const std::string& VCard::bday() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->bday();
	}

	void VCard::setJabberid( const std::string& jabberid_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setJabberid( jabberid_ );

		updateXml();
	}

	const std::string& VCard::jabberid() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->jabberid();
	}

	void VCard::setTitle( const std::string& title_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setTitle( title_ );

		updateXml();
	}

	const std::string& VCard::title() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->title();
	}

	void VCard::setRole( const std::string& role_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setRole( role_ );

		updateXml();
	}

	const std::string& VCard::role() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->role();
	}

	void VCard::setNote( const std::string& note_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setNote( note_ );

		updateXml();
	}

	const std::string& VCard::note() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->note();
	}

	void VCard::setDesc( const std::string& desc_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setDesc( desc_ );

		updateXml();
	}

	const std::string& VCard::desc() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->desc();
	}

	void VCard::setMailer( const std::string& mailer_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setMailer( mailer_ );

		updateXml();
	}

	const std::string& VCard::mailer() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->mailer();
	}

	void VCard::setRev( const std::string& rev_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setRev( rev_ );

		updateXml();
	}

	const std::string& VCard::rev() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->rev();
	}

	void VCard::setUid( const std::string& uid_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setUid( uid_ );

		updateXml();
	}

	const std::string& VCard::uid() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->uid();
	}

	void VCard::setTz( const std::string& tz_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setTz( tz_ );

		updateXml();
	}

	const std::string& VCard::tz() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->tz();
	}

	void VCard::setProdid( const std::string& prodid_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setProdid( prodid_ );

		updateXml();
	}

	const std::string& VCard::prodid() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->prodid();
	}

	void VCard::setSortstring( const std::string& sortstring_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->setSortstring( sortstring_ );

		updateXml();
	}

	const std::string& VCard::sortstring() const
	{
		if ( !m_vcard )
		{
			return "";
		}

		return m_vcard->sortstring();
	}

	void VCard::setPhoto( const std::string& type_, const std::string& binval_ )
	{
		if ( !m_photo || !m_vcard )
		{
			return;
		}

		m_photo->setType( type_ );
		m_photo->setBinval( binval_ );
		m_vcard->setPhoto( type_, binval_ ); 

		updateXml();
	}

	VCardPhoto* VCard::photo() const
	{
		return m_photo;
	}

	void VCard::setLogo( const std::string& type_, const std::string& binval_ )
	{
		if ( !m_logo || !m_vcard )
		{
			return;
		}

		m_logo->setType( type_ );
		m_logo->setBinval( binval_ );
		m_vcard->setLogo( type_, binval_ ); 

		updateXml();
	}

	VCardPhoto* VCard::logo() const
	{
		return m_logo;
	}

	void VCard::addEmail( const std::string& userid_, int type_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->addEmail( userid_, type_ );

		VCardEmail* email = new VCardEmail();
		email->setUserid( userid_ );
		email->setType( type_ );
		m_emailList.push_back( email );

		updateXml();
	}

	std::list<VCardEmail*>& VCard::emailList()
	{
		return m_emailList;
	}

	void VCard::addAddress( const std::string& pobox_, 
		                    const std::string& extadd_, 
							const std::string& street_, 
							const std::string& locality_,
							const std::string& region_, 
							const std::string& pcode_,
							const std::string& ctry_, 
							int type_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->addAddress( pobox_, extadd_, street_, locality_, region_, pcode_, ctry_, type_ );

		VCardAddress* address = new VCardAddress();
		address->setPobox( pobox_ );
		address->setExtadd( extadd_ );
		address->setStreet( street_ );
		address->setLocality( locality_ );
		address->setRegion( region_ );
		address->setPcode( pcode_ );
		address->setCtry( ctry_ );
		address->setType( type_ );
		m_addressList.push_back( address );

		updateXml();
	}

	std::list<VCardAddress*>& VCard::addressList()
	{
		return m_addressList;
	}

	void VCard::addLabel( const std::list<std::string>& lines_, int type_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->addLabel( lines_, type_ );

		VCardLabel* label = new VCardLabel();
		label->setLines( lines_ );
		label->setType( type_ );
		m_labelList.push_back( label );

		updateXml();
	}

	std::list<VCardLabel*>& VCard::labelList()
	{
		return m_labelList;
	}

	void VCard::addTelephone( const std::string& number_, int type_ )
	{
		if ( !m_vcard )
		{
			return;
		}

		m_vcard->addTelephone( number_, type_ );

		VCardTelephone* telephone = new VCardTelephone();
		telephone->setNumber( number_ );
		telephone->setType( type_ );
		m_telephoneList.push_back( telephone );

		updateXml();

	}

	std::list<VCardTelephone*>& VCard::telephoneList()
	{
		return m_telephoneList;
	}

	void VCard::setGeo( const std::string& lat_, const std::string& lon_ )
	{
		if ( !m_geo || !m_vcard )
		{
			return;
		}

		m_geo->setLatitude( lat_ );
		m_geo->setLongitude( lon_ );

		m_vcard->setGeo( lat_, lon_ );

		updateXml();
	}

	VCardGeo* VCard::geo() const
	{
		return m_geo;
	}

	void VCard::setOrg( const std::string& name_, const std::list<std::string>& units_ )
	{
		if ( !m_org || !m_vcard )
		{
			return;
		}

		m_org->setName( name_ );
		m_org->setUnits( units_ );

		m_vcard->setOrganization( name_, units_ );

		updateXml();
	}

	VCardOrg* VCard::org() const
	{
		return m_org;
	}

	void VCard::handleTag( gloox::Tag* tag_ )
	{
		if ( m_vcard )
		{
			delete m_vcard;
			m_vcard = 0;
		}
		
		m_vcard = new gloox::VCard( tag_ );
		m_isValid = true;
	}

	void VCard::updateXml()
	{
		if ( !m_vcard || !m_vcard->tag() )
		{
			return;
		}

		gloox::Tag* tag = m_vcard->tag();

		m_xml = tag->xml();
	}
}