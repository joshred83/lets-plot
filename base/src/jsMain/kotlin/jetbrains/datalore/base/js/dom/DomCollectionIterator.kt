/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.base.js.dom

class DomCollectionIterator<TypeT> internal constructor(private val myDomCollection: DomCollection<TypeT>) : Iterator<TypeT> {
    private var myIndex = 0

    override fun hasNext(): Boolean {
        return myIndex < myDomCollection.length
    }

    override fun next(): TypeT {
        return myDomCollection.item(myIndex++)!!
    }
}
