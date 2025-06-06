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

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mybatis.maven.testing.AbstractMojoTestCase;

public class MojoExtension implements AfterEachCallback, BeforeEachCallback {

  // Make AbstractMojoTestCase concrete
  protected AbstractMojoTestCase testCase = new AbstractMojoTestCase() {
  };

  protected void cleanup() {
    Path initMigrationDbFolder = Path.of("target/init");
    if (Files.exists(initMigrationDbFolder)) {
      deleteDir(initMigrationDbFolder);
    }
  }

  protected static boolean deleteDir(Path dir) {
    if (Files.isDirectory(dir)) {
      String[] children = dir.toFile().list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(dir.resolve(children[i]));
        if (!success) {
          return false;
        }
      }
    }

    // The directory is now empty so delete it
    return dir.toFile().delete();
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    // Setup test case
    testCase.setUp();
    cleanup();
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    cleanup();
  }
}
