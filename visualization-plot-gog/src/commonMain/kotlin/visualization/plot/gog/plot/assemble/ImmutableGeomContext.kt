package jetbrains.datalore.visualization.plot.gog.plot.assemble

import jetbrains.datalore.visualization.plot.gog.core.event3.GeomTargetCollector
import jetbrains.datalore.visualization.plot.gog.core.render.Aes
import jetbrains.datalore.visualization.plot.gog.core.render.Aesthetics
import jetbrains.datalore.visualization.plot.gog.core.render.GeomContext

interface ImmutableGeomContext : GeomContext {

    fun with(): Builder

    interface Builder {
        fun aesthetics(aesthetics: Aesthetics?): Builder

        fun <T> aestheticMappers(aestheticMappers: Map<Aes<T>, (Double) -> T>?): Builder

        fun geomTargetCollector(geomTargetCollector: GeomTargetCollector): Builder

        fun build(): ImmutableGeomContext
    }
}