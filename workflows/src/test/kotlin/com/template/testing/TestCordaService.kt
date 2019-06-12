package com.template.testing

import net.corda.core.node.AppServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.utilities.loggerFor

/**
 * This service is representative of a service we use in our primary project.
 * It contains logic that's very useful from the perspective of our cordapps testing.
 * Historically in Corda 3.x we could load this service into the MockNetwork node's classpath
 * This is no longer the case. The log line in the initialiser is never called.
 */
@CordaService
class TestCordaService(serviceHub: AppServiceHub) : SingletonSerializeAsToken() {
  companion object {
    private val log = loggerFor<TestCordaService>()
  }

  init {
    log.info("*** ${TestCordaService::class.simpleName} Started for ${serviceHub.myInfo.legalIdentities.first().name} ***")
  }

  fun doSomethingUseful() {
    log.info("doing something useful ...")
  }
}