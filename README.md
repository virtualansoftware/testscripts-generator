## maven plugin for "testscripts-generator"

### Why do we need maven plugin?
Currently, the testng test java code created in all the project. This one will help to generate the testng Java code for workflow ymls projected in the pom.xml.
This will remove the boiler plate code repeated for any idaithalam java project.

### How will this work?
This generator will create Java class and test method for all the configured ymls.

```xml
<plugin>
    <groupId>io.virtualan</groupId>
    <artifactId>testscripts-generator</artifactId>
    <version>0.1.0</version>
    <configuration>
        <generatedBasedir>src/test/java</generatedBasedir>
        <workflowScripts>
            <RestTestPlanExecutor>
                <property>
                    <name>workflowExecution_xl</name>
                    <value>rest-get.yml</value>
                </property>
            </RestTestPlanExecutor>
        </workflowScripts>
    </configuration>
    <executions>
        <execution>
            <id>test-generator</id>
            <goals>
                <goal>generateTest</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Generated JAVA Code

The above plugin would generate the following java code and would be excuted as well.

```java 
package io.virtualan.idaithalam.test;

import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import org.testng.Assert;

public class RestTestPlanExecutor {

	@org.testng.annotations.Test
	public void workflowExecution_xl(){
		try {
			boolean isSuccess = VirtualanTestPlanExecutor.invoke("rest-get.yml");
 			Assert.assertTrue(isSuccess);
 		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

}
```

### Email US info@virtualan.io for any questions.