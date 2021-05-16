/*
 *    Copyright 2010-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.maven.mvnmigrate;

import java.io.File;
import java.io.PrintStream;
import java.text.MessageFormat;

import org.apache.ibatis.migration.commands.ScriptCommand;
import org.apache.ibatis.migration.options.SelectedOptions;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which executes the ibatis migration script command.
 */
@Mojo(name = "script")
public final class ScriptCommandMojo extends AbstractCommandMojo<ScriptCommand> {

  /**
   * Initial version.
   */
  @Parameter(property = "migration.v1", required = true)
  private String v1;

  /**
   * Final version.
   */
  @Parameter(property = "migration.v2")
  private String v2;

  /**
   * The output file to be create.
   */
  @Parameter(property = "migration.output")
  private File output;

  @Override
  protected ScriptCommand createCommandClass(SelectedOptions options) {
    return new ScriptCommand(options);
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (isSkip()) {
      return;
    }

    try {
      init();

      if (this.output == null) {
        // Set the default System.out PrintStream
        this.getCommand().setPrintStream(System.out);
        if (this.getLog().isInfoEnabled()) {
          String[] args = { this.v1, this.v2 };
          MessageFormat format = new MessageFormat(
              getBundle(this.getLocale()).getString("migration.plugin.execution.command.script.sqlscript"));
          this.getLog().info(format.format(args));
        }
        // Print out all generated script. This is the standard migration tool behavior.
        System.out.println("  --- CUT HERE ---");
      } else {
        if (!this.output.exists()) {
          new File(this.output.getParent()).mkdirs();
        }
        this.getCommand().setPrintStream(new PrintStream(this.output));
      }

      StringBuilder cmdParams = new StringBuilder(v1);
      if (v2 != null) {
        cmdParams.append(" ").append(v2);
      }
      this.getCommand().execute(cmdParams.toString());

      if (this.getLog().isInfoEnabled()) {
        if (this.output != null) {
          File[] args = { this.output };
          MessageFormat format = new MessageFormat(
              getBundle(this.getLocale()).getString("migration.plugin.execution.command.create.file"));
          this.getLog().info(format.format(args));
        } else {
          String[] args = { this.v1, this.v2 };
          MessageFormat format = new MessageFormat(
              getBundle(this.getLocale()).getString("migration.plugin.execution.command.script.sqlscript"));
          this.getLog().info(format.format(args));
        }
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

}
