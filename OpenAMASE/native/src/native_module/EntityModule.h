#ifndef ENTITY_MODULE
#define ENTITY_MODULE

#include "avtas/lmcp/Object.h"
#include "avtas/lmcp/Node.h"
#include <jni.h>

class EntityModule {
public:

    /*
            The implementing module should override this method to receive timestep
            signals.  Times are in seconds.
     */
    virtual void step(double sim_time, double timestep);

    /*
            The implementing module should override this method to reveive initialization
            data.  
             - The entity_id is the id of the amase enitity that is associated with the model.  
             - The xmlNode is XML data that is contained in the input file where the module is created.
     */
    virtual void initialize(unsigned int entity_id, avtas::lmcp::Node* xmlNode);

    /*
            Events associated with the model.  The implementing module should override this method.  These
            events are NOT application-wide events, but events that are specific to the given aircraft or
            entity, such as navigational or payload actions.
     */
    virtual void model_event_occured(avtas::lmcp::Object* obj);

    /*
            Application-wide events.  Override this method to receive all LMCP events in the simulation.
     */
    virtual void app_event_occured(avtas::lmcp::Object* obj);

    /*
            Called when the module is removed on the Java side (such as by reseting the scenario). This 
            should be overridden to perform cleanup, such as releasing memory.
     */
    virtual void shutdown();

    //
    // Used internally by the JNI interface controller
    //

    void setJavaOwner(JNIEnv* java_env, jobject obj);

    jobject getJavaOwner();

    JNIEnv* getJavaEnvironment();

    void setEntityId(unsigned int id);

    unsigned int getEntityId();

    void fireModelEvent(avtas::lmcp::Object* obj);

    void fireApplicationEvent(avtas::lmcp::Object* obj);

	avtas::lmcp::Object* processEvent(JNIEnv* env, jbyteArray lmcp_bytes );

private:
    unsigned int entity_id;

    jobject calling_java_obj;

    JNIEnv* java_env;
    JavaVM* java_vm;

    jmethodID java_model_event_method;
    jmethodID java_app_event_method;



    void fireEvent(jmethodID methodId, avtas::lmcp::Object* obj);

};



#endif
/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */