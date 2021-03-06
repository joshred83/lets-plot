/*
 * Copyright (c) 2019 JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 *
 * This file has been modified by JetBrains : Java code has been converted to Kotlin code.
 *
 * THE FOLLOWING IS THE COPYRIGHT OF THE ORIGINAL DOCUMENT:
 *
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package jetbrains.datalore.base.gcommon.base

object Strings {

    // ToDo: use Kotlin `isNullOrEmpty` or `isNullOrBlank`
    fun isNullOrEmpty(s: String?): Boolean {
        return s == null || s.isEmpty()
    }

    fun nullToEmpty(string: String?): String {
        return string ?: ""
    }

    fun repeat(string: String, count: Int): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until count) {
            stringBuilder.append(string)
        }
        return stringBuilder.toString()
    }
}
