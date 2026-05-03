import java.io.{File, FileInputStream, FileOutputStream, BufferedOutputStream}
import java.util.zip.GZIPOutputStream
import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveOutputStream}
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.utils.IOUtils
import java.nio.file.{Paths, Path}
import scala.io.StdIn.readLine

@main def runConverter(): Unit = {
  println("=== Scala Archive Converter (Zip to Tar.Gz) ===")
  print("Chossing path (Zip/Rar): ")
  val inputStr = readLine().trim.replaceAll("\"", "")
  if (inputStr.isEmpty) return println("Path is required !")

  val inputPath: Path = Paths.get(inputStr).toAbsolutePath
  val inputFile = inputPath.toFile

  if (!inputFile.exists()) return println(s"Error: File not found at ${inputPath}")

  // 2. Yêu cầu nhập thư mục đầu ra
  print(s"Output re-archive file path (Press Enter to use default path: ${inputFile.getParent}): ")
  val outputDirStr = readLine().trim.replaceAll("\"", "")
  
  val outputDir: Path = if (outputDirStr.isEmpty) {
    inputPath.getParent
  } else {
    Paths.get(outputDirStr).toAbsolutePath
  }

  val outDirFile = outputDir.toFile
  if (!outDirFile.exists()) outDirFile.mkdirs()

  val baseName = inputFile.getName.substring(0, inputFile.getName.lastIndexOf("."))
  val destination = outputDir.resolve(s"$baseName.tar.gz").toFile
  
  try {
    println(s"\nProcessing: ${inputFile.getName}")
    println(s"Saved path: ${destination.getAbsolutePath}")

    val fos = new FileOutputStream(destination)
    val gzos = new GZIPOutputStream(new BufferedOutputStream(fos))
    val tos = new TarArchiveOutputStream(gzos)
    tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX)

    val fis = new FileInputStream(inputFile)
    val zis = new ZipArchiveInputStream(fis)
    
    var entry = zis.getNextZipEntry
    var fileCount = 0
    
    while (entry != null) {
      if (!entry.isDirectory) {
        val tarEntry = new TarArchiveEntry(entry.getName)
        tarEntry.setSize(entry.getSize)
        
        tos.putArchiveEntry(tarEntry)
        IOUtils.copy(zis, tos)
        tos.closeArchiveEntry()
        
        fileCount += 1
        print(s"\rRe-archived $fileCount file...")
      }
      entry = zis.getNextZipEntry
    }

    zis.close()
    tos.finish()
    tos.close()
    gzos.close()
    
    println(s"\n\nRe-archive success! Total $fileCount file convertered to Gzip.")
    println(s"File saved in: ${destination.getAbsolutePath}")

  } catch {
    case e: Exception => 
      println(s"\nRe-archive error: ${e.getMessage}")
      e.printStackTrace()
  }
}
