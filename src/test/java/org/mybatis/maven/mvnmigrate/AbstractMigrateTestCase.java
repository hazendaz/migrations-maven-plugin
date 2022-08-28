/*
 *    Copyright 2010-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.maven.mvnmigrate;

import java.io.File;

import org.apache.ibatis.migration.commands.InitializeCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

@ExtendWith(MojoExtension.class)
public abstract class AbstractMigrateTestCase {

  @RegisterExtension
  private static MojoExtension extension = new MojoExtension();

  protected AbstractMojoTestCase testCase;

  protected File testPom = new File("src/test/resources/unit/basic-test/basic-test-plugin-config.xml");

  public AbstractMigrateTestCase() {
    testCase = extension.testCase;
  }

  @SuppressWarnings("unchecked")
  protected void initEnvironment() throws Exception {
    AbstractCommandMojo<InitializeCommand> mojo = (AbstractCommandMojo<InitializeCommand>) testCase.lookupMojo("init",
        testPom);
    Assertions.assertNotNull(mojo);

    final File newRep = new File("target/init");
    testCase.setVariableValueToObject(mojo, "repository", newRep);
    mojo.execute();
    Assertions.assertTrue(newRep.exists());
  }

}
