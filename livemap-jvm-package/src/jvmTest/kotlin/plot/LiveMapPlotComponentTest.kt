/*
 * Copyright (c) 2020. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package plot

import jetbrains.datalore.base.registration.Disposable
import jetbrains.datalore.livemap.jvmPackage.MonolithicAwtLM
import jetbrains.datalore.plot.SimpleTestSpecs.simpleBunch
import jetbrains.datalore.plot.SimpleTestSpecs.simplePlot
import jetbrains.datalore.plot.SimpleTestSpecs.simplePointLayer
import jetbrains.datalore.plot.builder.presentation.Style
import jetbrains.datalore.plot.config.Option.GeomName.LIVE_MAP
import jetbrains.datalore.plot.config.Option.Layer
import jetbrains.datalore.vis.svg.SvgSvgElement
import jetbrains.datalore.vis.swing.SceneMapperJfxPanel
import jetbrains.datalore.vis.swing.runOnFxThread
import javax.swing.JComponent
import javax.swing.JLabel
import kotlin.test.Test
import kotlin.test.assertTrue

class LiveMapPlotComponentTest {
    private fun simpleLiveMapLayer(): MutableMap<String, Any> {
        return mutableMapOf(Layer.GEOM to LIVE_MAP)
    }

    @Test
    internal fun isDisposable_singlePlot() {
        val plotSpec = simplePlot(listOf(simpleLiveMapLayer(), simplePointLayer()))
        val component = buildPlotFromRawSpecs(plotSpec)

        assertDisposable(component)
        (component as Disposable).dispose()
    }

    @Test
    internal fun isDisposable_bunch() {
        val geoms = listOf(simpleLiveMapLayer() + simplePointLayer(), simpleLiveMapLayer() + simplePointLayer())
        val plotSpec = simpleBunch(geoms)
        val component = buildPlotFromRawSpecs(plotSpec)

        assertDisposable(component)
        (component as Disposable).dispose()
    }

    private fun assertDisposable(o: Any) {
        assertTrue(o is Disposable, "Expected Disposable, was ${o::class.simpleName}")
    }

    companion object {
        private val COMPONENT_FACTORY = { svg: SvgSvgElement ->
            SceneMapperJfxPanel(
                svg,
                listOf(Style.JFX_PLOT_STYLESHEET)
            )
        }

        private fun buildPlotFromRawSpecs(plotSpec: MutableMap<String, Any>): JComponent {
            val component = MonolithicAwtLM.buildPlotFromRawSpecs(
                plotSpec = plotSpec,
                plotSize = null,
                svgComponentFactory = COMPONENT_FACTORY,
                executor = ::runOnFxThread
            ) {
                if (it.isNotEmpty()) {
                    throw RuntimeException("Unexpected computation messages: $it")
                }
            }

            if (component is JLabel) {
                throw RuntimeException("Error while building plot: ${component.text}")
            }
            return component
        }
    }
}