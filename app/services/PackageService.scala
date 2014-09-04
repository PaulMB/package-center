package services

import models.{PackageDescription, PackageSummary}

import scala.concurrent.Future

trait PackageService {

  /**
   * Return the list of packages available for the specified architecture and dsm version. Packages with 'noarch'
   * architecture can be deployed on any architecture.
   * @return the packages as Json
   */
  def findMatching (installedDsmFirmware: String, arch: String): Future[List[PackageDescription]]

  /**
   * Return a summary of each unique package
   * @return the summaries as Json
   */
  def find (): Future[List[PackageSummary]]

  /**
   * Get a description of the package identified by its name, architecture and version
   * @param name the package name
   * @param arch its architecture
   * @param version its version
   * @return future
   */
  def get (name: String, arch: String, version: String): Future[Option[PackageDescription]]

  /**
   * Insert a new package description in the persistence layer
   * @param packageDescription the description
   * @return future
   */
  def insert (packageDescription: PackageDescription): Future[_]

  /**
   * Delete a package identified by its name, architecture and version
   * @param name the package name
   * @param arch its architecture
   * @param version its version
   * @return future
   */
  def delete (name: String, arch: String, version: String): Future[_]

}
