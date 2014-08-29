#ifndef KL_VCARD_H__
#define KL_VCARD_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/taghandler.h"

namespace gloox
{
	class Parser;
	class VCard;
}

namespace kl
{
	class VCardName;
	class VCardPhoto;
	class VCardEmail;
	class VCardAddress;
	class VCardLabel;
	class VCardTelephone;
	class VCardGeo;
	class VCardOrg;

	class KLXMPP_API VCard : public gloox::TagHandler
	{
	public:
		VCard();

		VCard( const std::string& xml_ );
		
		virtual ~VCard();

	public: /* API */

		bool isValid() const;

		const std::string& xml() const;
		
		// <FN>
		void setFormattedname( const std::string& name_ );

		const std::string& formattedname() const;

		// <N>
		void setName( const std::string& family_, const std::string& given_ );

		VCardName* name() const;

		// <NICKNAME>
		void setNickname( const std::string& nickname_ );

		const std::string& nickname() const;
		
		// <URL>
		void setUrl( const std::string& url_ );

		const std::string& url() const;
		
		// <BDAY>
		void setBday( const std::string& bday_ );

		const std::string& bday() const;
		
		// <JABBERID>
		void setJabberid( const std::string& jabberid_ );

		const std::string& jabberid() const;
		
		// <TITLE>
		void setTitle( const std::string& title_ );

		const std::string& title() const;
		
		// <ROLE>
		void setRole( const std::string& role_ );

		const std::string& role() const;
		
		// <NOTE>
		void setNote( const std::string& note_ );

		const std::string& note() const;
		
		// <DESC>
		void setDesc( const std::string& desc_ );

		const std::string& desc() const;
		
		// <MAILER>
		void setMailer( const std::string& mailer_ );

		const std::string& mailer() const;
		
		// <REV>
		void setRev( const std::string& rev_ );

		const std::string& rev() const;

		// <UID>
		void setUid( const std::string& uid_ );

		const std::string& uid() const;

		// <TZ>
		void setTz( const std::string& tz_ );

		const std::string& tz() const;
		
		// <PRODID>
		void setProdid( const std::string& prodid_ );

		const std::string& prodid() const;

		// <SORTSTRING>
		void setSortstring( const std::string& sortstring_ );

		const std::string& sortstring() const;
		
		// <PHOTO>
		//void setPhoto( const std::string& extval_ );

		void setPhoto( const std::string& type_, const std::string& binval_ );

		VCardPhoto* photo() const;

		// <LOGO>
		void setLogo( const std::string& type_, const std::string& binval_ );

		VCardPhoto* logo() const;

		// <EMAIL>
		void addEmail( const std::string& userid_, int type_ );

		std::list<VCardEmail*>& emailList();

		// <ADDRESS>
		void addAddress( const std::string& pobox_, 
			             const std::string& extadd_, 
						 const std::string& street_, 
						 const std::string& locality_,
						 const std::string& region_, 
						 const std::string& pcode_,
						 const std::string& ctry_, 
						 int type_ );

		std::list<VCardAddress*>& addressList();

		// <LABEL>
		void addLabel( const std::list<std::string>& lines_, int type_ );

		std::list<VCardLabel*>& labelList();

		// <TELEPHONE>
		void addTelephone( const std::string& number_, int type_ );
		
		std::list<VCardTelephone*>& telephoneList();

		// <GEO>
		void setGeo( const std::string& lat_, const std::string& lon_ );
		
		VCardGeo* geo() const;

		// <ORG>

		void setOrg( const std::string& name_, const std::list<std::string>& units_ );

		VCardOrg* org() const;


#ifndef SWIG // SWIG过滤以下接口

		void handleTag( gloox::Tag* tag_ );

#endif // end #ifndef SWIG	

	private:
		void updateXml();

	private:
		bool           m_isValid;
		gloox::Parser* m_parser;
		gloox::VCard*  m_vcard;
		std::string    m_xml;
		VCardName*     m_name;
		VCardPhoto*    m_photo;
		VCardPhoto*    m_logo;
		VCardGeo*      m_geo;
		VCardOrg*      m_org;
		std::list<VCardEmail*>     m_emailList;
		std::list<VCardAddress*>   m_addressList;
		std::list<VCardLabel*>     m_labelList;
		std::list<VCardTelephone*> m_telephoneList;
	};
}

#endif // KL_VCARD_H__
