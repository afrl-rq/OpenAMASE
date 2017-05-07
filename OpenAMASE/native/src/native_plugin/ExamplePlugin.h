#ifndef EXAMPLE_PLUGIN_H
#define EXAMPLE_PLUGIN_H

#include "AmasePlugin.h"

class ExamplePlugin : public AmasePlugin {

public:

	ExamplePlugin();

	virtual void step(double sim_time, double timestep);

	virtual void initialize(avtas::lmcp::Node* xmlNode, const char* config_directory);

	virtual void app_event_occured(avtas::lmcp::Object* obj);

	virtual void shutdown();

private:
	bool firedEvent;


};



#endif
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */