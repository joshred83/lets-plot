package jetbrains.datalore.plotDemo.plotAssembler

import jetbrains.datalore.plotDemo.model.plotAssembler.LinearRegressionPlotDemo
import jetbrains.datalore.vis.swing.BatikMapperDemoFrame

class LinearRegressionPlotDemoBatik : LinearRegressionPlotDemo() {

    private fun show() {
        val plots = createPlots()
        val svgRoots = createSvgRootsFromPlots(plots)
        BatikMapperDemoFrame.showSvg(svgRoots, demoComponentSize, "Linear regression plot")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            LinearRegressionPlotDemoBatik().show()
        }
    }
}