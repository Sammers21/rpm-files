/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.yegor256.rpm;

import java.io.IOException;
import java.nio.file.Path;
import org.redline_rpm.header.Header;
import org.xembly.Directives;

/**
 * The filelists XML file.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
final class Filelists {

    /**
     * The path of XML.
     */
    private final Path xml;

    /**
     * Ctor.
     * @param path The path of XML file
     */
    Filelists(final Path path) {
        this.xml = path;
    }

    /**
     * Update the RPM package from the header.
     *
     * @param pkg The package
     * @throws IOException If fails
     */
    public void update(final Pkg pkg) throws IOException {
        new Update(this.xml).apply(
            new Directives()
                .xpath("/")
                .addIf("filelists")
                .xpath(
                    String.format(
                        "/metadata/package[name='%s']",
                        pkg.tag(Header.HeaderTag.NAME)
                    )
                )
                .remove()
                .xpath("/filelists")
                .attr("xmlns", "http://linux.duke.edu/metadata/filelists")
                .attr("packages", 1)
                .add("package")
                .attr("pkgid", pkg.hash())
                .attr("name", pkg.tag(Header.HeaderTag.NAME))
                .attr("arch", pkg.tag(Header.HeaderTag.ARCH))
                .add("version")
                .attr("epoch", pkg.num(Header.HeaderTag.EPOCH))
                .attr("ver", pkg.tag(Header.HeaderTag.VERSION))
                .attr("rel", pkg.tag(Header.HeaderTag.RELEASE))
                .up()
                .add("file")
                .set("/test")
                .up()
        );
    }

}
