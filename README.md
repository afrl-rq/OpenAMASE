# License

*OpenAMASE* is developed by the Air Force Research Laboratory, Aerospace System Directorate, Power and Control Division.
All source code for *OpenAMASE* is publicaly released under the Air Force Open Source Agreement
Version 1.0. See LICENSE.md for complete details. The Air Force Open Source Agreement closely follows the NASA Open Source
Agreement Verion 1.3. **NOTE the terms of the license include registering use of the software by emailing <a href="mailto:afrl.rq.opensource@us.af.mil?subject=OpenAMASE Registration&body=Please register me for use of OpenAMASE. Name: ____________">afrl.rq.opensource@us.af.mil</a>.**

# Introduction

The development of UAV command and control technologies can be aided greatly through the use of simulation. The AMASE simulation (Aerospace Multi-agent Simulation Environment) aims to provide a basic simulation environment for the demonstration and testing of UAV control technologies. AMASE brings several vehicle motion, systems, and control models together to form a single, basic-fidelity model for UAV simulation. It is designed to allow for command and control of multiple UAVs using control algorithms within the simulation as well as external control via a network interface.

## Full UAV Mission-Level Simulation
AMASE is a simulation toolset for the analysis and demonstration of aircraft automation and autonomy. AMASE includes the necessary components to create scenarios, simulate aircraft with basic EO/IR cameras, and interact with control algorithms to command aircraft in a scenario. Graphical user interfaces allow AMASE users to evaluate the actions of one or more aircraft at runtime, and the data output feature saves scenario data for post-processing. An integrated network server allows clients to connect to AMASE and send/receive data. AMASE includes three main programs: the simulation, a data playback tool, and a scenario setup tool.

The AMASE simulation models 5-DOF (coordinated turning) flight dynamics with self-configured performance at a set of design points. AMASE UAVs feature an autopilot that manages coordinated turns, altitude hold, heading hold, vertical speed hold, speed hold (auto-throttle), maintain track in wind fields, loiters (Figure-Eight, Orbit (circular), Racetrack) and waypoint following. Additionally, AMASE UAVs can be equipped with gimbaled and fixed sensors and the simualtion performs footprint analysis for target detection and includes line-of-sight calculation for obscuration of sensors by terrain.

## Requirements

*OpenAMASE* is a Java program that will run on any system with [Java][java download] installed.

[java download]: https://java.com/en/download/

To modify *OpenAMASE*, the Java JDK 1.8 or higher is required. All external libraries that *OpenAMASE*
requires are included in the `lib` folder. For convenience, Netbeans project files are included to allow
developers a quick way to change and re-build *OpenAMASE*.

## Running *OpenAMASE*

*OpenAMASE* has multiple options and configurations. For details, see `AMASE Tutorial.pptx` and `AmaseQuickstart.pdf`. In-depth
documentation is provided in the `docs` directory.

To run *OpenAMASE* without building from source, the entire source must still be downloaded (due to dependancies on multiple
libraries). The release version of `OpenAMASE.jar` should then be placed in the `dist` directory.

## 3rd Party Licenses
AMASE uses several 3rd party libraries distributed under open source licenses. The binary libraries are included in the `lib` folder. The original source, data, and applicable license files can be found in the subfolders of `lib`.
