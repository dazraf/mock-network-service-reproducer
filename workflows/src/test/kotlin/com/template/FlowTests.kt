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
import net.corda.testing.node.internal.CustomCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class FlowTests {
  // we collect the distinct set of paths in the event that we don't add the same cordapp twice
  private val cordapps = listOf(
    TemplateContract::class,
    Initiator::class
  ).map { it.packageName }.distinct().map { TestCordapp.findCordapp(it) }

  private val customTestCordapp = CustomCordapp(packages = setOf(TestCordaService::class.packageName),
    classes = setOf(TestCordaService::class.java))

  private val network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = cordapps + customTestCordapp))
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