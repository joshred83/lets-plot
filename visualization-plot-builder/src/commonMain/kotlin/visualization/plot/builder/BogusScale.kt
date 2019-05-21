package jetbrains.datalore.visualization.plot.builder

import jetbrains.datalore.base.gcommon.collect.ClosedRange
import jetbrains.datalore.visualization.plot.base.scale.Scale2
import jetbrains.datalore.visualization.plot.base.scale.Transform

internal class BogusScale : Scale2<Double> {
    override val name: String
        get() = throw IllegalStateException("Bogus scale is not supposed to be used.")

    override val breaks: List<Any>
        get() = throw IllegalStateException("Bogus scale is not supposed to be used.")

    override val labels: MutableList<String>
        get() = throw IllegalStateException("Bogus scale is not supposed to be used.")

    override val isContinuous: Boolean
        get() = throw IllegalStateException("Bogus scale is not supposed to be used.")

    override val isContinuousDomain: Boolean
        get() = throw IllegalStateException("Bogus scale is not supposed to be used.")

    override val domainLimits: ClosedRange<Double>
        get() = throw IllegalStateException("Bogus scale is not supposed to be used.")

    override val multiplicativeExpand: Double
        get() = throw IllegalStateException("Bogus scale is not supposed to be used.")

    override val additiveExpand: Double
        get() = throw IllegalStateException("Bogus scale is not supposed to be used.")

    override val transform: Transform
        get() = throw IllegalStateException("Bogus scale is not supposed to be used.")

    override val mapper: (Double?) -> Double?
        get() = throw IllegalStateException("Bogus scale is not supposed to be used.")

    override fun hasBreaks(): Boolean {
        throw IllegalStateException("Bogus scale is not supposed to be used.")
    }

    override fun hasLabels(): Boolean {
        throw IllegalStateException("Bogus scale is not supposed to be used.")
    }

    override fun hasDomainLimits(): Boolean {
        throw IllegalStateException("Bogus scale is not supposed to be used.")
    }

    override fun isInDomainLimits(v: Any): Boolean {
        throw IllegalStateException("Bogus scale is not supposed to be used.")
    }

    override fun asNumber(input: Any?): Double {
        throw IllegalStateException("Bogus scale is not supposed to be used.")
    }

    override fun with(): Scale2.Builder<Double> {
        throw IllegalStateException("Bogus scale is not supposed to be used.")
    }
}