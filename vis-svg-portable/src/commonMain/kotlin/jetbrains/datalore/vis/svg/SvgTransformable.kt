/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.vis.svg

import jetbrains.datalore.base.observable.property.Property

interface SvgTransformable : SvgLocatable {

    companion object {
        val TRANSFORM: SvgAttributeSpec<SvgTransform> =
            SvgAttributeSpec.createSpec("transform")
    }

    fun transform(): Property<SvgTransform?>
}