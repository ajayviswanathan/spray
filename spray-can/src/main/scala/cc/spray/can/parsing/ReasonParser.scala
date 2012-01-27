/*
 * Copyright (C) 2011, 2012 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.spray.can
package parsing

import java.lang.{StringBuilder => JStringBuilder}
import config.HttpParserConfig
import model.{StatusLine, HttpProtocol}

class ReasonParser(config: HttpParserConfig, protocol: HttpProtocol, status: Int) extends CharacterParser {
  val reason = new JStringBuilder

  def handleChar(cursor: Char) = {
    if (reason.length <= config.maxResponseReasonLength) {
      cursor match {
        case '\r' => this
        case '\n' => new HeaderNameParser(config, StatusLine(protocol, status, reason.toString))
        case _ => reason.append(cursor); this
      }
    } else {
      ErrorState("Reason phrases with more than " + config.maxResponseReasonLength + " characters are not supported")
    }
  }

}