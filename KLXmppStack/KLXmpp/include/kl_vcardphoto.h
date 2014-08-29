#ifndef KL_VCARDPHOTO_H__
#define KL_VCARDPHOTO_H__

#include "../../KLXmpp/include/kl_common.h"
#include "../../gloox/src/vcard.h"

namespace kl
{
	class KLXMPP_API VCardPhoto
	{
	public:
		VCardPhoto();

#ifndef SWIG // SWIG过滤以下接口

		VCardPhoto( const gloox::VCard::Photo& photo_ );

#endif // SWIG

		virtual ~VCardPhoto();
		
		void setExtval( const std::string& extval_ );

		const std::string& extval() const;

		void setBinval( const std::string& binval_ );

		const std::string& binval() const;

		void setType( const std::string& type_ );

		const std::string& type() const;
		
	private:
		gloox::VCard::Photo* m_photo;
	};
}

#endif // KL_VCARDNAME_H__
