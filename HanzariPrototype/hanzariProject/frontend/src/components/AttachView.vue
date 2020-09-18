<template>
  <div>
    <button @click="buttonClick" class="figureBtn">
      <img src="../assets/triangle.png" />
    </button>
    <canvas ref="canvas" class="canvas" width="800" height="600"></canvas>
  </div>
</template>

<script>
export default {
  methods: {
    buttonClick() {
      const ref = this.$refs.canvas;
      const canvas = new fabric.Canvas(ref);
      var shapes = [];

      for (let i = 0; i <= 10; i++) {
        var tri = new fabric.Triangle({
          left: 100,
          top: 100,
          fill: "orange",
          width: 100,
          height: 100
        });
        shapes.push(tri);
        canvas.add(shapes[i]);
      }
    }
  }
};
</script>


<style scoped>
.figureBtn {
  width: 100px;
  height: 100px;
}

.canvas {
  border: 1px solid #000;
  background: aliceblue;
}
</style>