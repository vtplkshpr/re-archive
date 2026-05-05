import java.io.{BufferedOutputStream, File, FileOutputStream}
import java.nio.file.{Paths, Path}
import java.util.zip.GZIPOutputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import scala.io.StdIn.readLine

@main def runConverter(): Unit = {
  println("=== Scala Archive Converter (Zip/Rar to Tar.Gz) ===")
  print("Input file path: ")
  val inputStr = readLine().trim.replaceAll("\"", "")
  if (inputStr.isEmpty) return println("Path is required!")

  val inputPath = Paths.get(inputStr).toAbsolutePath
  val inputFile = inputPath.toFile
  if (!inputFile.exists()) return println(s"Error: File not found!")

  // Chọn processor
  val processor: Option[ArchiveProcessor] = inputFile.getName.toLowerCase match {
    case s if s.endsWith(".zip") => Some(ZipProcessor)
    case s if s.endsWith(".rar") => Some(RarProcessor)
    case _ => None
  }

  processor match {
    case None => println("Unsupported format! Only .zip and .rar are allowed.")
    case Some(proc) =>
      val baseName = inputFile.getName.substring(0, inputFile.getName.lastIndexOf("."))
      val destination = inputPath.getParent.resolve(s"$baseName.tar.gz").toFile

      try {
        val fos = new FileOutputStream(destination)
        val gzos = new GZIPOutputStream(new BufferedOutputStream(fos))
        val tos = new TarArchiveOutputStream(gzos)
        tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX)

        val total = proc.process(inputFile, tos)

        tos.finish()
        tos.close()
        gzos.close()
        
        println(s"\nSuccess! Total $total files converted to ${destination.getName}")
      } catch {
        case e: Exception => println(s"\nError: ${e.getMessage}")
      }
  }
}