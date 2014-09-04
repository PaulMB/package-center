package services

import javax.inject.Inject

import models.{PackageDescription, PackageSummary}
import play.api.libs.json.Json
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.indexes.{Index, IndexType, NSIndex}

import scala.concurrent.Future

class ReactiveMongoPackageService @Inject() (db: reactivemongo.api.DefaultDB) extends PackageService {

  import models.PackageJsonFormats._

import scala.concurrent.ExecutionContext.Implicits.global

  /**
   * Ensure the base is indexed on package name, architecture and version
   */
  db.indexesManager.ensure(NSIndex("packages", Index(Seq(
    "packageName" -> IndexType.Ascending,
    "architecture" -> IndexType.Ascending,
    "version" -> IndexType.Ascending
  ), name = Some("packageIndex"), unique = true)))

  /**
   * @return the packages collection
   */
  def collection: JSONCollection = db.collection[JSONCollection]("packages")

  //TODO get one version
  //TODO multi architecture support
  override def findMatching(installedDsmFirmware: String, arch: String): Future[List[PackageDescription]] = collection.
    find(Json.obj(
    "firmware" -> Json.obj("$lt" -> installedDsmFirmware),
    "$or" -> Json.arr(Json.obj("architecture" -> "noarch"), Json.obj("architecture" -> arch)))
    ).
    cursor[PackageDescription].collect[List]()

  //TODO unique
  override def get(name: String, arch: String, version: String): Future[Option[PackageDescription]] = collection.find(Json.obj(
        "packageName" -> name,
        "architecture" -> arch,
        "version" -> version)
      ).cursor[PackageDescription].headOption

  override def delete(name: String, arch: String, version: String): Future[_] = collection.remove(Json.obj(
        "packageName" -> name,
        "architecture" -> arch,
        "version" -> version)
      )

  override def find(): Future[List[PackageSummary]] = collection.find(Json.obj()).
          cursor[PackageSummary].
          collect[List]()

  override def insert(packageDescription: PackageDescription): Future[_] = collection.insert(packageDescription)
}
