// Copyright 2013 Square, Inc.
package com.fabernovel.technologies.utils.screenshot;

import java.io.File;

class Chmod {

  static void chmodPlusR(File file) {
    file.setReadable(true, false);
  }

  static void chmodPlusRWX(File file) {
    file.setReadable(true, false);
    file.setWritable(true, false);
    file.setExecutable(true, false);
  }
}
