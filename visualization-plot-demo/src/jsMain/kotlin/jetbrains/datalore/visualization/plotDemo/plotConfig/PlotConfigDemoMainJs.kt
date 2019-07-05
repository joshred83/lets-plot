package jetbrains.datalore.visualization.plotDemo.plotConfig

import jetbrains.datalore.base.geometry.DoubleVector
import jetbrains.datalore.base.jsObject.dynamicObjectToMap
import jetbrains.datalore.base.observable.property.ValueProperty
import jetbrains.datalore.visualization.base.svg.SvgNodeContainer
import jetbrains.datalore.visualization.base.svgMapper.dom.SvgRootDocumentMapper
import jetbrains.datalore.visualization.plot.MonolithicJs
import jetbrains.datalore.visualization.plot.builder.PlotContainer
import org.w3c.dom.Node
import org.w3c.dom.svg.SVGSVGElement


/**
 * This method to be invoked with actual (demo) plot specs.
 * The demo HTML/JS is generated by JVM demo apps like `BarPlotBrowser.kt`
 */
@JsName("buildPlotSvg")
fun buildPlotSvg(plotSpecJs: dynamic, parentElement: dynamic) {
    val plotSpec = dynamicObjectToMap(plotSpecJs)

    val plotSize = DoubleVector(400.0, 300.0)  // ToDo: should be parameter
    val svg = buildPlotToSvgIntern(plotSpec, plotSize)
    (parentElement as Node).appendChild(svg)
}

private fun buildPlotToSvgIntern(plotSpec: MutableMap<String, Any>, plotSize: DoubleVector): SVGSVGElement {
    val plot = MonolithicJs.createPlot(plotSpec, null)
    val plotContainer = PlotContainer(plot, ValueProperty(plotSize))
    plotContainer.ensureContentBuilt()

    val svgRoot = plotContainer.svg
    val mapper = SvgRootDocumentMapper(svgRoot)
    SvgNodeContainer(svgRoot)
    mapper.attachRoot()
    return mapper.target
}



