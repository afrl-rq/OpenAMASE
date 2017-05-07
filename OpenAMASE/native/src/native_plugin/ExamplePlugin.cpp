
#include "ExamplePlugin.h"
#include <iostream>
#include "afrl/cmasi/VehicleActionCommand.h"
#include "afrl/cmasi/FlightDirectorAction.h"

ExamplePlugin::ExamplePlugin() {

	firedEvent = false;
}

void ExamplePlugin::step(double sim_time, double timestep) {

	std::cout << "sim time : " << sim_time << " time step : " << timestep << std::endl; 

	if (sim_time > 10 && !firedEvent) {

		firedEvent = true;

		afrl::cmasi::VehicleActionCommand* vac = new afrl::cmasi::VehicleActionCommand();
		vac->setVehicleID(1);

		afrl::cmasi::FlightDirectorAction* fda = new afrl::cmasi::FlightDirectorAction();
		fda->setHeading(10);
		vac->getVehicleActionList().push_back(fda);

		fireApplicationEvent(vac);
		
	}

}

void ExamplePlugin::initialize(avtas::lmcp::Node* xmlNode, const char* config_directory) {

	std::cout << " init plugin: " << xmlNode->toString() <<  std::endl;
}

void ExamplePlugin::app_event_occured(avtas::lmcp::Object* obj) {
	std::cout << "app event: " << obj->getLmcpTypeName() << std::endl;
}

void ExamplePlugin::shutdown() {
	std::cout << "shutdown" << std::endl;
}
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */