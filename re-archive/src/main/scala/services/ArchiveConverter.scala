import java.io.InputStream
import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveOutputStream}
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.File

trait ArchiveProcessor {
  def process(inputFile: File, tos: TarArchiveOutputStream): Unit
}