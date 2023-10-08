package se331.lab.rest.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import jakarta.servlet.ServletException;
import se331.lab.rest.entity.StorageFileDTO;

@Component("cloudStorageHelper")
public class CloudStorageHelper {
    private static Storage storage = null;
    static {
        InputStream serviceAccount = null;
        try {
            serviceAccount = new ClassPathResource("am-l-4b665-004def44257e.json").getInputStream();
            storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("am-l-4b665").build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String uploadFile(MultipartFile filePart, final String bucketName) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmssSSS");
        String dtString = sdf.format(new Date());
        final String fileName = String.format("%s-%s", dtString, filePart.getOriginalFilename());
        InputStream inputStream = filePart.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] readBuf = new byte[4096];
        while (inputStream.available() > 0) {
            int bytesRead = inputStream.read(readBuf);
            byteArrayOutputStream.write(readBuf, 0, bytesRead);
        }
        BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName, fileName)
                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                .setContentType(filePart.getContentType()).build(), byteArrayOutputStream.toByteArray());
        return blobInfo.getMediaLink();
    }

    public String getImageUrl(MultipartFile file, final String bucketName) throws IOException, ServletException {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
            final String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String[] allowedExt = { "jpg", "jpeg", "png", "gif" };
            for (String string : allowedExt) {
                if (extension.toLowerCase().equals(string)) {
                    return this.uploadFile(file, bucketName);
                }
            }
            throw new ServletException("File must be jpg, jpeg, png, or gif");
        }
        return null;
    }

    public StorageFileDTO getStorageFileDTO(MultipartFile file, final String bucketName)
            throws IOException, ServletException {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
            final String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String[] allowedExt = { "jpg", "jpeg", "png", "gif" };
            for (String string : allowedExt) {
                if (extension.toLowerCase().equals(string)) {
                    String urlName = uploadFile(file, bucketName);
                    return StorageFileDTO.builder().name(urlName).build();
                }
            }
            throw new ServletException("File must be jpg, jpeg, png, or gif");
        }
        return null;
    }
}
