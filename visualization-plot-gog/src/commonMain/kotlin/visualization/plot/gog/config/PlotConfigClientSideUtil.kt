package jetbrains.datalore.visualization.plot.gog.config

import jetbrains.datalore.base.gcommon.base.Preconditions.checkState
import jetbrains.datalore.base.geometry.DoubleVector
import jetbrains.datalore.visualization.plot.base.GeomKind
import jetbrains.datalore.visualization.plot.base.data.DataFrame
import jetbrains.datalore.visualization.plot.base.event3.GeomTargetLocator.LookupStrategy
import jetbrains.datalore.visualization.plot.base.render.Aes
import jetbrains.datalore.visualization.plot.builder.GeomLayer
import jetbrains.datalore.visualization.plot.builder.assemble.GeomLayerBuilder
import jetbrains.datalore.visualization.plot.builder.assemble.PlotAssembler
import jetbrains.datalore.visualization.plot.builder.assemble.TypedScaleProviderMap
import jetbrains.datalore.visualization.plot.builder.event3.GeomInteractionBuilder
import jetbrains.datalore.visualization.plot.builder.event3.GeomInteractionBuilder.Companion.AREA_GEOM
import jetbrains.datalore.visualization.plot.builder.event3.GeomInteractionBuilder.Companion.NON_AREA_GEOM
import jetbrains.datalore.visualization.plot.gog.config.Option.Plot

object PlotConfigClientSideUtil {

    fun createPlotAssembler(opts: Map<String, Any>): PlotAssembler {
        val config = PlotConfigClientSide.create(opts)
        val coordProvider = config.coordProvider
        val layersByTile = buildPlotLayers(config)
        val assembler = PlotAssembler.multiTile(layersByTile, coordProvider, config.theme)
        assembler.setTitle(config.title)
        assembler.setGuideOptionsMap(config.guideOptionsMap)
        assembler.facets = config.facets
        return assembler
    }

    fun getPlotSizeOrNull(opts: Map<String, Any>): DoubleVector? {
        if (!opts.containsKey(Plot.SIZE)) {
            return null
        }
        val map = OptionsAccessor.over(opts).getMap(Plot.SIZE)
        val sizeSpec = OptionsAccessor.over(map)
        val width = sizeSpec.getDouble("width")!!
        val height = sizeSpec.getDouble("height")!!
        return DoubleVector(width, height)
    }

    private fun buildPlotLayers(cfg: PlotConfig): List<List<GeomLayer>> {
        val dataByLayer = ArrayList<DataFrame>()
        for (layerConfig in cfg.layerConfigs) {
            val layerData = layerConfig.combinedData
            dataByLayer.add(layerData)
        }

        val layersDataByTile = PlotConfigUtil.toLayersDataByTile(dataByLayer, cfg.facets).iterator()

        val scaleProvidersMap = cfg.scaleProvidersMap
        val layerBuilders = ArrayList<GeomLayerBuilder>()
        val layersByTile = ArrayList<List<GeomLayer>>()
        while (layersDataByTile.hasNext()) {
            val panelLayers = ArrayList<GeomLayer>()
            val tileDataByLayer = layersDataByTile.next()

            val isMultilayer = tileDataByLayer.size > 1

            for (layerIndex in tileDataByLayer.indices) {
                checkState(layerBuilders.size >= layerIndex)

                if (layerBuilders.size == layerIndex) {
                    val layerConfig = cfg.layerConfigs[layerIndex]
                    val layerBuilder = createLayerBuilder(layerConfig, scaleProvidersMap)
                    configGeomTargets(layerBuilder, layerConfig, isMultilayer)
                    layerBuilders.add(layerBuilder)
                }

                val layerTileData = tileDataByLayer[layerIndex]
                val layer = layerBuilders[layerIndex].build(layerTileData)
                panelLayers.add(layer)
            }
            layersByTile.add(panelLayers)
        }

        return layersByTile
    }

    private fun configGeomTargets(layerBuilder: GeomLayerBuilder, layerConfig: LayerConfig, multilayer: Boolean) {
        val geomProvider = layerConfig.geomProvider

        val geomInteraction = createGeomInteractionBuilder(
                geomProvider.renders(),
                geomProvider.geomKind,
                layerConfig.statKind,
                multilayer
        ).build()

        layerBuilder
                .locatorLookupSpec(geomInteraction.createLookupSpec())
                .contextualMappingProvider(geomInteraction)
    }

    private fun createLayerBuilder(layerConfig: LayerConfig, scaleProvidersMap: TypedScaleProviderMap): GeomLayerBuilder {
        val geomProvider = layerConfig.geomProvider

        val stat = layerConfig.stat
        val layerBuilder = GeomLayerBuilder()
                .stat(stat)
                .geom(geomProvider)
                .pos(layerConfig.posProvider)


        val constantAesMap = layerConfig.constantsMap
        for (aes in constantAesMap.keys) {
            layerBuilder.addConstantAes(aes as Aes<Any>, constantAesMap[aes]!!)
        }

        if (layerConfig.hasExplicitGrouping()) {
            layerBuilder.groupingVarName(layerConfig.explicitGroupingVarName!!)
        }

        // variable bindings
        val bindings = layerConfig.varBindings
        for (binding in bindings) {
            layerBuilder.addBinding(binding)
        }

        // scale providers
        for (aes in scaleProvidersMap.keySet()) {
            layerBuilder.addScaleProvider(aes as Aes<Any>, scaleProvidersMap[aes])
        }

        layerBuilder.disableLegend(layerConfig.isLegendDisabled)

        return layerBuilder
    }

    internal fun createGeomInteractionBuilder(renders: List<Aes<*>>,
                                              geomKind: GeomKind,
                                              statKind: StatKind,
                                              multilayer: Boolean): GeomInteractionBuilder {

        val builder = initGeomInteractionBuilder(renders, geomKind, statKind)

        if (multilayer) {
            // Only these kinds of geoms should be switched to NEAREST XY strategy on a multilayer plot.
            // Rect, histogram and other column alike geoms should not switch searching strategy, otherwise
            // tooltips behaviour becomes unexpected(histogram shows tooltip when cursor is close enough,
            // but not above a column).
            if (listOf(GeomKind.LINE, GeomKind.DENSITY, GeomKind.AREA, GeomKind.FREQPOLY).contains(geomKind)) {
                builder.multilayerLookupStrategy()
            } else if (statKind === StatKind.SMOOTH) {
                when (geomKind) {
                    GeomKind.POINT, GeomKind.CONTOUR -> builder.multilayerLookupStrategy()

                    else -> {
                    }
                }
            }
        }

        return builder
    }

    private fun initGeomInteractionBuilder(renders: List<Aes<*>>, geomKind: GeomKind, statKind: StatKind): GeomInteractionBuilder {
        val builder = GeomInteractionBuilder(renders)
        if (statKind === StatKind.SMOOTH) {
            when (geomKind) {
                GeomKind.POINT, GeomKind.CONTOUR -> return builder.univariateFunction(LookupStrategy.NEAREST)

                else -> {
                }
            }
        }

        when (geomKind) {
            GeomKind.DENSITY, GeomKind.FREQPOLY, GeomKind.BOX_PLOT, GeomKind.HISTOGRAM, GeomKind.LINE, GeomKind.AREA -> return builder.univariateFunction(LookupStrategy.HOVER)

            GeomKind.BAR, GeomKind.ERROR_BAR -> return builder.univariateFunction(LookupStrategy.NEAREST)

            GeomKind.SMOOTH, GeomKind.POINT, GeomKind.CONTOUR, GeomKind.RIBBON, GeomKind.DENSITY2D, GeomKind.TILE -> {
                if (geomKind === GeomKind.SMOOTH) {
                    builder.axisAes(listOf(Aes.X))
                }
                return builder.bivariateFunction(NON_AREA_GEOM)
            }

            GeomKind.PATH -> {
                when (statKind) {
                    StatKind.CONTOUR, StatKind.CONTOURF, StatKind.DENSITY2D -> return builder.bivariateFunction(NON_AREA_GEOM)
                    else -> {
                    }
                }
                return builder.bivariateFunction(AREA_GEOM)
            }
            // fall through

            GeomKind.DENSITY2DF, GeomKind.CONTOURF, GeomKind.POLYGON, GeomKind.MAP -> return builder.bivariateFunction(AREA_GEOM)

            GeomKind.LIVE_MAP -> return builder.multilayerLookupStrategy()

            else -> return builder.none()
        }
    }
}
