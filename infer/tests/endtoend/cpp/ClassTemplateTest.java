/*
 * Copyright (c) 2015 - present Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package endtoend.cpp;

import static org.hamcrest.MatcherAssert.assertThat;
import static utils.matchers.ResultContainsExactly.containsExactly;


import com.google.common.collect.ImmutableList;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import utils.DebuggableTemporaryFolder;
import utils.InferException;
import utils.InferResults;
import utils.InferRunner;

public class ClassTemplateTest {

  public static final String FILE =
      "infer/tests/codetoanalyze/cpp/frontend/templates/class_template_instantiate.cpp";

  private static ImmutableList<String> inferCmd;

  public static final String DIVIDE_BY_ZERO = "DIVIDE_BY_ZERO";

  @ClassRule
  public static DebuggableTemporaryFolder folder =
      new DebuggableTemporaryFolder();

  @BeforeClass
  public static void runInfer() throws InterruptedException, IOException {
    inferCmd = InferRunner.createCPPInferCommand(folder, FILE);
  }

  @Test
  public void whenInferRunsOnDiv0FunctionsErrorIsFound()
      throws InterruptedException, IOException, InferException {
    String[] procedures = {
        "choose1_div0",
        "choose2_div0_extra",
        "ExecStore<Choose2>_call_div",
    };
    InferResults inferResults = InferRunner.runInferCPP(inferCmd);
    assertThat(
        "Results should contain divide by 0 error",
        inferResults,
        containsExactly(
            DIVIDE_BY_ZERO,
            FILE,
            procedures
        )
    );
  }
}
