package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.flows.*
import net.corda.core.utilities.ProgressTracker

// *********
// * Flows *
// *********
@InitiatingFlow
@StartableByRPC
class Initiator : FlowLogic<Int>() {
  override val progressTracker = ProgressTracker()

  @Suspendable
  override fun call() :Int {
    return Int.MAX_VALUE
  }
}

@InitiatedBy(Initiator::class)
class Responder(val counterpartySession: FlowSession) : FlowLogic<Unit>() {
  @Suspendable
  override fun call() {
    // Responder flow logic goes here.
  }
}
