#include "../include/kl_vcardphoto.h"

namespace kl
{
	VCardPhoto::VCardPhoto()
	{
		m_photo = new gloox::VCard::Photo();
	}

	VCardPhoto::VCardPhoto( const gloox::VCard::Photo& photo_ )
	{
		m_photo = new gloox::VCard::Photo();
	}

	VCardPhoto::~VCardPhoto()
	{
		delete m_photo;
	}

	void VCardPhoto::setExtval( const std::string& extval_ )
	{
		m_photo->extval = extval_;
	}

	const std::string& VCardPhoto::extval() const
	{
		return m_photo->extval;
	}

	void VCardPhoto::setBinval( const std::string& binval_ )
	{
		m_photo->binval = binval_;
	}

	const std::string& VCardPhoto::binval() const
	{
		return m_photo->binval;
	}

	void VCardPhoto::setType( const std::string& type_ )
	{
		m_photo->type = type_;
	}

	const std::string& VCardPhoto::type() const
	{
		return m_photo->type;
	}
}