<template>
  <div>
    <button @click="buttonClick" class="figureBtn">
      <img src="../assets/triangle.png" />
    </button>
    <canvas ref="canvas" class="canvas" width="800" height="800"></canvas>
    <v-btn @click="buttonClick2">click to new rect on canvas</v-btn>
    <v-btn @click="buttonClick3">click to load svg image</v-btn>
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
    },
    buttonClick2 () {
      const ref = this.$refs.canvas;
      const canvas = new fabric.Canvas(ref);
      const rect = new fabric.Rect({
      fill: 'red',
      width: 50,
      height: 50
      });
      canvas.add(rect);
    },
    buttonClick3 () {
      
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