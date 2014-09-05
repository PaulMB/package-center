package models

case class PackageSummary ( displayName: String,
                            description: String,
                            icon: String)

case class PackageDescription ( packageName: String,
                                version: String,
                                firmware: String,
                                architecture: String,
                                displayName: String,
                                description: Option[String],
                                link: String,
                                md5: Option[String],
                                icon: String,
                                size: Long = 0,
                                quickInstall: Boolean = false,
                                serviceDependencies: Option[String],
                                packageDependencies: Option[String],
                                start: Boolean = false,
                                maintainer: Option[String],
                                changeLog: Option[String],
                                beta: Boolean = false)

object PackageDescription {

  def fromMap(map: scala.collection.mutable.Map[String, String]): PackageDescription = {
    val properties: scala.collection.Map[String, String] = map.mapValues[String] { value =>
      value.replaceAllLiterally("\"", "")
    }
    PackageDescription(
      properties.get("package").get,
      properties.get("version").get,
      properties.get("firmware").get,
      properties.get("arch").get,
      properties.getOrElse("displayname", properties.get("package").get),
      properties.get("description"),
      properties.get("link").get,
      properties.get("md5"),
      properties.get("icon").get,
      properties.getOrElse("size", "5000000").toLong,
      properties.getOrElse("qinst", "false").toBoolean,
      properties.get("install_dep_services"),
      properties.get("install_dep_packages"),
      properties.getOrElse("start", "false").toBoolean,
      properties.get("maintainer"),
      properties.get("changelog"),
      properties.getOrElse("beta", "false").toBoolean
    )
  }
}

object PackageJsonFormats {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._
  /**
   * Explicit writer with field renaming when returning Json for DSM package center
   */
  val packageWriter: Writes[PackageDescription] = (
      (__ \ "package").write[String] and
      (__ \ "version").write[String] and
      (__ \ "firmware").write[String] and
      (__ \ "arch").write[String] and
      (__ \ "dname").write[String] and
      (__ \ "desc").write[Option[String]] and
      (__ \ "link").write[String] and
      (__ \ "md5").write[Option[String]] and
      (__ \ "icon").write[String] and
      (__ \ "size").write[Long] and
      (__ \ "qinst").write[Boolean] and
      (__ \ "depsers").write[Option[String]] and
      (__ \ "deppkgs").write[Option[String]] and
      (__ \ "start").write[Boolean] and
      (__ \ "maintainer").write[Option[String]] and
      (__ \ "changelog").write[Option[String]] and
      (__ \ "beta").write[Boolean]
    )(unlift(PackageDescription.unapply))
  /**
   * Explicit reader with field renaming when returning Json for DSM package center
   */
  val packageReader: Reads[PackageDescription] = (
      (__ \ "package").read[String] and
      (__ \ "version").read[String] and
      (__ \ "firmware").read[String] and
      (__ \ "arch").read[String] and
      (__ \ "dname").read[String] and
      (__ \ "desc").read[Option[String]] and
      (__ \ "link").read[String] and
      (__ \ "md5").read[Option[String]] and
      (__ \ "icon").read[String] and
      (__ \ "size").read[Long] and
      (__ \ "qinst").read[Boolean] and
      (__ \ "depsers").read[Option[String]] and
      (__ \ "deppkgs").read[Option[String]] and
      (__ \ "start").read[Boolean] and
      (__ \ "maintainer").read[Option[String]] and
      (__ \ "changelog").read[Option[String]] and
      (__ \ "beta").read[Boolean]
    )(PackageDescription.apply _)
  /*
   * Generates Writes and Reads
   */
  implicit val packageFormat = Json.format[PackageDescription]
  implicit val packageSummaryFormat = Json.format[PackageSummary]
}
