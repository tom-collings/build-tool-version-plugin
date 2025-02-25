package com.barclays.build_tool_version_plugin.collectors;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class JavaCollector {

    private final String GRADLE_FILE_PATTERN="gradle-wrapper.properties";
    private final String GRADLE_PROP_NAME="distributionUrl";
    private final String MAVEN_FILE_PATTERN="maven-wrapper.properties";
    private final String MAVEN_PROP_NAME="distributionUrl";

    private File rootDirectory;

    public JavaCollector(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public JSONArray findVersions(JSONArray versions) {

        findVersionsGradle(new File(this.rootDirectory.getPath()), versions);
        findVersionsMaven(new File(this.rootDirectory.getPath()), versions);

        return versions;
    }

    private void findVersionsGradle(File dir, JSONArray versions) {
        List<File> files = FileUtils.listFiles(
                dir,
                new RegexFileFilter(GRADLE_FILE_PATTERN),
                DirectoryFileFilter.DIRECTORY
        ).stream().toList();

        for (File f : files) {
            Properties prop = new Properties();
            try {
                FileInputStream input = new FileInputStream(f);
                prop.load(input);
                if (prop.containsKey(GRADLE_PROP_NAME)) {
                    String distUrl = prop.getProperty(GRADLE_PROP_NAME);
                    int beginIndex = distUrl.indexOf("gradle-") + 7;
                    int endIndex = distUrl.indexOf("-bin.zip");
                    String version = distUrl.substring(beginIndex, endIndex);
                    System.out.println("found a version of " + version);
                    JSONObject versionJsonObject = new JSONObject();
                    versionJsonObject.put("gradle.version", version);
                    versions.put(versionJsonObject);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void findVersionsMaven(File dir, JSONArray versions) {
        List<File> files = FileUtils.listFiles(
                dir,
                new RegexFileFilter(MAVEN_FILE_PATTERN),
                DirectoryFileFilter.DIRECTORY
        ).stream().toList();

        for (File f : files) {
            Properties prop = new Properties();
            try {
                FileInputStream input = new FileInputStream(f);
                prop.load(input);
                if (prop.containsKey(MAVEN_PROP_NAME)) {
                    String distUrl = prop.getProperty(GRADLE_PROP_NAME);
                    int beginIndex = distUrl.indexOf("maven-") + 6;
                    int endIndex = distUrl.indexOf("-bin.zip");
                    String version = distUrl.substring(beginIndex, endIndex);
                    System.out.println("found a version of " + version);
                    JSONObject versionJsonObject = new JSONObject();
                    versionJsonObject.put("maven.version", version);
                    versions.put(versionJsonObject);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    
}
