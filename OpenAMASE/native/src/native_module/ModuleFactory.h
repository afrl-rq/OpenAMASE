#ifndef MODULE_FACTORY
#define MODULE_FACTORY


#include <string>
#include <string.h>
#include "EntityModule.h"


namespace ModuleFactory {

	EntityModule* createModule(const char* name);
}

#endif
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */