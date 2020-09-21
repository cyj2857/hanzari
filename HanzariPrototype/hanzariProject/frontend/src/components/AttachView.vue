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
  props: {
    myCanvas: {
      type: Object,
      default: null
    }
  },
  methods: {
    initializing() {
      if (this.myCanvas == null) {
        const ref = this.$refs.canvas;
        this.myCanvas = new fabric.Canvas(ref);
      }
    },
    buttonClick() {
      // var shapes = [];

      // for (let i = 0; i <= 10; i++) {
      //   var tri = new fabric.Triangle({
      //     left: 100,
      //     top: 100,
      //     fill: "orange",
      //     width: 100,
      //     height: 100
      //   });
      //   shapes.push(tri);
      //   canvas.add(shapes[i]);
      // }
      
      this.initializing();

      var rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: "red",
        opacity: 1
      });

      var textObject = new fabric.IText("Ma Hyori", {
        left: 0,
        top: 0,
        fontSize: 13,
        fill: "#000000"
      });

      var group = new fabric.Group([rectangle, textObject], {
        left: 150,
        top: 150
      });
      this.myCanvas.add(group);
    },
    buttonClick2() {
      
    },
    buttonClick3() {
      const card = this.myCanvas;
      card.setWidth(1000);
      card.setHeight(1000);
      fabric.Image.fromURL("https://konvajs.org/assets/yoda.jpg", img => {
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