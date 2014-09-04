import com.google.inject.{AbstractModule, Guice}
import play.api.GlobalSettings
import reactivemongo.api.DefaultDB
import services.{ReactiveMongoDBProvider, PackageService, ReactiveMongoPackageService}

/**
 * Set up the Guice injector
 */
object Global extends GlobalSettings {

  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(classOf[DefaultDB]).toProvider(classOf[ReactiveMongoDBProvider])
      bind(classOf[PackageService]).to(classOf[ReactiveMongoPackageService])
    }
  })

  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)
}
