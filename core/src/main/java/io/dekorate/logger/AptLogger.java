/**
 * Copyright 2018 The original authors.
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
 * 
**/

package io.dekorate.logger;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

import io.dekorate.Logger;
import io.dekorate.LoggerFactory;

public class AptLogger extends LoggerFactory<Messager> implements Logger {

  private final Messager messager;

  public Logger create(Messager messager) {
    return new AptLogger(messager);
  }

  //Should not be used by user code. Only needed for Java SPI.
  public AptLogger() {
    this.messager = null;
  }

  public AptLogger(Messager messager) {
    this.messager = messager;
    check();
  }

  @Override
  public void debug(String message) {
    check();
    messager.printMessage(Kind.NOTE, String.format(DEBUG, message));
  }

  @Override
  public void info(String message) {
    check();
    messager.printMessage(Kind.NOTE, String.format(INFO, message));
  }

  @Override
  public void warning(String message) {
    check();
    messager.printMessage(Kind.WARNING, String.format(WARN, message));
  }

  @Override
  public void error(String message) {
    check();
    messager.printMessage(Kind.ERROR, String.format(ERROR, message));
  }

  private void check() {
    if (messager == null) {
      throw new IllegalStateException("AptLogger requires a Messager instance.");
    }
  }
}
