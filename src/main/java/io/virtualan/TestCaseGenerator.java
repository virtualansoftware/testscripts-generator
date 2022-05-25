package io.virtualan;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

@Mojo(name = "generateTest",
        defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES,
        requiresOnline = false, requiresProject = true,
        threadSafe = false)
public class TestCaseGenerator extends AbstractMojo {

    @Parameter(property = "generatedBasedir", required = false)
    protected String generatedBasedir;

    @Parameter(property = "workflowScripts", required = true)
    protected Map<String, Properties> workflowScripts;

    public void execute() throws MojoExecutionException {
        StringBuilder sourceBuilder = new StringBuilder();
        File file =  null;
        if(generatedBasedir == null || generatedBasedir.isEmpty()) {
            file = new File("target/generated-test-sources/test-annotations/io/virtualan/idaithalam/test");
            file.mkdirs();
        } else if(generatedBasedir != null){
            file = new File(generatedBasedir + File.separator + "io/virtualan/idaithalam/test");
            file.mkdirs();
        }
        System.out.println( "basedir" + generatedBasedir);
        if(file.exists()) {
            try {
                if (workflowScripts != null) {
                    for (Entry<String, Properties> item : workflowScripts.entrySet()) {
                        String clazzzName = item.getKey();
                        sourceBuilder.append("package io.virtualan.idaithalam.test;\n\n");
                        sourceBuilder.append("import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;\n" +
                                "import org.testng.Assert;\n\n");
                        sourceBuilder.append("public class " + clazzzName + " {\n");
                        Properties workflowNames = item.getValue();
                        for (Entry<Object, Object> methodAFileName : workflowNames.entrySet()) {
                            sourceBuilder.append("\n\t@org.testng.annotations.Test\n");
                            sourceBuilder.append("\tpublic void ")
                                    .append(methodAFileName.getKey())
                                    .append("()")
                                    .append("{\n");
                            sourceBuilder.append("\t\ttry {\n" +
                                    "\t\t\tboolean isSuccess = VirtualanTestPlanExecutor.invoke(\"" + methodAFileName.getValue() + "\");\n" +
                                    " \t\t\tAssert.assertTrue(isSuccess);\n" +
                                    " \t\t} catch (Exception e) {\n" +
                                    "\t\t\tAssert.assertTrue(false);\n" +
                                    "\t\t}\n");
                            sourceBuilder.append("\t}\n");
                        }
                        sourceBuilder.append("\n}\n");
                        createFile(file.getAbsolutePath() + File.separator + clazzzName + ".java", sourceBuilder.toString());
                    }
                }
            } catch (Exception e) {
                throw new MojoExecutionException("Error creating files " , e);
            }
        }
    }

    private void createFile(String fileName, String content) throws MojoExecutionException {
        try (FileWriter w = new FileWriter(fileName)) {
            w.write(content);
            w.flush();
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + fileName, e);
        }
    }


}
