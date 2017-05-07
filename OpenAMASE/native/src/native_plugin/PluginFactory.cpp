


#include "PluginFactory.h"
#include <stdio.h>
#include <stdlib.h>

#include "ExamplePlugin.h"

namespace PluginFactory {

// override this function with code to create a new plugin by name
AmasePlugin* createPlugin(const char* name) {

    AmasePlugin* pi = NULL;
	if ( strcmp(name, "Example") == 0 ) {
		pi = new ExamplePlugin();
	}
	// add custom code here to find a matching plugin

	if (pi != NULL) {
		std::cout << "created plugin" << std::endl;
		pi->plugin_name = (char*) name;
	}
	
	return pi;
}

};
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */