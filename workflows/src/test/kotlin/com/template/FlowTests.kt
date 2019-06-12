package com.template

import com.template.contracts.TemplateContract
import com.template.flows.Initiator
import com.template.flows.Responder
import com.template.testing.TestCordaService
import net.corda.core.internal.packageName
import net.corda.core.utilities.getOrThrow
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.TestCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class FlowTests {
  private val network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = listOf(
    TestCordapp.findCordapp(TemplateContract::class.packageName),
    TestCordapp.findCordapp(Initiator::class.packageName),
    TestCordapp.findCordapp(TestCordaService::class.packageName)
  )))
  private val a = network.createNode()
  private val b = network.createNode()

  init {
    listOf(a, b).forEach {
      it.registerInitiatedFlow(Responder::class.java)
    }
  }

  @Before
  fun setup() = network.runNetwork()

  @After
  fun tearDown() = network.stopNodes()

  @Test
  fun `dummy test`() {
    // quick check that we can call a flow
    val result = a.startFlow(Initiator()).toCompletableFuture().also { network.runNetwork() }.getOrThrow()
    assertEquals(Int.MAX_VALUE, result)

    // this is where we look up our testing CordaService
    val service = a.services.cordaService(TestCordaService::class.java)
    // we never get here
    service.doSomethingUseful()
  }
}