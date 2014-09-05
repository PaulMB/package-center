package controllers

import java.net.URI
import javax.inject.{Inject, Singleton}

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import services.{PackageReader, PackageService}

import scala.concurrent.Future

/**
 * Packages controller
 */
@Singleton
class Packages @Inject() (packageService: PackageService) extends Controller {

  import models.PackageJsonFormats._
  import models._

  /**
   * Return the list of packages available for the specified architecture and dsm version. Packages with 'noarch'
   * architecture can be deployed on any architecture.
   * @return the packages as Json
   */
  def findMatchingPackages = Action.async(parse.urlFormEncoded) {
    request =>
      /** Implicit writer for field name mapping */
      implicit val reader = packageWriter
      val body: Map[String, String] = request.body.mapValues( l => l.head)
      val list: Future[List[PackageDescription]] = packageService.findMatching(body.get("major") + "." + body.get("minor") + "-" + body.get("build"), body.get("arch").get)
      list.map { packages =>
        Ok(new JsArray(packages.map( item => Json.toJson(item)(packageWriter))))
      }
  }

  /**
   * Return a summary of each unique package
   * @return the summaries as Json
   */
  def findPackagesSummary = Action.async {
    packageService.find().map { packages =>
      Json.arr(packages)
    }.map { packages =>
      Ok(packages(0))
    }
  }

  /**
   * Delete a package identified by its name, architecture and version
   * @param name the package name
   * @param arch its architecture
   * @param version its version
   * @return result
   */
  def deletePackage(name: String, arch: String, version: String) = Action.async {
    packageService.delete(name, arch, version).map( status =>
      Ok("")
    )
  }

  /**
   * Get a redirection to download link of the package identified by its name, architecture and version
   * @param name the package name
   * @param arch its architecture
   * @param version its version
   * @return result
   */
  def getPackage(name: String, arch: String, version: String) = Action.async {
    packageService.get(name, arch, version).map {
      case None => NotFound("")
      case Some(packageDescription) => Redirect(packageDescription.link)
    }
  }

  /**
   * Add metadata for the package specified in the body URL. Does not store the package content.
   * @return
   */
  def createExternalPackage = Action.async(parse.text) {
    request =>
      val description = PackageReader.readPackage(URI.create(request.body))
      packageService.insert(description).map {
        _ => Ok(Json.toJson(description)(packageWriter))
      }
  }
}
