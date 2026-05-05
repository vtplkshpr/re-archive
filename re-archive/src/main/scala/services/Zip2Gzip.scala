import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import java.io.File
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import java.io.FileInputStream 

object ZipProcessor extends ArchiveProcessor {
  override def process(inputFile: File, tos: TarArchiveOutputStream): Unit = {
    val zis = new ZipArchiveInputStream(new java.io.FileInputStream(inputFile))
    try {
      var entry = zis.getNextZipEntry
      while (entry != null) {
        if (!entry.isDirectory) {
          val tarEntry = new TarArchiveEntry(entry.getName)
          tarEntry.setSize(entry.getSize)
          tos.putArchiveEntry(tarEntry)
          
          val buffer = new Array[Byte](8192)
          var len = 0
          while ({ len = zis.read(buffer); len != -1 }) {
            tos.write(buffer, 0, len)
          }
          tos.closeArchiveEntry()
        }
        entry = zis.getNextZipEntry
      }
    } finally {
      zis.close()
    }
  }
}