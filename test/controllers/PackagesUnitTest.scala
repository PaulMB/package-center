package controllers

import models.PackageSummary
import org.specs2.mock.Mockito
import org.specs2.mutable._
import play.api.test._
import services.PackageService

import scala.concurrent.Future

class PackagesUnitTest extends Specification with Mockito {
  
  "Packages" should {
    
    "invoke find" in {
      val packageService = mock[PackageService]
      val packages = new controllers.Packages(packageService)

      packageService.find() returns Future.successful(List(PackageSummary("name", "description", "icon")))

      packages.findPackagesSummary(FakeRequest())

      there was one(packageService).find
    }
  }
}