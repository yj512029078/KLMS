#ifndef KL_XMLPARSER_H__
#define KL_XMLPARSER_H__

#include "../../gloox/src/parser.h"
#include "../../gloox/src/taghandler.h"
#include <string>

namespace kl
{
	class XmlParser : public gloox::TagHandler
	{
	public:
		XmlParser() : m_tag( 0 )  {}
		virtual ~XmlParser() 
		{
			delete m_tag;
			m_tag = 0;
		}
	public:
		/* override gloox::TagHandler */
		void handleTag( gloox::Tag* tag_ )
		{
			delete m_tag;
			m_tag = 0;
			m_tag = tag_->clone();
		}

		void feed( const std::string& xml_ )
		{
			gloox::Parser* p = new gloox::Parser( this );
			p->feed( (std::string&) xml_ );
			delete p;
			p = 0;
		}

		const gloox::Tag* tag() const 
		{ 
			return m_tag; 
		}

	private:
		gloox::Tag* m_tag;
	};
}

#endif // 