/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.gov.gchq.gaffer.doc.walkthrough;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.commonutil.CommonConstants;
import uk.gov.gchq.gaffer.commonutil.StreamUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class AbstractWalkthroughRunner {
    public static final String EXAMPLE_DIVIDER = "\n\n";
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWalkthroughRunner.class);

    private final List<Class<? extends AbstractWalkthrough>> examples;

    private final String modulePath;
    private final String resourcePrefix;

    public AbstractWalkthroughRunner(final List<Class<? extends AbstractWalkthrough>> examples, final String modulePath, final String resourcePrefix) {
        this.examples = examples;
        this.modulePath = modulePath;
        this.resourcePrefix = resourcePrefix;
    }

    public void run() throws Exception {
        printHeader();
        printTableOfContents();
        printIntro();
        printWalkthroughTitle();
        for (final Class<? extends AbstractWalkthrough> aClass : examples) {
            LOGGER.info(aClass.newInstance().walkthrough());
            LOGGER.info(EXAMPLE_DIVIDER);
        }
    }

    private void printIntro() {
        final String intro;
        try (final InputStream stream = StreamUtil.openStream(getClass(), resourcePrefix + "/walkthrough/Intro.md")) {
            intro = new String(IOUtils.toByteArray(stream), CommonConstants.UTF_8);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info(WalkthroughStrSubstitutor.substitute(intro, modulePath));
    }

    private void printHeader() {
        LOGGER.info("Copyright 2016-2017 Crown Copyright\n"
                + "\n"
                + "Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                + "you may not use this file except in compliance with the License.\n"
                + "You may obtain a copy of the License at\n"
                + "\n"
                + "  http://www.apache.org/licenses/LICENSE-2.0\n"
                + "\n"
                + "Unless required by applicable law or agreed to in writing, software\n"
                + "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                + "See the License for the specific language governing permissions and\n"
                + "limitations under the License.\n"
                + "\n"
                + "_This page has been generated from code. To make any changes please update the walkthrough docs in the [doc](https://github.com/gchq/Gaffer/tree/master/doc/) module, run it and replace the content of this page with the output._\n\n");
    }

    private void printTableOfContents() throws InstantiationException, IllegalAccessException {
        int index = 1;
        LOGGER.info("{}. [Introduction](#introduction)", index);
        index++;
        LOGGER.info("{}. [Walkthroughs](#walkthroughs)", index);

        index = 1;
        for (final Class<? extends AbstractWalkthrough> aClass : examples) {
            final String header = aClass.newInstance().getHeader();
            LOGGER.info("   {}. [{}](#{})", index, header, header.toLowerCase(Locale.getDefault()).replace(" ", "-"));
            index++;
        }
        LOGGER.info("\n");
    }

    private void printWalkthroughTitle() {
        LOGGER.info("## Walkthroughs");
    }
}
