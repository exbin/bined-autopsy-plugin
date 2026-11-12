/*
 * Copyright (C) ExBin Project, https://exbin.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.bined.autopsy.contentviewer;

import java.io.InputStream;
import java.io.OutputStream;
import org.jspecify.annotations.NullMarked;
import org.exbin.jaguif.document.api.StreamDocumentSource;
import org.sleuthkit.datamodel.Content;

/**
 * File object document source.
 */
@NullMarked
public class DataContentDocumentSource implements StreamDocumentSource {

    protected final Content dataObject;

    public DataContentDocumentSource(Content dataObject) {
        this.dataObject = dataObject;
    }

    @Override
    public String getDocumentTitle() {
        return dataObject.getName();
    }

    @Override
    public InputStream openInputStream() {
        return new ContentBinaryData(dataObject).getDataInputStream();
    }

    @Override
    public OutputStream openOutputStream() {
        throw new IllegalStateException();
    }
}
