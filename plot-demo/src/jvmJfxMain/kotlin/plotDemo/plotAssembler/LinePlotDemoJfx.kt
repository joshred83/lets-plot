/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.plotDemo.plotAssembler

import jetbrains.datalore.plot.builder.presentation.Style
import jetbrains.datalore.plotDemo.model.plotAssembler.LinePlotDemo
import jetbrains.datalore.vis.demoUtils.SceneMapperDemoFrame

class LinePlotDemoJfx : LinePlotDemo() {

    private fun show() {
        val plots = createPlots()
        val svgRoots = createSvgRootsFromPlots(plots)
        SceneMapperDemoFrame.showSvg(svgRoots, listOf(Style.JFX_PLOT_STYLESHEET), demoComponentSize, "Line plot")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            LinePlotDemoJfx().show()
        }
    }
}
