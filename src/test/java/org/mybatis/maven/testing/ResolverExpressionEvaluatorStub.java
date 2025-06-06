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
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mybatis.maven.testing;

import java.io.File;
import java.nio.file.Path;

import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;

/**
 * Mybatis: Borrowed from 'https://github.com/apache/maven-plugin-testing/blob/master/maven-plugin-testing-harness/src/main/java/org/apache/maven/plugin/testing/ConfiguationException.java'
 * Reason: Removed from release 4.0.0-beta-1 and we need to have junit 5 support.  Maven dropped maven 3 support here!
 * Git: From release 4.0.0-alpha-2
 */

/**
 * Stub for {@link ExpressionEvaluator}
 *
 * @author jesse
 */
public class ResolverExpressionEvaluatorStub implements ExpressionEvaluator {
  /** {@inheritDoc} */
  @Override
  public Object evaluate(String expr) throws ExpressionEvaluationException {

    Object value = null;

    if (expr == null) {
      return null;
    }

    String expression = stripTokens(expr);

    if (expression.equals(expr)) {
      int index = expr.indexOf("${");
      if (index >= 0) {
        int lastIndex = expr.indexOf("}", index);
        if (lastIndex >= 0) {
          String retVal = expr.substring(0, index);

          if (index > 0 && expr.charAt(index - 1) == '$') {
            retVal += expr.substring(index + 1, lastIndex + 1);
          } else {
            retVal += evaluate(expr.substring(index, lastIndex + 1));
          }

          retVal += evaluate(expr.substring(lastIndex + 1));
          return retVal;
        }
      }

      // Was not an expression
      if (expression.indexOf("$$") > -1) {
        return expression.replaceAll("\\$\\$", "\\$");
      }
    }

    if ("basedir".equals(expression) || "project.basedir".equals(expression)) {
      return PlexusTestCase.getBasedir();
    } else if (expression.startsWith("basedir") || expression.startsWith("project.basedir")) {
      int pathSeparator = expression.indexOf("/");

      if (pathSeparator > 0) {
        value = PlexusTestCase.getBasedir() + expression.substring(pathSeparator);
      } else {
        System.out.println("Got expression '" + expression + "' that was not recognised");
      }
      return value;
    } else if ("localRepository".equals(expression)) {
      Path localRepo = Path.of(PlexusTestCase.getBasedir(), "target/local-repo");
      return new MavenArtifactRepository("localRepository", "file://" + localRepo.toFile().getAbsolutePath(),
          new DefaultRepositoryLayout(), null, null);
    } else {
      return expr;
    }
  }

  private String stripTokens(String expr) {
    if (expr.startsWith("${") && expr.indexOf("}") == expr.length() - 1) {
      expr = expr.substring(2, expr.length() - 1);
    }

    return expr;
  }

  /** {@inheritDoc} */
  @Override
  public File alignToBaseDirectory(File file) {
    if (file.getAbsolutePath().startsWith(PlexusTestCase.getBasedir())) {
      return file;
    } else if (file.isAbsolute()) {
      return file;
    } else {
      return Path.of(PlexusTestCase.getBasedir(), file.getPath()).toFile();
    }
  }
}
