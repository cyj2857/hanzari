<template>
  <div>
    <button @click="makeRectBtn" class="figureBtn">
      <img src="../assets/triangle.png" />
    </button>
    <v-btn @click="makeTextBox" class="textBoxBtn">click to make textbox</v-btn>
    <canvas ref="canvas" class="canvas" width="800" height="800"></canvas>
    <input type="file" @change="onFileChange" />
    <!-- 추후에 이 svg에 image 태그는 background에 넣고 matrix만 load하면 될 듯 -->
    <v-btn @click="saveCanvasBtn" class="saveCanvas">canvas to svg (check in console log)</v-btn>
    <v-btn @click="deleteAllBtn">delete shapes on canvas</v-btn>
  </div>
</template>

<script>
import { eventBus } from '../main.js'
export default {
  props: {
    myCanvas: {
      type: Object,
      default: null
    }
  },
  created() {
    eventBus.$on('createdRect', () =>{
      this.makeRectBtn();
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
      this.initializing()
      var image = new Image()
      var reader = new FileReader()
      reader.onload = e => {
        fabric.Image.fromURL(e.target.result, img => {
          img.set({
            scaleX: this.myCanvas.width / img.width,
            scaleY: this.myCanvas.height / img.height
          })
          this.myCanvas.setBackgroundImage(
            img,
            this.myCanvas.renderAll.bind(this.myCanvas)
          )
          this.myCanvas.renderAll();
        })
      }
      reader.readAsDataURL(file);
    },
    onFileChange(e) {
      var files = e.target.files || e.dataTransfer.files
      if (!files.length) return
      this.createImage(files[0])
    },
    makeRectBtn() {
      this.initializing()
      var rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: "red",
        opacity: 1
      })
      var textObject = new fabric.IText("Ma Hyori", {
        left: 0,
        top: 0,
        fontSize: 13,
        fill: "#000000"
      })
      var group = new fabric.Group([rectangle, textObject], {
        left: 150,
        top: 150
      })
      this.myCanvas.add(group)
    },
    makeTextBox () {
      this.initializing()
      var textObject = new fabric.IText("Ma Hyori", {
        left: 0,
        top: 0,
        fontSize: 13,
        fill: "#000000"
      })
      this.myCanvas.add(textObject)
    },
    deleteAllBtn() {
      this.initializing()
      this.myCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          this.myCanvas.remove(obj)
        })
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
  border: 1px solid #000;
  background: aliceblue;
}
</style>