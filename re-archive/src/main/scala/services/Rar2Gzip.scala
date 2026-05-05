import com.github.junrar.Archive
import com.github.junrar.rarfile.FileHeader
import java.io.File
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import java.io.FileInputStream 

object RarProcessor extends ArchiveProcessor {
  override def process(inputFile: File, tos: TarArchiveOutputStream): Unit = {
    val archive = new Archive(inputFile)
    try {
      var header = archive.nextFileHeader()
      while (header != null) {
        if (!header.isDirectory) {
          val tarEntry = new TarArchiveEntry(header.getFileNameString)
          tarEntry.setSize(header.getFullUnpackSize)
          tos.putArchiveEntry(tarEntry)
          
          archive.extractFile(header, tos)
          
          tos.closeArchiveEntry()
        }
        header = archive.nextFileHeader()
      }
    } finally {
      archive.close()
    }
  }
}