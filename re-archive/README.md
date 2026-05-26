## Run Scala project on terminal
# 1. Ép sbt nhận thư mục lưu cache và thư viện (.jar) nằm trong USB (Thay vì ổ C máy host)
$env:SBT_GLOBAL_BASE="E:\scala-portable\sbt-global"
$env:SBT_BOOT_DIRECTORY="E:\scala-portable\sbt-boot"
$env:REPOSITORIES="E:\scala-portable\ivy2-cache"
$env:IVY_HOME="E:\scala-portable\ivy2-cache"

# 2. Khai báo đường dẫn tạm thời trỏ vào thư mục chứa file sbt.bat trong USB
$env:PATH="E:\scala-portable\sbt\bin;" + $env:PATH

# 3. Giờ bạn gõ lệnh sbt để dev/test như bình thường
sbt
