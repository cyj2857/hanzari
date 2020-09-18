<template>
  <canvas ref="can" width="2000" height="2000"></canvas>
</template>

<script>
import { fabric } from 'fabric';

export default {
  mounted() {
    const ref = this.$refs.can;
    const canvas = new fabric.Canvas(ref);
    const rect = new fabric.Rect({
      fill: 'red',
      width: 100,
      height: 100
    });
    canvas.add(rect);

    const rect2 = new fabric.Triangle({
      fill: 'blue',
      width: 100,
      height: 100
    });
    canvas.add(rect2);



  }
};
</script>