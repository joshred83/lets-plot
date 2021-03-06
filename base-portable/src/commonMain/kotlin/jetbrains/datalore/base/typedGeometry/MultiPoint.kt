/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.base.typedGeometry

class MultiPoint<TypeT>(geometry: List<Vec<TypeT>>) : AbstractGeometryList<Vec<TypeT>>(geometry)