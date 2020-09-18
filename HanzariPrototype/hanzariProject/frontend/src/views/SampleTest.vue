<template>
  <div>
    <button @click="buttonClick" class="figureBtn">
      <img src="../assets/triangle.png" />
    </button>
    <button @click="buttonClick1" class="figureBtn">
      <img src="../assets/square.png" />
    </button>
    <canvas ref="canvas" class="canvas" width="1000" height="1000"></canvas>
    <v-btn @click="buttonClick2">Create Shapes</v-btn>
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
        canvas.toSVG();
      }
    },
    buttonClick1() {
      const ref = this.$refs.canvas;
      const canvas = new fabric.Canvas(ref);
      var rect = new fabric.Rect({
        fill: "green",
        width: 100,
        height: 100,
        originX: "center",
        originY: "center"
      });

      var text = new fabric.Text("yujin", {
        fontSize: 30,
        originX: "center",
        originY: "center"
      });

      var group = new fabric.Group([rect, text], {
        left: 150,
        top: 100
      });
      canvas.add(group);
    },
    buttonClick2() {
      const ref = this.$refs.canvas;
      const canvas = new fabric.Canvas(ref);
      var red = new fabric.Rect({
        top: 100,
        left: 0,
        width: 80,
        height: 50,
        fill: "red"
      });
      var blue = new fabric.Circle({
        top: 200,
        left: 100,
        radius: 100,
        fill: "blue",
        scaleY: 0.5,
        scaleX: 0.5,
        originX: "center",
        originY: "center"
      });
      var green = new fabric.Rect({
        top: 100,
        left: 100,
        width: 60,
        height: 60,
        fill: "green"
      });
      canvas.add(red, blue, green);
    },
    buttonClick3() {
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