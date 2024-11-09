import libraries.FileDownload;
import libraries.FilePathGetter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        FilePathGetter getter = new FilePathGetter();
        String pageAddress = String.format("https://tosk.tmb.ru/yuridicheskim_litsam/normativno_pravovaya_baza/");
        String downloadFolderName = "Download";
        try {
            download(pageAddress, downloadFolderName, getter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void download(String pageAddress, String downloadFolderName, FilePathGetter getter) throws IOException {
        Map<String, String> documentsLinks = getter.getDocumentsLinks(pageAddress, "https://www.tosk.tmb.ru", "UTF-8", true);
        List<String> errorList = new ArrayList<String>();
        FileDownload downloader = new FileDownload();
        String currentDir = System.getProperty("user.dir") + "/";
        errorList.add(String.format("Don't downloaded from %s:\n", pageAddress));
        for (Map.Entry<String, String> stringStringEntry : documentsLinks.entrySet()) {
            String filePath = currentDir +
                    "/" +
                    downloadFolderName + "/" + stringStringEntry.getValue().replace("\t","") + "1/";
            new File(filePath).mkdirs();
            try {
                boolean b = downloader.downloadFile(filePath, stringStringEntry.getKey().replace("\t",""));
            } catch (IOException e) {
                errorList.add(stringStringEntry.getValue() + " = " + stringStringEntry.getKey() + " = " + e.getMessage() + "\n");
                continue;
            }
        }
        {// ERROR Save
            FileWriter writer = new FileWriter(currentDir + "/" + downloadFolderName + "/" + "error.txt");
            for (String str : errorList) {
                writer.write(str);
            }
            writer.close();
        }
        downloader = null;
    }
}
