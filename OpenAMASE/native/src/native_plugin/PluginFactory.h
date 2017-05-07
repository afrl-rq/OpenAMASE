#ifndef PLUGIN_FACTORY
#define PLUGIN_FACTORY


#include <string>
#include <string.h>
#include "AmasePlugin.h"


namespace PluginFactory {

	AmasePlugin* createPlugin(const char* name);
}

#endif
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */