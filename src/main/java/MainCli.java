/*
 * This file is part of symfinder.
 *
 * symfinder is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * symfinder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with symfinder. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2018-2019 Johann Mortara <johann.mortara@univ-cotedazur.fr>
 * Copyright 2018-2019 Xhevahire Tërnava <xhevahire.ternava@lip6.fr>
 * Copyright 2018-2019 Philippe Collet <philippe.collet@univ-cotedazur.fr>
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.Callable;

@Command(showDefaultValues = true)
public class MainCli implements Callable<Integer> {

    private static final Logger LOG = LogManager.getLogger(MainCli.class);
    @Option(names = {"-l", "--language"}, defaultValue = "${SYMFINDER_LANGUAGE:-java}", description = "Language of the project to analyze: ${COMPLETION-CANDIDATES}")
    Language language;
    @Parameters(paramLabel = "SRC_DIR", description = "The directory where the sources to analyze are located", defaultValue = "${SOURCES_PACKAGE}")
    private File sourceDir;
    @Option(names = {"-o", "--output-dir"}, defaultValue = "${GRAPH_OUTPUT_PATH:-out}", description = "Output dir where the results are written")
    private String outputDir;
    @Option(names = {"-n", "--project-name"}, defaultValue = "${PROJECT_NAME:-symfinder}", description = "Project name")
    private String name;

    public static void main(String[] args) {
        new CommandLine(new MainCli()).execute(args);

    }

    @Override
    public Integer call() throws Exception {
        try {
            System.setProperty("logfilename", Optional.ofNullable(name).orElse("debug.log"));
            String lang = "java";
            if (this.language == Language.cpp) {
                lang = "cpp";
            }
            System.out.println("TCA AA TEST " + sourceDir + " " + outputDir + " " + lang);
            new Symfinder(sourceDir.getAbsolutePath(), outputDir, lang).run();
            return 0;
        } catch (Exception e) {
            LOG.error("Error while running symfinder", e);
            return 1;
        }
    }

    enum Language {java, cpp}
}
