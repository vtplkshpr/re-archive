import net.sf.sevenzipjbinding._
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream
import java.io.{File, RandomAccessFile}
import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveOutputStream}

object RarProcessor extends ArchiveProcessor {
  override def process(inputFile: File, tos: TarArchiveOutputStream): Unit = {
    // Khởi tạo thư viện
    
    val raf = new RandomAccessFile(inputFile, "r")
    val inStream = new RandomAccessFileInStream(raf)
    val archive = SevenZip.openInArchive(null, inStream)

    try {
      val count = archive.getNumberOfItems
      for (i <- 0 until count) {
        val fileName = archive.getProperty(i, PropID.PATH).asInstanceOf[String]
        val isFolder = archive.getProperty(i, PropID.IS_FOLDER).asInstanceOf[Boolean]
        val size = archive.getProperty(i, PropID.SIZE).asInstanceOf[Long]

        if (!isFolder && fileName != null) {
          val tarEntry = new TarArchiveEntry(fileName)
          tarEntry.setSize(size)
          tos.putArchiveEntry(tarEntry)

          // Sử dụng Callback đúng chuẩn của SevenZip
          archive.extract(Array(i), false, new IArchiveExtractCallback {
            override def getStream(index: Int, extractAskMode: ExtractAskMode): ISequentialOutStream = {
              if (extractAskMode != ExtractAskMode.EXTRACT) null
              else new ISequentialOutStream {
                override def write(data: Array[Byte]): Int = {
                  if (data == null || data.length == 0) return 0
                  tos.write(data)
                  data.length
                }
              }
            }

            override def prepareOperation(extractAskMode: ExtractAskMode): Unit = {}
            override def setOperationResult(extractOperationResult: ExtractOperationResult): Unit = {}
            override def setTotal(total: Long): Unit = {}
            override def setCompleted(complete: Long): Unit = {}
          })

          tos.closeArchiveEntry()
        }
      }
    } finally {
      archive.close()
      inStream.close()
      raf.close()
    }
  }
}
