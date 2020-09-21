<template>
  <div>
    <v-btn @click="makeTextBox" class="textBoxBtn">click to make textbox</v-btn>
    <canvas ref="canvas" class="canvas" width="900px" height="800px"></canvas>
    <input type="file" @change="onFileChange" />
    <!-- Ã¬Â¶??Ã???ºâ?????”Â? ?ÂÂ´ svg???”Â? image ??’Å“ÃªÂ·Â¸Ã«Å â?? background???”Â? ???žÂ£ÃªÂ?? matrixÃ«Â§? load???¢ËœÃ«Â?? ?ÂÂ  ???œÂ? -->
    <v-btn @click="saveCanvasBtn" class="saveCanvas">canvas to svg (check in console log)</v-btn>
    <v-btn @click="deleteAllBtn">delete shapes on canvas</v-btn>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
export default {
  props: {
    myCanvas: {
      type: Object,
      default: null,
    }
  },
  created() {
    eventBus.$on("createdRect", item => {
      this.makeRectBtn(item);
    });
  },
  methods: {
    initializing() {
      if (this.myCanvas == null) {
        const ref = this.$refs.canvas;
        this.myCanvas = new fabric.Canvas(ref);
      
      }
    },
    createImage(file) {
      this.initializing();
      var image = new Image();
      var reader = new FileReader();
      reader.onload = e => {
        fabric.Image.fromURL(e.target.result, img => {
          img.set({
            scaleX: this.myCanvas.width / img.width,
            scaleY: this.myCanvas.height / img.height
          });
          this.myCanvas.setBackgroundImage(
            img,
            this.myCanvas.renderAll.bind(this.myCanvas)
          );
          this.myCanvas.renderAll();
        });
      };
      reader.readAsDataURL(file);
    },
    onFileChange(e) {
      var files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.createImage(files[0]);
    },
    makeRectBtn(item) {
      this.initializing();
      var rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: "red",
        opacity: 1
      });
      
      var textObject = new fabric.IText(item.name, {
        left: 0,
        top: 0,
        fontSize: 13,
        fill: "#000000"
      });
      var group = new fabric.Group([rectangle, textObject], {
        left: 150,
        top: 150
      });

      group.on("mouseover", function(e) {
        var group = e.target;
        alert(group.item(1).text);
      });
      
      this.myCanvas.add(group);
      
    },
    makeTextBox() {
      this.initializing();
      var textObject = new fabric.IText("Ma Hyori", {
        left: 0,
        top: 0,
        fontSize: 13,
        fill: "#000000"
      });
      this.myCanvas.add(textObject);
    },
    deleteAllBtn() {
      this.initializing();
      this.myCanvas
        .getObjects()
        .slice()
        .forEach(obj => {

          this.myCanvas.remove(obj);
        });
    },
    saveCanvasBtn () {
      this.initializing()
      console.log('svg : ' + this.myCanvas.toSVG())//logs the SVG representation of canvas
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
  margin: 8px;
  background: aliceblue;
  height: 800px;
  width: 900px;
}
</style>