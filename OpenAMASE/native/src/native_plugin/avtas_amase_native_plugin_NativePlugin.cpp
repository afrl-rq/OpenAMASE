

#include "avtas_amase_native_plugin_NativePlugin.h"
#include <iostream>
#include "avtas\lmcp\Factory.h"
#include "PluginFactory.h"
#include <vector>
#include "avtas/lmcp/XMLParser.h"

vector<AmasePlugin*> pluginList;


JNIEXPORT jlong JNICALL Java_avtas_amase_natives_NativePlugin_createNativePlugin
  (JNIEnv *env, jobject callingObj, jstring name) {

	  jboolean isCopy;
	  const char* c_name = env->GetStringUTFChars(name, &isCopy );
	  
	  std::cout << "loading new plugin: " << c_name << std::endl;

	  AmasePlugin* pi = PluginFactory::createPlugin(c_name);

	  jlong ret = -1L;

	  if (pi != NULL) {
		  pluginList.push_back(pi);
		  pi->setJavaOwner( env, callingObj );
		  ret =  pluginList.size()-1;
	  }

	  if (isCopy) {
		  env->ReleaseStringUTFChars(name, c_name);
	  }

	  return ret;
}


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativePlugin_nativeStep
  (JNIEnv *env, jobject callingObj, jlong modulePtr, jdouble simTime, jdouble timestep) {
	  
	  if (modulePtr == -1) {
		  return;
	  }
	  AmasePlugin* module = pluginList[modulePtr];

	  if (module != NULL) {
		  module->step(simTime, timestep);
	  }

}


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativePlugin_nativeInitialize
  (JNIEnv *env, jobject callingObj, jlong modulePtr, jstring xmlStr, jstring config_dir) {

	  if (modulePtr == -1) {
		  return;
	  }
	  AmasePlugin* module = pluginList[modulePtr];

	  jboolean isCopy;
	  const char* xml_cstr = env->GetStringUTFChars(xmlStr, &isCopy);
	  const char* config_dir_cstr = env->GetStringUTFChars(config_dir, &isCopy);

	  if (module != NULL) {
		  avtas::lmcp::Node* xmlNode = avtas::lmcp::XMLParser::parseString( xml_cstr, false);
		  module->initialize( xmlNode, config_dir_cstr);
	  }

	  if (isCopy) {
		  env->ReleaseStringUTFChars(xmlStr, xml_cstr);
	  }
}


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativePlugin_nativeAppEventOccured
  (JNIEnv *env, jobject callingObj, jlong modulePtr, jbyteArray lmcp_bytes) {

	  if (modulePtr == -1) {
		  return;
	  }
	  AmasePlugin* module = pluginList[modulePtr];
	  
	  if (module != NULL) {
		  module->processEvent(env, lmcp_bytes);
	  }
}


JNIEXPORT void JNICALL Java_avtas_amase_natives_NativePlugin_nativeShutdown
  (JNIEnv *, jobject, jlong modulePtr ) {

	  if (modulePtr == -1) {
		  return;
	  }
	  AmasePlugin* module = pluginList[modulePtr];

	  if (module != NULL) {
		  module->shutdown();
                  delete module;
	  }


}




/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */