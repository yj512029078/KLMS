#ifndef KL_DISCO_IDENTITY_H__
#define KL_DISCO_IDENTITY_H__

#include <string>

namespace kl
{
	namespace disco
	{
		struct Identity
		{
			std::string category;
			std::string name;
			std::string type;
		};
	}
}

#endif // KL_DISCO_IDENTITY_H__