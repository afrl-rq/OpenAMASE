#ifndef EXAMPLE_MODULE_H
#define EXAMPLE_MODULE_H

#include "EntityModule.h"

class ExampleModule : public EntityModule {

public:

	ExampleModule();

	virtual void step(double sim_time, double timestep);

	virtual void initialize(unsigned int entity_id, avtas::lmcp::Node* xmlNode);

	virtual void model_event_occured(avtas::lmcp::Object* obj);

	virtual void app_event_occured(avtas::lmcp::Object* obj);

	virtual void shutdown();

private:
	bool firedEvent;


};



#endif
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */