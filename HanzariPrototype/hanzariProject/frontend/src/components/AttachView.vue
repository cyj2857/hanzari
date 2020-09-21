<template>
  <div>
    <button @click="makeRectBtn" class="figureBtn">
      <img src="../assets/triangle.png" />
    </button>
    <v-btn @click="makeTextBox" class="textBoxBtn">click to make textbox</v-btn>
    <canvas ref="canvas" class="canvas" width="900px" height="800px"></canvas>
    <input type="file" @change="onFileChange" />
    <!-- ì¶”í›„?— ?´ svg?— image ?ƒœê·¸ëŠ” background?— ?„£ê³? matrixë§? load?•˜ë©? ?  ?“¯ -->
    <v-btn @click="saveCanvasBtn" class="saveCanvas">canvas to svg (check in console log)</v-btn>
    <v-btn @click="deleteAllBtn">delete shapes on canvas</v-btn>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
export default {
  props: {
    myCanvas: {
      type: Object
      //default: null,
    }
  },
  created() {
    eventBus.$on("createdRect", itemName => {
      this.makeRectBtn(itemName);
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
    makeRectBtn(itemName) {
      this.initializing();
      var rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: "red",
        opacity: 1
      });
<<<<<<< HEAD
=======
      
>>>>>>> aa98a5c92b79c116ea1e9d9b7623e4ca0b9183f6
      var textObject = new fabric.IText(itemName, {
        left: 0,
        top: 0,
        fontSize: 13,
        fill: "#000000"
      });
      var group = new fabric.Group([rectangle, textObject], {
        left: 150,
        top: 150
      });
<<<<<<< HEAD
      this.myCanvas.add(group);
=======

      group.on("mouseover", function(e) {
        var group = e.target;
        alert(group.item(1).text);
      });
      
      this.myCanvas.add(group);
      
>>>>>>> aa98a5c92b79c116ea1e9d9b7623e4ca0b9183f6
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
<<<<<<< HEAD
=======

>>>>>>> aa98a5c92b79c116ea1e9d9b7623e4ca0b9183f6
          this.myCanvas.remove(obj);
        });
    },
    saveCanvasBtn() {
      this.initializing();
      console.log("svg : " + this.myCanvas.toSVG()); //logs the SVG representation of canvas
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
  margin-left: 45px;
  border: 1px solid #000;
  background: aliceblue;
  height: 800px;
  width: 900px;
}
</style>