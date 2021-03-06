/*
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 8044859
 * @summary test support for -Xdoclint
 * @modules jdk.compiler/com.sun.tools.javac.api
 *          jdk.compiler/com.sun.tools.javac.file
 *          jdk.compiler/com.sun.tools.javac.main
 *          jdk.compiler/com.sun.tools.javac.util
 * @build Tester
 * @run main DocLintTest
 */

import com.sun.tools.javac.main.Main;
import java.io.IOException;

public class DocLintTest extends Tester {
    public static void main(String... args) throws Exception {
        DocLintTest t = new DocLintTest();
        t.runTests();
    }

    @Test
    void testDocLint() throws IOException {
        writeFile("src/C.java", "/** & */ class C { }");
        mkdirs("classes");

        String[] opts = { "-d", "classes", "-Xdoclint" };
        String[] files = { "src/C.java" };

        runMain(opts, files)
                .checkResult(Main.Result.ERROR.exitCode);

        runCall(opts, files)
                .checkResult(false);

        // 1. doclint runs at the beginning of analyze
        // 2. the analyze call itself succeeds, so we check for errors being reported
        runAnalyze(opts, files)
                .checkResult(true)
                .checkLog(Log.DIRECT, "^");
    }
}

