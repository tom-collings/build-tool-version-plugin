package com.barclays.build_tool_version_plugin;

import java.io.File;

import org.codehaus.jettison.json.JSONArray;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.barclays.build_tool_version_plugin.collectors.JavaCollector;
import com.barclays.build_tool_version_plugin.util.FileExplorer;

@SpringBootApplication
public class BuildToolVersionPluginApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BuildToolVersionPluginApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (args != null && args.length == 2) {
            String appDir = args[0];
            String outputDir = args[1];

            System.out.println("App Dir =>" + appDir);
            System.out.println("Output Dir =>" + outputDir);

			JSONArray versions = new JSONArray();

            JavaCollector javaHandler = new JavaCollector(new File(appDir));
            javaHandler.findVersions(versions);

            FileExplorer.createFile(new File(outputDir + "/" + "versions.json"), versions.toString());
            //FileExplorer.createFile(new File(outputDir + "/" + "dependencies.csv"), CsvWriter.generateDependencyReportFromJson(dependencies));
        }
        else {
            System.err.println("Provide input and output paths!");
        }
	}

}
