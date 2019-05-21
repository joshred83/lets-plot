package jetbrains.datalore.visualization.plot.builder

import jetbrains.datalore.visualization.plot.base.render.*
import jetbrains.datalore.visualization.plot.base.render.svg.SvgComponent

class SvgLayerRenderer(private val myAesthetics: Aesthetics, private val myGeom: Geom, private val myPos: PositionAdjustment, private val myCoord: CoordinateSystem, private val myGeomContext: GeomContext) : SvgComponent(), SvgRoot, GeomLayerRenderer {

    override fun buildComponent() {
        buildLayer()
    }

    private fun buildLayer() {
        myGeom.build(this, myAesthetics, myPos, myCoord, myGeomContext)
    }
}