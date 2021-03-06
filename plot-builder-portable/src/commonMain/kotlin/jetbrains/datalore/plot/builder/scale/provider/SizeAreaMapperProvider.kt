/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.plot.builder.scale.provider

import jetbrains.datalore.plot.base.aes.AesScaling

class SizeAreaMapperProvider(
    max: Double?,
    naValue: Double
) : DirectlyProportionalMapperProvider(max ?: DEF_MAX, naValue) {

    companion object {
        val DEF_MAX = AesScaling.sizeFromCircleDiameter(21.0)
    }
}