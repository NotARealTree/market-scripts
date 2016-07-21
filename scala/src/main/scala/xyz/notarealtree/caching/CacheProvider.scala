package xyz.notarealtree.caching

/**
  * Created by Francis on 21/07/2016.
  */
trait CacheProvider {
    def isCached(query: String): Boolean
    def getCachedValue(query: String): String
    def cacheValue(query: String, value: String): Unit
}
