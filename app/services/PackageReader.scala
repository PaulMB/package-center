package services

import java.io._
import java.net.URI
import java.util.Properties

import com.migcomponents.migbase64.Base64IO
import models.PackageDescription
import org.apache.commons.compress.archivers.ArchiveStreamFactory

import scala.collection.mutable
import scala.util.control.Exception._

object PackageReader {

  def withCloseable[T <: Closeable, R](t: T)(f: T => R): R = {
    allCatch.andFinally{
      t.close()
    }.apply {
      f(t)
    }
  }

  def getProperties (uri: URI): mutable.Map[String, String] = {
    import scala.collection.JavaConverters._

    val properties: Properties = new Properties()
    withCloseable(new ArchiveStreamFactory().createArchiveInputStream(new BufferedInputStream(uri.toURL.openStream()))) { archiveStream =>
      Stream.continually(archiveStream.getNextEntry).takeWhile(_ != null).foreach(entry =>
        entry.getName match {
          case "INFO" => properties.load(archiveStream)
          case "PACKAGE_ICON.PNG" => properties.put("icon",  base64Encode(archiveStream))
          case _ =>
        })
    }
    properties.put("link", uri.toString)
    properties.asScala
  }

  def readPackage (uri: URI): PackageDescription = {
    val properties: mutable.Map[String, String] = getProperties(uri)
    PackageDescription.fromMap(properties)
  }

  def base64Encode (stream: InputStream): String = {
    val buffer = new ByteArrayOutputStream()
    Base64IO.encode(new BufferedInputStream(stream), buffer, false)
    buffer.toString
  }

}
