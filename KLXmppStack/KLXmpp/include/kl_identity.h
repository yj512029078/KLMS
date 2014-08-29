#ifndef KL_IDENTITY_H__
#define KL_IDENTITY_H__

#include "../../KLXmpp/include/kl_common.h"
#include <string>

namespace kl
{
	class KLXMPP_API Identity
	{
	public:
		Identity( const std::string& category_, const std::string& name_, const std::string& type_ )
			: m_category( category_ ),
			  m_name( name_ ),
			  m_type( type_ )
		{
		}
		virtual ~Identity()
		{
		}
	public: /* Getter */
		const std::string category() const { return m_category; }
		const std::string name() const { return m_name; }
		const std::string type() const { return m_type; }
	private:
		const std::string m_category;
		const std::string m_name;
		const std::string m_type;
	};
}

#endif // KL_IDENTITY_H__
