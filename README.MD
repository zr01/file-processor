Pre-requisites:
- If using a Linux machine, make sure to create the following directories beforehand:
  /var/log/file-processor (chmod 777 just to make sure)
  /var/spool/futures (chmod 777 just to make sure)
- Alternatively, if lazy, just use sudo on the command line prior to running any commands in this README
- If using a Windows machine, it should automatically create the directories on its own
- Install Java (JDK8) and Maven

Running:
1. Open terminal or command prompt
2. Navigate to the directory where you cloned the file-processor, it should have a pom.xml when you check the directory via ls or dir
3. Type the following command: mvn spring-boot:run
4. Copy the file you want to process in /var/spool/futures/pending
5. Output file will be present in /var/spool/futures/output/Output.csv
6. If the file processes successfully, the input file will be moved to /var/spool/futures/processed
7. If an error occurs, you can find the input file in /var/spool/futures/error

The log file can be found in /var/log/file-processor/file-processor.log.

Advanced Configuration
- Directories can be configured to a different location, it can be changed by using the -D parameters when running 'mvn spring-boot:run'
  Example:
  mvn spring-boot:run \
  -Dfile.processor.futures.input-dir=/tmp/spool/futures/pending \
  -Dfile.processor.futures.processed-dir=/tmp/spool/futures/processed \
  -Dfile.processor.futures.error-dir=/tmp/spool/futures/error \
  -Dfile.processor.futures.output-dir=/tmp/spool/futures/output
