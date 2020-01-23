/*
 * Copyright (c) 2020. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.plot.base.render.svg

object SvgUID {
    private var nextIndex = 0

    // Use in tests to stabilize ids
    fun reset() {
        nextIndex = 0
    }

    fun get(prefix: String): String {
        return "$prefix${nextIndex++}"
    }
}