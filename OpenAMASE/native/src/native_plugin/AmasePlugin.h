#ifndef ENTITY_PLUGIN
#define ENTITY_PLUGIN

#include "avtas/lmcp/Object.h"
#include "avtas/lmcp/Node.h"
#include <jni.h>

class AmasePlugin {
public:

    virtual string getPluginName();

    /*
            The implementing plugin should override this method to receive timestep
            signals.  Times are in seconds.
     */
    virtual void step(double sim_time, double timestep);

    /*
            The implementing plugin should override this method to receive initialization
            data.   
             - The xmlNode is XML data that is contained in the input file where the plugin is created.
			 - the config_directory is the config folder used to initialize the application.
     */
    virtual void initialize(avtas::lmcp::Node* xmlNode, const char* config_directory);

    /*
            Application-wide events.  Override this method to receive all LMCP events in the simulation.
     */
    virtual void app_event_occured(avtas::lmcp::Object* obj);

    /*
            Called when the plugin is removed on the Java side (such as by reseting the scenario). This 
            should be overridden to perform cleanup, such as releasing memory.
     */
    virtual void shutdown();

    //
    // Used internally by the JNI interface controller
    //

    void setJavaOwner(JNIEnv* java_env, jobject obj);

    jobject getJavaOwner();

    JNIEnv* getJavaEnvironment();
	
	char* plugin_name;


    void fireApplicationEvent(avtas::lmcp::Object* obj);

	void processEvent(JNIEnv* env, jbyteArray lmcp_bytes );

private:
    unsigned int entity_id;

    jobject calling_java_obj;

    JNIEnv* java_env;
    JavaVM* java_vm;

    jmethodID java_app_event_method;



    void fireEvent(jmethodID methodId, avtas::lmcp::Object* obj);

};



#endif
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */