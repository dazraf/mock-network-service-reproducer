This reproducer aims to create the conditions that we have in our project source with Corda 4.x.

We sometimes have to create an additional `@CordaService`, defined in the `test` source, that provides access and 
diagnostics not available via CordaRPC. In Corda 3.x it was possible to inject this class using its package, into the 
MockNetwork construction e.g.

```kotlin
  private val network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = listOf(
    TestCordapp.findCordapp(TemplateContract::class.packageName),
    TestCordapp.findCordapp(Initiator::class.packageName),
    TestCordapp.findCordapp(TestCordaService::class.packageName)
  )))
```

This no longer works. Is there another way of achieving the intended outcome?

## ... after some experimentation

I found the very useful class `net.corda.testing.node.internal.CustomCordapp`

```kotlin
  // we collect the distinct set of paths in the event that we don't add the same cordapp twice
  private val cordapps = listOf(
    TemplateContract::class,
    Initiator::class
  ).map { it.packageName }.distinct().map { TestCordapp.findCordapp(it) }

  private val customTestCordapp = CustomCordapp(packages = setOf(TestCordaService::class.packageName),
    classes = setOf(TestCordaService::class.java))

  private val network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = cordapps + customTestCordapp))
  // ... 
```