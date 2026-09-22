package com.rajankumar.encyclopaedia.feature.notebook

import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NotebookLayerOrderTest {
  private val layers = listOf(
    NotebookLayerEntity(id="a",pageId="page",name="A",sortOrder=0),
    NotebookLayerEntity(id="b",pageId="page",name="B",sortOrder=1),
    NotebookLayerEntity(id="c",pageId="page",name="C",sortOrder=2)
  )

  @Test fun moveUp_returnsAdjacentSwap(){val s=layerOrderSwap(layers,"b",-1)!!;assertEquals("b",s.firstId);assertEquals(1,s.firstOrder);assertEquals("a",s.secondId);assertEquals(0,s.secondOrder)}
  @Test fun moveDown_returnsAdjacentSwap(){val s=layerOrderSwap(layers,"b",1)!!;assertEquals("b",s.firstId);assertEquals("c",s.secondId)}
  @Test fun firstLayer_cannotMoveUp(){assertNull(layerOrderSwap(layers,"a",-1))}
  @Test fun lastLayer_cannotMoveDown(){assertNull(layerOrderSwap(layers,"c",1))}
  @Test fun unknownLayer_isRejected(){assertNull(layerOrderSwap(layers,"missing",1))}
  @Test fun nonAdjacentDelta_isRejected(){assertNull(layerOrderSwap(layers,"b",2))}
}
