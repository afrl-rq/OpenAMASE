


#include "ModuleFactory.h"
#include <stdio.h>
#include <stdlib.h>

#include "ExampleModule.h"

namespace ModuleFactory {

// override this function with code to create a new entity module by name
EntityModule* createModule(const char* name) {

	if ( strcmp(name, "Example") == 0 ) {

		return new ExampleModule();
	}

	return NULL;
}

};
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */