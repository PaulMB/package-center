package services

import com.google.inject.Provider
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api.DefaultDB

class ReactiveMongoDBProvider extends Provider[DefaultDB] {
  override def get(): DefaultDB = {
    import play.api.Play.current
    ReactiveMongoPlugin.db
  }
}
