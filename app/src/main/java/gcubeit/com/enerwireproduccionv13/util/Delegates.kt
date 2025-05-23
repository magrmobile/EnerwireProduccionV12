package gcubeit.com.enerwireproduccionv13.util

import kotlinx.coroutines.*

@DelicateCoroutinesApi
fun<T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}