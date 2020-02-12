package main;

import java.io.*;

/**
 * Разделяет один большой лог на файлs поменьше
 * Принимает в качестве аргументов имя лога и постоянную часть имени файлов
 * создаёт пять файлов, создаёт директорию и складывает туда файлы
 */
public class Splitter {

    String nameOfLog;
    String constPartOfNameFile;
    String outputDirectoryName;
    long numSplits;
    String pathForNewFile = null;

    public Splitter(String nameOfLog, String constPartOfNameFile, String outputDirectoryName, long numSplits){
        this.nameOfLog = nameOfLog;
        this.constPartOfNameFile = constPartOfNameFile;
        this.outputDirectoryName = outputDirectoryName;
        this.numSplits = numSplits;
    }

    // Cоздать директорию для выходных файлов
    public boolean makeDirectoryAndPathForOutFiles(){
        //Создаём директорию и формируем путь для новых файлов
        File file = new File(nameOfLog);
        File dir = new File(file.getParent() + "/" + outputDirectoryName);
        boolean createdNewDirectory = dir.mkdir();
        if(createdNewDirectory){
            pathForNewFile = dir.getAbsolutePath() + "/" + constPartOfNameFile;
            System.out.println("Directory was created.");
            return true;
        }else{
            return false;
        }
    }

    // Выполняет чтение байт из входного файла и записывает их в буфер выходного файла
    private void readInputFileWriteOutputFiles(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        // Если есть что читать в объекте входного файла, писать в буфер выходного файла
        if (val != -1) {
            bw.write(buf);
        }
    }

    public void splitAndWriteNewFiles() throws IOException {
        // Данный объект позволяет читать входной файл с любого места, где стоит курсор
        RandomAccessFile raf = new RandomAccessFile(nameOfLog, "r");
        // Количество выходных файлов
        long numSplits = this.numSplits;
        // Измеряем размер входного файла
        long sourceSize = raf.length();
        // Определяем размер выходного файла
        long bytesPerSplit = sourceSize / numSplits;
        // Определяем остаток байт
        long remainingBytes = sourceSize % numSplits;
        // Устанавливам размер буфера
        int maxReadBufferSize = 8 * 1024;
        // Для каждого из числа выходных файлов
        for(int outFile = 1; outFile <= numSplits; outFile++) {
            // Создаём буфер для потока байт в выходной файл
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(pathForNewFile + outFile));
            // Если размер выходного файла больше буфера
            if (bytesPerSplit > maxReadBufferSize) {
                // Определяем число раз чтения
                long numReads = bytesPerSplit / maxReadBufferSize;
                // Определяем остаток байт
                long numRemainingRead = bytesPerSplit % maxReadBufferSize;
                // Пока индекс меньше числа раз чтения
                for (int i = 0; i < numReads; i++) {
                    // Читать из входного файла и писать в выходной файл
                    readInputFileWriteOutputFiles(raf, bw, maxReadBufferSize);
                }
                // Если есть остаток нераспределённых байт
                if (numRemainingRead > 0) {
                    // Дочитать и дописать в выходной файл остаток байт
                    readInputFileWriteOutputFiles(raf, bw, numRemainingRead);
                }
            } else {
                // Читать из входного файла и писать в выходной файл
                readInputFileWriteOutputFiles(raf, bw, bytesPerSplit);
            }
            // Закрыть буфер выходного файла
            bw.close();
        }
        // Если есть остаток байт во входном файле
        if(remainingBytes > 0) {
            // Дописываем остаток байт в отдельный файл
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(pathForNewFile + "split." + (numSplits + 1)));
            readInputFileWriteOutputFiles(raf, bw, remainingBytes);
            bw.close();
        }
        System.out.println("Finished create new files.");
        raf.close();
    }
}