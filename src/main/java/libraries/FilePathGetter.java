package libraries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilePathGetter {
    public Map<String, String> getDocumentsLinks(String htmlPageAddress, String addressPrefix, String pageEncoding, boolean onlyDownloads){
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        Map<String, String> result = new HashMap<String, String>();
        try {
//            System.out.println(htmlPageAddress);
            url = new URL(htmlPageAddress);
            is = url.openStream();  // throws an IOException
            InputStreamReader tmpInputStream = new InputStreamReader(is, pageEncoding);
//            System.out.println("tmpInputStream.getEncoding() " + tmpInputStream.getEncoding());
            br = new BufferedReader(tmpInputStream);

            String regex = "(?:<a(?: href=[\"']?([^'\"> ]+)([^<]+)<\\/a>(?!<a)?))+";
            Pattern pattern = Pattern.compile(regex);

            String h ="";
            while ((line = br.readLine()) != null) {
//                    System.out.println("line.lastIndexOf(\"/\"): " + line.lastIndexOf("/"));
                if (onlyDownloads) {
                    if (line.contains("href=")
                            && (!line.contains(".css"))
                            && (!line.contains(".ico"))
                            && (!line.contains(".shtml"))
                            || (line.contains(".doc"))
                            || (line.contains(".pdf"))
                            || (line.contains(".xls"))
                    ) {
                        h = line.trim();
                        Matcher matcher = pattern.matcher(h);
                        while (matcher.find()) {
                            String href = matcher.group(1);
                            if (!href.contains(addressPrefix))
                                href = addressPrefix + href;
                            String anchor = matcher.group(2);
                            result.put(href, anchor.substring(2, anchor.length()));
                        }
                    }
                }else {
                    h = line.trim();
                    Matcher matcher = pattern.matcher(h);
                    while (matcher.find()) {
                        String href = matcher.group(1);
                        if (!href.contains(addressPrefix))
                            href = addressPrefix + href;
                        String anchor = matcher.group(2);
                        result.put(href, anchor.substring(2, anchor.length()));
                    }
                }
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                //exception
            }
        }
        return result;
    }

}
