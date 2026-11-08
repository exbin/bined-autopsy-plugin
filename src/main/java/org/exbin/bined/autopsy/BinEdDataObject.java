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
package org.exbin.bined.autopsy;

import org.jspecify.annotations.NullMarked;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;

/**
 * BinEd data object.
 */
@NullMarked
@MIMEResolver.ExtensionRegistration(displayName = "#BinEdDataObject.extensionDisplayName", mimeType = BinEdDataObject.MIME_TYPE, extension = {BinEdDataObject.MMD_EXT})
@DataObject.Registration(displayName = "#BinEdDataObject.displayName", mimeType = BinEdDataObject.MIME_TYPE, iconBase = "org/exbin/bined/autopsy/resources/icons/icon.png")
public class BinEdDataObject extends MultiDataObject {

  public static final String MIME_TYPE = "application/octet-stream"; //NOI18N
  public static final String MMD_EXT = "bin"; //NOI18N

  public BinEdDataObject(FileObject fo, MultiFileLoader loader) throws DataObjectExistsException {
        super(fo, loader);
    }
}
