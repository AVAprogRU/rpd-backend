package com.suai.department43.loutsker.rpddrafter.service.business.internal;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.enumeration.FileExtension;
import com.suai.department43.loutsker.rpddrafter.exception.business.DraftFailureException;
import com.suai.department43.loutsker.rpddrafter.exception.business.FileRecognitionException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Component
public class FileHelper {
    private String tempPath;

    public FileHelper() {
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    /**
     * Copies given XWPF document. The copy of the document is stored on temporal path.
     * Uses temporal file with path declared in the class field.
     * @param originalDocument document to be copied
     * @return copied XWPF document
     * @throws IOException
     */
    public XWPFDocument copyXWPFDocument(XWPFDocument originalDocument) throws IOException {
        clearTempFile();
        System.out.println("\ntempPath):" + tempPath);
        File tempFile = File.createTempFile(tempPath, FileExtension.DOCX.toString());
        FileOutputStream outStream = new FileOutputStream(tempFile);
        originalDocument.write(outStream);
        FileInputStream inStream = new FileInputStream(tempFile);
        XWPFDocument copy = new XWPFDocument(inStream);
        return copy;
    }

    /**
     * Clears file on temporal file path declared in the class field if such exists.
     * @throws IOException
     */
    private void clearTempFile() throws IOException {
        Files.deleteIfExists(Path.of(tempPath));
    }

    /**
     * Extracts document of given type from binary string of Base64 format.
     * Uses temporal file with path declared in the class field.
     * @param binaryString string of Base64 format
     * @param extension target file extension
     * @return extracted file
     */
    public File extractDocumentFromBase64Format(String binaryString, FileExtension extension) {
        try {
            clearTempFile();
            byte[] binaryData = Base64.getDecoder().decode(binaryString);
            File file = Files.createFile(Path.of(tempPath)).toFile();
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(binaryData);
            outStream.close();
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new FileRecognitionException(extension.toString());
        }
    }

    public String convertToBase64(XWPFDocument document) {
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            document.write(outByteStream);
            byte[] bytes = outByteStream.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            throw new DraftFailureException("Enable to convert to Base64 format");
        }

    }

    public String convertToBase64(XSSFWorkbook document) {
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            document.write(outByteStream);
            byte[] bytes = outByteStream.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            throw new DraftFailureException("Enable to convert to Base64 format");
        }
    }

    public String convertToBase64(Object object) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            byte[] bytes = bos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Error converting HashMap to Base64 string", e);
        }
    }
}
