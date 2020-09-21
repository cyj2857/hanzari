<template>
  <div>
    <button @click="buttonClick1" class="figureBtn">
      <img src="../assets/triangle.png" />
    </button>
    <button @click="buttonClick2" class="figureBtn">
      <img src="../assets/square.png" />
    </button>
    <canvas ref="canvas" class="canvas" id="canvas"  width="1000" height="1000"></canvas>
    <v-btn @click="buttonClick3">Create Shapes</v-btn>
    <v-btn @click="buttonClick4">background image</v-btn>
  </div>
</template>

<script>
export default {
  methods: {
    buttonClick1() {
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
    buttonClick2() {
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

    buttonClick3() {
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
      buttonClick4() {
      const card = new fabric.Canvas("canvas");
      card.setWidth(1000);
      card.setHeight(1000);
      fabric.Image.fromURL("data:image/jpeg;base64,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", img => {
        img.set({
          scaleX: card.width / img.width,
          scaleY: card.height / img.height
        });
        card.setBackgroundImage(img, card.renderAll.bind(card));
        card.renderAll();
      });
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