/*
 *    Copyright 2010-2025 the original author or authors.
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

import org.apache.ibatis.migration.commands.UpCommand;
import org.apache.ibatis.migration.commands.VersionCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
class VersionCommandMojoTest extends AbstractMigrateTestCase {

  @Test
  void testUpVersionVersionDownGoal() throws Exception {
    runUpGoal();
    runVersionGoal();
    runVersionDownGoal();
  }

  protected void runUpGoal() throws Exception {
    AbstractCommandMojo<UpCommand> mojo = (AbstractCommandMojo<UpCommand>) testCase.lookupMojo("up", testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "upSteps", "1");
    mojo.execute();
  }

  protected void runVersionGoal() throws Exception {
    AbstractCommandMojo<VersionCommand> mojo = (AbstractCommandMojo<VersionCommand>) testCase.lookupMojo("version",
        testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "version", "20100400000003");
    mojo.execute();
  }

  protected void runVersionDownGoal() throws Exception {
    AbstractCommandMojo<VersionCommand> mojo = (AbstractCommandMojo<VersionCommand>) testCase.lookupMojo("version",
        testPom);
    Assertions.assertNotNull(mojo);
    testCase.setVariableValueToObject(mojo, "version", "20100400000001");
    mojo.execute();
  }

}
