package libraries;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileDownload {
    public  boolean downloadFile(String pathToSave, String fileAddress) throws IOException {
        String[] split = fileAddress.split("/");
        String finalFileName = split[split.length -1];
        split = null;

        String fileName = pathToSave + finalFileName;
        System.out.println(fileName);
        URL url = new URL(fileAddress);

        InputStream in = new BufferedInputStream(url.openStream());
        FileOutputStream out = new FileOutputStream(fileName);

//        System.out.println(String.format("Download %s start!", fileAddress));

        byte buffer[] = new byte[1024];
        int count = -1;
        while((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
        }
//        System.out.println(String.format("Download %s finish!", fileAddress));

        in.close();
        out.close();
        return false;
    }
}
