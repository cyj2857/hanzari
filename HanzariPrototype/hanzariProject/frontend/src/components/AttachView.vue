<template>
  <div>
    <button @click="makeRectBtn" class="figureBtn">
      <img src="../assets/triangle.png" />
    </button>
    <input type="file" @change="onFileChange">
    <canvas ref="canvas" class="canvas" width="800" height="800"></canvas>
    <v-btn @click="loadImageBtn">click to load svg image</v-btn>
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
    createImage(file) {
      this.initializing()
      var image = new Image();
      var reader = new FileReader();
      
      reader.onload = (e) => {
        
        fabric.Image.fromURL(e.target.result, img => {
          img.set({
            scaleX: this.myCanvas.width / img.width,
            scaleY: this.myCanvas.height / img.height
          });
          this.myCanvas.setBackgroundImage(img, this.myCanvas.renderAll.bind(this.myCanvas));
          this.myCanvas.renderAll();
        });
      }
      reader.readAsDataURL(file);
    },
    onFileChange(e) {
      var files = e.target.files || e.dataTransfer.files;
      if (!files.length)
        return;
      this.createImage(files[0]);
    },
    makeRectBtn() {
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
    loadImageBtn() {
      //���⼭�� ��ü ���� �ܰ� �ڵ尡 ���� ĵ���� ��ü�� �������ü�����.
      this.initializing();

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