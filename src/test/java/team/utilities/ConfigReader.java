package team.utilities;

import java.io.*;
import java.util.Properties;


    public class ConfigReader {

        //    Bu sinif configuration.properties file i okumak icin kullanilir
//    property file i okumak icin properti objecsi kullanilir
        private static Properties properties;

        //    static block : ilk calisir
        static {
//        data cekmek istedigim dosyaninin path i
            String path = "configuration.properties";
            try {
//            configuration.property dosyasini acar
                FileInputStream fileInputStream = new FileInputStream(path);
//            properties objesini instantiate ediyoruz
                properties = new Properties();
//            configuration.property dosyasindaki datalari yukler
                properties.load(fileInputStream);
//            file input steam i kapatilir
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //    ConfigReader.getProperty("browser"); -> chrome
//    ConfigReader.getProperty("amazon_url"); -> https://www.amazon.com
//    ConfigReader.getProperty("username"); -> ali
        public static String getProperty(String key) {
            String value = properties.getProperty(key);
            return value;
        }

        public static String setProperty(String key, String value) {
            Properties properties = new Properties();
            try {
                InputStream inputStream = new FileInputStream("configuration.properties");
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            properties.setProperty(key, value);
            try {
                OutputStream outputStream = new FileOutputStream("configuration.properties");
                properties.store(outputStream, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return key;

        }
    }